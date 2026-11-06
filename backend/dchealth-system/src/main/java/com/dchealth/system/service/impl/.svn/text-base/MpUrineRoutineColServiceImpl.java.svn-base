package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpUrineRoutineColMapper;
import com.dchealth.system.domain.MpUrineRoutineCol;
import com.dchealth.system.service.IMpUrineRoutineColService;

/**
 * 尿常规Service业务层处理
 * 
 * @author lchbu
 * @date 2024-05-31
 */
@Service
public class MpUrineRoutineColServiceImpl implements IMpUrineRoutineColService 
{
    @Autowired
    private MpUrineRoutineColMapper mpUrineRoutineColMapper;

    /**
     * 查询尿常规
     * 
     * @param id 尿常规主键
     * @return 尿常规
     */
    @Override
    public MpUrineRoutineCol selectMpUrineRoutineColById(Long id)
    {
        return mpUrineRoutineColMapper.selectMpUrineRoutineColById(id);
    }

    /**
     * 查询尿常规列表
     * 
     * @param mpUrineRoutineCol 尿常规
     * @return 尿常规
     */
    @Override
    public List<MpUrineRoutineCol> selectMpUrineRoutineColList(MpUrineRoutineCol mpUrineRoutineCol)
    {
        return mpUrineRoutineColMapper.selectMpUrineRoutineColList(mpUrineRoutineCol);
    }

    /**
     * 新增尿常规
     * 
     * @param mpUrineRoutineCol 尿常规
     * @return 结果
     */
    @Override
    public int insertMpUrineRoutineCol(MpUrineRoutineCol mpUrineRoutineCol)
    {
        return mpUrineRoutineColMapper.insertMpUrineRoutineCol(mpUrineRoutineCol);
    }

    /**
     * 修改尿常规
     * 
     * @param mpUrineRoutineCol 尿常规
     * @return 结果
     */
    @Override
    public int updateMpUrineRoutineCol(MpUrineRoutineCol mpUrineRoutineCol)
    {
        return mpUrineRoutineColMapper.updateMpUrineRoutineCol(mpUrineRoutineCol);
    }

    /**
     * 批量删除尿常规
     * 
     * @param ids 需要删除的尿常规主键
     * @return 结果
     */
    @Override
    public int deleteMpUrineRoutineColByIds(Long[] ids)
    {
        return mpUrineRoutineColMapper.deleteMpUrineRoutineColByIds(ids);
    }

    /**
     * 删除尿常规信息
     * 
     * @param id 尿常规主键
     * @return 结果
     */
    @Override
    public int deleteMpUrineRoutineColById(Long id)
    {
        return mpUrineRoutineColMapper.deleteMpUrineRoutineColById(id);
    }
}
