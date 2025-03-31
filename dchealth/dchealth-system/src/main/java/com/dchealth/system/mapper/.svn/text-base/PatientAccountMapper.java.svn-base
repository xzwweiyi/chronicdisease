package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.PatientAccount;

/**
 * 患者账号表Mapper接口
 *
 * @author lchbu
 * @date 2022-09-14
 */
public interface PatientAccountMapper
{
    /**
     * 查询患者账号表
     *
     * @param accountId 患者账号表主键
     * @return 患者账号表
     */
    public PatientAccount selectPatientAccountByAccountId(Long accountId);

    /**
     * 查询患者账号表列表
     *
     * @param patientAccount 患者账号表
     * @return 患者账号表集合
     */
    public List<PatientAccount> selectPatientAccountList(PatientAccount patientAccount);

    /**
     * 新增患者账号表
     *
     * @param patientAccount 患者账号表
     * @return 结果
     */
    public int insertPatientAccount(PatientAccount patientAccount);

    /**
     * 修改患者账号表
     *
     * @param patientAccount 患者账号表
     * @return 结果
     */
    public int updatePatientAccount(PatientAccount patientAccount);

    /**
     * 删除患者账号表
     *
     * @param accountId 患者账号表主键
     * @return 结果
     */
    public int deletePatientAccountByAccountId(Long accountId);

    /**
     * 批量删除患者账号表
     *
     * @param accountIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePatientAccountByAccountIds(Long[] accountIds);
}
