package com.evan.service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Classname ISchemaService
 * @Description
 * @Date 2020/1/16 16:22
 * @Created by Evan
 */
public interface ISchemaService {

    LinkedHashMap<String, String> getDataBySql(String sql);

    List<String> getTableColumnByName(String tableName);

}

