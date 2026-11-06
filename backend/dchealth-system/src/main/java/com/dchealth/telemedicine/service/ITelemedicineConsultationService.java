package com.dchealth.telemedicine.service;

import java.util.List;
import java.util.Map;
import com.dchealth.telemedicine.domain.TelemedicineConsultation;

public interface ITelemedicineConsultationService {
    public List<TelemedicineConsultation> selectTelemedicineConsultationList(TelemedicineConsultation entity);
    public TelemedicineConsultation selectTelemedicineConsultationById(Long id);
    public int insertTelemedicineConsultation(TelemedicineConsultation entity);
    public int updateTelemedicineConsultation(TelemedicineConsultation entity);
    public int deleteTelemedicineConsultationById(Long id);
    public int deleteTelemedicineConsultationByIds(Long[] ids);

    /** 关联 mp_person 查询会诊列表 */
    public List<TelemedicineConsultation> selectTelemedicineConsultationWithPatient(Map<String, Object> params);
}
