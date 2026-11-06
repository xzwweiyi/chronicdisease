package com.dchealth.datacheck.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 数据检查配置对象 sys_check_config
 * 
 * @author lchbu
 * @date 2024-05-14
 */
public class SysCheckConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 表中文名 */
    @Excel(name = "表中文名")
    private String tableCnName;

    /** 表英文名 */
    @Excel(name = "表英文名")
    private String tableEnName;

    /** 字段中文名 */
    @Excel(name = "字段中文名")
    private String fieldCn;

    /** 字段英文名 */
    @Excel(name = "字段英文名")
    private String fieldEn;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTableCnName(String tableCnName) 
    {
        this.tableCnName = tableCnName;
    }

    public String getTableCnName() 
    {
        return tableCnName;
    }
    public void setTableEnName(String tableEnName) 
    {
        this.tableEnName = tableEnName;
    }

    public String getTableEnName() 
    {
        return tableEnName;
    }
    public void setFieldCn(String fieldCn) 
    {
        this.fieldCn = fieldCn;
    }

    public String getFieldCn() 
    {
        return fieldCn;
    }
    public void setFieldEn(String fieldEn) 
    {
        this.fieldEn = fieldEn;
    }

    public String getFieldEn() 
    {
        return fieldEn;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("tableCnName", getTableCnName())
            .append("tableEnName", getTableEnName())
            .append("fieldCn", getFieldCn())
            .append("fieldEn", getFieldEn())
            .append("createTime", getCreateTime())
            .toString();
    }
}
