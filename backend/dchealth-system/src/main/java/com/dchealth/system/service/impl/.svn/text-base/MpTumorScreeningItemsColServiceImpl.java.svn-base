package com.dchealth.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.system.mapper.MpTumorScreeningItemsColMapper;
import com.dchealth.system.domain.MpTumorScreeningItemsCol;
import com.dchealth.system.service.IMpTumorScreeningItemsColService;

/**
 * 便常规Service业务层处理
 * 
 * @author lchbu
 * @date 2024-07-26
 */
@Service
public class MpTumorScreeningItemsColServiceImpl implements IMpTumorScreeningItemsColService 
{
    @Autowired
    private MpTumorScreeningItemsColMapper mpTumorScreeningItemsColMapper;

    /**
     * 查询便常规
     * 
     * @param id 便常规主键
     * @return 便常规
     */
    @Override
    public MpTumorScreeningItemsCol selectMpTumorScreeningItemsColById(Long id)
    {
        return mpTumorScreeningItemsColMapper.selectMpTumorScreeningItemsColById(id);
    }

    /**
     * 查询便常规列表
     * 
     * @param mpTumorScreeningItemsCol 便常规
     * @return 便常规
     */
    @Override
    public List<MpTumorScreeningItemsCol> selectMpTumorScreeningItemsColList(MpTumorScreeningItemsCol mpTumorScreeningItemsCol)
    {
        return mpTumorScreeningItemsColMapper.selectMpTumorScreeningItemsColList(mpTumorScreeningItemsCol);
    }

    /**
     * 新增便常规
     * 
     * @param mpTumorScreeningItemsCol 便常规
     * @return 结果
     */
    @Override
    public int insertMpTumorScreeningItemsCol(MpTumorScreeningItemsCol mpTumorScreeningItemsCol)
    {
        return mpTumorScreeningItemsColMapper.insertMpTumorScreeningItemsCol(mpTumorScreeningItemsCol);
    }

    /**
     * 修改便常规
     * 
     * @param mpTumorScreeningItemsCol 便常规
     * @return 结果
     */
    @Override
    public int updateMpTumorScreeningItemsCol(MpTumorScreeningItemsCol mpTumorScreeningItemsCol)
    {
        return mpTumorScreeningItemsColMapper.updateMpTumorScreeningItemsCol(mpTumorScreeningItemsCol);
    }

    /**
     * 批量删除便常规
     * 
     * @param ids 需要删除的便常规主键
     * @return 结果
     */
    @Override
    public int deleteMpTumorScreeningItemsColByIds(Long[] ids)
    {
        return mpTumorScreeningItemsColMapper.deleteMpTumorScreeningItemsColByIds(ids);
    }

    /**
     * 删除便常规信息
     * 
     * @param id 便常规主键
     * @return 结果
     */
    @Override
    public int deleteMpTumorScreeningItemsColById(Long id)
    {
        return mpTumorScreeningItemsColMapper.deleteMpTumorScreeningItemsColById(id);
    }
}
