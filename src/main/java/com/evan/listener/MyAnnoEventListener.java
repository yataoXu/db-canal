package com.evan.listener;

import cn.hutool.core.date.DateUtil;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.evan.annotation.CanalEventListener;
import com.evan.annotation.ddl.AlertTableListenPoint;
import com.evan.annotation.ddl.CreateIndexListenPoint;
import com.evan.annotation.ddl.CreateTableListenPoint;
import com.evan.annotation.ddl.DropTableListenPoint;
import com.evan.annotation.dml.DeleteListenPoint;
import com.evan.annotation.dml.InsertListenPoint;
import com.evan.annotation.dml.UpdateListenPoint;
import com.evan.core.CanalMsg;
import com.evan.service.ISchemaService;
import com.evan.util.ClearTagUtils;
import com.evan.util.CreateSqlUtil;
import com.evan.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CanalEventListener
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MyAnnoEventListener {

    private final ISchemaService schemaService;

    @InsertListenPoint
    public void onEventInsertData(CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
        log.info("新增数据操作");
        initInsertWriteData(rowChange, canalMsg);
    }

    @UpdateListenPoint
    public void onEventUpdateData(CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
        log.info("更新数据操作");
        initUpdateWriteData(rowChange, canalMsg);
    }

    @DeleteListenPoint
    public void onEventDeleteData(CanalEntry.RowChange rowChange, CanalMsg canalMsg) {

        log.info("删除数据操作");
        String eventType = rowChange.getEventType().toString();
        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();

        StringBuffer sb = new StringBuffer();
        if (rowDataList.isEmpty()) {
            try {
                List<String> tableColumnByName = schemaService.getTableColumnByName(canalMsg.getTableName());
                Map<String, String> expressionsMap = CreateSqlUtil.deleteEventType(rowChange.getSql());
                for (String column : tableColumnByName) {
                    System.out.println("colum 为" + column);
                    String columnData = expressionsMap.get(column);
                    sb.append(columnData).append("\t");
                }
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
            writeDatAdd(sb, eventType, canalMsg.getSchemaName(), canalMsg.getTableName());
        } else {
            for (CanalEntry.RowData rowData : rowDataList) {
                rowData.getBeforeColumnsList().forEach((c) -> {
                    sb.append(c.getValue() + "\t");
                });
                writeDatAdd(sb, eventType, canalMsg.getSchemaName(), canalMsg.getTableName());
            }
        }
        sb.delete(0, sb.length());
    }


    private void initUpdateWriteData(CanalEntry.RowChange rowChange, CanalMsg canalMsg) {
        String eventType = rowChange.getEventType().toString();
        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
        if (rowDataList.isEmpty()) {
            try {
                log.info("binlog中的插入语句{}", rowChange.getSql());
                String sql = CreateSqlUtil.updateEventType(rowChange.getSql());


                getDataBySql(sql, canalMsg);
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
        } else {
            rowChangeOfRowDatasListIsNull(eventType, canalMsg.getSchemaName(), canalMsg.getTableName(), rowDataList);
        }
    }

    private void initInsertWriteData(CanalEntry.RowChange rowChange, CanalMsg canalMsg) {
        String eventType = rowChange.getEventType().toString();
        List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();

        if (rowDataList.isEmpty()) {
            try {
                log.info("binlog中的插入语句{}", rowChange.getSql());
                String sql = CreateSqlUtil.insertIntoEventType(rowChange.getSql(), canalMsg.getSchemaName());
                if (sql != null) {
                    getDataBySql(sql, canalMsg);
                }
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
        } else {
            rowChangeOfRowDatasListIsNull(eventType, canalMsg.getSchemaName(), canalMsg.getTableName(), rowDataList);
        }
    }

    private void getDataBySql(String sql, CanalMsg canalMsg) {

        StringBuffer values = new StringBuffer();
        log.info("根据binlog中的插入语句生成的查询语句", sql);
        LinkedHashMap<String, String> dataBySql = schemaService.getDataBySql(sql);
        log.info("根据binlog中的插入语句生成的查询的结果", dataBySql);
        if (dataBySql.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> entry : dataBySql.entrySet()) {
            String clearTag = ClearTagUtils.getClearTag(String.valueOf(entry.getValue()));
            values.append(clearTag + "\t");
        }
        writeDatAdd(values, canalMsg.getEventType(), canalMsg.getSchemaName(), canalMsg.getTableName());
    }


    private void rowChangeOfRowDatasListIsNull(String eventType, String schemaName, String tableName, List<CanalEntry.RowData> rowDatasList) {

        for (CanalEntry.RowData rowData : rowDatasList) {
            StringBuffer values = new StringBuffer();
            rowData.getAfterColumnsList().forEach((c) -> {
                values.append(c.getValue() + "\t");
            });
            writeDatAdd(values, eventType, schemaName, tableName);
        }
    }

    private void writeDatAdd(StringBuffer values, String eventType, String schemaName, String tableName) {
        values.append(eventType).append("\t").append(DateUtil.now()).append("\n");
        String content = values.toString();
        log.info("库名：{},表名：{}，操作类型：{},数据：{}", schemaName, tableName, eventType, content);
        FileUtils.writeFile(eventType, schemaName, tableName, content);
    }

    @CreateTableListenPoint
    public void onEventCreateTable(CanalEntry.RowChange rowChange) {
        log.info("创建表操作");
        log.info("Original SQL 语句");
        log.info("use " + rowChange.getDdlSchemaName() + ";\n" + rowChange.getSql());
    }

    @DropTableListenPoint
    public void onEventDropTable(CanalEntry.RowChange rowChange) {
        log.info("删除表操作");
        log.info("Original SQL 语句");
        log.info("use " + rowChange.getDdlSchemaName() + ";\n" + rowChange.getSql());
    }

    @AlertTableListenPoint
    public void onEventAlertTable(CanalEntry.RowChange rowChange) {
        log.info("修改表信息操作");
        log.info("Original SQL 语句");
        log.info("use " + rowChange.getDdlSchemaName() + ";\n" + rowChange.getSql());
    }

    @CreateIndexListenPoint
    public void onEventCreateIndex(CanalMsg canalMsg, CanalEntry.RowChange rowChange) {
        log.info("创建索引操作");
        log.info("Original SQL 语句");
        log.info("use " + canalMsg.getSchemaName() + ";\n" + rowChange.getSql());

    }


}
