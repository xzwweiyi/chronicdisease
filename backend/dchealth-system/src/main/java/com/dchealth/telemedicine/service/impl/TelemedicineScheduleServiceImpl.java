package com.dchealth.telemedicine.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.telemedicine.domain.TelemedicineSchedule;
import com.dchealth.telemedicine.mapper.TelemedicineScheduleMapper;
import com.dchealth.telemedicine.service.ITelemedicineScheduleService;

/**
 * 远程排班Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class TelemedicineScheduleServiceImpl implements ITelemedicineScheduleService
{
    @Autowired
    private TelemedicineScheduleMapper telemedicineScheduleMapper;

    /**
     * 查询远程排班列表
     *
     * @param entity 远程排班
     * @return 远程排班集合
     */
    @Override
    public List<TelemedicineSchedule> selectTelemedicineScheduleList(TelemedicineSchedule entity)
    {
        return telemedicineScheduleMapper.selectTelemedicineScheduleList(entity);
    }

    /**
     * 查询远程排班
     *
     * @param id 远程排班主键
     * @return 远程排班
     */
    @Override
    public TelemedicineSchedule selectTelemedicineScheduleById(Long id)
    {
        return telemedicineScheduleMapper.selectTelemedicineScheduleById(id);
    }

    /**
     * 新增远程排班
     *
     * @param entity 远程排班
     * @return 结果
     */
    @Override
    public int insertTelemedicineSchedule(TelemedicineSchedule entity)
    {
        return telemedicineScheduleMapper.insertTelemedicineSchedule(entity);
    }

    /**
     * 修改远程排班
     *
     * @param entity 远程排班
     * @return 结果
     */
    @Override
    public int updateTelemedicineSchedule(TelemedicineSchedule entity)
    {
        return telemedicineScheduleMapper.updateTelemedicineSchedule(entity);
    }

    /**
     * 删除远程排班信息
     *
     * @param id 远程排班主键
     * @return 结果
     */
    @Override
    public int deleteTelemedicineScheduleById(Long id)
    {
        return telemedicineScheduleMapper.deleteTelemedicineScheduleById(id);
    }

    /**
     * 批量删除远程排班
     *
     * @param ids 需要删除的远程排班主键集合
     * @return 结果
     */
    @Override
    public int deleteTelemedicineScheduleByIds(Long[] ids)
    {
        return telemedicineScheduleMapper.deleteTelemedicineScheduleByIds(ids);
    }
}
