package com.dchealth.system.domain;

import lombok.Data;

/**
 * 【请填写功能名称】对象 rel_mp_drug_exposure
 *
 * @author wangpf
 * @date 2025-08-20
 */
@Data
public class RelMpDrugExposure  {
    private static final long serialVersionUID = 1L;


    private Long id;


    private String drugExposureId;


    private String personId;


    private String visitOccurrenceId;


    private String drugConceptId;


    private String drugExposureStartDate;


    private String drugExposureEndDate;


    private String drugSourceValue;


    private String drugValue;


    private String drugSourceConceptId;


    private String drugTypeConceptId;


    private String drugType;


    private String stopReason;


    private String refills;


    private String quantity;


    private String daysSupply;


    private String sig;


    private String routeConceptId;


    private String effectiveDrugDose;


    private String doseUnitConceptId;


    private String lotNumber;


    private String providerId;


    private String routeSourceValue;


    private String doseUnitSourceValue;


    private String departmentId;


    private String medicationFrequency;


    private String routeCode;


    private String drugPackageSpec;


    private String drugProperties;


    private String isAntibiotics;


    private String rcptNo;


    private String dosageForm;


    private String orderType;


    private String totalUnit;


    private String executionTime;


    private String orderStatus;

    private String dataVendorId;


    private String atlasFlag;


    private String visitSourceConceptId;


    private String providerName;


    private String departmentName;

    /** 搜索值 */
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private String createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private String updateTime;

    /** 备注 */
    private String remark;

}
