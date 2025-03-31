package com.dchealth.datacheck.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 数据检查对象 sys_data_check
 * 
 * @author lchbu
 * @date 2024-05-14
 */
public class SysDataCheck extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String tableName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String fieldName;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String rowId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTableName(String tableName) 
    {
        this.tableName = tableName;
    }

    public String getTableName() 
    {
        return tableName;
    }
    public void setFieldName(String fieldName) 
    {
        this.fieldName = fieldName;
    }

    public String getFieldName() 
    {
        return fieldName;
    }
    public void setRowId(String rowId) 
    {
        this.rowId = rowId;
    }

    public String getRowId() 
    {
        return rowId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tableName", getTableName())
            .append("fieldName", getFieldName())
            .append("rowId", getRowId())
            .append("createTime", getCreateTime())
            .toString();
    }
}
