package com.dchealth.telemedicine.service;

import java.util.List;
import com.dchealth.telemedicine.domain.TelemedicineSchedule;

/**
 * 远程排班Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface ITelemedicineScheduleService
{
    /**
     * 查询远程排班列表
     *
     * @param entity 远程排班
     * @return 远程排班集合
     */
    public List<TelemedicineSchedule> selectTelemedicineScheduleList(TelemedicineSchedule entity);

    /**
     * 查询远程排班
     *
     * @param id 远程排班主键
     * @return 远程排班
     */
    public TelemedicineSchedule selectTelemedicineScheduleById(Long id);

    /**
     * 新增远程排班
     *
     * @param entity 远程排班
     * @return 结果
     */
    public int insertTelemedicineSchedule(TelemedicineSchedule entity);

    /**
     * 修改远程排班
     *
     * @param entity 远程排班
     * @return 结果
     */
    public int updateTelemedicineSchedule(TelemedicineSchedule entity);

    /**
     * 删除远程排班信息
     *
     * @param id 远程排班主键
     * @return 结果
     */
    public int deleteTelemedicineScheduleById(Long id);

    /**
     * 批量删除远程排班
     *
     * @param ids 需要删除的远程排班主键集合
     * @return 结果
     */
    public int deleteTelemedicineScheduleByIds(Long[] ids);
}
