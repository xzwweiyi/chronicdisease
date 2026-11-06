package com.dchealth.warn.mapper;

import java.util.List;
import com.dchealth.warn.domain.WarnMedicalEarlyCondition;

/**
 * 预警表单子表Mapper接口
 * 
 * @author lchbu
 * @date 2023-01-09
 */
public interface WarnMedicalEarlyConditionMapper 
{
    /**
     * 查询预警表单子表
     * 
     * @param id 预警表单子表主键
     * @return 预警表单子表
     */
    public WarnMedicalEarlyCondition selectWarnMedicalEarlyConditionById(Long id);

    /**
     * 查询预警表单子表列表
     * 
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 预警表单子表集合
     */
    public List<WarnMedicalEarlyCondition> selectWarnMedicalEarlyConditionList(WarnMedicalEarlyCondition warnMedicalEarlyCondition);

    /**
     * 查询预警表单子表列表：来源小程序端
     *
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 预警表单子表集合
     */
    public List<WarnMedicalEarlyCondition> selectConditionAndWarnMedicalEarlyList(WarnMedicalEarlyCondition warnMedicalEarlyCondition);

    /**
     * 新增预警表单子表
     * 
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 结果
     */
    public int insertWarnMedicalEarlyCondition(WarnMedicalEarlyCondition warnMedicalEarlyCondition);

    /**
     * 修改预警表单子表
     * 
     * @param warnMedicalEarlyCondition 预警表单子表
     * @return 结果
     */
    public int updateWarnMedicalEarlyCondition(WarnMedicalEarlyCondition warnMedicalEarlyCondition);

    /**
     * 删除预警表单子表
     * 
     * @param id 预警表单子表主键
     * @return 结果
     */
    public int deleteWarnMedicalEarlyConditionById(Long id);

    /**
     * 删除预警表单子表
     * @param id
     * @return
     */
    public int deleteWarnMedicalEarlyConditionByWarnMedicalId(Long id);

    /**
     * 删除预警表单子表
     * @param id
     * @return
     */
    public int deleteWarnMedicalEarlyConditionByFollowId(Long id);

    /**
     * 批量删除预警表单子表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnMedicalEarlyConditionByIds(Long[] ids);

    public int deleteWarnMedicalEarlyConditionByPropertyIds(List<Long> propertyIds);

    public int deleteWarnMedicalEarlyConditionByPropertyId(Long propertyId);
}
