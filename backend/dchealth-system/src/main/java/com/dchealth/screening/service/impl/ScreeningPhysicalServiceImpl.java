package com.dchealth.screening.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.screening.domain.ScreeningPhysical;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;
import com.dchealth.screening.mapper.ScreeningPhysicalMapper;
import com.dchealth.screening.service.IScreeningPhysicalService;

@Service
public class ScreeningPhysicalServiceImpl implements IScreeningPhysicalService
{
    @Autowired
    private ScreeningPhysicalMapper screeningPhysicalMapper;

    @Override
    public List<ScreeningPhysical> selectScreeningPhysicalList(ScreeningPhysical entity) { return screeningPhysicalMapper.selectScreeningPhysicalList(entity); }
    @Override
    public ScreeningPhysical selectScreeningPhysicalById(Long id) { return screeningPhysicalMapper.selectScreeningPhysicalById(id); }
    @Override
    public int insertScreeningPhysical(ScreeningPhysical entity) { return screeningPhysicalMapper.insertScreeningPhysical(entity); }
    @Override
    public int updateScreeningPhysical(ScreeningPhysical entity) { return screeningPhysicalMapper.updateScreeningPhysical(entity); }
    @Override
    public int deleteScreeningPhysicalById(Long id) { return screeningPhysicalMapper.deleteScreeningPhysicalById(id); }
    @Override
    public int deleteScreeningPhysicalByIds(Long[] ids) { return screeningPhysicalMapper.deleteScreeningPhysicalByIds(ids); }

    @Override
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params) {
        return screeningPhysicalMapper.selectScreeningPatientList(params);
    }
}
