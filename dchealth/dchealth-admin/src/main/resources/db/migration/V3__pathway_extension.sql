-- ================================================================
-- V3: Pathway Extension - Add management pathway support to existing tables
-- Run this SQL against dchealth_td_test database
-- Strategy: Extend existing tables with pathway-specific columns
--           system_type = '1' (default) = queue system
--           system_type = '2' = management pathway system
-- ================================================================

-- ================================================================
-- Step 1: Extend research_info with pathway fields
-- ================================================================
ALTER TABLE research_info
    ADD COLUMN IF NOT EXISTS system_type VARCHAR(2) DEFAULT '1' COMMENT '系统类型：1-队列管理，2-管理路径',
    ADD COLUMN IF NOT EXISTS pathway_stage_config JSON COMMENT '路径阶段配置（JSON：阶段顺序、类型、名称）',
    ADD COLUMN IF NOT EXISTS baseline_config JSON COMMENT '基线配置（JSON：基线表单、基线评估等）',
    ADD COLUMN IF NOT EXISTS assessment_config JSON COMMENT '评估配置（JSON：评估工具、评估规则）',
    ADD COLUMN IF NOT EXISTS exit_criteria TEXT COMMENT '退出标准',
    ADD COLUMN IF NOT EXISTS target_patient_num INT DEFAULT 0 COMMENT '目标管理患者数',
    ADD COLUMN IF NOT EXISTS pathway_status VARCHAR(2) DEFAULT '0' COMMENT '路径状态：0-草稿，1-已发布，2-进行中，3-已关闭';

-- ================================================================
-- Step 2: Extend patient_record_template with node type fields
-- ================================================================
ALTER TABLE patient_record_template
    ADD COLUMN IF NOT EXISTS node_type VARCHAR(20) DEFAULT 'follow_up' COMMENT '节点类型：follow_up-随访，assessment-评估，intervention-干预，education-宣教，reminder-提醒，orders-医嘱',
    ADD COLUMN IF NOT EXISTS node_config JSON COMMENT '节点专属配置（JSON：各类型节点的专属配置）',
    ADD COLUMN IF NOT EXISTS node_order INT DEFAULT 1 COMMENT '节点排序号',
    ADD COLUMN IF NOT EXISTS is_baseline TINYINT DEFAULT 0 COMMENT '是否为基线节点：0-否，1-是',
    ADD COLUMN IF NOT EXISTS reminder_advance_days INT DEFAULT NULL COMMENT '提醒提前天数',
    ADD COLUMN IF NOT EXISTS reminder_template_id BIGINT DEFAULT NULL COMMENT '提醒模板ID',
    ADD COLUMN IF NOT EXISTS required_forms JSON COMMENT '必填表单列表（JSON数组）',
    ADD COLUMN IF NOT EXISTS optional_forms JSON COMMENT '可选表单列表（JSON数组）';

-- ================================================================
-- Step 3: Extend patient_info with pathway enrollment fields
-- ================================================================
ALTER TABLE patient_info
    ADD COLUMN IF NOT EXISTS system_type VARCHAR(2) DEFAULT '1' COMMENT '系统类型：1-队列，2-路径',
    ADD COLUMN IF NOT EXISTS pathway_stage VARCHAR(50) COMMENT '当前路径阶段',
    ADD COLUMN IF NOT EXISTS baseline_completed TINYINT DEFAULT 0 COMMENT '基线是否完成：0-否，1-是',
    ADD COLUMN IF NOT EXISTS pathway_progress DECIMAL(5,2) COMMENT '路径进度百分比（0-100）';

-- ================================================================
-- Step 4: Extend patient_record with pathway execution fields
-- ================================================================
ALTER TABLE patient_record
    ADD COLUMN IF NOT EXISTS node_type VARCHAR(20) DEFAULT 'follow_up' COMMENT '节点类型：follow_up-随访，assessment-评估等',
    ADD COLUMN IF NOT EXISTS exit_reason VARCHAR(500) COMMENT '退出原因',
    ADD COLUMN IF NOT EXISTS exit_time DATETIME COMMENT '退出时间',
    ADD COLUMN IF NOT EXISTS intervention_content TEXT COMMENT '干预内容',
    ADD COLUMN IF NOT EXISTS assessment_result JSON COMMENT '评估结果（JSON）',
    ADD COLUMN IF NOT EXISTS orders_content TEXT COMMENT '医嘱内容';

-- ================================================================
-- Step 5: Create pathway_node_execution table
-- ================================================================
CREATE TABLE IF NOT EXISTS pathway_node_execution (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    research_id BIGINT NOT NULL COMMENT '路径ID',
    template_id BIGINT COMMENT '节点模板ID（关联patient_record_template.follow_id）',
    record_id BIGINT COMMENT '随访记录ID（关联patient_record.record_id）',
    node_type VARCHAR(20) COMMENT '节点类型：follow_up-随访，assessment-评估，intervention-干预，education-宣教，reminder-提醒，orders-医嘱',
    execution_status VARCHAR(2) DEFAULT '0' COMMENT '执行状态：0-待执行，1-执行中，2-已完成，3-已跳过',
    scheduled_time DATETIME COMMENT '计划执行时间',
    actual_time DATETIME COMMENT '实际执行时间',
    execution_result JSON COMMENT '执行结果（JSON）',
    executed_by BIGINT COMMENT '执行人ID',
    remark VARCHAR(500) COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_patient_research (patient_id, research_id),
    INDEX idx_template (template_id),
    INDEX idx_status (execution_status)
) COMMENT '路径节点执行记录';

-- ================================================================
-- Step 6: Create pathway_education_binding table
-- ================================================================
CREATE TABLE IF NOT EXISTS pathway_education_binding (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    research_id BIGINT NOT NULL COMMENT '路径ID',
    template_id BIGINT COMMENT '节点模板ID（null表示路径全局绑定）',
    article_id BIGINT COMMENT '患教文章ID',
    binding_type VARCHAR(2) DEFAULT '1' COMMENT '绑定类型：1-节点绑定，2-路径全局',
    push_timing VARCHAR(2) COMMENT '推送时机：1-入组时，2-节点开始前，3-节点完成后',
    push_advance_days INT DEFAULT 0 COMMENT '提前推送天数（节点开始前N天推送）',
    sort_order INT DEFAULT 0 COMMENT '排序',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_research (research_id),
    INDEX idx_template (template_id)
) COMMENT '路径宣教内容绑定';

-- ================================================================
-- Step 7: Create pathway_intervention_record table (干预方案记录)
-- ================================================================
CREATE TABLE IF NOT EXISTS pathway_intervention_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL COMMENT '患者ID',
    research_id BIGINT NOT NULL COMMENT '路径ID',
    record_id BIGINT COMMENT '随访记录ID',
    intervention_type VARCHAR(2) COMMENT '干预类型：1-药物，2-饮食，3-运动，4-心理，5-其他',
    intervention_content TEXT COMMENT '干预内容',
    target_value VARCHAR(100) COMMENT '目标值',
    current_value VARCHAR(100) COMMENT '当前值',
    adjust_reason VARCHAR(500) COMMENT '调整原因',
    system_suggestion TEXT COMMENT '系统建议',
    doctor_adjustment TEXT COMMENT '医生调整',
    start_date DATE COMMENT '开始日期',
    end_date DATE COMMENT '计划结束日期',
    status VARCHAR(2) DEFAULT '1' COMMENT '状态：1-执行中，2-已完成，3-已终止',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_patient (patient_id),
    INDEX idx_research (research_id)
) COMMENT '路径干预方案记录';
