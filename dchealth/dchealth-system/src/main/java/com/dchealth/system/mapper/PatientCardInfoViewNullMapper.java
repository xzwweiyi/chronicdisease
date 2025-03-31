package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.PatientCardInfoViewNull;

/**
 * 空时患者号Mapper接口
 * 
 * @author lchbu
 * @date 2024-11-23
 */
public interface PatientCardInfoViewNullMapper 
{
    /**
     * 查询空时患者号
     * 
     * @param id 空时患者号主键
     * @return 空时患者号
     */
    public PatientCardInfoViewNull selectPatientCardInfoViewNullById(Long id);

    /**
     * 查询空时患者号列表
     * 
     * @param patientCardInfoViewNull 空时患者号
     * @return 空时患者号集合
     */
    public List<PatientCardInfoViewNull> selectPatientCardInfoViewNullList(PatientCardInfoViewNull patientCardInfoViewNull);

    /**
     * 新增空时患者号
     * 
     * @param patientCardInfoViewNull 空时患者号
     * @return 结果
     */
    public int insertPatientCardInfoViewNull(PatientCardInfoViewNull patientCardInfoViewNull);

    /**
     * 修改空时患者号
     * 
     * @param patientCardInfoViewNull 空时患者号
     * @return 结果
     */
    public int updatePatientCardInfoViewNull(PatientCardInfoViewNull patientCardInfoViewNull);

    /**
     * 删除空时患者号
     * 
     * @param id 空时患者号主键
     * @return 结果
     */
    public int deletePatientCardInfoViewNullById(Long id);

    /**
     * 批量删除空时患者号
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientCardInfoViewNullByIds(Long[] ids);
}
