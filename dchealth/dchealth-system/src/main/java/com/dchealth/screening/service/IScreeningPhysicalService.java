package com.dchealth.screening.service;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningPhysical;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface IScreeningPhysicalService {
    public List<ScreeningPhysical> selectScreeningPhysicalList(ScreeningPhysical entity);
    public ScreeningPhysical selectScreeningPhysicalById(Long id);
    public int insertScreeningPhysical(ScreeningPhysical entity);
    public int updateScreeningPhysical(ScreeningPhysical entity);
    public int deleteScreeningPhysicalById(Long id);
    public int deleteScreeningPhysicalByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
