package com.dchealth.cro.form.mapper;

import com.dchealth.cro.form.domain.FMedicalExamForm;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 单模板-问卷Mapper接口
 */
@Repository
public interface FMedicalExamFromMapper {
    /**
     * 查询单模板-问卷
     *
     * @param medicalFormId 单模板-问卷主键
     * @return 单模板-问卷
     */
    public FMedicalExamForm selectFMedicalExamFormByMedicalFormId(Long medicalFormId);

    /**
     * 查询单模板-问卷列表
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 单模板-问卷集合
     */
    public List<FMedicalExamForm> selectFMedicalExamFormList(FMedicalExamForm fMedicalExamForm);

    /**
     * 新增单模板-问卷
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 结果
     */
    public int insertFMedicalExamForm(FMedicalExamForm fMedicalExamForm);

    /**
     * 修改单模板-问卷
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 结果
     */
    public int updateFMedicalExamForm(FMedicalExamForm fMedicalExamForm);

    /**
     * 删除单模板-问卷
     *
     * @param medicalFormId 单模板-问卷主键
     * @return 结果
     */
    public int deleteFMedicalExamFormByMedicalFormId(Long medicalFormId);

    /**
     * 批量删除单模板-问卷
     *
     * @param medicalFormIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalExamFormByMedicalFormIds(Long[] medicalFormIds);

    /**
     * 查询模板信息根据表单id
     * @param medicalFormId
     * @return
     */
    FMedicalExamForm selectFMedicalFormByMedicalFormId(Long medicalFormId);

    /**
     * 查询模板信息根据问卷id
     * @param articleId
     * @return
     */
    FMedicalExamForm selectFMedicalExamFormByArticleId(Long articleId);

    /**
     * 根据问卷id更新名称等信息
     * @param fMedicalExamForm
     * @return
     */
    int updateFMedicalExamFormByArticleId(FMedicalExamForm fMedicalExamForm);
}
