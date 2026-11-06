-- ================================================================
-- V6: AI评估分析 — 评估数据表 + DeepSeek 大模型接入配置
-- Run this SQL against chronic_disease_new database
-- ================================================================

-- Step 1: 评估类型列扩容（支持中文评估类型）
-- ALTER TABLE ai_evaluation_project MODIFY COLUMN evaluation_type VARCHAR(50) COMMENT '评估类型';

-- Step 2: AI评估数据表
CREATE TABLE IF NOT EXISTS ai_evaluation_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    project_id BIGINT NOT NULL COMMENT '所属评估项目ID',
    patient_name VARCHAR(100) DEFAULT NULL COMMENT '患者姓名',
    eval_date DATE DEFAULT NULL COMMENT '评估日期',
    eval_result VARCHAR(50) DEFAULT NULL COMMENT '评估结果(高风险/中风险/低风险)',
    score INT DEFAULT NULL COMMENT '评估评分',
    evaluator VARCHAR(100) DEFAULT NULL COMMENT '评估人',
    eval_detail TEXT COMMENT '评估明细数据(各项指标描述)',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    ai_analysis TEXT COMMENT 'AI分析结果',
    ai_measures TEXT COMMENT '下一步采取措施',
    ai_model VARCHAR(100) DEFAULT NULL COMMENT '分析所用模型',
    ai_status CHAR(1) DEFAULT '0' COMMENT '分析状态 0未分析 1分析中 2已完成 3失败',
    ai_time DATETIME DEFAULT NULL COMMENT '分析时间',
    status CHAR(1) DEFAULT '0' COMMENT '状态(0正常 1停用)',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME DEFAULT NULL COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME DEFAULT NULL COMMENT '更新时间',
    INDEX idx_project (project_id)
) COMMENT 'AI评估数据';

-- Step 3: DeepSeek 接口配置（可在 基础管理-参数设置 中修改）
INSERT IGNORE INTO sys_config (config_name, config_key, config_value, config_type, create_by, create_time, remark)
VALUES ('DeepSeek API密钥', 'ai.deepseek.apiKey', '', 'Y', 'admin', NOW(), 'AI评估分析 DeepSeek 接口密钥，配置后即可启用 AI 分析'),
       ('DeepSeek API地址', 'ai.deepseek.apiUrl', 'https://api.deepseek.com/anthropic/v1/messages', 'Y', 'admin', NOW(), 'DeepSeek Anthropic 兼容接口地址'),
       ('DeepSeek 模型', 'ai.deepseek.model', 'deepseek-chat', 'Y', 'admin', NOW(), 'AI评估分析所用模型');
