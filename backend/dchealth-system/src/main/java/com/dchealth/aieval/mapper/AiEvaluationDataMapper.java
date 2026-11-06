package com.dchealth.aieval.mapper;

import java.util.List;
import com.dchealth.aieval.domain.AiEvaluationData;
import com.dchealth.aieval.domain.vo.AiEvaluationQueueVo;
import com.dchealth.medical.domain.PatientRecord;

/**
 * AI评估数据Mapper接口
 *
 * @author dchealth
 * @date 2026-09-02
 */
public interface AiEvaluationDataMapper {

    /**
     * 查询慢病路径管理下已发布的队列列表（供AI评估分析使用）
     *
     * @param vo 查询条件
     * @return 队列集合
     */
    List<AiEvaluationQueueVo> selectPublishedQueueList(AiEvaluationQueueVo vo);

    /**
     * 查询AI评估数据列表
     *
     * @param entity 查询条件
     * @return 数据集合
     */
    List<AiEvaluationData> selectAiEvaluationDataList(AiEvaluationData entity);

    /**
     * 查询AI评估数据详情
     *
     * @param id 主键
     * @return 数据
     */
    AiEvaluationData selectAiEvaluationDataById(Long id);

    /**
     * 新增AI评估数据
     *
     * @param entity 数据
     * @return 结果
     */
    int insertAiEvaluationData(AiEvaluationData entity);

    /**
     * 修改AI评估数据
     *
     * @param entity 数据
     * @return 结果
     */
    int updateAiEvaluationData(AiEvaluationData entity);

    /**
     * 更新AI分析结果（分析结论/措施/状态/模型/时间）
     *
     * @param entity 数据
     * @return 结果
     */
    int updateAiAnalysisResult(AiEvaluationData entity);

    /**
     * 批量删除AI评估数据
     *
     * @param ids 主键集合
     * @return 结果
     */
    int deleteAiEvaluationDataByIds(Long[] ids);

    /**
     * 查询队列下已填写完成患者的随访记录（含患者姓名），用于读取历次表单数据生成评估数据
     *
     * @param projectId 队列ID（research_info.id）
     * @return 已填写完成的随访记录集合
     */
    List<PatientRecord> selectQueueFilledRecordList(Long projectId);
}
