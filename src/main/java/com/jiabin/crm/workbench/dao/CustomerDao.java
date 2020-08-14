package com.jiabin.crm.workbench.dao;

import com.jiabin.crm.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer cus);
}
