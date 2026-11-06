package com.dchealth.screening.service;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningClinic;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface IScreeningClinicService {
    public List<ScreeningClinic> selectScreeningClinicList(ScreeningClinic entity);
    public ScreeningClinic selectScreeningClinicById(Long id);
    public int insertScreeningClinic(ScreeningClinic entity);
    public int updateScreeningClinic(ScreeningClinic entity);
    public int deleteScreeningClinicById(Long id);
    public int deleteScreeningClinicByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
