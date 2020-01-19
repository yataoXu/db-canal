package com.evan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Classname SchemaMapper
 * @Description
 * @Date 2020/1/16 16:24
 * @Created by Evan
 */

@Mapper
@Component
public interface SchemaMapper {

    @Select("${sql}")
    List<LinkedHashMap> getDataBySql(@Param("sql") String sql);

    @Select("select COLUMN_NAME from information_schema.COLUMNS where TABLE_SCHEMA = (select database()) and TABLE_NAME= #{tableName} order by ORDINAL_POSITION ASC")
    List<String> getTableColumnByTable(@Param("tableName") String tableName);

}
