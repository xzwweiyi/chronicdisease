package com.dchealth.medical.service.impl;

import com.dchealth.medical.domain.DrugDict;
import com.dchealth.medical.mapper.DrugDictMapper;
import com.dchealth.medical.service.IDrugDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 药品字典Service业务层处理
 * 
 * @author wangpf
 * @date 2025-08-13
 */
@Service
public class DrugDictServiceImpl implements IDrugDictService
{
    @Autowired
    private DrugDictMapper drugDictMapper;

    /**
     * 查询药品字典
     * 
     * @param id 药品字典主键
     * @return 药品字典
     */
    @Override
    public DrugDict selectDrugDictById(Long id)
    {
        return drugDictMapper.selectDrugDictById(id);
    }

    /**
     * 查询药品字典列表
     * 
     * @param drugDict 药品字典
     * @return 药品字典
     */
    @Override
    public List<DrugDict> selectDrugDictList(DrugDict drugDict)
    {
        return drugDictMapper.selectDrugDictList(drugDict);
    }

    /**
     * 新增药品字典
     * 
     * @param drugDict 药品字典
     * @return 结果
     */
    @Override
    public int insertDrugDict(DrugDict drugDict)
    {
        return drugDictMapper.insertDrugDict(drugDict);
    }

    /**
     * 修改药品字典
     * 
     * @param drugDict 药品字典
     * @return 结果
     */
    @Override
    public int updateDrugDict(DrugDict drugDict)
    {
        return drugDictMapper.updateDrugDict(drugDict);
    }

    /**
     * 批量删除药品字典
     * 
     * @param ids 需要删除的药品字典主键
     * @return 结果
     */
    @Override
    public int deleteDrugDictByIds(Long[] ids)
    {
        return drugDictMapper.deleteDrugDictByIds(ids);
    }

    /**
     * 删除药品字典信息
     * 
     * @param id 药品字典主键
     * @return 结果
     */
    @Override
    public int deleteDrugDictById(Long id)
    {
        return drugDictMapper.deleteDrugDictById(id);
    }
}
