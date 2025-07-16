package com.dchealth.medical.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import com.dchealth.research.domain.ResearchInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 患者信息对象 patient_info
 * 
 * @author ruoyi
 * @date 2022-09-15
 */
@Data
public class PatientInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 患者ID */
    private Long patientId;
    private Integer visitType;

    private String tdPatientId;

    private String personId;

    /** 患者账号id */
    @Excel(name = "患者账号id")
    private Long accountId;


    /** 患者姓名 */
    @Excel(name = "患者姓名")
    private String patientName;

    /** 就诊号 */
    @Excel(name = "就诊号")
    private String clinicNo;

    /** 患者编号 */
    @Excel(name = "患者编号")
    private String patientCode;

    /** 研究ID */
    @Excel(name = "研究ID")
    private Long researchId;

    /** 医院ID */
    @Excel(name = "医院ID")
    private Long deptId;

    /** 创建医生ID */
    @Excel(name = "创建医生ID")
    private Long doctorId;

    /** 手机号 */
    @Excel(name = "手机号")
    private String patientMobile;

    /** 患者状态 0未填写 1基线 2随访 3完成 4失访 5申请退出 6已退出 */
    @Excel(name = "患者状态0未填写1基线2随访3完成4失访 5申请退出 6已退出")
    private String patientStatus;
    // 查询使用
    private String notPatientStatus;

    private String edcNames;

    //搜索状态 2进行中3已完成4失访
    private String searchType;

    /**
     * 患者队列状态1进行中2完成(包含正常完成和非正常完成)
     */
    private String patientActivityStatus;



    /** 入组状态：0待分配1已入组 */
    @Excel(name = "入组状态：0待分配1已入组")
    private String enrollmentStatus;

    /** 失访原因0无失访1不在参与2失联3其他 */
    @Excel(name = "失访原因0无失访1不在参与2失联3其他")
    private String lossCause;

    /** 失访原因其他 */
    @Excel(name = "失访原因其他")
    private String lossCauseOther;

    /** 随访名称 */
    private String followName;

    /**
     * 入组时间
     */
    private Date enrollmentTime;

    /** 性别（0:未知，1：男，2：女） */
    private Integer sex;

    /**
     * 计划队列
     */
    private Long planResearchId;

    //调整队列1保持原队列2更换队列
    private String adjustType;
    //旧患者id
    private Long oldPatientId;


    private String idCard;


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

    //是否结束创建(根据配置规则)后续周期0否1是
    private String isEndCreateRecord;

    //渠道来源1后台2扫码
    private String channelType;

    //是否在app显示0否1是,扫码入组审核修改到别的队列，旧队列不应该在app显示
    private String isShowApp;

    private String planScanResearchId;

    /** 当前随访名称 */
    private Long followId;

    private String registrySigns;//就诊标识
    private String mobile;//手机号

    private String projectName;//队列名称
    private String deptName;//科室名称

    private String deptIdNoZero;

    /**
     * 自定义开始时间
     */
    private Date startTime;

    /**
     * 自定义结束
     */
    private Date endTime;

    /**
     * 0未绑定 1已绑定
     */
    private String bindStatus;

    private List<Long> patientIds;

    private List<Long> notPatientIds;

    private Long userId;

    private Integer refuseCount;

    private Integer smsSendCount;

    private Long diseaseId;
    //是否填写基线 1 是 0否
    private Integer isSubmitBase;
    //是否完成随访  1是 0否
    private Integer isCompleteFollowup;
    //是否完成随访  1是 0否
    private Integer existsCompleteFollowup;
    private Integer provinceId;
    private Integer cityId;
    private Integer areaId;
    //排除队列状态
    private String notResearchStatus;

    private Integer messageNums;

    private Integer messageStatus;
    /**
     * 随访提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitTime;

    public PatientInfo(){}

    public PatientInfo(PatientInfo old, ResearchInfo researchInfo, String createBy){
        Date now = new Date();
        this.accountId = old.getAccountId();
        this.personId = old.getPersonId();
        this.patientName = old.getPatientName();
//        this.planResearchId = researchInfo.getId();
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
