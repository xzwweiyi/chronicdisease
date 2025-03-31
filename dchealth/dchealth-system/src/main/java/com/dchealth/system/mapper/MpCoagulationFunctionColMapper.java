package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.MpCoagulationFunctionCol;

/**
 * 便常规Mapper接口
 * 
 * @author lchbu
 * @date 2024-07-26
 */
public interface MpCoagulationFunctionColMapper 
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
     * 删除便常规
     * 
     * @param id 便常规主键
     * @return 结果
     */
    public int deleteMpCoagulationFunctionColById(Long id);

    /**
     * 批量删除便常规
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpCoagulationFunctionColByIds(Long[] ids);
}
