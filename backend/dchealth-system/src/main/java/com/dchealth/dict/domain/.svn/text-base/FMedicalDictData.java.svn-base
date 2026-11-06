package com.dchealth.dict.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 f_medical_dict_data
 *
 * @author ruoyi
 * @date 2022-06-06
 */
public class FMedicalDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 字典名称 */
    @Excel(name = "字典名称")
    private String dictType;

    /** 字典值 */
    @Excel(name = "字典值")
    private String dictValue;

    /** 编码标签 */
    @Excel(name = "编码标签")
    private String dictLabel;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 父id */
    @Excel(name = "父id")
    private Long parentId;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    public String getDictType()
    {
        return dictType;
    }
    public void setDictValue(String dictValue)
    {
        this.dictValue = dictValue;
    }

    public String getDictValue()
    {
        return dictValue;
    }
    public void setDictLabel(String dictLabel)
    {
        this.dictLabel = dictLabel;
    }

    public String getDictLabel()
    {
        return dictLabel;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getStatus()
    {
        return status;
    }
    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getParentId()
    {
        return parentId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("dictType", getDictType())
                .append("dictValue", getDictValue())
                .append("dictLabel", getDictLabel())
                .append("status", getStatus())
                .append("parentId", getParentId())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("remark", getRemark())
                .toString();
    }
}
