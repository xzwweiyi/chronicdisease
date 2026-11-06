package com.dchealth.screening.mapper;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningApply;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

public interface ScreeningApplyMapper {
    List<ScreeningApply> selectScreeningApplyList(ScreeningApply entity);
    ScreeningApply selectScreeningApplyById(Long id);
    int insertScreeningApply(ScreeningApply entity);
    int updateScreeningApply(ScreeningApply entity);
    int deleteScreeningApplyById(Long id);
    int deleteScreeningApplyByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
