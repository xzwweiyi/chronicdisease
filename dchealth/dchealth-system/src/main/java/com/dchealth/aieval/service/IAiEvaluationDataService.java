package com.dchealth.aieval.service;

import java.util.List;
import com.dchealth.aieval.domain.AiEvaluationData;
import com.dchealth.aieval.domain.vo.AiEvaluationQueueVo;

/**
 * AI评估数据Service接口
 *
 * @author dchealth
 * @date 2026-09-02
 */
public interface IAiEvaluationDataService {

    /**
     * 查询慢病路径管理下已发布的队列列表
     */
    List<AiEvaluationQueueVo> selectPublishedQueueList(AiEvaluationQueueVo vo);

    /**
     * 根据队列下患者已填写完成的历次随访表单数据，自动生成/更新评估数据
     *
     * @param projectId 队列ID
     * @return 生成（新增+更新）的评估数据条数
     */
    int generateEvaluationData(Long projectId);

    /**
     * 查询AI评估数据列表
     */
    List<AiEvaluationData> selectAiEvaluationDataList(AiEvaluationData entity);

    /**
     * 查询AI评估数据详情
     */
    AiEvaluationData selectAiEvaluationDataById(Long id);

    /**
     * 新增AI评估数据
     */
    int insertAiEvaluationData(AiEvaluationData entity);

    /**
     * 修改AI评估数据
     */
    int updateAiEvaluationData(AiEvaluationData entity);

    /**
     * 批量删除AI评估数据
     */
    int deleteAiEvaluationDataByIds(Long[] ids);

    /**
     * 调用DeepSeek大模型进行AI分析，输出分析结果与下一步措施
     */
    AiEvaluationData analyze(Long id);
}
