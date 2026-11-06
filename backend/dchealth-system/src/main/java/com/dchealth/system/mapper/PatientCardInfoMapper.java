package com.dchealth.system.mapper;

import java.util.List;

import com.dchealth.common.core.domain.entity.SysDictData;
import com.dchealth.system.domain.PatientCardInfo;

/**
 * 患者主号管理Mapper接口
 * 
 * @author a
 * @date 2024-10-30
 */
public interface PatientCardInfoMapper 
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

    /**
     * 修改患者主号管理
     * 
     * @param patientCardInfo 患者主号管理
     * @return 结果
     */
    public int updatePatientCardInfo(PatientCardInfo patientCardInfo);
    public int updatePatientCardInfoAll(PatientCardInfo patientCardInfo);

    /**
     * 删除患者主号管理
     * 
     * @param id 患者主号管理主键
     * @return 结果
     */
    public int deletePatientCardInfoById(Long id);

    /**
     * 批量删除患者主号管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientCardInfoByIds(Long[] ids);

    public int updatePatientAllPersonId(SysDictData cardInfo);
    public int updatePatientAllPersonIdOfPatientAccount();

    void updateMasterPeson();
}

