package com.dchealth.telemedicine.mapper;

import java.util.List;
import com.dchealth.telemedicine.domain.TelemedicineSchedule;

public interface TelemedicineScheduleMapper {
    List<TelemedicineSchedule> selectTelemedicineScheduleList(TelemedicineSchedule entity);
    TelemedicineSchedule selectTelemedicineScheduleById(Long id);
    int insertTelemedicineSchedule(TelemedicineSchedule entity);
    int updateTelemedicineSchedule(TelemedicineSchedule entity);
    int deleteTelemedicineScheduleById(Long id);
    int deleteTelemedicineScheduleByIds(Long[] ids);
}
