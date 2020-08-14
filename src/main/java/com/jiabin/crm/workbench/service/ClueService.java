package com.jiabin.crm.workbench.service;

import com.jiabin.crm.workbench.domain.Clue;
import com.jiabin.crm.workbench.domain.Tran;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);
}
