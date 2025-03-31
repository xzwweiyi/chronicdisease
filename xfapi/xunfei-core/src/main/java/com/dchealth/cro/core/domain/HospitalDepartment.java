package com.dchealth.cro.core.domain;

import java.util.Date;

import com.dchealth.cro.common.annotation.Excel;

import lombok.Data;

/**
 * 科室管理对象 hospital_department
 * 
 * @author genemax1107@aliyun.com
 * @date 2020-01-06
 */
@Data
public class HospitalDepartment 
{

    /** 主键 */
    private Integer id;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 科室名称 */
    @Excel(name = "科室名称")
    private String departName;

    /** 排序 */
    @Excel(name = "排序")
    private Integer orderNum;

    /** 删除标记 */
    private String delFlag;

    private Date createTime;
    private Date updateTime;
    private String createBy;
    private String updateBy;

  
}
