package com.dchealth.dict.mapper;

import java.util.List;
import com.dchealth.dict.domain.FormDictData;

/**
 * 字典数据Mapper接口
 * 
 * @author ruoyi
 * @date 2022-03-29
 */
public interface FormDictDataMapper 
{
    /**
     * 查询字典数据
     * 
     * @param dictCode 字典数据主键
     * @return 字典数据
     */
    public FormDictData selectFormDictDataByDictCode(Long dictCode);

    /**
     * 查询字典数据列表
     * 
     * @param formDictData 字典数据
     * @return 字典数据集合
     */
    public List<FormDictData> selectFormDictDataList(FormDictData formDictData);

    /**
     * 新增字典数据
     * 
     * @param formDictData 字典数据
     * @return 结果
     */
    public int insertFormDictData(FormDictData formDictData);

    /**
     * 修改字典数据
     * 
     * @param formDictData 字典数据
     * @return 结果
     */
    public int updateFormDictData(FormDictData formDictData);

    /**
     * 删除字典数据
     * 
     * @param dictCode 字典数据主键
     * @return 结果
     */
    public int deleteFormDictDataByDictCode(Long dictCode);

    /**
     * 批量删除字典数据
     * 
     * @param dictCodes 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFormDictDataByDictCodes(Long[] dictCodes);
}
