package com.jiabin.crm.workbench.dao;

import com.jiabin.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);

    List<String> getCustomerName(String name);
}
