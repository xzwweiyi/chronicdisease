package com.dchealth.applet.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 患者小程序信息对象 patient_mininfo
 *
 * @author ruoyi
 * @date 2022-06-23
 */
public class PatientMinInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 患者ID */
    private Long patientId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long appId;

    /** 患者头像 */
    @Excel(name = "患者头像")
    private String userHead;

    /** OPEN_ID */
    @Excel(name = "OPEN_ID")
    private String openId;

    public void setPatientId(Long patientId)
    {
        this.patientId = patientId;
    }

    public Long getPatientId()
    {
        return patientId;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setUserHead(String userHead)
    {
        this.userHead = userHead;
    }

    public String getUserHead()
    {
        return userHead;
    }
    public void setOpenId(String openId)
    {
        this.openId = openId;
    }

    public String getOpenId()
    {
        return openId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("patientId", getPatientId())
                .append("appId", getAppId())
                .append("userHead", getUserHead())
                .append("openId", getOpenId())
                .append("updateBy", getUpdateBy())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
