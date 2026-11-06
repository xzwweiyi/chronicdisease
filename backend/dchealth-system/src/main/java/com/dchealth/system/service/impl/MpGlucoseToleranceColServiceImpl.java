package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpGlucoseToleranceColMapper;
import com.dchealth.system.domain.MpGlucoseToleranceCol;
import com.dchealth.system.service.IMpGlucoseToleranceColService;

/**
 * 肝or肾Service业务层处理
 * 
 * @author lchbu
 * @date 2024-08-12
 */
@Service
public class MpGlucoseToleranceColServiceImpl implements IMpGlucoseToleranceColService 
{
    @Autowired
    private MpGlucoseToleranceColMapper mpGlucoseToleranceColMapper;

    /**
     * 查询肝or肾
     * 
     * @param id 肝or肾主键
     * @return 肝or肾
     */
    @Override
    public MpGlucoseToleranceCol selectMpGlucoseToleranceColById(Long id)
    {
        return mpGlucoseToleranceColMapper.selectMpGlucoseToleranceColById(id);
    }

    /**
     * 查询肝or肾列表
     * 
     * @param mpGlucoseToleranceCol 肝or肾
     * @return 肝or肾
     */
    @Override
    public List<MpGlucoseToleranceCol> selectMpGlucoseToleranceColList(MpGlucoseToleranceCol mpGlucoseToleranceCol)
    {
        return mpGlucoseToleranceColMapper.selectMpGlucoseToleranceColList(mpGlucoseToleranceCol);
    }

    /**
     * 新增肝or肾
     * 
     * @param mpGlucoseToleranceCol 肝or肾
     * @return 结果
     */
    @Override
    public int insertMpGlucoseToleranceCol(MpGlucoseToleranceCol mpGlucoseToleranceCol)
    {
        return mpGlucoseToleranceColMapper.insertMpGlucoseToleranceCol(mpGlucoseToleranceCol);
    }

    /**
     * 修改肝or肾
     * 
     * @param mpGlucoseToleranceCol 肝or肾
     * @return 结果
     */
    @Override
    public int updateMpGlucoseToleranceCol(MpGlucoseToleranceCol mpGlucoseToleranceCol)
    {
        return mpGlucoseToleranceColMapper.updateMpGlucoseToleranceCol(mpGlucoseToleranceCol);
    }

    /**
     * 批量删除肝or肾
     * 
     * @param ids 需要删除的肝or肾主键
     * @return 结果
     */
    @Override
    public int deleteMpGlucoseToleranceColByIds(Long[] ids)
    {
        return mpGlucoseToleranceColMapper.deleteMpGlucoseToleranceColByIds(ids);
    }

    /**
     * 删除肝or肾信息
     * 
     * @param id 肝or肾主键
     * @return 结果
     */
    @Override
    public int deleteMpGlucoseToleranceColById(Long id)
    {
        return mpGlucoseToleranceColMapper.deleteMpGlucoseToleranceColById(id);
    }
}
