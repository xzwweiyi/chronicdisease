package com.dchealth.aieval.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.aieval.domain.AiEvaluationProject;
import com.dchealth.aieval.mapper.AiEvaluationProjectMapper;
import com.dchealth.aieval.service.IAiEvaluationProjectService;

/**
 * AI评估项目Service业务层处理
 *
 * @author ruoyi
 * @date 2025-06-27
 */
@Service
public class AiEvaluationProjectServiceImpl implements IAiEvaluationProjectService
{
    @Autowired
    private AiEvaluationProjectMapper aiEvaluationProjectMapper;

    /**
     * 查询AI评估项目列表
     *
     * @param entity AI评估项目
     * @return AI评估项目集合
     */
    @Override
    public List<AiEvaluationProject> selectAiEvaluationProjectList(AiEvaluationProject entity)
    {
        return aiEvaluationProjectMapper.selectAiEvaluationProjectList(entity);
    }

    /**
     * 查询AI评估项目
     *
     * @param id AI评估项目主键
     * @return AI评估项目
     */
    @Override
    public AiEvaluationProject selectAiEvaluationProjectById(Long id)
    {
        return aiEvaluationProjectMapper.selectAiEvaluationProjectById(id);
    }

    /**
     * 新增AI评估项目
     *
     * @param entity AI评估项目
     * @return 结果
     */
    @Override
    public int insertAiEvaluationProject(AiEvaluationProject entity)
    {
        return aiEvaluationProjectMapper.insertAiEvaluationProject(entity);
    }

    /**
     * 修改AI评估项目
     *
     * @param entity AI评估项目
     * @return 结果
     */
    @Override
    public int updateAiEvaluationProject(AiEvaluationProject entity)
    {
        return aiEvaluationProjectMapper.updateAiEvaluationProject(entity);
    }

    /**
     * 删除AI评估项目信息
     *
     * @param id AI评估项目主键
     * @return 结果
     */
    @Override
    public int deleteAiEvaluationProjectById(Long id)
    {
        return aiEvaluationProjectMapper.deleteAiEvaluationProjectById(id);
    }

    /**
     * 批量删除AI评估项目
     *
     * @param ids 需要删除的AI评估项目主键集合
     * @return 结果
     */
    @Override
    public int deleteAiEvaluationProjectByIds(Long[] ids)
    {
        return aiEvaluationProjectMapper.deleteAiEvaluationProjectByIds(ids);
    }
}
