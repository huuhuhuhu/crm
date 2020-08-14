package com.jiabin.crm.workbench.dao;

import com.jiabin.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);
}
