package com.dchealth.screening.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningClinic;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface ScreeningClinicMapper {
    List<ScreeningClinic> selectScreeningClinicList(ScreeningClinic entity);
    ScreeningClinic selectScreeningClinicById(Long id);
    int insertScreeningClinic(ScreeningClinic entity);
    int updateScreeningClinic(ScreeningClinic entity);
    int deleteScreeningClinicById(Long id);
    int deleteScreeningClinicByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
