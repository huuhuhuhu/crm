package com.jiabin.crm.settings.service.impl;

import com.jiabin.crm.settings.dao.DicTypeDao;
import com.jiabin.crm.settings.dao.DicValueDao;
import com.jiabin.crm.settings.domain.DicType;
import com.jiabin.crm.settings.domain.DicValue;
import com.jiabin.crm.settings.service.DicService;
import com.jiabin.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author 北京动力节点
 */
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();

        //将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        //将字典类型列表遍历
        for(DicType dt : dtList){

            //取得每一种类型的字典类型编码
            String code = dt.getCode();

            //根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code+"List", dvList);


        }

        return map;
    }
}




















