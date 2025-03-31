package com.dchealth.cro.core.domain;

import java.util.Date;

import com.dchealth.cro.common.annotation.Excel;

import lombok.Data;

/**
 * 地区码对象 sys_area
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-06
 */
@Data
public class SysArea 
{

    /** 地区Id */
    private Integer areaId;

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
    private Integer level;

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
    private Integer parentId;

    /** null */
    @Excel(name = "null")
    private Integer orderNum;

    /** 删除状态 */
    private String delFlag;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

   private Date createTime;
   private Date updateTime;
   private String createBy;
   private String updateBy;
}
