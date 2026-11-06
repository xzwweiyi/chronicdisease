-- ================================================================
-- V7: AI评估分析 — 评估数据关联患者，支持根据队列下患者已填写完成的
--     历次随访表单数据自动生成评估数据
-- Run this SQL against chronic_disease_new database
-- ================================================================

-- ai_evaluation_data 增加患者ID，用于按队列自动生成评估数据时的幂等 upsert
ALTER TABLE ai_evaluation_data
    ADD COLUMN patient_id BIGINT DEFAULT NULL COMMENT '患者ID' AFTER project_id;

-- 可选：为按队列+患者查询增加索引
ALTER TABLE ai_evaluation_data
    ADD INDEX idx_project_patient (project_id, patient_id);
