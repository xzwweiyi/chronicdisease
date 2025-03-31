package com.dchealth.dict.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.dict.domain.FMedicalDictType;
import com.dchealth.dict.mapper.FMedicalDictTypeMapper;
import com.dchealth.dict.service.IFMedicalDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 字典类型Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-06
 */
@Service
public class FMedicalDictTypeServiceImpl implements IFMedicalDictTypeService
{
    @Autowired
    private FMedicalDictTypeMapper fMedicalDictTypeMapper;

    /**
     * 查询字典类型
     *
     * @param dictId 字典类型主键
     * @return 字典类型
     */
    @Override
    public FMedicalDictType selectFMedicalDictTypeByDictId(Long dictId)
    {
        return fMedicalDictTypeMapper.selectFMedicalDictTypeByDictId(dictId);
    }

    /**
     * 查询字典类型列表
     *
     * @param fMedicalDictType 字典类型
     * @return 字典类型
     */
    @Override
    public List<FMedicalDictType> selectFMedicalDictTypeList(FMedicalDictType fMedicalDictType)
    {
        return fMedicalDictTypeMapper.selectFMedicalDictTypeList(fMedicalDictType);
    }

    /**
     * 新增字典类型
     *
     * @param fMedicalDictType 字典类型
     * @return 结果
     */
    @Override
    public int insertFMedicalDictType(FMedicalDictType fMedicalDictType)
    {
        fMedicalDictType.setCreateTime(DateUtils.getNowDate());
        return fMedicalDictTypeMapper.insertFMedicalDictType(fMedicalDictType);
    }

    /**
     * 修改字典类型
     *
     * @param fMedicalDictType 字典类型
     * @return 结果
     */
    @Override
    public int updateFMedicalDictType(FMedicalDictType fMedicalDictType)
    {
        fMedicalDictType.setUpdateTime(DateUtils.getNowDate());
        return fMedicalDictTypeMapper.updateFMedicalDictType(fMedicalDictType);
    }

    /**
     * 批量删除字典类型
     *
     * @param dictIds 需要删除的字典类型主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalDictTypeByDictIds(Long[] dictIds)
    {
        return fMedicalDictTypeMapper.deleteFMedicalDictTypeByDictIds(dictIds);
    }

    /**
     * 删除字典类型信息
     *
     * @param dictId 字典类型主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalDictTypeByDictId(Long dictId)
    {
        return fMedicalDictTypeMapper.deleteFMedicalDictTypeByDictId(dictId);
    }
}

