package com.dchealth.medical.service;

import com.dchealth.medical.domain.MpPerson;

import java.util.List;

/**
 * 患者信息-对应omopService接口
 * 
 * @author lchbu
 * @date 2023-11-08
 */
public interface IMpPersonService{
    /**
     * 查询患者信息-对应omop
     * 
     * @param id 患者信息-对应omop主键
     * @return 患者信息-对应omop
     */
    public MpPerson selectMpPersonById(Long id);

    /**
     * 查询患者信息-对应omop列表
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 患者信息-对应omop集合
     */
    public List<MpPerson> selectMpPersonList(MpPerson mpPerson);

    /**
     * 新增患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    public int insertMpPerson(MpPerson mpPerson);

    /**
     * 修改患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    public int updateMpPerson(MpPerson mpPerson);

    /**
     * 批量删除患者信息-对应omop
     * 
     * @param ids 需要删除的患者信息-对应omop主键集合
     * @return 结果
     */
    public int deleteMpPersonByIds(Long[] ids);

    /**
     * 删除患者信息-对应omop信息
     * 
     * @param id 患者信息-对应omop主键
     * @return 结果
     */
    public int deleteMpPersonById(Long id);
}
