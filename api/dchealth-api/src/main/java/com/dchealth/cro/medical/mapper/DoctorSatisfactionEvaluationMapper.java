package com.dchealth.cro.medical.mapper;


import com.dchealth.cro.medical.domain.DoctorSatisfactionEvaluation;
import com.dchealth.cro.medical.form.SatisfactionForm;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface DoctorSatisfactionEvaluationMapper {
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


    public List<DoctorSatisfactionEvaluation> selectListByForm(SatisfactionForm satisfactionForm);

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
