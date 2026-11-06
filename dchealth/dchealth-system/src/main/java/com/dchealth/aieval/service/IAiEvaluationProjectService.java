package com.dchealth.aieval.service;

import java.util.List;
import com.dchealth.aieval.domain.AiEvaluationProject;

/**
 * AI评估项目Service接口
 *
 * @author ruoyi
 * @date 2025-06-27
 */
public interface IAiEvaluationProjectService
{
    /**
     * 查询AI评估项目列表
     *
     * @param entity AI评估项目
     * @return AI评估项目集合
     */
    public List<AiEvaluationProject> selectAiEvaluationProjectList(AiEvaluationProject entity);

    /**
     * 查询AI评估项目
     *
     * @param id AI评估项目主键
     * @return AI评估项目
     */
    public AiEvaluationProject selectAiEvaluationProjectById(Long id);

    /**
     * 新增AI评估项目
     *
     * @param entity AI评估项目
     * @return 结果
     */
    public int insertAiEvaluationProject(AiEvaluationProject entity);

    /**
     * 修改AI评估项目
     *
     * @param entity AI评估项目
     * @return 结果
     */
    public int updateAiEvaluationProject(AiEvaluationProject entity);

    /**
     * 删除AI评估项目信息
     *
     * @param id AI评估项目主键
     * @return 结果
     */
    public int deleteAiEvaluationProjectById(Long id);

    /**
     * 批量删除AI评估项目
     *
     * @param ids 需要删除的AI评估项目主键集合
     * @return 结果
     */
    public int deleteAiEvaluationProjectByIds(Long[] ids);
}
