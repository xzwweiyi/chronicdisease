package com.dchealth.aieval.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dchealth.aieval.domain.AiEvaluationData;
import com.dchealth.aieval.domain.AiEvaluationProject;
import com.dchealth.aieval.domain.AiEvaluationRule;
import com.dchealth.aieval.domain.vo.AiEvaluationQueueVo;
import com.dchealth.aieval.mapper.AiEvaluationDataMapper;
import com.dchealth.aieval.service.IAiEvaluationDataService;
import com.dchealth.aieval.service.IAiEvaluationProjectService;
import com.dchealth.aieval.service.IAiEvaluationRuleService;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.system.service.ISysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * AI评估数据Service业务层处理
 *
 * @author dchealth
 * @date 2026-09-02
 */
@Service
public class AiEvaluationDataServiceImpl implements IAiEvaluationDataService {

    private static final Logger log = LoggerFactory.getLogger(AiEvaluationDataServiceImpl.class);

    /** 分析状态：未分析 */
    private static final String AI_STATUS_NONE = "0";
    /** 分析状态：分析中 */
    private static final String AI_STATUS_RUNNING = "1";
    /** 分析状态：已完成 */
    private static final String AI_STATUS_DONE = "2";
    /** 分析状态：失败 */
    private static final String AI_STATUS_FAIL = "3";

    @Autowired
    private AiEvaluationDataMapper aiEvaluationDataMapper;

    @Autowired
    private IAiEvaluationProjectService aiEvaluationProjectService;

    @Autowired
    private IAiEvaluationRuleService aiEvaluationRuleService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private com.dchealth.research.service.IResearchInfoService researchInfoService;

    @Override
    public List<AiEvaluationQueueVo> selectPublishedQueueList(AiEvaluationQueueVo vo) {
        return aiEvaluationDataMapper.selectPublishedQueueList(vo);
    }

    @Override
    public List<AiEvaluationData> selectAiEvaluationDataList(AiEvaluationData entity) {
        return aiEvaluationDataMapper.selectAiEvaluationDataList(entity);
    }

    @Override
    public AiEvaluationData selectAiEvaluationDataById(Long id) {
        return aiEvaluationDataMapper.selectAiEvaluationDataById(id);
    }

    @Override
    public int insertAiEvaluationData(AiEvaluationData entity) {
        entity.setStatus("0");
        entity.setAiStatus(AI_STATUS_NONE);
        return aiEvaluationDataMapper.insertAiEvaluationData(entity);
    }

    @Override
    public int updateAiEvaluationData(AiEvaluationData entity) {
        // 数据变更后重置分析状态，需重新分析
        entity.setAiStatus(AI_STATUS_NONE);
        return aiEvaluationDataMapper.updateAiEvaluationData(entity);
    }

    @Override
    public int deleteAiEvaluationDataByIds(Long[] ids) {
        return aiEvaluationDataMapper.deleteAiEvaluationDataByIds(ids);
    }

    /**
     * 调用 DeepSeek 大模型（Anthropic 兼容接口）进行 AI 分析
     */
    @Override
    public AiEvaluationData analyze(Long id) {
        AiEvaluationData data = aiEvaluationDataMapper.selectAiEvaluationDataById(id);
        if (data == null) {
            throw new ServiceException("评估数据不存在");
        }
        // 读取配置
        String apiUrl = configService.selectConfigByKey("ai.deepseek.apiUrl");
        String apiKey = configService.selectConfigByKey("ai.deepseek.apiKey");
        String model = configService.selectConfigByKey("ai.deepseek.model");
        if (StringUtils.isEmpty(apiUrl)) {
            throw new ServiceException("未配置 DeepSeek API 地址（ai.deepseek.apiUrl）");
        }
        if (StringUtils.isEmpty(apiKey)) {
            throw new ServiceException("未配置 DeepSeek API 密钥，请在【基础管理-参数设置】中配置 ai.deepseek.apiKey");
        }
        if (StringUtils.isEmpty(model)) {
            model = "deepseek-chat";
        }
        // 标记分析中
        AiEvaluationData statusUpdate = new AiEvaluationData();
        statusUpdate.setId(id);
        statusUpdate.setAiStatus(AI_STATUS_RUNNING);
        aiEvaluationDataMapper.updateAiAnalysisResult(statusUpdate);
        try {
            String prompt = buildPrompt(data);
            String reply = callDeepSeek(apiUrl, apiKey, model, prompt);
            // 解析模型输出
            String[] parsed = parseModelOutput(reply);
            AiEvaluationData result = new AiEvaluationData();
            result.setId(id);
            result.setAiAnalysis(parsed[0]);
            result.setAiMeasures(parsed[1]);
            result.setAiModel(model);
            result.setAiStatus(AI_STATUS_DONE);
            result.setAiTime(new Date());
            aiEvaluationDataMapper.updateAiAnalysisResult(result);
            return aiEvaluationDataMapper.selectAiEvaluationDataById(id);
        } catch (Exception e) {
            log.error("AI分析失败, dataId={}", id, e);
            AiEvaluationData fail = new AiEvaluationData();
            fail.setId(id);
            fail.setAiStatus(AI_STATUS_FAIL);
            aiEvaluationDataMapper.updateAiAnalysisResult(fail);
            if (e instanceof ServiceException) {
                throw (ServiceException) e;
            }
            throw new ServiceException("AI分析失败：" + e.getMessage());
        }
    }

    /**
     * 组装提示词：评估主体（评估项目或已发布队列）+ 评估规则 + 患者评估数据
     */
    private String buildPrompt(AiEvaluationData data) {
        StringBuilder sb = new StringBuilder();
        AiEvaluationProject project = aiEvaluationProjectService.selectAiEvaluationProjectById(data.getProjectId());
        if (project != null) {
            // 主体为AI评估项目
            sb.append("【评估项目】\n");
            sb.append("项目名称：").append(project.getProjectName()).append("\n");
            sb.append("适用病种：").append(project.getDiseaseType()).append("\n");
            sb.append("评估类型：").append(project.getEvaluationType()).append("\n");
            sb.append("项目描述：").append(project.getDescription()).append("\n");
        } else {
            // 主体为慢病路径管理下已发布的随访队列
            com.dchealth.research.domain.ResearchInfo queue = researchInfoService.selectResearchInfoById(data.getProjectId());
            sb.append("【随访队列】\n");
            if (queue != null) {
                sb.append("队列名称：").append(queue.getProjectName()).append("\n");
                sb.append("队列简介：").append(queue.getResearchSynopsis() == null ? "无" : queue.getResearchSynopsis()).append("\n");
                sb.append("目标患者数：").append(queue.getPatientNum() == null ? "未设置" : queue.getPatientNum()).append("\n");
                sb.append("队列状态：已发布\n");
            } else {
                sb.append("队列ID：").append(data.getProjectId()).append("\n");
            }
        }
        // 评估规则
        AiEvaluationRule ruleQuery = new AiEvaluationRule();
        ruleQuery.setProjectId(data.getProjectId());
        List<AiEvaluationRule> rules = aiEvaluationRuleService.selectAiEvaluationRuleList(ruleQuery);
        if (rules != null && !rules.isEmpty()) {
            sb.append("\n【评估规则】\n");
            int idx = 1;
            for (AiEvaluationRule rule : rules) {
                sb.append(idx++).append(". ").append(rule.getRuleName());
                if (StringUtils.isNotEmpty(rule.getRuleExpression())) {
                    sb.append("（规则表达式：").append(rule.getRuleExpression()).append("）");
                }
                if (StringUtils.isNotEmpty(rule.getScoreRange())) {
                    sb.append("，分值区间：").append(rule.getScoreRange());
                }
                if (StringUtils.isNotEmpty(rule.getRiskLevel())) {
                    sb.append("，风险等级：").append(rule.getRiskLevel());
                }
                if (StringUtils.isNotEmpty(rule.getAdvice())) {
                    sb.append("，处置建议：").append(rule.getAdvice());
                }
                sb.append("\n");
            }
        }
        // 患者评估数据
        sb.append("\n【患者评估数据】\n");
        sb.append("患者姓名：").append(data.getPatientName() == null ? "未记录" : data.getPatientName()).append("\n");
        sb.append("评估日期：").append(data.getEvalDate() == null ? "未记录" : data.getEvalDate()).append("\n");
        sb.append("评估结果：").append(data.getEvalResult() == null ? "未记录" : data.getEvalResult()).append("\n");
        sb.append("评估评分：").append(data.getScore() == null ? "未记录" : data.getScore()).append("\n");
        sb.append("评估人：").append(data.getEvaluator() == null ? "未记录" : data.getEvaluator()).append("\n");
        sb.append("评估明细：").append(data.getEvalDetail() == null ? "未记录" : data.getEvalDetail()).append("\n");
        sb.append("备注：").append(data.getRemark() == null ? "无" : data.getRemark()).append("\n");
        sb.append("\n请以慢病管理医学专家身份，基于上述评估项目、评估规则与患者评估数据，进行专业分析评估，并输出 JSON（不要 markdown 代码块），格式严格为：");
        sb.append("{\"analysis\":\"分析结果：包含评估结论、风险解读、关键指标分析\",\"measures\":\"下一步采取的措施：分条列举（1. 2. 3. ...），涵盖用药、饮食、运动、随访监测、转诊建议等方面\"}");
        return sb.toString();
    }

    /**
     * 调用 DeepSeek Anthropic 兼容接口（/anthropic/v1/messages）
     */
    private String callDeepSeek(String apiUrl, String apiKey, String model, String prompt) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(120000);
        RestTemplate restTemplate = new RestTemplate(factory);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);
        headers.set("anthropic-version", "2023-06-01");

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("max_tokens", 4096);
        body.put("system", "你是资深慢病管理医学专家，负责对AI评估项目中的患者评估数据进行分析，并给出下一步干预措施建议。");
        body.put("messages", Arrays.asList(new HashMap<String, Object>() {{
            put("role", "user");
            put("content", prompt);
        }}));

        HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(body), headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, entity, String.class);
            JSONObject respJson = JSON.parseObject(response.getBody());
            JSONArray content = respJson.getJSONArray("content");
            StringBuilder text = new StringBuilder();
            if (content != null) {
                for (int i = 0; i < content.size(); i++) {
                    JSONObject item = content.getJSONObject(i);
                    if ("text".equals(item.getString("type"))) {
                        text.append(item.getString("text"));
                    }
                }
            }
            return text.toString();
        } catch (RestClientException e) {
            log.error("DeepSeek接口调用失败", e);
            throw new ServiceException("DeepSeek 接口调用失败：" + e.getMessage());
        }
    }

    /**
     * 解析模型输出，提取 analysis 与 measures
     *
     * @return [分析结果, 下一步措施]
     */
    private String[] parseModelOutput(String modelOutput) {
        String analysis = modelOutput;
        String measures = "";
        try {
            String text = modelOutput;
            int start = text.indexOf("{");
            int end = text.lastIndexOf("}");
            if (start >= 0 && end > start) {
                JSONObject obj = JSON.parseObject(text.substring(start, end + 1));
                String a = obj.getString("analysis");
                String m = obj.getString("measures");
                if (StringUtils.isNotEmpty(a)) {
                    analysis = a;
                }
                if (StringUtils.isNotEmpty(m)) {
                    measures = m;
                }
            }
        } catch (Exception e) {
            log.warn("模型输出JSON解析失败，使用原始文本作为分析结果", e);
        }
        return new String[]{analysis, measures};
    }
}
