package com.dchealth.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 地区码对象 sys_area
 * 
 * @author ruoyi
 * @date 2022-03-18
 */
@Data
public class SysArea extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地区Id */
    private Long areaId;

    /** 上级信息 */
    @Excel(name = "上级信息")
    private String ancestors;

    /** 地区编码 */
    @Excel(name = "地区编码")
    private String areaCode;

    /** 地区名 */
    @Excel(name = "地区名")
    private String areaName;

    /** 地区级别（1:省份province,2:市city,3:区县district,4:街道street） */
    @Excel(name = "地区级别", readConverterExp = "1=:省份province,2:市city,3:区县district,4:街道street")
    private Long level;

    /** 城市编码 */
    @Excel(name = "城市编码")
    private String cityCode;

    /** 城市中心点（即：经纬度坐标） */
    @Excel(name = "城市中心点", readConverterExp = "即=：经纬度坐标")
    private String longitude;

    /** 纬度 */
    @Excel(name = "纬度")
    private String latitude;

    /** 地区父节点 */
    @Excel(name = "地区父节点")
    private Long parentId;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Long orderNum;

    /** 删除状态 */
    private String delFlag;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    private List<SysArea> child = new ArrayList<>();

    public void setAreaId(Long areaId) 
    {
        this.areaId = areaId;
    }

    public Long getAreaId() 
    {
        return areaId;
    }
    public void setAncestors(String ancestors) 
    {
        this.ancestors = ancestors;
    }

    public String getAncestors() 
    {
        return ancestors;
    }
    public void setAreaCode(String areaCode) 
    {
        this.areaCode = areaCode;
    }

    public String getAreaCode() 
    {
        return areaCode;
    }
    public void setAreaName(String areaName) 
    {
        this.areaName = areaName;
    }

    public String getAreaName() 
    {
        return areaName;
    }
    public void setLevel(Long level) 
    {
        this.level = level;
    }

    public Long getLevel() 
    {
        return level;
    }
    public void setCityCode(String cityCode) 
    {
        this.cityCode = cityCode;
    }

    public String getCityCode() 
    {
        return cityCode;
    }
    public void setLongitude(String longitude) 
    {
        this.longitude = longitude;
    }

    public String getLongitude() 
    {
        return longitude;
    }
    public void setLatitude(String latitude) 
    {
        this.latitude = latitude;
    }

    public String getLatitude() 
    {
        return latitude;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
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
            .append("areaId", getAreaId())
            .append("ancestors", getAncestors())
            .append("areaCode", getAreaCode())
            .append("areaName", getAreaName())
            .append("level", getLevel())
            .append("cityCode", getCityCode())
            .append("longitude", getLongitude())
            .append("latitude", getLatitude())
            .append("parentId", getParentId())
            .append("orderNum", getOrderNum())
            .append("delFlag", getDelFlag())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
