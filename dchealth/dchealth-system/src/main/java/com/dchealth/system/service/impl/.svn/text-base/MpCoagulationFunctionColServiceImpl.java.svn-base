package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpCoagulationFunctionColMapper;
import com.dchealth.system.domain.MpCoagulationFunctionCol;
import com.dchealth.system.service.IMpCoagulationFunctionColService;

/**
 * 便常规Service业务层处理
 * 
 * @author lchbu
 * @date 2024-07-26
 */
@Service
public class MpCoagulationFunctionColServiceImpl implements IMpCoagulationFunctionColService 
{
    @Autowired
    private MpCoagulationFunctionColMapper mpCoagulationFunctionColMapper;

    /**
     * 查询便常规
     * 
     * @param id 便常规主键
     * @return 便常规
     */
    @Override
    public MpCoagulationFunctionCol selectMpCoagulationFunctionColById(Long id)
    {
        return mpCoagulationFunctionColMapper.selectMpCoagulationFunctionColById(id);
    }

    /**
     * 查询便常规列表
     * 
     * @param mpCoagulationFunctionCol 便常规
     * @return 便常规
     */
    @Override
    public List<MpCoagulationFunctionCol> selectMpCoagulationFunctionColList(MpCoagulationFunctionCol mpCoagulationFunctionCol)
    {
        return mpCoagulationFunctionColMapper.selectMpCoagulationFunctionColList(mpCoagulationFunctionCol);
    }

    /**
     * 新增便常规
     * 
     * @param mpCoagulationFunctionCol 便常规
     * @return 结果
     */
    @Override
    public int insertMpCoagulationFunctionCol(MpCoagulationFunctionCol mpCoagulationFunctionCol)
    {
        return mpCoagulationFunctionColMapper.insertMpCoagulationFunctionCol(mpCoagulationFunctionCol);
    }

    /**
     * 修改便常规
     * 
     * @param mpCoagulationFunctionCol 便常规
     * @return 结果
     */
    @Override
    public int updateMpCoagulationFunctionCol(MpCoagulationFunctionCol mpCoagulationFunctionCol)
    {
        return mpCoagulationFunctionColMapper.updateMpCoagulationFunctionCol(mpCoagulationFunctionCol);
    }

    /**
     * 批量删除便常规
     * 
     * @param ids 需要删除的便常规主键
     * @return 结果
     */
    @Override
    public int deleteMpCoagulationFunctionColByIds(Long[] ids)
    {
        return mpCoagulationFunctionColMapper.deleteMpCoagulationFunctionColByIds(ids);
    }

    /**
     * 删除便常规信息
     * 
     * @param id 便常规主键
     * @return 结果
     */
    @Override
    public int deleteMpCoagulationFunctionColById(Long id)
    {
        return mpCoagulationFunctionColMapper.deleteMpCoagulationFunctionColById(id);
    }
}
