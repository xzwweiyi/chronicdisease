package com.dchealth.system.service;

import java.util.List;
import com.dchealth.system.domain.MpStoolRoutineCol;

/**
 * 便常规Service接口
 * 
 * @author lchbu
 * @date 2024-06-03
 */
public interface IMpStoolRoutineColService 
{
    /**
     * 查询便常规
     * 
     * @param id 便常规主键
     * @return 便常规
     */
    public MpStoolRoutineCol selectMpStoolRoutineColById(Long id);

    /**
     * 查询便常规列表
     * 
     * @param mpStoolRoutineCol 便常规
     * @return 便常规集合
     */
    public List<MpStoolRoutineCol> selectMpStoolRoutineColList(MpStoolRoutineCol mpStoolRoutineCol);

    /**
     * 新增便常规
     * 
     * @param mpStoolRoutineCol 便常规
     * @return 结果
     */
    public int insertMpStoolRoutineCol(MpStoolRoutineCol mpStoolRoutineCol);

    /**
     * 修改便常规
     * 
     * @param mpStoolRoutineCol 便常规
     * @return 结果
     */
    public int updateMpStoolRoutineCol(MpStoolRoutineCol mpStoolRoutineCol);

    /**
     * 批量删除便常规
     * 
     * @param ids 需要删除的便常规主键集合
     * @return 结果
     */
    public int deleteMpStoolRoutineColByIds(Long[] ids);

    /**
     * 删除便常规信息
     * 
     * @param id 便常规主键
     * @return 结果
     */
    public int deleteMpStoolRoutineColById(Long id);
}
