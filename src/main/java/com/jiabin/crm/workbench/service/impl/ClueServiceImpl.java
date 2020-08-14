package com.jiabin.crm.workbench.service.impl;

import com.jiabin.crm.utils.SqlSessionUtil;
import com.jiabin.crm.utils.UUIDUtil;
import com.jiabin.crm.workbench.dao.ClueActivityRelationDao;
import com.jiabin.crm.workbench.dao.ClueDao;
import com.jiabin.crm.workbench.domain.Clue;
import com.jiabin.crm.workbench.domain.ClueActivityRelation;
import com.jiabin.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {
    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    public boolean save(Clue clue) {
        boolean flag=true;
        int count=clueDao.save(clue);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    //根据id,查单条记录
    public Clue detail(String id) {
        Clue c=clueDao.detail(id);
        return c;
    }

    //根据线索市场活动关联表id,删除单条记录
    public boolean unbund(String id) {
        boolean flag=true;
        int count=clueActivityRelationDao.delete(id);
        if (count!=1){
            flag=false;
        }
        return flag;
    }

    public boolean bund(String cid, String[] aids) {
        boolean flag=true;

        for (String aid:aids){
            //取得每一个aid与cid做关联
            ClueActivityRelation car=new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setClueId(cid);
            car.setActivityId(aid);
            //添加关联关系表中的记录
            int count=clueActivityRelationDao.bund(car);
            if (count!=1){
                flag=false;
            }
        }

        return flag;
    }
}
