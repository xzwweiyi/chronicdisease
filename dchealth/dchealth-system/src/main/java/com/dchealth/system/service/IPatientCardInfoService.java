package com.dchealth.system.service;

import java.util.List;
import java.util.Map;

import com.dchealth.system.domain.PatientCardInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * 患者主号管理Service接口
 * 
 * @author a
 * @date 2024-10-30
 */
public interface IPatientCardInfoService 
{
    /**
     * 查询患者主号管理
     * 
     * @param id 患者主号管理主键
     * @return 患者主号管理
     */
    public PatientCardInfo selectPatientCardInfoById(Long id);

    /**
     * 查询患者主号管理列表
     * 
     * @param patientCardInfo 患者主号管理
     * @return 患者主号管理集合
     */
    public List<PatientCardInfo> selectPatientCardInfoList(PatientCardInfo patientCardInfo);

    /**
     * 新增患者主号管理
     * 
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    public int insertPatientCardInfo(PatientCardInfo patientCardInfo);

    @Transactional
    int updateMasterNumber(PatientCardInfo patientCardInfo);

    /**
     * 修改患者主号管理
     * 
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    public int updatePatientCardInfo(PatientCardInfo patientCardInfo);

    Map<String, Integer> updatePatientAllPersonId();

    void syncToMasterTable();

    void updateMasterPeson();

    /**
     * 批量删除患者主号管理
     * 
     * @param ids 需要删除的患者主号管理主键集合
     * @return 结果
     */
    public int deletePatientCardInfoByIds(Long[] ids);

    /**
     * 删除患者主号管理信息
     * 
     * @param id 患者主号管理主键
     * @return 结果
     */
    public int deletePatientCardInfoById(Long id);
}
