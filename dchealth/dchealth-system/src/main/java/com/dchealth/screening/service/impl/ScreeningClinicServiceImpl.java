package com.dchealth.screening.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.screening.domain.ScreeningClinic;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;
import com.dchealth.screening.mapper.ScreeningClinicMapper;
import com.dchealth.screening.service.IScreeningClinicService;

@Service
public class ScreeningClinicServiceImpl implements IScreeningClinicService
{
    @Autowired
    private ScreeningClinicMapper screeningClinicMapper;

    @Override
    public List<ScreeningClinic> selectScreeningClinicList(ScreeningClinic entity) { return screeningClinicMapper.selectScreeningClinicList(entity); }
    @Override
    public ScreeningClinic selectScreeningClinicById(Long id) { return screeningClinicMapper.selectScreeningClinicById(id); }
    @Override
    public int insertScreeningClinic(ScreeningClinic entity) { return screeningClinicMapper.insertScreeningClinic(entity); }
    @Override
    public int updateScreeningClinic(ScreeningClinic entity) { return screeningClinicMapper.updateScreeningClinic(entity); }
    @Override
    public int deleteScreeningClinicById(Long id) { return screeningClinicMapper.deleteScreeningClinicById(id); }
    @Override
    public int deleteScreeningClinicByIds(Long[] ids) { return screeningClinicMapper.deleteScreeningClinicByIds(ids); }

    @Override
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params) {
        return screeningClinicMapper.selectScreeningPatientList(params);
    }
}
