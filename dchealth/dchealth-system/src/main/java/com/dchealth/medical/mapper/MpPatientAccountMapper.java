package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.MpPatientAccount;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @date 2023-11-08
 */
public interface MpPatientAccountMapper{
    /**
     * 查询【请填写功能名称】
     * 
     * @param personId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public MpPatientAccount selectMpPatientAccountByPersonId(String personId);

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param mpPatientAccount 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<MpPatientAccount> selectMpPatientAccountList(MpPatientAccount mpPatientAccount);

    /**
     * 新增【请填写功能名称】
     * 
     * @param mpPatientAccount 【请填写功能名称】
     * @return 结果
     */
    public int insertMpPatientAccount(MpPatientAccount mpPatientAccount);

    /**
     * 修改【请填写功能名称】
     * 
     * @param mpPatientAccount 【请填写功能名称】
     * @return 结果
     */
    public int updateMpPatientAccount(MpPatientAccount mpPatientAccount);

    /**
     * 删除【请填写功能名称】
     * 
     * @param personId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteMpPatientAccountByPersonId(String personId);

    /**
     * 批量删除【请填写功能名称】
     * 
     * @param personIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpPatientAccountByPersonIds(String[] personIds);
}
