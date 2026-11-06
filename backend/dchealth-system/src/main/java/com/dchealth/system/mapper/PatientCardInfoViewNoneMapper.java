package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.PatientCardInfoViewNone;

/**
 * 增量患者号Mapper接口
 * 
 * @author lchbu
 * @date 2024-11-23
 */
public interface PatientCardInfoViewNoneMapper 
{
    /**
     * 查询增量患者号
     * 
     * @param id 增量患者号主键
     * @return 增量患者号
     */
    public PatientCardInfoViewNone selectPatientCardInfoViewNoneById(Long id);

    /**
     * 查询增量患者号列表
     * 
     * @param patientCardInfoViewNone 增量患者号
     * @return 增量患者号集合
     */
    public List<PatientCardInfoViewNone> selectPatientCardInfoViewNoneList(PatientCardInfoViewNone patientCardInfoViewNone);

    /**
     * 新增增量患者号
     * 
     * @param patientCardInfoViewNone 增量患者号
     * @return 结果
     */
    public int insertPatientCardInfoViewNone(PatientCardInfoViewNone patientCardInfoViewNone);

    /**
     * 修改增量患者号
     * 
     * @param patientCardInfoViewNone 增量患者号
     * @return 结果
     */
    public int updatePatientCardInfoViewNone(PatientCardInfoViewNone patientCardInfoViewNone);

    /**
     * 删除增量患者号
     * 
     * @param id 增量患者号主键
     * @return 结果
     */
    public int deletePatientCardInfoViewNoneById(Long id);

    /**
     * 批量删除增量患者号
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientCardInfoViewNoneByIds(Long[] ids);
}
