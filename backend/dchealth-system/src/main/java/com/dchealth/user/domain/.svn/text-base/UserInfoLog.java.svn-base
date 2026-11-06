package com.dchealth.user.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 user_info_log
 *
 * @author ruoyi
 * @date 2022-06-13
 */
public class UserInfoLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long logId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 出生日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "出生日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date birthday;

    /** 学历 1：中专及中专以下 2:大专 3:大学本科 4:硕士 5:博士 6:博士后 7:博士导师7:硕士导师' */
    @Excel(name = "学历 1：中专及中专以下 2:大专 3:大学本科 4:硕士 5:博士 6:博士后 7:博士导师7:硕士导师'")
    private String education;

    /** 省ID */
    @Excel(name = "省ID")
    private Long provinceId;

    /** 市ID */
    @Excel(name = "市ID")
    private Long cityId;

    /** 区县ID */
    @Excel(name = "区县ID")
    private Long areaId;

    /** 街道ID */
    @Excel(name = "街道ID")
    private Long streetId;

    /** 详情地址 */
    @Excel(name = "详情地址")
    private String detailAddress;

    /** '医生职称 1:住院医师  2:主治医师  3:副主任医师 4:主任医师' */
    @Excel(name = "'医生职称 1:住院医师  2:主治医师  3:副主任医师 4:主任医师'")
    private String title;

    /** 专长 */
    @Excel(name = "专长")
    private String expert;

    /** 个人介绍 */
    @Excel(name = "个人介绍")
    private String personalIntro;

    /** 行业开始时间 */
    @Excel(name = "行业开始时间")
    private String workYear;

    /** 形象照片 */
    @Excel(name = "形象照片")
    private String figureImage;

    /** 审核状态 1 未提交 2 待审核 3 审核通过  4 未通过 */
    @Excel(name = "审核状态 1 未提交 2 待审核 3 审核通过  4 未通过")
    private String authStatus;

    /** 审核内容 */
    @Excel(name = "审核内容")
    private String authContext;

    public void setLogId(Long logId)
    {
        this.logId = logId;
    }

    public Long getLogId()
    {
        return logId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setBirthday(Date birthday)
    {
        this.birthday = birthday;
    }

    public Date getBirthday()
    {
        return birthday;
    }
    public void setEducation(String education)
    {
        this.education = education;
    }

    public String getEducation()
    {
        return education;
    }
    public void setProvinceId(Long provinceId)
    {
        this.provinceId = provinceId;
    }

    public Long getProvinceId()
    {
        return provinceId;
    }
    public void setCityId(Long cityId)
    {
        this.cityId = cityId;
    }

    public Long getCityId()
    {
        return cityId;
    }
    public void setAreaId(Long areaId)
    {
        this.areaId = areaId;
    }

    public Long getAreaId()
    {
        return areaId;
    }
    public void setStreetId(Long streetId)
    {
        this.streetId = streetId;
    }

    public Long getStreetId()
    {
        return streetId;
    }
    public void setDetailAddress(String detailAddress)
    {
        this.detailAddress = detailAddress;
    }

    public String getDetailAddress()
    {
        return detailAddress;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setExpert(String expert)
    {
        this.expert = expert;
    }

    public String getExpert()
    {
        return expert;
    }
    public void setPersonalIntro(String personalIntro)
    {
        this.personalIntro = personalIntro;
    }

    public String getPersonalIntro()
    {
        return personalIntro;
    }
    public void setWorkYear(String workYear)
    {
        this.workYear = workYear;
    }

    public String getWorkYear()
    {
        return workYear;
    }
    public void setFigureImage(String figureImage)
    {
        this.figureImage = figureImage;
    }

    public String getFigureImage()
    {
        return figureImage;
    }
    public void setAuthStatus(String authStatus)
    {
        this.authStatus = authStatus;
    }

    public String getAuthStatus()
    {
        return authStatus;
    }
    public void setAuthContext(String authContext)
    {
        this.authContext = authContext;
    }

    public String getAuthContext()
    {
        return authContext;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("logId", getLogId())
                .append("userId", getUserId())
                .append("birthday", getBirthday())
                .append("education", getEducation())
                .append("provinceId", getProvinceId())
                .append("cityId", getCityId())
                .append("areaId", getAreaId())
                .append("streetId", getStreetId())
                .append("detailAddress", getDetailAddress())
                .append("title", getTitle())
                .append("expert", getExpert())
                .append("personalIntro", getPersonalIntro())
                .append("workYear", getWorkYear())
                .append("figureImage", getFigureImage())
                .append("authStatus", getAuthStatus())
                .append("authContext", getAuthContext())
                .toString();
    }
}
