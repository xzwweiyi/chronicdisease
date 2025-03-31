package com.dchealth.dict.service;

import java.util.List;
import com.dchealth.dict.domain.FormDictType;

/**
 * 字典类型Service接口
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
public interface IFormDictTypeService 
{
    /**
     * 查询字典类型
     * 
     * @param dictId 字典类型主键
     * @return 字典类型
     */
    public FormDictType selectFormDictTypeByDictId(Long dictId);

    /**
     * 查询字典类型列表
     * 
     * @param formDictType 字典类型
     * @return 字典类型集合
     */
    public List<FormDictType> selectFormDictTypeList(FormDictType formDictType);

    /**
     * 新增字典类型
     * 
     * @param formDictType 字典类型
     * @return 结果
     */
    public int insertFormDictType(FormDictType formDictType);

    /**
     * 修改字典类型
     * 
     * @param formDictType 字典类型
     * @return 结果
     */
    public int updateFormDictType(FormDictType formDictType);

    /**
     * 批量删除字典类型
     * 
     * @param dictIds 需要删除的字典类型主键集合
     * @return 结果
     */
    public int deleteFormDictTypeByDictIds(Long[] dictIds);

    /**
     * 删除字典类型信息
     * 
     * @param dictId 字典类型主键
     * @return 结果
     */
    public int deleteFormDictTypeByDictId(Long dictId);
}
