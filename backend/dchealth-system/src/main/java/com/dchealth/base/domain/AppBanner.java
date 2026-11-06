package com.dchealth.base.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 【请填写功能名称】对象 app_banner
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
public class AppBanner extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 业务数据值 */
    @Excel(name = "业务数据值")
    private String dataId;

    /** 类型 1资讯 2 真实研究项目  3 外网地址 */
    @Excel(name = "类型 1资讯 2 真实研究项目  3 外网地址")
    private String type;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 位置1 医生端轮播  2 患者端轮播 */
    @Excel(name = "位置1 医生端轮播  2 患者端轮播 ")
    private String positionType;

    /** 名称 */
    @Excel(name = "名称")
    private String bannerName;

    /** 链接地址 */
    @Excel(name = "链接地址")
    private String link;

    /** 图片地址 */
    @Excel(name = "图片地址")
    private String imageUrl;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;
    /** 科室id */
    private Long deptId;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setDataId(String dataId) 
    {
        this.dataId = dataId;
    }

    public String getDataId() 
    {
        return dataId;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setTenantId(String tenantId) 
    {
        this.tenantId = tenantId;
    }

    public String getTenantId() 
    {
        return tenantId;
    }
    public void setPositionType(String positionType) 
    {
        this.positionType = positionType;
    }

    public String getPositionType() 
    {
        return positionType;
    }
    public void setBannerName(String bannerName) 
    {
        this.bannerName = bannerName;
    }

    public String getBannerName() 
    {
        return bannerName;
    }
    public void setLink(String link) 
    {
        this.link = link;
    }

    public String getLink() 
    {
        return link;
    }
    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
}
