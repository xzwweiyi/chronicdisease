package com.dchealth.cro.form.mapper;


import com.dchealth.cro.form.domain.PatientExamRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 患者问卷填报关系Mapper接口
 *
 * @author lchbu
 * @date 2022-11-17
 */
@Repository
public interface PatientExamRecordMapper
{
    /**
     * 查询患者问卷填报关系
     *
     * @param id 患者问卷填报关系主键
     * @return 患者问卷填报关系
     */
    public PatientExamRecord selectPatientExamRecordById(Long id);

    public PatientExamRecord selectPatientExamRecordByArticleId(Long articleId);

    /**
     * 查询患者问卷填报关系列表
     *
     * @param patientExamRecord 患者问卷填报关系
     * @return 患者问卷填报关系集合
     */
    public List<PatientExamRecord> selectPatientExamRecordList(PatientExamRecord patientExamRecord);

    /**
     * 新增患者问卷填报关系
     *
     * @param patientExamRecord 患者问卷填报关系
     * @return 结果
     */
    public int insertPatientExamRecord(PatientExamRecord patientExamRecord);

    /**
     * 修改患者问卷填报关系
     *
     * @param patientExamRecord 患者问卷填报关系
     * @return 结果
     */
    public int updatePatientExamRecord(PatientExamRecord patientExamRecord);

    /**
     * 删除患者问卷填报关系
     *
     * @param id 患者问卷填报关系主键
     * @return 结果
     */
    public int deletePatientExamRecordById(Long id);

    /**
     * 批量删除患者问卷填报关系
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientExamRecordByIds(Long[] ids);

    public List<PatientExamRecord> selectPatientExamRecordByArticleIdAndPatientId(@Param(value = "articleId") Long articleId,
                                                                @Param(value = "accountId") Long accountId);
}

