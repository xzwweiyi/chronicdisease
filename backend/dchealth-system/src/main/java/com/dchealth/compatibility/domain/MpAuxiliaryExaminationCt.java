package com.dchealth.compatibility.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 唐都-辅助检查-ct项目相关对象 mp_auxiliary_examination_ct
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
public class MpAuxiliaryExaminationCt extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增主键 */
    private Long id;

    /** 体检申请id */
    @Excel(name = "体检申请id")
    private String studyId;

    /** 病人标识id */
    @Excel(name = "病人标识id")
    private String personId;

    /** 健康档案检查申请号 */
    @Excel(name = "健康档案检查申请号")
    private String examNo;

    /** 健康档案检验申请号 */
    @Excel(name = "健康档案检验申请号")
    private String testNo;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "报告时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date resultTime;

    /** ct检查 */
    @Excel(name = "ct检查")
    private String ct;

    /** pet—ct检查 */
    @Excel(name = "pet—ct检查")
    private String petCt;

    /** 头颅平扫ct（轴位） */
    @Excel(name = "头颅平扫ct", readConverterExp = "轴=位")
    private String headPlainScanCt;

    /** 胸部平扫ct（轴位+冠位） */
    @Excel(name = "胸部平扫ct", readConverterExp = "轴=位+冠位")
    private String chestPlainScanCt;

    /** 胸部平扫ct+薄层扫描+肺结节 */
    @Excel(name = "胸部平扫ct+薄层扫描+肺结节")
    private String clpCt;

    /** 上腹部ct平扫（肝胆胰脾） */
    @Excel(name = "上腹部ct平扫", readConverterExp = "肝=胆胰脾")
    private String upperAbdominalCtPs;

    /** 下腹部ct平扫（轴位+冠位） */
    @Excel(name = "下腹部ct平扫", readConverterExp = "轴=位+冠位")
    private String lowerAbdominalCtPs;

    /** 上腹部ct增强 */
    @Excel(name = "上腹部ct增强")
    private String upperAbdominalCt;

    /** 下腹部ct增强 */
    @Excel(name = "下腹部ct增强")
    private String lowerAbdominalCt;

    /** 上腹部+下腹部ct增强 */
    @Excel(name = "上腹部+下腹部ct增强")
    private String ulAbdominalCt;

    /** 盆腔ct增强 */
    @Excel(name = "盆腔ct增强")
    private String pelvicCt;

    /** 全身骨静态显像 */
    @Excel(name = "全身骨静态显像")
    private String boneStaticImaging;

    /** 全身pet/mr */
    @Excel(name = "全身pet/mr")
    private String petMr;

    /** 医疗机构id */
    @Excel(name = "医疗机构id")
    private Long careSiteId;

    /** 检查类别 */
    @Excel(name = "检查类别")
    private String technologyGroup;

    /** 身体部位【body_position_dict】 */
    @Excel(name = "身体部位【body_position_dict】")
    private String positionSctid;

    /** 检查部位名称 */
    @Excel(name = "检查部位名称")
    private String position;

    /** 检查方式（增强，平扫） */
    @Excel(name = "检查方式", readConverterExp = "增=强，平扫")
    private String testMethod;

    /** 检查报告结果--主观提示 */
    @Excel(name = "检查报告结果--主观提示")
    private String subjectiveHints;

    /** 检查诊断 */
    @Excel(name = "检查诊断")
    private String relevantClinicDiagnosis;

    /** 报告类型1健康, 2体检 */
    @Excel(name = "报告类型1健康, 2体检")
    private Long reportType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setStudyId(String studyId)
    {
        this.studyId = studyId;
    }

    public String getStudyId()
    {
        return studyId;
    }
    public void setPersonId(String personId)
    {
        this.personId = personId;
    }

    public String getPersonId()
    {
        return personId;
    }
    public void setExamNo(String examNo)
    {
        this.examNo = examNo;
    }

    public String getExamNo()
    {
        return examNo;
    }
    public void setTestNo(String testNo)
    {
        this.testNo = testNo;
    }

    public String getTestNo()
    {
        return testNo;
    }
    public void setResultTime(Date resultTime)
    {
        this.resultTime = resultTime;
    }

    public Date getResultTime()
    {
        return resultTime;
    }
    public void setCt(String ct)
    {
        this.ct = ct;
    }

    public String getCt()
    {
        return ct;
    }
    public void setPetCt(String petCt)
    {
        this.petCt = petCt;
    }

    public String getPetCt()
    {
        return petCt;
    }
    public void setHeadPlainScanCt(String headPlainScanCt)
    {
        this.headPlainScanCt = headPlainScanCt;
    }

    public String getHeadPlainScanCt()
    {
        return headPlainScanCt;
    }
    public void setChestPlainScanCt(String chestPlainScanCt)
    {
        this.chestPlainScanCt = chestPlainScanCt;
    }

    public String getChestPlainScanCt()
    {
        return chestPlainScanCt;
    }
    public void setClpCt(String clpCt)
    {
        this.clpCt = clpCt;
    }

    public String getClpCt()
    {
        return clpCt;
    }
    public void setUpperAbdominalCtPs(String upperAbdominalCtPs)
    {
        this.upperAbdominalCtPs = upperAbdominalCtPs;
    }

    public String getUpperAbdominalCtPs()
    {
        return upperAbdominalCtPs;
    }
    public void setLowerAbdominalCtPs(String lowerAbdominalCtPs)
    {
        this.lowerAbdominalCtPs = lowerAbdominalCtPs;
    }

    public String getLowerAbdominalCtPs()
    {
        return lowerAbdominalCtPs;
    }
    public void setUpperAbdominalCt(String upperAbdominalCt)
    {
        this.upperAbdominalCt = upperAbdominalCt;
    }

    public String getUpperAbdominalCt()
    {
        return upperAbdominalCt;
    }
    public void setLowerAbdominalCt(String lowerAbdominalCt)
    {
        this.lowerAbdominalCt = lowerAbdominalCt;
    }

    public String getLowerAbdominalCt()
    {
        return lowerAbdominalCt;
    }
    public void setUlAbdominalCt(String ulAbdominalCt)
    {
        this.ulAbdominalCt = ulAbdominalCt;
    }

    public String getUlAbdominalCt()
    {
        return ulAbdominalCt;
    }
    public void setPelvicCt(String pelvicCt)
    {
        this.pelvicCt = pelvicCt;
    }

    public String getPelvicCt()
    {
        return pelvicCt;
    }
    public void setBoneStaticImaging(String boneStaticImaging)
    {
        this.boneStaticImaging = boneStaticImaging;
    }

    public String getBoneStaticImaging()
    {
        return boneStaticImaging;
    }
    public void setPetMr(String petMr)
    {
        this.petMr = petMr;
    }

    public String getPetMr()
    {
        return petMr;
    }
    public void setCareSiteId(Long careSiteId)
    {
        this.careSiteId = careSiteId;
    }

    public Long getCareSiteId()
    {
        return careSiteId;
    }
    public void setTechnologyGroup(String technologyGroup)
    {
        this.technologyGroup = technologyGroup;
    }

    public String getTechnologyGroup()
    {
        return technologyGroup;
    }
    public void setPositionSctid(String positionSctid)
    {
        this.positionSctid = positionSctid;
    }

    public String getPositionSctid()
    {
        return positionSctid;
    }
    public void setPosition(String position)
    {
        this.position = position;
    }

    public String getPosition()
    {
        return position;
    }
    public void setTestMethod(String testMethod)
    {
        this.testMethod = testMethod;
    }

    public String getTestMethod()
    {
        return testMethod;
    }
    public void setSubjectiveHints(String subjectiveHints)
    {
        this.subjectiveHints = subjectiveHints;
    }

    public String getSubjectiveHints()
    {
        return subjectiveHints;
    }
    public void setRelevantClinicDiagnosis(String relevantClinicDiagnosis)
    {
        this.relevantClinicDiagnosis = relevantClinicDiagnosis;
    }

    public String getRelevantClinicDiagnosis()
    {
        return relevantClinicDiagnosis;
    }
    public void setReportType(Long reportType)
    {
        this.reportType = reportType;
    }

    public Long getReportType()
    {
        return reportType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("studyId", getStudyId())
            .append("patientId", getPersonId())
            .append("examNo", getExamNo())
            .append("testNo", getTestNo())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("resultTime", getResultTime())
            .append("ct", getCt())
            .append("petCt", getPetCt())
            .append("headPlainScanCt", getHeadPlainScanCt())
            .append("chestPlainScanCt", getChestPlainScanCt())
            .append("clpCt", getClpCt())
            .append("upperAbdominalCtPs", getUpperAbdominalCtPs())
            .append("lowerAbdominalCtPs", getLowerAbdominalCtPs())
            .append("upperAbdominalCt", getUpperAbdominalCt())
            .append("lowerAbdominalCt", getLowerAbdominalCt())
            .append("ulAbdominalCt", getUlAbdominalCt())
            .append("pelvicCt", getPelvicCt())
            .append("boneStaticImaging", getBoneStaticImaging())
            .append("petMr", getPetMr())
            .append("careSiteId", getCareSiteId())
            .append("technologyGroup", getTechnologyGroup())
            .append("positionSctid", getPositionSctid())
            .append("position", getPosition())
            .append("testMethod", getTestMethod())
            .append("subjectiveHints", getSubjectiveHints())
            .append("relevantClinicDiagnosis", getRelevantClinicDiagnosis())
            .append("reportType", getReportType())
            .toString();
    }
}
