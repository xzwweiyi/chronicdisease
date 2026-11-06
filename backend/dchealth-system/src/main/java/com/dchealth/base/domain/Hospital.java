package com.dchealth.base.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 机构管理对象 sys_hospital
 *
 * @author ruoyi
 */
@Data
public class Hospital extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 中心名称 */
    @Excel(name = "中心名称")
    private String hospitalName;

    /** 中心编号 */
    @Excel(name = "中心编号")
    private String hospitalCode;

    /** 省份ID */
    private Long provinceId;

    /** 城市ID */
    private Long cityId;

    /** 区县ID */
    private Long areaId;

    /** 地址 */
    private String address;

    /** 中心介绍 */
    private String hospitalIntro;

    /** 中心图片 */
    private String hospitalImage;

    /** 省份名称（关联查询，非持久化） */
    private String provinceName;

    /** 城市名称（关联查询，非持久化） */
    private String cityName;

    /** 区县名称（关联查询，非持久化） */
    private String areaName;
}
