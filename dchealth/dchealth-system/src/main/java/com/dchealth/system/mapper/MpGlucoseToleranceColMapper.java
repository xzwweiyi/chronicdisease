package com.dchealth.system.mapper;

import java.util.List;
import com.dchealth.system.domain.MpGlucoseToleranceCol;

/**
 * 肝or肾Mapper接口
 * 
 * @author lchbu
 * @date 2024-08-12
 */
public interface MpGlucoseToleranceColMapper 
{
    /**
     * 查询肝or肾
     * 
     * @param id 肝or肾主键
     * @return 肝or肾
     */
    public MpGlucoseToleranceCol selectMpGlucoseToleranceColById(Long id);

    /**
     * 查询肝or肾列表
     * 
     * @param mpGlucoseToleranceCol 肝or肾
     * @return 肝or肾集合
     */
    public List<MpGlucoseToleranceCol> selectMpGlucoseToleranceColList(MpGlucoseToleranceCol mpGlucoseToleranceCol);

    /**
     * 新增肝or肾
     * 
     * @param mpGlucoseToleranceCol 肝or肾
     * @return 结果
     */
    public int insertMpGlucoseToleranceCol(MpGlucoseToleranceCol mpGlucoseToleranceCol);

    /**
     * 修改肝or肾
     * 
     * @param mpGlucoseToleranceCol 肝or肾
     * @return 结果
     */
    public int updateMpGlucoseToleranceCol(MpGlucoseToleranceCol mpGlucoseToleranceCol);

    /**
     * 删除肝or肾
     * 
     * @param id 肝or肾主键
     * @return 结果
     */
    public int deleteMpGlucoseToleranceColById(Long id);

    /**
     * 批量删除肝or肾
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMpGlucoseToleranceColByIds(Long[] ids);
}
