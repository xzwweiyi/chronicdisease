package com.dchealth.template.service;

import java.util.List;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.vo.FMedicalFormSortReq;
import com.dchealth.template.domain.vo.FMedicalInfoReq;
import com.dchealth.template.domain.vo.MedicalDetailInfoVo;

/**
 * 单模板Service接口
 * 
 * @author ruoyi
 * @date 2022-06-08
 */
public interface IFMedicalFormService 
{
    /**
     * 查询单模板
     * 
     * @param medicalFormId 单模板主键
     * @return 单模板
     */
    public FMedicalForm selectFMedicalFormByMedicalFormId(Long medicalFormId);


    /**
     * 查询内容详情
     * @param medicalFormId
     * @return
     */
    public FMedicalForm detail(Long medicalFormId);

    public List<FMedicalForm> detailAll(Long followId);

    /**
     * 查询单模板列表
     * 
     * @param fMedicalForm 单模板
     * @return 单模板集合
     */
    public List<FMedicalForm> selectFMedicalFormList(FMedicalForm fMedicalForm);

    /**
     * 新增单模板
     * 
     * @param fMedicalForm 单模板
     * @return 结果
     */
    public int insertFMedicalForm(FMedicalForm fMedicalForm);

    /**
     * 导入模板
     * @param templateId
     * @param followId
     * @return
     */
    public int importOrUpdate(Long templateId, Long followId, Integer operateType);

    /**
     * 修改单模板
     * 
     * @param fMedicalForm 单模板
     * @return 结果
     */
    public int updateFMedicalForm(FMedicalForm fMedicalForm);

    /**
     * 批量删除单模板
     * 
     * @param medicalFormIds 需要删除的单模板主键集合
     * @return 结果
     */
    public int deleteFMedicalFormByMedicalFormIds(Long[] medicalFormIds);

    /**
     * 删除单模板信息
     * 
     * @param medicalFormId 单模板主键
     * @return 结果
     */
    public int deleteFMedicalFormByMedicalFormId(Long medicalFormId);

    /**
     * 查询随访信息
     * @param fMedicalInfoReq
     * @return
     */
    MedicalDetailInfoVo selectFollowFMedicalFormInfo(FMedicalInfoReq fMedicalInfoReq);

    /**
     * 获取问卷详细嘻嘻
     * @param fMedicalInfoReq
     * @return
     */
    FMedicalForm getFMedicalFormExamInfo(FMedicalInfoReq fMedicalInfoReq);

    /**
     * 判定表单与模板是否有重复项
     * @param templateId 模板id
     * @param followId 随访id
     * @param templateFromIds 模块id集合
     * @return
     */
    int getFMedicalFormIsRepeat(Long templateId, Long followId, List<String> templateFromIds);

    /**
     * CRF导入模板模块信息
     * @param templateId
     * @param followId
     * @param templateFromIds
     * @return
     */
    int importOrUpdateByTemplateFromIds(Long templateId, Long followId, List<String> templateFromIds, Integer operateType);

    public int batchUpdateSortNum(FMedicalFormSortReq fMedicalFormSortReq);

    List<FMedicalForm> queryFMedicalFormList(List<Long> followId);

}
