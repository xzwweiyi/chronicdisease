package com.dchealth.warn.service;

import java.util.List;
import com.dchealth.warn.domain.WarnMedicalEarly;

/**
 * 预警表单Service接口
 * 
 * @author lchbu
 * @date 2023-01-09
 */
public interface IWarnMedicalEarlyService 
{
    /**
     * 查询预警表单
     * 
     * @param id 预警表单主键
     * @return 预警表单
     */
    public WarnMedicalEarly selectWarnMedicalEarlyById(Long id);

    /**
     * 查询预警表单列表
     * 
     * @param warnMedicalEarly 预警表单
     * @return 预警表单集合
     */
    public List<WarnMedicalEarly> selectWarnMedicalEarlyList(WarnMedicalEarly warnMedicalEarly);

    /**
     * 新增预警表单
     * 
     * @param warnMedicalEarly 预警表单
     * @return 结果
     */
    public int insertWarnMedicalEarly(WarnMedicalEarly warnMedicalEarly);

    /**
     * 修改预警表单
     * 
     * @param warnMedicalEarly 预警表单
     * @return 结果
     */
    public int updateWarnMedicalEarly(WarnMedicalEarly warnMedicalEarly);

    /**
     * 批量删除预警表单
     * 
     * @param ids 需要删除的预警表单主键集合
     * @return 结果
     */
    public int deleteWarnMedicalEarlyByIds(Long[] ids);

    /**
     * 删除预警表单信息
     * 
     * @param id 预警表单主键
     * @return 结果
     */
    public int deleteWarnMedicalEarlyById(Long id);
}
