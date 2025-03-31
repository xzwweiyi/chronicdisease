package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.MpBloodRoutineCol;

/**
 * 血常规Mapper接口
 * 
 * @author lchbu
 * @date 2024-06-12
 */
public interface MpBloodRoutineColMapper 
{
    /**
     * 查询血常规
     * 
     * @param id 血常规主键
     * @return 血常规
     */
    public MpBloodRoutineCol selectMpBloodRoutineColById(Long id);

    /**
     * 查询血常规列表
     * 
     * @param mpBloodRoutineCol 血常规
     * @return 血常规集合
     */
    public List<MpBloodRoutineCol> selectMpBloodRoutineColList(MpBloodRoutineCol mpBloodRoutineCol);

    /**
     * 新增血常规
     * 
     * @param mpBloodRoutineCol 血常规
     * @return 结果
     */
    public int insertMpBloodRoutineCol(MpBloodRoutineCol mpBloodRoutineCol);

    /**
     * 修改血常规
     * 
     * @param mpBloodRoutineCol 血常规
     * @return 结果
     */
    public int updateMpBloodRoutineCol(MpBloodRoutineCol mpBloodRoutineCol);

    /**
     * 删除血常规
     * 
     * @param id 血常规主键
     * @return 结果
     */
    public int deleteMpBloodRoutineColById(Long id);

    /**
     * 批量删除血常规
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpBloodRoutineColByIds(Long[] ids);
}
