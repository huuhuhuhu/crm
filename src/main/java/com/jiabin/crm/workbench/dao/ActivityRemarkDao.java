package com.jiabin.crm.workbench.dao;

import com.jiabin.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String activityId);

    int deleteById(String id);

    int save(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
