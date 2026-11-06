package com.dchealth.template.service;

import java.util.List;
import java.util.Map;

import com.dchealth.template.domain.FMedicalExamForm;
import com.dchealth.template.domain.vo.FMedicalExamFormVO;
import com.dchealth.template.domain.vo.FMedicalInfoReq;

import javax.servlet.http.HttpServletResponse;

/**
 * 单模板-问卷Service接口
 *
 * @author lchbu
 * @date 2022-11-04
 */
public interface IFMedicalExamFormService
{
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
    public FMedicalExamForm insertFMedicalExamForm(FMedicalExamForm fMedicalExamForm);

    /**
     * 修改单模板-问卷
     *
     * @param fMedicalExamForm 单模板-问卷
     * @return 结果
     */
    public int updateFMedicalExamForm(FMedicalExamForm fMedicalExamForm);

    /**
     * 批量删除单模板-问卷
     *
     * @param medicalFormIds 需要删除的单模板-问卷主键集合
     * @return 结果
     */
    public int deleteFMedicalExamFormByMedicalFormIds(Long[] medicalFormIds);

    /**
     * 删除单模板-问卷信息
     *
     * @param medicalFormId 单模板-问卷主键
     * @return 结果
     */
    public int deleteFMedicalExamFormByMedicalFormId(Long medicalFormId);

    /**
     * 问卷模板信息
     * @param fMedicalInfoReq
     * @return
     */
    FMedicalExamForm getFMedicalFormExamInfo(FMedicalInfoReq fMedicalInfoReq);

    List<Map<String, String>> exportForm(FMedicalExamForm fMedicalExamForm);

    /**
     * 问卷填报详情信息
     * @param fMedicalInfoReq
     * @return
     */
    FMedicalExamForm getFMedicalFormExamFillInfo(FMedicalInfoReq fMedicalInfoReq);

    /**
     * 查询调查问卷详情
     * @param fMedicalInfoReq
     * @return
     */
    FMedicalExamFormVO queryMedicalExamForm(FMedicalInfoReq fMedicalInfoReq);
}
