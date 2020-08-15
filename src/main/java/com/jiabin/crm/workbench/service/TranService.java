package com.jiabin.crm.workbench.service;

import com.jiabin.crm.workbench.domain.Tran;

public interface TranService {
    boolean save(Tran t, String customerName);
}
