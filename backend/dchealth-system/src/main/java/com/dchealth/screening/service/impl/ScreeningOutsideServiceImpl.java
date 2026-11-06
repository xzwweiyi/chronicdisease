package com.dchealth.screening.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.screening.domain.ScreeningOutside;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;
import com.dchealth.screening.mapper.ScreeningOutsideMapper;
import com.dchealth.screening.service.IScreeningOutsideService;

@Service
public class ScreeningOutsideServiceImpl implements IScreeningOutsideService
{
    @Autowired
    private ScreeningOutsideMapper screeningOutsideMapper;

    @Override
    public List<ScreeningOutside> selectScreeningOutsideList(ScreeningOutside entity) { return screeningOutsideMapper.selectScreeningOutsideList(entity); }
    @Override
    public ScreeningOutside selectScreeningOutsideById(Long id) { return screeningOutsideMapper.selectScreeningOutsideById(id); }
    @Override
    public int insertScreeningOutside(ScreeningOutside entity) { return screeningOutsideMapper.insertScreeningOutside(entity); }
    @Override
    public int updateScreeningOutside(ScreeningOutside entity) { return screeningOutsideMapper.updateScreeningOutside(entity); }
    @Override
    public int deleteScreeningOutsideById(Long id) { return screeningOutsideMapper.deleteScreeningOutsideById(id); }
    @Override
    public int deleteScreeningOutsideByIds(Long[] ids) { return screeningOutsideMapper.deleteScreeningOutsideByIds(ids); }

    @Override
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params) {
        return screeningOutsideMapper.selectScreeningPatientList(params);
    }
}
