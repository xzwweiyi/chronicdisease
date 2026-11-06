package com.dchealth.dict.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.dict.domain.FMedicalDictData;
import com.dchealth.dict.mapper.FMedicalDictDataMapper;
import com.dchealth.dict.service.IFMedicalDictDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-06
 */
@Service
public class FMedicalDictDataServiceImpl implements IFMedicalDictDataService
{
    @Autowired
    private FMedicalDictDataMapper fMedicalDictDataMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public FMedicalDictData selectFMedicalDictDataById(Long id)
    {
        return fMedicalDictDataMapper.selectFMedicalDictDataById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param fMedicalDictData 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<FMedicalDictData> selectFMedicalDictDataList(FMedicalDictData fMedicalDictData)
    {
        return fMedicalDictDataMapper.selectFMedicalDictDataList(fMedicalDictData);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param fMedicalDictData 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertFMedicalDictData(FMedicalDictData fMedicalDictData)
    {
        fMedicalDictData.setCreateTime(DateUtils.getNowDate());
        return fMedicalDictDataMapper.insertFMedicalDictData(fMedicalDictData);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param fMedicalDictData 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateFMedicalDictData(FMedicalDictData fMedicalDictData)
    {
        return fMedicalDictDataMapper.updateFMedicalDictData(fMedicalDictData);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalDictDataByIds(Long[] ids)
    {
        return fMedicalDictDataMapper.deleteFMedicalDictDataByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteFMedicalDictDataById(Long id)
    {
        return fMedicalDictDataMapper.deleteFMedicalDictDataById(id);
    }
}

