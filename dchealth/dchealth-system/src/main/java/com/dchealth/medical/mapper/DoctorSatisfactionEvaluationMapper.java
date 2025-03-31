package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.DoctorSatisfactionEvaluation;

import java.util.List;


public interface DoctorSatisfactionEvaluationMapper{
    /**
     * 查询医生满意度评价
     * 
     * @param id 医生满意度评价主键
     * @return 医生满意度评价
     */
    public DoctorSatisfactionEvaluation selectDoctorSatisfactionEvaluationById(Long id);

    /**
     * 查询医生满意度评价列表
     * 
     * @param doctorSatisfactionEvaluation 医生满意度评价
     * @return 医生满意度评价集合
     */
    public List<DoctorSatisfactionEvaluation> selectDoctorSatisfactionEvaluationList(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation);

    /**
     * 新增医生满意度评价
     * 
     * @param doctorSatisfactionEvaluation 医生满意度评价
     * @return 结果
     */
    public int insertDoctorSatisfactionEvaluation(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation);

    public Integer getSumScore();

    /**
     * 修改医生满意度评价
     * 
     * @param doctorSatisfactionEvaluation 医生满意度评价
     * @return 结果
     */
    public int updateDoctorSatisfactionEvaluation(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation);

    /**
     * 删除医生满意度评价
     * 
     * @param id 医生满意度评价主键
     * @return 结果
     */
    public int deleteDoctorSatisfactionEvaluationById(Long id);

    /**
     * 批量删除医生满意度评价
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDoctorSatisfactionEvaluationByIds(Long[] ids);
}
