package com.dchealth.compatibility.domain;

import java.util.Date;

import lombok.Data;

/**
 * 手术信息对象 mp_procedure_occurrence
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpProcedureOccurrence {
	  /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 自增主键 */
    private Long id;

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;

    /** 就诊id */
    private String visitOccurrenceId;

    /** 手术及操作编码 */
    private String procedureCode;

    /** 手术及操作名称 */
    private String procedureName;

    /** 手术部位名称 */
    private String modifierName;

    /** 手术类型 */
    private String procedureType;

    /** 主刀医生姓名 */
    private String operationDoctorName;

    /** 手术等级名称 */
    private String operationGrade;

    /** 手术科室名称 */
    private String departmentName;

   
}
