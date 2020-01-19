package com.evan.service.impl;

import com.evan.mapper.SchemaMapper;
import com.evan.service.ISchemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Description
 * @ClassName SchemaService
 * @Author Evan
 * @date 2019.09.29 13:12
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SchemaServiceImpl implements ISchemaService {

    private final SchemaMapper schemaMapper;

    @Override
    public LinkedHashMap<String, String> getDataBySql(String sql) {
        List<LinkedHashMap> dataBySql = schemaMapper.getDataBySql(sql);
        // 若有多条数据，获得数据库id最大的数据
        LinkedHashMap insertData = dataBySql.get(dataBySql.size() - 1);
        return insertData;

    }


    @Override
    public  List<String>getTableColumnByName(String tableName){
        return schemaMapper.getTableColumnByTable(tableName);
    }



}
