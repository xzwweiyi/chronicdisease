package com.dchealth.system.service;

import java.util.List;
import com.dchealth.system.domain.MpCoagulationFunctionCol;

/**
 * 便常规Service接口
 * 
 * @author lchbu
 * @date 2024-07-26
 */
public interface IMpCoagulationFunctionColService 
{
    /**
     * 查询便常规
     * 
     * @param id 便常规主键
     * @return 便常规
     */
    public MpCoagulationFunctionCol selectMpCoagulationFunctionColById(Long id);

    /**
     * 查询便常规列表
     * 
     * @param mpCoagulationFunctionCol 便常规
     * @return 便常规集合
     */
    public List<MpCoagulationFunctionCol> selectMpCoagulationFunctionColList(MpCoagulationFunctionCol mpCoagulationFunctionCol);

    /**
     * 新增便常规
     * 
     * @param mpCoagulationFunctionCol 便常规
     * @return 结果
     */
    public int insertMpCoagulationFunctionCol(MpCoagulationFunctionCol mpCoagulationFunctionCol);

    /**
     * 修改便常规
     * 
     * @param mpCoagulationFunctionCol 便常规
     * @return 结果
     */
    public int updateMpCoagulationFunctionCol(MpCoagulationFunctionCol mpCoagulationFunctionCol);

    /**
     * 批量删除便常规
     * 
     * @param ids 需要删除的便常规主键集合
     * @return 结果
     */
    public int deleteMpCoagulationFunctionColByIds(Long[] ids);

    /**
     * 删除便常规信息
     * 
     * @param id 便常规主键
     * @return 结果
     */
    public int deleteMpCoagulationFunctionColById(Long id);
}
