package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpStoolRoutineColMapper;
import com.dchealth.system.domain.MpStoolRoutineCol;
import com.dchealth.system.service.IMpStoolRoutineColService;

/**
 * 便常规Service业务层处理
 * 
 * @author lchbu
 * @date 2024-06-03
 */
@Service
public class MpStoolRoutineColServiceImpl implements IMpStoolRoutineColService 
{
    @Autowired
    private MpStoolRoutineColMapper mpStoolRoutineColMapper;

    /**
     * 查询便常规
     * 
     * @param id 便常规主键
     * @return 便常规
     */
    @Override
    public MpStoolRoutineCol selectMpStoolRoutineColById(Long id)
    {
        return mpStoolRoutineColMapper.selectMpStoolRoutineColById(id);
    }

    /**
     * 查询便常规列表
     * 
     * @param mpStoolRoutineCol 便常规
     * @return 便常规
     */
    @Override
    public List<MpStoolRoutineCol> selectMpStoolRoutineColList(MpStoolRoutineCol mpStoolRoutineCol)
    {
        return mpStoolRoutineColMapper.selectMpStoolRoutineColList(mpStoolRoutineCol);
    }

    /**
     * 新增便常规
     * 
     * @param mpStoolRoutineCol 便常规
     * @return 结果
     */
    @Override
    public int insertMpStoolRoutineCol(MpStoolRoutineCol mpStoolRoutineCol)
    {
        return mpStoolRoutineColMapper.insertMpStoolRoutineCol(mpStoolRoutineCol);
    }

    /**
     * 修改便常规
     * 
     * @param mpStoolRoutineCol 便常规
     * @return 结果
     */
    @Override
    public int updateMpStoolRoutineCol(MpStoolRoutineCol mpStoolRoutineCol)
    {
        return mpStoolRoutineColMapper.updateMpStoolRoutineCol(mpStoolRoutineCol);
    }

    /**
     * 批量删除便常规
     * 
     * @param ids 需要删除的便常规主键
     * @return 结果
     */
    @Override
    public int deleteMpStoolRoutineColByIds(Long[] ids)
    {
        return mpStoolRoutineColMapper.deleteMpStoolRoutineColByIds(ids);
    }

    /**
     * 删除便常规信息
     * 
     * @param id 便常规主键
     * @return 结果
     */
    @Override
    public int deleteMpStoolRoutineColById(Long id)
    {
        return mpStoolRoutineColMapper.deleteMpStoolRoutineColById(id);
    }
}
