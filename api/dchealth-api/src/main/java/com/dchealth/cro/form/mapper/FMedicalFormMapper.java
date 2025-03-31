package com.dchealth.cro.form.mapper;

import com.dchealth.cro.form.domain.FMedicalForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单模板Mapper接口
 * 
 * @author ruoyi
 * @date 2022-09-09
 */
@Repository
public interface FMedicalFormMapper 
{
    /**
     * 查询单模板
     * 
     * @param medicalFormId 单模板主键
     * @return 单模板
     */
    public FMedicalForm selectFMedicalFormByMedicalFormId(Long medicalFormId);

    /**
     * 根据随访id获取随访表单
     * @param followId
     * @return
     */
    public List<FMedicalForm> selectFMedicalFormByMedicalFollowId(Long followId);

    public List<FMedicalForm> selectFMedicalFormJoinByMedicalFollowId(@Param("followId") Long followId, @Param("recordId") Long recordId);

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
     * 修改单模板
     * 
     * @param fMedicalForm 单模板
     * @return 结果
     */
    public int updateFMedicalForm(FMedicalForm fMedicalForm);

    /**
     * 删除单模板
     * 
     * @param medicalFormId 单模板主键
     * @return 结果
     */
    public int deleteFMedicalFormByMedicalFormId(Long medicalFormId);

    /**
     * 批量删除单模板
     * 
     * @param medicalFormIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalFormByMedicalFormIds(Long[] medicalFormIds);
}
