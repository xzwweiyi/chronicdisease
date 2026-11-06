package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.DoctorSatisfactionEvaluation;
import com.dchealth.medical.mapper.DoctorSatisfactionEvaluationMapper;
import com.dchealth.medical.service.IDoctorSatisfactionEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DoctorSatisfactionEvaluationServiceImpl implements IDoctorSatisfactionEvaluationService {
    @Autowired
    private DoctorSatisfactionEvaluationMapper doctorSatisfactionEvaluationMapper;

    /**
     * 查询医生满意度评价
     * 
     * @param id 医生满意度评价主键
     * @return 医生满意度评价
     */
    @Override
    public DoctorSatisfactionEvaluation selectDoctorSatisfactionEvaluationById(Long id)
    {
        return doctorSatisfactionEvaluationMapper.selectDoctorSatisfactionEvaluationById(id);
    }

    /**
     * 查询医生满意度评价列表
     * 
     * @param doctorSatisfactionEvaluation 医生满意度评价
     * @return 医生满意度评价
     */
    @Override
    public List<DoctorSatisfactionEvaluation> selectDoctorSatisfactionEvaluationList(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation)
    {
        return doctorSatisfactionEvaluationMapper.selectDoctorSatisfactionEvaluationList(doctorSatisfactionEvaluation);
    }

    /**
     * 新增医生满意度评价
     * 
     * @param doctorSatisfactionEvaluation 医生满意度评价
     * @return 结果
     */
    @Override
    public int insertDoctorSatisfactionEvaluation(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation)
    {
        doctorSatisfactionEvaluation.setCreateTime(DateUtils.getNowDate());
        return doctorSatisfactionEvaluationMapper.insertDoctorSatisfactionEvaluation(doctorSatisfactionEvaluation);
    }

    @Override
    public Integer getSumScore() {
        return doctorSatisfactionEvaluationMapper.getSumScore();
    }

    /**
     * 修改医生满意度评价
     * 
     * @param doctorSatisfactionEvaluation 医生满意度评价
     * @return 结果
     */
    @Override
    public int updateDoctorSatisfactionEvaluation(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation)
    {
        return doctorSatisfactionEvaluationMapper.updateDoctorSatisfactionEvaluation(doctorSatisfactionEvaluation);
    }

    /**
     * 批量删除医生满意度评价
     * 
     * @param ids 需要删除的医生满意度评价主键
     * @return 结果
     */
    @Override
    public int deleteDoctorSatisfactionEvaluationByIds(Long[] ids)
    {
        return doctorSatisfactionEvaluationMapper.deleteDoctorSatisfactionEvaluationByIds(ids);
    }

    /**
     * 删除医生满意度评价信息
     * 
     * @param id 医生满意度评价主键
     * @return 结果
     */
    @Override
    public int deleteDoctorSatisfactionEvaluationById(Long id)
    {
        return doctorSatisfactionEvaluationMapper.deleteDoctorSatisfactionEvaluationById(id);
    }
}
