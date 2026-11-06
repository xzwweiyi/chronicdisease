package com.dchealth.dynamic.templete;

import com.dchealth.common.utils.StringUtils;
import com.dchealth.dynamic.domain.DynamicColumn;
import com.dchealth.dynamic.exception.DynamicException;
import com.dchealth.dynamic.mapper.DynamicTableMapper;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.dchealth.dynamic.DynamicConstant.*;

/**
 * 动态创表基类
 */
public abstract class DynamicBase {
    /**
     * 表前缀
     */
    protected String tableNamePre;

    /**
     * 表名
     */
    protected String tableName;

    /**
     * 创表次数
     */
    protected int createTimes;

    /**
     * 已存在的同前缀表
     */
    protected List<String> samePreTables = new ArrayList<>();

    /**
     * 数据库列
     */
    protected List<DynamicColumn> columnList = new ArrayList<>();

    @Resource
    public DynamicTableMapper dynamicTableMapper;

    public DynamicBase init() {
        columnList.clear();
        samePreTables.clear();
        buildDynamicTableName();
        buildDynamicColumn();
        return this;
    }
    /**
     * 构建表名
     */
    public abstract void buildDynamicTableName();
    /**
     * 构建列
     */
    public abstract void buildDynamicColumn();
    public DynamicBase createTable() {
        String createTableSQL = getCreateTableSQL("");
        dynamicTableMapper.createTable(createTableSQL);
        return this;
    }

    public DynamicBase createTableBySuffix(int suffixId) throws DynamicException {
        String createTableSQL = getCreateTableSQLBySuffix(suffixId);
        dynamicTableMapper.createTable(createTableSQL);
        return this;
    }

    public DynamicBase createArticleTableBySuffix(int suffixId) {
        String createTableSQL = getCreateArticleTableSQLBySuffix(suffixId);
        if(createTableSQL != null){
            dynamicTableMapper.createTable(createTableSQL);
        }
        return this;
    }

    /**
     * 标识符加双引号（KingbaseES 兼容，MySQL 反引号在 KES 下不可用）
     */
    private String quoteIdent(String ident) {
        return "\"" + ident.replace("\"", "\"\"") + "\"";
    }

    /**
     * MySQL 列类型 -> KingbaseES 列类型
     */
    private String mapColumnType(String mysqlType) {
        if (mysqlType == null) {
            return "varchar(255)";
        }
        String t = mysqlType.trim().toLowerCase();
        if (t.startsWith("int(") || "int".equals(t) || "integer".equals(t)) {
            return "integer";
        }
        if (t.startsWith("bigint(")) {
            return "bigint";
        }
        if (t.startsWith("tinyint")) {
            return "smallint";
        }
        if ("datetime".equals(t) || t.startsWith("datetime(")) {
            return "timestamp";
        }
        return mysqlType;
    }

    private String getCreateTableSQL(String suffixId) {
        StringBuilder stringBuilder = new StringBuilder();
        String tableNameTmp = StringUtils.isNotEmpty(this.tableName)
                ? this.tableName : this.tableNamePre + suffixId;
        stringBuilder.append(quoteIdent(tableNameTmp)).append("(");
        for (DynamicColumn column : this.columnList) {
            stringBuilder.append(quoteIdent(column.getColumnName())).append(BLANK_STR);

            String colType = (column.isAutoIncrease() && column.isPrimaryKey())
                    ? "bigserial" : mapColumnType(column.getColumnType());
            stringBuilder.append(colType).append(BLANK_STR);
            if (!column.isAllowedNull()) {
                stringBuilder.append(SQL_NOT_NULL).append(BLANK_STR);
            } else {
                stringBuilder.append(SQL_NULL).append(BLANK_STR);
            }
            if (column.isAutoIncrease() && column.isPrimaryKey()) {
                stringBuilder.append("PRIMARY KEY").append(BLANK_STR);
            } else if (StringUtils.length(column.getDefaultValue()) != 0) {
                stringBuilder.append("DEFAULT '").append(column.getDefaultValue()).append("'").append(BLANK_STR);
            } else {
                stringBuilder.append("DEFAULT NULL").append(BLANK_STR);
            }
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append(")");
        this.columnList.clear();
        return stringBuilder.toString();
    }

    /**
     * 查询动态表列是否已存在时使用的建表复制语句
     * MySQL: CREATE TABLE x LIKE y  ->  KES: CREATE TABLE x (LIKE y INCLUDING ALL)
     */
    private String buildLikeTableSql(String newTable, String sourceTable) {
        return quoteIdent(newTable) + " (LIKE " + quoteIdent(sourceTable) + " INCLUDING ALL)";
    }

    private String getCreateTableSQLBySuffix(int suffixId) throws DynamicException {
        if (CollectionUtils.isNotEmpty(this.samePreTables)) {
            if (this.samePreTables.contains(tableNamePre + suffixId)) {
                throw new DynamicException(tableNamePre + suffixId + "表已存在");
            }
            return buildLikeTableSql(tableNamePre + suffixId, samePreTables.get(0));
        }
        return getCreateTableSQL(String.valueOf(suffixId));
    }

    private String getCreateArticleTableSQLBySuffix(int suffixId){
        if (CollectionUtils.isNotEmpty(this.samePreTables)) {
            if (this.samePreTables.contains(tableNamePre + suffixId)) {
                return null;
            }
            return buildLikeTableSql(tableNamePre + suffixId, samePreTables.get(0));
        }
        return getCreateTableSQL(String.valueOf(suffixId));
    }

    public DynamicColumn buildColumnPrimaryKey(String columnName, String type, boolean isAutoIncrease) {
        DynamicColumn dynamicColumn = new DynamicColumn();
        dynamicColumn.setColumnName(columnName);
        dynamicColumn.setColumnType(type);
        dynamicColumn.setPrimaryKey(true);
        dynamicColumn.setAutoIncrease(isAutoIncrease);
        dynamicColumn.setAllowedNull(false);
        dynamicColumn.setComment("主键");
        return dynamicColumn;
    }

    public DynamicColumn buildColumnDefaultNull(String columnName, String type, String comment, boolean createIndex) {
        DynamicColumn dynamicColumn = new DynamicColumn();
        dynamicColumn.setColumnName(columnName);
        dynamicColumn.setColumnType(type);
        dynamicColumn.setAllowedNull(true);
        dynamicColumn.setComment(comment);
        dynamicColumn.setCreateIndex(createIndex);
        return dynamicColumn;
    }

    public DynamicColumn buildColumnDefaultNull(String columnName, String type, String comment) {
        DynamicColumn dynamicColumn = new DynamicColumn();
        dynamicColumn.setColumnName(columnName);
        dynamicColumn.setColumnType(type);
        dynamicColumn.setAllowedNull(true);
        dynamicColumn.setComment(comment);
        return dynamicColumn;
    }

    public DynamicColumn buildColumnWithDefaultValue(String columnName, String type, String defaultValue, String comment, boolean createIndex) {
        DynamicColumn dynamicColumn = new DynamicColumn();
        dynamicColumn.setColumnName(columnName);
        dynamicColumn.setColumnType(type);
        dynamicColumn.setAllowedNull(false);
        dynamicColumn.setDefaultValue(defaultValue);
        dynamicColumn.setComment(comment);
        dynamicColumn.setCreateIndex(createIndex);
        return dynamicColumn;
    }

    public DynamicColumn buildColumnWithDefaultValue(String columnName, String type, String defaultValue, String comment) {
        DynamicColumn dynamicColumn = new DynamicColumn();
        dynamicColumn.setColumnName(columnName);
        dynamicColumn.setColumnType(type);
        dynamicColumn.setAllowedNull(false);
        dynamicColumn.setDefaultValue(defaultValue);
        dynamicColumn.setComment(comment);
        return dynamicColumn;
    }

    protected boolean isNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public String getTableNamePre() {
        return tableNamePre;
    }

    public void setTableNamePre(String tableNamePre) {
        this.tableNamePre = tableNamePre;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getCreateTimes() {
        return createTimes;
    }

    public void setCreateTimes(int createTimes) {
        this.createTimes = createTimes;
    }

    public List<String> getSamePreTables() {
        return samePreTables;
    }

    public void setSamePreTables(List<String> samePreTables) {
        this.samePreTables = samePreTables;
    }
}
