package com.dchealth.screening.service;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningOutside;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface IScreeningOutsideService {
    public List<ScreeningOutside> selectScreeningOutsideList(ScreeningOutside entity);
    public ScreeningOutside selectScreeningOutsideById(Long id);
    public int insertScreeningOutside(ScreeningOutside entity);
    public int updateScreeningOutside(ScreeningOutside entity);
    public int deleteScreeningOutsideById(Long id);
    public int deleteScreeningOutsideByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
