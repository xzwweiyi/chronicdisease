package com.dchealth.telemedicine.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.telemedicine.domain.TelemedicineConsultation;
import com.dchealth.telemedicine.mapper.TelemedicineConsultationMapper;
import com.dchealth.telemedicine.service.ITelemedicineConsultationService;

@Service
public class TelemedicineConsultationServiceImpl implements ITelemedicineConsultationService
{
    @Autowired
    private TelemedicineConsultationMapper telemedicineConsultationMapper;

    @Override
    public List<TelemedicineConsultation> selectTelemedicineConsultationList(TelemedicineConsultation entity) { return telemedicineConsultationMapper.selectTelemedicineConsultationList(entity); }
    @Override
    public TelemedicineConsultation selectTelemedicineConsultationById(Long id) { return telemedicineConsultationMapper.selectTelemedicineConsultationById(id); }
    @Override
    public int insertTelemedicineConsultation(TelemedicineConsultation entity) { return telemedicineConsultationMapper.insertTelemedicineConsultation(entity); }
    @Override
    public int updateTelemedicineConsultation(TelemedicineConsultation entity) { return telemedicineConsultationMapper.updateTelemedicineConsultation(entity); }
    @Override
    public int deleteTelemedicineConsultationById(Long id) { return telemedicineConsultationMapper.deleteTelemedicineConsultationById(id); }
    @Override
    public int deleteTelemedicineConsultationByIds(Long[] ids) { return telemedicineConsultationMapper.deleteTelemedicineConsultationByIds(ids); }

    @Override
    public List<TelemedicineConsultation> selectTelemedicineConsultationWithPatient(Map<String, Object> params) {
        return telemedicineConsultationMapper.selectTelemedicineConsultationWithPatient(params);
    }
}
