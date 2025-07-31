package com.dchealth.cro.medical.domain;

import com.dchealth.cro.research.domain.ResearchInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 患者信息对象 patient_info
 * 
 * @author ruoyi
 * @date 2022-09-14
 */
@Data
@Getter
@Setter
public class PatientInfo
{

    /** 患者ID */
    private Long patientId;
    /**
     * 旧队列中的patientId
     **/
    private Long oldPatientId;

    /** 患者账号id */
    private Long accountId;

    /** 患者库ID */
    private String personId;

    /** 患者姓名 */
    private String patientName;

    /** 就诊号 */
    private String clinicNo;

    /** 患者编号 */
    private String patientCode;

    /** 研究ID */
    private Long researchId;

    /** 医院ID */
    private Long deptId;

    /** 创建医生ID */
    private Long doctorId;

    /** 手机号 */
    private String patientMobile;

    /** 患者状态 0未填写 1基线 2随访 3完成 4失访 */
    private String patientStatus;

    /** 入组状态：0待分配1已入组  */
    private String enrollmentStatus;

    /** 失访原因0无失访1不在参与2失联3其他 */
    private String lossCause;

    /** 失访原因其他 */
    private String lossCauseOther;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /**
     * 入组时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enrollmentTime;

    /**
     * 患者队列信息
     */
    private ResearchInfo researchInfo;

    /**
     * 患者队列状态1进行中2完成(包含正常完成和非正常完成)
     */
    private String patientActivityStatus;

    /** 随访名称 */
    private String followName;


    /** 性别（0:未知，1：男，2：女） */
    private Integer sex;

    /**
     * 计划队列
     */
    private Long planResearchId;

    private Long planScanResearchId;

    /**
     * 入组处理结果0无状态1待分配2变更3确认入组
     */
    private String enrollmentResult;

    /**
     * 入组结果文本
     */
    private String enrollmentResultContent;

    /**
     * 入组处理状态1待处理3已完成
     */
    private String enrollmentHandlerStatus;

    /**
     * 是否待分配0否1是
     */
    private String allocatedStatus;

    /**
     * 待分配处理状态0无状态1待处理3已完成
     */
    private String allocatedHandlerStatus;

    /** 当前随访名称 */
    private Long followId;

    private String channelType;//渠道类型 1后台 2扫码
    /**
     * 是否结束创建(根据配置规则)后续周期0否1是
     */
    private String isEndCreateRecord;
    /**
     * 调整队列1保持原队列2更换队列
     */
    private String adjustType;

    private Long provinceId;
    private Long cityId;
    private Long areaId;

    //是否在app显示0否1是,扫码入组审核修改到别的队列，旧队列不应该在app显示
    private String isShowApp;

    private String visitType;

    private Integer refuseCount;

    private Integer smsSendCount;

    private String projectName;

    public PatientInfo(){}

    public PatientInfo(PatientInfo old, ResearchInfo researchInfo, String createBy){
        Date now = new Date();
        this.accountId = old.getAccountId();
        this.personId = old.getPersonId();
        this.patientName = old.getPatientName();
        this.oldPatientId =old.getPatientId();
        this.researchId = researchInfo.getId();
        this.deptId = researchInfo.getDeptId();
        this.enrollmentStatus = "1";
        this.enrollmentTime = now;
        this.enrollmentResult = "3";
        this.enrollmentHandlerStatus = "3";
        this.enrollmentResultContent = "确认入组";
        this.patientMobile = old.getPatientMobile();
        this.setCreateTime(now);
        this.setCreateBy(createBy);
    }
}
