package com.dchealth.cro.medical.mapper;


import com.dchealth.cro.medical.domain.PatientInformed;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author wangpf
 * @date 2025-09-05
 */
public interface PatientInformedMapper 
{
    /**
     * 查询【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public PatientInformed selectPatientInformedById(Long id);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param patientInformed 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<PatientInformed> selectPatientInformedList(PatientInformed patientInformed);

    /**
     * 新增【请填写功能名称】
     * 
     * @param patientInformed 【请填写功能名称】
     * @return 结果
     */
    public int insertPatientInformed(PatientInformed patientInformed);

    /**
     * 修改【请填写功能名称】
     * 
     * @param patientInformed 【请填写功能名称】
     * @return 结果
     */
    public int updatePatientInformed(PatientInformed patientInformed);

    /**
     * 删除【请填写功能名称】
     * 
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deletePatientInformedById(Long id);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientInformedByIds(Long[] ids);
}
