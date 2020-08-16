package com.jiabin.crm.workbench.service.impl;

import com.jiabin.crm.utils.DateTimeUtil;
import com.jiabin.crm.utils.SqlSessionUtil;
import com.jiabin.crm.utils.UUIDUtil;
import com.jiabin.crm.workbench.dao.CustomerDao;
import com.jiabin.crm.workbench.dao.TranDao;
import com.jiabin.crm.workbench.dao.TranHistoryDao;
import com.jiabin.crm.workbench.domain.Customer;
import com.jiabin.crm.workbench.domain.Tran;
import com.jiabin.crm.workbench.domain.TranHistory;
import com.jiabin.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TranServiceImpl implements TranService {
    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    @Override
    public boolean save(Tran t, String customerName) {
        /*
            交易添加业务：
                在做添加之前，参数t里面就少了一项信息，即使客户的主键，customerid
                先处理客户相关的需求
                1.先判断customername，根据客户名称在客户表进行精确查询
                如果有这个客户，则取出这个客户的id，封装到t对象中
                如果没有这个客户，则在客户表中新建一条客户信息，然后将信息客户的id,封装到t对象中

                2.经过以上操作之后，t对象中的信息是全的，执行添加交易的操作

                3.添加交易成功后，创建一条交易历史

         */
        boolean flag= true;
        Customer cus=customerDao.getCustomerByName(customerName);
        //新建客户
        if (cus==null){
            cus=new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setContactSummary(t.getContactSummary());
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(t.getCreateTime());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setName(t.getName());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1=customerDao.save(cus);
            if (count1!=1){
                flag=false;
            }
        }

        //取得客户id
        t.setCustomerId(cus.getId());

        //添加交易
        int count2=tranDao.save(t);
        if (count2!=1){
            flag=false;
        }

        //添加交易历史
        TranHistory th=new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3=tranHistoryDao.save(th);
        if (count3!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran t=tranDao.detail(id);
        return t;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList=tranHistoryDao.getHistoryListByTranId(tranId);
        return thList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
        //改变交易阶段
        int count1=tranDao.changeStage(t);
        if(count1!=1){
            flag=false;
        }

        //交易阶段改变后，生成一条交易历史
        TranHistory th=new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getEditBy());
        //添加交易历史
        int count2=tranHistoryDao.save(th);
        if(count2!=1){
            flag=false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total=tranDao.getTotal();

        //取得dataList,dao层中返回值是map,时，多组map会自动封装成list
        List<Map<String,Object>> dataList=tranDao.getCharts();

        //将以上保存到map中返回
        Map<String,Object> map=new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
