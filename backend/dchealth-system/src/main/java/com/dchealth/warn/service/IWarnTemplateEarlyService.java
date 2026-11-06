package com.dchealth.warn.service;

import java.util.List;
import com.dchealth.warn.domain.WarnTemplateEarly;

/**
 * 预警模板Service接口
 * 
 * @author lchbu
 * @date 2023-01-09
 */
public interface IWarnTemplateEarlyService 
{
    /**
     * 查询预警模板
     * 
     * @param id 预警模板主键
     * @return 预警模板
     */
    public WarnTemplateEarly selectWarnTemplateEarlyById(Long id);

    /**
     * 查询预警模板列表
     * 
     * @param warnTemplateEarly 预警模板
     * @return 预警模板集合
     */
    public List<WarnTemplateEarly> selectWarnTemplateEarlyList(WarnTemplateEarly warnTemplateEarly);

    /**
     * 新增预警模板
     * 
     * @param warnTemplateEarly 预警模板
     * @return 结果
     */
    public int insertWarnTemplateEarly(WarnTemplateEarly warnTemplateEarly);

    /**
     * 修改预警模板
     * 
     * @param warnTemplateEarly 预警模板
     * @return 结果
     */
    public int updateWarnTemplateEarly(WarnTemplateEarly warnTemplateEarly);

    /**
     * 批量删除预警模板
     * 
     * @param ids 需要删除的预警模板主键集合
     * @return 结果
     */
    public int deleteWarnTemplateEarlyByIds(Long[] ids);

    /**
     * 删除预警模板信息
     * 
     * @param id 预警模板主键
     * @return 结果
     */
    public int deleteWarnTemplateEarlyById(Long id);
}
