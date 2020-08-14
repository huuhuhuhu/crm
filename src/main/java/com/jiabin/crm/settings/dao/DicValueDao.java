package com.jiabin.crm.settings.dao;

import com.jiabin.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
