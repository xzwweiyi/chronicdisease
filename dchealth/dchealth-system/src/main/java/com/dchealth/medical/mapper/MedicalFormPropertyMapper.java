package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.MedicalFormProperty;

import java.util.List;

/**
 * 随访单属性Mapper接口
 * 
 * @author wangpf
 * @date 2026-01-08
 */
public interface MedicalFormPropertyMapper 
{
    /**
     * 查询随访单属性
     * 
     * @param id 随访单属性主键
     * @return 随访单属性
     */
    public MedicalFormProperty selectMedicalFormPropertyById(Long id);

    /**
     * 查询随访单属性列表
     * 
     * @param medicalFormProperty 随访单属性
     * @return 随访单属性集合
     */
    public List<MedicalFormProperty> selectMedicalFormPropertyList(MedicalFormProperty medicalFormProperty);

    /**
     * 新增随访单属性
     * 
     * @param medicalFormProperty 随访单属性
     * @return 结果
     */
    public int insertMedicalFormProperty(MedicalFormProperty medicalFormProperty);


    public int batchInsertMedicalFormProperty(List<MedicalFormProperty> list);

    /**
     * 修改随访单属性
     * 
     * @param medicalFormProperty 随访单属性
     * @return 结果
     */
    public int updateMedicalFormProperty(MedicalFormProperty medicalFormProperty);

    /**
     * 删除随访单属性
     * 
     * @param id 随访单属性主键
     * @return 结果
     */
    public int deleteMedicalFormPropertyById(Long id);

    /**
     * 批量删除随访单属性
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMedicalFormPropertyByIds(Long[] ids);

    List<MedicalFormProperty> selectFollowPatientInfoList(MedicalFormProperty medicalFormProperty);
}
