package com.dchealth.research.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

/**
 * 队列基础信息对象 research_info
 * 
 * @author lchbu
 * @date 2022-08-22
 */
@Data
public class ResearchInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 科室ID */
    @Excel(name = "科室ID")
    private Long deptId;

    /** 队列展示图 */
    @Excel(name = "队列展示图")
    private String imageUrl;

    /** 队列名称 */
    @Excel(name = "队列名称")
    private String projectName;

    /** 队列介绍 */
    @Excel(name = "队列介绍")
    private String researchSynopsis;

    /** 当前患者数 */
    @Excel(name = "当前患者数")
    private Long patientNum;

    /** 中心数 */
    @Excel(name = "中心数")
    private Long hospitalNum;

    /** 队列状态0待发布1进行中3已关闭 */
    @Excel(name = "队列状态0待发布1进行中2已关闭")
    private String researchStatus;

    /** 病种id */
    @Excel(name = "病种id")
    private Long diseaseTypeId;

    /** 队列负责人 */
    @Excel(name = "队列负责人")
    private Long doctorId;

    private String researchType;

    /** 删除标志（0代表存在 1代表删除）*/
    private String delFlag;

    /** 阶段（0-简介，1-随访周期，2-CRF，3-患教，4-外呼，5-随访患者，6-随访医生，7-队列状态） */
    private String stage;

    /** 是否开启随访率：1-开启，0-关闭 */
    private Integer isFollowRate;

    /**
     * 后台质效待评价数量
     */
    private Integer evaluateBackPendingNum;

    /**
     * 后台质效完成数量
      */
    private Integer evaluateBackCompletedNum;


    /**
     * 后台预警质效待评价数量
     */
    private Integer warnEvaluateBackPendingNum;

    /**
     * 后台预警质效完成数量
     */
    private Integer warnEvaluateBackCompletedNum;


    /**
     * 后台超窗质效待评价数量
     */
    private Integer overEvaluateBackPendingNum;

    /**
     * 后台超窗质效完成数量
     */
    private Integer overEvaluateBackCompletedNum;




    /**
     * app随访评价待评价数量
     */
    private Integer evaluatePendingNum;

    /**
     * app随访评价完成数量
     */
    private Integer evaluateCompletedNum;
    
    
    private String evaluateStatusBack;
    private String warnStatus;
    private String warnEvaluateStatusBack;
    private String overTimeStatus;
    private String overEvaluateStatusBack;
    private String followStatus;
    private String evaluateStatus;




    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deptId", getDeptId())
            .append("imageUrl", getImageUrl())
            .append("projectName", getProjectName())
            .append("researchSynopsis", getResearchSynopsis())
            .append("patientNum", getPatientNum())
            .append("hospitalNum", getHospitalNum())
            .append("researchStatus", getResearchStatus())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("diseaseTypeId", getDiseaseTypeId())
            .append("doctorId", getDoctorId())
            .toString();
    }
}
