package com.dchealth.screening.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningOutside;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface ScreeningOutsideMapper {
    List<ScreeningOutside> selectScreeningOutsideList(ScreeningOutside entity);
    ScreeningOutside selectScreeningOutsideById(Long id);
    int insertScreeningOutside(ScreeningOutside entity);
    int updateScreeningOutside(ScreeningOutside entity);
    int deleteScreeningOutsideById(Long id);
    int deleteScreeningOutsideByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
