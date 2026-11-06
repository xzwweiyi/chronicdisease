package com.dchealth.screening.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningPhysical;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface ScreeningPhysicalMapper {
    List<ScreeningPhysical> selectScreeningPhysicalList(ScreeningPhysical entity);
    ScreeningPhysical selectScreeningPhysicalById(Long id);
    int insertScreeningPhysical(ScreeningPhysical entity);
    int updateScreeningPhysical(ScreeningPhysical entity);
    int deleteScreeningPhysicalById(Long id);
    int deleteScreeningPhysicalByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
