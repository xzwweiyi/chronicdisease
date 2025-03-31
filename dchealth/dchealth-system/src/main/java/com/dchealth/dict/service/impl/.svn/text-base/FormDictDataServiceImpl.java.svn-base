package com.dchealth.dict.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.dict.mapper.FormDictDataMapper;
import com.dchealth.dict.domain.FormDictData;
import com.dchealth.dict.service.IFormDictDataService;

/**
 * 字典数据Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
@Service
public class FormDictDataServiceImpl implements IFormDictDataService 
{
    @Autowired
    private FormDictDataMapper formDictDataMapper;

    /**
     * 查询字典数据
     * 
     * @param dictCode 字典数据主键
     * @return 字典数据
     */
    @Override
    public FormDictData selectFormDictDataByDictCode(Long dictCode)
    {
        return formDictDataMapper.selectFormDictDataByDictCode(dictCode);
    }

    /**
     * 查询字典数据列表
     * 
     * @param formDictData 字典数据
     * @return 字典数据
     */
    @Override
    public List<FormDictData> selectFormDictDataList(FormDictData formDictData)
    {
        return formDictDataMapper.selectFormDictDataList(formDictData);
    }

    /**
     * 新增字典数据
     * 
     * @param formDictData 字典数据
     * @return 结果
     */
    @Override
    public int insertFormDictData(FormDictData formDictData)
    {
        formDictData.setCreateTime(DateUtils.getNowDate());
        return formDictDataMapper.insertFormDictData(formDictData);
    }

    /**
     * 修改字典数据
     * 
     * @param formDictData 字典数据
     * @return 结果
     */
    @Override
    public int updateFormDictData(FormDictData formDictData)
    {
        formDictData.setUpdateTime(DateUtils.getNowDate());
        return formDictDataMapper.updateFormDictData(formDictData);
    }

    /**
     * 批量删除字典数据
     * 
     * @param dictCodes 需要删除的字典数据主键
     * @return 结果
     */
    @Override
    public int deleteFormDictDataByDictCodes(Long[] dictCodes)
    {
        return formDictDataMapper.deleteFormDictDataByDictCodes(dictCodes);
    }

    /**
     * 删除字典数据信息
     * 
     * @param dictCode 字典数据主键
     * @return 结果
     */
    @Override
    public int deleteFormDictDataByDictCode(Long dictCode)
    {
        return formDictDataMapper.deleteFormDictDataByDictCode(dictCode);
    }
}
