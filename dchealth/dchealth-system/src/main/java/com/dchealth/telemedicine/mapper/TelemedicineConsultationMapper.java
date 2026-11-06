package com.dchealth.telemedicine.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.telemedicine.domain.TelemedicineConsultation;

public interface TelemedicineConsultationMapper {
    List<TelemedicineConsultation> selectTelemedicineConsultationList(TelemedicineConsultation entity);
    TelemedicineConsultation selectTelemedicineConsultationById(Long id);
    int insertTelemedicineConsultation(TelemedicineConsultation entity);
    int updateTelemedicineConsultation(TelemedicineConsultation entity);
    int deleteTelemedicineConsultationById(Long id);
    int deleteTelemedicineConsultationByIds(Long[] ids);

    /** 关联 mp_person 查询远程会诊列表（通过 patient_info 桥接） */
    List<TelemedicineConsultation> selectTelemedicineConsultationWithPatient(Map<String, Object> params);
}
