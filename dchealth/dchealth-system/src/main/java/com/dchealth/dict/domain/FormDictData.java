package com.dchealth.dict.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 字典数据对象 form_dict_data
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
public class FormDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    private Long dictCode;

    /** 字典排序 */
    @Excel(name = "字典排序")
    private Long dictSort;

    /** 字典标签 */
    @Excel(name = "字典标签")
    private String dictLabel;

    /** 字典键值 */
    @Excel(name = "字典键值")
    private String dictValue;

    /** 字典类型 */
    @Excel(name = "字典类型")
    private String dictType;

    /** 值类型 1=正常 2=存在手填 3=存在下拉框 */
    @Excel(name = "值类型 1=正常 2=存在手填 3=存在下拉框")
    private String valueType;

    /** 表格回显样式 */
    @Excel(name = "表格回显样式")
    private String selectProperty;

    /** 是否默认（Y是 N否） */
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setDictCode(Long dictCode) 
    {
        this.dictCode = dictCode;
    }

    public Long getDictCode() 
    {
        return dictCode;
    }
    public void setDictSort(Long dictSort) 
    {
        this.dictSort = dictSort;
    }

    public Long getDictSort() 
    {
        return dictSort;
    }
    public void setDictLabel(String dictLabel) 
    {
        this.dictLabel = dictLabel;
    }

    public String getDictLabel() 
    {
        return dictLabel;
    }
    public void setDictValue(String dictValue) 
    {
        this.dictValue = dictValue;
    }

    public String getDictValue() 
    {
        return dictValue;
    }
    public void setDictType(String dictType) 
    {
        this.dictType = dictType;
    }

    public String getDictType() 
    {
        return dictType;
    }
    public void setValueType(String valueType) 
    {
        this.valueType = valueType;
    }

    public String getValueType() 
    {
        return valueType;
    }
    public void setSelectProperty(String selectProperty) 
    {
        this.selectProperty = selectProperty;
    }

    public String getSelectProperty() 
    {
        return selectProperty;
    }
    public void setIsDefault(String isDefault) 
    {
        this.isDefault = isDefault;
    }

    public String getIsDefault() 
    {
        return isDefault;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("valueType", getValueType())
            .append("selectProperty", getSelectProperty())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
