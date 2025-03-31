package com.dchealth.medical.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.service.IMpPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 患者信息-对应omopService业务层处理
 * 
 * @author lchbu
 * @date 2023-11-08
 */
@Service
public class MpPersonServiceImpl implements IMpPersonService {
    @Autowired
    private MpPersonMapper mpPersonMapper;

    /**
     * 查询患者信息-对应omop
     * 
     * @param id 患者信息-对应omop主键
     * @return 患者信息-对应omop
     */
    @Override
    public MpPerson selectMpPersonById(Long id)
    {
        return mpPersonMapper.selectMpPersonById(id);
    }

    /**
     * 查询患者信息-对应omop列表
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 患者信息-对应omop
     */
    @Override
    public List<MpPerson> selectMpPersonList(MpPerson mpPerson)
    {
        return mpPersonMapper.selectMpPersonList(mpPerson);
    }

    /**
     * 新增患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    @Override
    public int insertMpPerson(MpPerson mpPerson)
    {
        mpPerson.setCreateTime(DateUtils.getNowDate());
        return mpPersonMapper.insertMpPerson(mpPerson);
    }

    /**
     * 修改患者信息-对应omop
     * 
     * @param mpPerson 患者信息-对应omop
     * @return 结果
     */
    @Override
    public int updateMpPerson(MpPerson mpPerson)
    {
        mpPerson.setUpdateTime(DateUtils.getNowDate());
        return mpPersonMapper.updateMpPerson(mpPerson);
    }

    /**
     * 批量删除患者信息-对应omop
     * 
     * @param ids 需要删除的患者信息-对应omop主键
     * @return 结果
     */
    @Override
    public int deleteMpPersonByIds(Long[] ids)
    {
        return mpPersonMapper.deleteMpPersonByIds(ids);
    }

    /**
     * 删除患者信息-对应omop信息
     * 
     * @param id 患者信息-对应omop主键
     * @return 结果
     */
    @Override
    public int deleteMpPersonById(Long id)
    {
        return mpPersonMapper.deleteMpPersonById(id);
    }
}
