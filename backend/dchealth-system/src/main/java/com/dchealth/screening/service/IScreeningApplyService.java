package com.dchealth.screening.service;

import java.util.List;
import java.util.Map;
import com.dchealth.screening.domain.ScreeningApply;
import com.dchealth.screening.domain.vo.ScreeningPatientVO;

/**
 * 筛查申请Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IScreeningApplyService
{
    public List<ScreeningApply> selectScreeningApplyList(ScreeningApply entity);
    public ScreeningApply selectScreeningApplyById(Long id);
    public int insertScreeningApply(ScreeningApply entity);
    public int updateScreeningApply(ScreeningApply entity);
    public int deleteScreeningApplyById(Long id);
    public int deleteScreeningApplyByIds(Long[] ids);

    /** 关联 mp_person 查询筛查患者列表 */
    public List<ScreeningPatientVO> selectScreeningPatientList(Map<String, Object> params);
}
