package com.dchealth.medical.mapper;

import com.dchealth.medical.domain.DrugDict;

import java.util.List;

/**
 * 药品字典Mapper接口
 * 
 * @author wangpf
 * @date 2025-08-13
 */
public interface DrugDictMapper 
{
    /**
     * 查询药品字典
     * 
     * @param id 药品字典主键
     * @return 药品字典
     */
    public DrugDict selectDrugDictById(Long id);

    /**
     * 查询药品字典列表
     * 
     * @param drugDict 药品字典
     * @return 药品字典集合
     */
    public List<DrugDict> selectDrugDictList(DrugDict drugDict);

    /**
     * 新增药品字典
     * 
     * @param drugDict 药品字典
     * @return 结果
     */
    public int insertDrugDict(DrugDict drugDict);

    /**
     * 修改药品字典
     * 
     * @param drugDict 药品字典
     * @return 结果
     */
    public int updateDrugDict(DrugDict drugDict);

    /**
     * 删除药品字典
     * 
     * @param id 药品字典主键
     * @return 结果
     */
    public int deleteDrugDictById(Long id);

    /**
     * 批量删除药品字典
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteDrugDictByIds(Long[] ids);
}
