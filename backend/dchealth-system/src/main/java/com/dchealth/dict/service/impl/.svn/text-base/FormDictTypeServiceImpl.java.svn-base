package com.dchealth.dict.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.dict.mapper.FormDictTypeMapper;
import com.dchealth.dict.domain.FormDictType;
import com.dchealth.dict.service.IFormDictTypeService;

/**
 * 字典类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
@Service
public class FormDictTypeServiceImpl implements IFormDictTypeService 
{
    @Autowired
    private FormDictTypeMapper formDictTypeMapper;

    /**
     * 查询字典类型
     * 
     * @param dictId 字典类型主键
     * @return 字典类型
     */
    @Override
    public FormDictType selectFormDictTypeByDictId(Long dictId)
    {
        return formDictTypeMapper.selectFormDictTypeByDictId(dictId);
    }

    /**
     * 查询字典类型列表
     * 
     * @param formDictType 字典类型
     * @return 字典类型
     */
    @Override
    public List<FormDictType> selectFormDictTypeList(FormDictType formDictType)
    {
        return formDictTypeMapper.selectFormDictTypeList(formDictType);
    }

    /**
     * 新增字典类型
     * 
     * @param formDictType 字典类型
     * @return 结果
     */
    @Override
    public int insertFormDictType(FormDictType formDictType)
    {
        formDictType.setCreateTime(DateUtils.getNowDate());
        return formDictTypeMapper.insertFormDictType(formDictType);
    }

    /**
     * 修改字典类型
     * 
     * @param formDictType 字典类型
     * @return 结果
     */
    @Override
    public int updateFormDictType(FormDictType formDictType)
    {
        formDictType.setUpdateTime(DateUtils.getNowDate());
        return formDictTypeMapper.updateFormDictType(formDictType);
    }

    /**
     * 批量删除字典类型
     * 
     * @param dictIds 需要删除的字典类型主键
     * @return 结果
     */
    @Override
    public int deleteFormDictTypeByDictIds(Long[] dictIds)
    {
        return formDictTypeMapper.deleteFormDictTypeByDictIds(dictIds);
    }

    /**
     * 删除字典类型信息
     * 
     * @param dictId 字典类型主键
     * @return 结果
     */
    @Override
    public int deleteFormDictTypeByDictId(Long dictId)
    {
        return formDictTypeMapper.deleteFormDictTypeByDictId(dictId);
    }
}
