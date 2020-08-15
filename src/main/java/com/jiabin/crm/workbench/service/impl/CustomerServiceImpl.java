package com.jiabin.crm.workbench.service.impl;

import com.jiabin.crm.utils.SqlSessionUtil;
import com.jiabin.crm.workbench.dao.CustomerDao;
import com.jiabin.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> sList=customerDao.getCustomerName(name);
        return sList;
    }
}
