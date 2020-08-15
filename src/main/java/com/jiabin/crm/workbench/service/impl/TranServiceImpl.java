package com.jiabin.crm.workbench.service.impl;

import com.jiabin.crm.utils.SqlSessionUtil;
import com.jiabin.crm.workbench.dao.TranDao;
import com.jiabin.crm.workbench.dao.TranHistoryDao;
import com.jiabin.crm.workbench.domain.Tran;
import com.jiabin.crm.workbench.service.TranService;


public class TranServiceImpl implements TranService {
    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

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
        return false;
    }
}
