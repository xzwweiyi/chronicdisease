package com.dchealth.screening.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.screening.domain.ScreeningApply;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;
import com.dchealth.screening.mapper.ScreeningApplyMapper;
import com.dchealth.screening.service.IScreeningApplyService;

@Service
public class ScreeningApplyServiceImpl implements IScreeningApplyService
{
    @Autowired
    private ScreeningApplyMapper screeningApplyMapper;

    @Override
    public List<ScreeningApply> selectScreeningApplyList(ScreeningApply entity) { return screeningApplyMapper.selectScreeningApplyList(entity); }
    @Override
    public ScreeningApply selectScreeningApplyById(Long id) { return screeningApplyMapper.selectScreeningApplyById(id); }
    @Override
    public int insertScreeningApply(ScreeningApply entity) { return screeningApplyMapper.insertScreeningApply(entity); }
    @Override
    public int updateScreeningApply(ScreeningApply entity) { return screeningApplyMapper.updateScreeningApply(entity); }
    @Override
    public int deleteScreeningApplyById(Long id) { return screeningApplyMapper.deleteScreeningApplyById(id); }
    @Override
    public int deleteScreeningApplyByIds(Long[] ids) { return screeningApplyMapper.deleteScreeningApplyByIds(ids); }

    @Override
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params) {
        return screeningApplyMapper.selectScreeningPatientList(params);
    }
}
