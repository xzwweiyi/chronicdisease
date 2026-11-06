package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpBloodRoutineColMapper;
import com.dchealth.system.domain.MpBloodRoutineCol;
import com.dchealth.system.service.IMpBloodRoutineColService;

/**
 * 血常规Service业务层处理
 * 
 * @author lchbu
 * @date 2024-06-12
 */
@Service
public class MpBloodRoutineColServiceImpl implements IMpBloodRoutineColService 
{
    @Autowired
    private MpBloodRoutineColMapper mpBloodRoutineColMapper;

    /**
     * 查询血常规
     * 
     * @param id 血常规主键
     * @return 血常规
     */
    @Override
    public MpBloodRoutineCol selectMpBloodRoutineColById(Long id)
    {
        return mpBloodRoutineColMapper.selectMpBloodRoutineColById(id);
    }

    /**
     * 查询血常规列表
     * 
     * @param mpBloodRoutineCol 血常规
     * @return 血常规
     */
    @Override
    public List<MpBloodRoutineCol> selectMpBloodRoutineColList(MpBloodRoutineCol mpBloodRoutineCol)
    {
        return mpBloodRoutineColMapper.selectMpBloodRoutineColList(mpBloodRoutineCol);
    }

    /**
     * 新增血常规
     * 
     * @param mpBloodRoutineCol 血常规
     * @return 结果
     */
    @Override
    public int insertMpBloodRoutineCol(MpBloodRoutineCol mpBloodRoutineCol)
    {
        return mpBloodRoutineColMapper.insertMpBloodRoutineCol(mpBloodRoutineCol);
    }

    /**
     * 修改血常规
     * 
     * @param mpBloodRoutineCol 血常规
     * @return 结果
     */
    @Override
    public int updateMpBloodRoutineCol(MpBloodRoutineCol mpBloodRoutineCol)
    {
        return mpBloodRoutineColMapper.updateMpBloodRoutineCol(mpBloodRoutineCol);
    }

    /**
     * 批量删除血常规
     * 
     * @param ids 需要删除的血常规主键
     * @return 结果
     */
    @Override
    public int deleteMpBloodRoutineColByIds(Long[] ids)
    {
        return mpBloodRoutineColMapper.deleteMpBloodRoutineColByIds(ids);
    }

    /**
     * 删除血常规信息
     * 
     * @param id 血常规主键
     * @return 结果
     */
    @Override
    public int deleteMpBloodRoutineColById(Long id)
    {
        return mpBloodRoutineColMapper.deleteMpBloodRoutineColById(id);
    }
}
