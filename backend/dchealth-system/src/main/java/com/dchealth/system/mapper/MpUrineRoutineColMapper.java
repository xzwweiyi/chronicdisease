package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.MpUrineRoutineCol;

/**
 * 尿常规Mapper接口
 * 
 * @author lchbu
 * @date 2024-05-31
 */
public interface MpUrineRoutineColMapper 
{
    /**
     * 查询尿常规
     * 
     * @param id 尿常规主键
     * @return 尿常规
     */
    public MpUrineRoutineCol selectMpUrineRoutineColById(Long id);

    /**
     * 查询尿常规列表
     * 
     * @param mpUrineRoutineCol 尿常规
     * @return 尿常规集合
     */
    public List<MpUrineRoutineCol> selectMpUrineRoutineColList(MpUrineRoutineCol mpUrineRoutineCol);

    /**
     * 新增尿常规
     * 
     * @param mpUrineRoutineCol 尿常规
     * @return 结果
     */
    public int insertMpUrineRoutineCol(MpUrineRoutineCol mpUrineRoutineCol);

    /**
     * 修改尿常规
     * 
     * @param mpUrineRoutineCol 尿常规
     * @return 结果
     */
    public int updateMpUrineRoutineCol(MpUrineRoutineCol mpUrineRoutineCol);

    /**
     * 删除尿常规
     * 
     * @param id 尿常规主键
     * @return 结果
     */
    public int deleteMpUrineRoutineColById(Long id);

    /**
     * 批量删除尿常规
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpUrineRoutineColByIds(Long[] ids);
}
