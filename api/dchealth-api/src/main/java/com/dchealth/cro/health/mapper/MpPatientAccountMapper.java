package com.dchealth.cro.health.mapper;

import com.dchealth.cro.health.domain.MpPatientAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 患者用户关联Mapper接口
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Repository
public interface MpPatientAccountMapper 
{
    /**
     * 查询患者用户关联
     * 
     * @param patientId 患者用户关联主键
     * @return 患者用户关联
     */
    public MpPatientAccount selectMpPatientAccountByPatientId(String patientId);
    public MpPatientAccount selectMpPatientAccountByAccountId(Long accountId);

    public MpPatientAccount selectMpPatientAccountBypersonId(String personId);

    /**
     * 查询患者用户关联列表
     * 
     * @param mpPatientAccount 患者用户关联
     * @return 患者用户关联集合
     */
    public List<MpPatientAccount> selectMpPatientAccountList(MpPatientAccount mpPatientAccount);

    /**
     * 新增患者用户关联
     * 
     * @param mpPatientAccount 患者用户关联
     * @return 结果
     */
    public int insertMpPatientAccount(MpPatientAccount mpPatientAccount);

    /**
     * 修改患者用户关联
     * 
     * @param mpPatientAccount 患者用户关联
     * @return 结果
     */
    public int updateMpPatientAccount(MpPatientAccount mpPatientAccount);

    /**
     * 修改患者用户关联
     *
     * @param mpPatientAccount 患者用户关联
     * @return 结果
     */
    public int updateMpPatientAccountByAccountId(MpPatientAccount mpPatientAccount);

    /**
     * 删除患者用户关联
     * 
     * @param patientId 患者用户关联主键
     * @return 结果
     */
    public int deleteMpPatientAccountByPatientId(String patientId);

    /**
     * 批量删除患者用户关联
     * 
     * @param patientIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpPatientAccountByPatientIds(String[] patientIds);
    
}
