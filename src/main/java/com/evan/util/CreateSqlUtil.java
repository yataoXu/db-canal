package com.evan.util;

import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Maps;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Classname CreateSqlUtil
 * @Description
 * @Date 2020/1/17 13:20
 * @Created by Evan
 */
public class CreateSqlUtil {

    public static String insertIntoEventType(String sql, String schemaName) throws JSQLParserException {

        // 此处的运行时类是Insert
        Insert insert = (Insert) CCJSqlParserUtil.parse(sql);
        String tableName = insert.getTable().toString();
        List<Column> columns = insert.getColumns();
        ExpressionList expressionList = (ExpressionList) insert.getItemsList();
        List<Expression> expressions = expressionList.getExpressions();
        StringBuffer sb = new StringBuffer();
        // 获得要插入的列名
        if (columns == null) {
            // 获得要插入数据的值
            for (Expression expression : expressions) {
                sb.append(expression).append("\t");
            }
            sb.deleteCharAt(sb.length() - 1).append("\n");
            sb.append("INSERT").append("\t").append(DateUtil.now()).append("\n");
            String content = sb.toString();
            FileUtils.writeFile("INSERT", schemaName, tableName, content);
            return null;
        } else {
            sb.append("select * from ").append(tableName).append(" where ");
            for (int i = 0; i < columns.size(); i++) {
                sb.append(columns.get(i)).append(" = ").append(expressions.get(i)).append(" and ");
            }
            String createSql = sb.substring(0, sb.lastIndexOf("and"));
            return createSql;
        }
    }

    public static String updateEventType(String sql) throws JSQLParserException {

        // 此处的运行时类是Update
        Update update = (Update) CCJSqlParserUtil.parse(sql);
        StringBuffer sb = new StringBuffer();
        String tableName = update.getTable().toString();
        Expression where = update.getWhere();
        sb.append("select * from ").append(tableName).append(" where ");
        if (where != null) {
            sb.append(where);
        }
        return sb.toString();
    }

    public static  Map<String, String> deleteEventType(String sql) throws JSQLParserException {

        Delete delete = (Delete) CCJSqlParserUtil.parse(sql);

        Expression getWhere = delete.getWhere();

        String getWhereToLowCase = getWhere.toString().trim().toLowerCase();
        getWhereToLowCase = getWhereToLowCase.replaceAll("\'", "");
        getWhereToLowCase = getWhereToLowCase.replaceAll("\"", "");

        if (Pattern.matches(".*<.*", getWhereToLowCase) || Pattern.matches(".*>.*", getWhereToLowCase)) {
            throw new RuntimeException("包含非等值操作");
        }

        String[] whereCondition = getWhereToLowCase.split("and");
        Map<String, String> expressions = Maps.newHashMap();

        for (int i = 0; i < whereCondition.length; i++) {
            String[] split = whereCondition[i].trim().split("=");
            if (split.length != 2) {
                throw new RuntimeException("包含非等值操作");
            }
            expressions.put(split[0].trim(), split[1].trim());
        }
        return expressions;
    }
}
