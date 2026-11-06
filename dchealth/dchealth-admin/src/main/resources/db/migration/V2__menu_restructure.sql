-- ================================================================
-- V2: Menu Restructure - Transform from sidebar to horizontal top-nav
-- Run this SQL against dchealth_td_test database
-- ================================================================

-- Step 0: Capture old top-level menu IDs into variables
SELECT menu_id INTO @old_queue FROM sys_menu WHERE parent_id=0 AND menu_name='队列管理' LIMIT 1;
SELECT menu_id INTO @old_follow FROM sys_menu WHERE parent_id=0 AND menu_name='随访工作台' LIMIT 1;
SELECT menu_id INTO @old_stat FROM sys_menu WHERE parent_id=0 AND menu_name='统计管理' LIMIT 1;
SELECT menu_id INTO @old_cms FROM sys_menu WHERE parent_id=0 AND menu_name IN ('患教库管理','CMS管理','内容管理','患教库') LIMIT 1;
SELECT menu_id INTO @old_eval FROM sys_menu WHERE parent_id=0 AND menu_name IN ('评价管理','质效评价') LIMIT 1;
SELECT menu_id INTO @old_research FROM sys_menu WHERE parent_id=0 AND menu_name IN ('研究管理','队列研究') LIMIT 1;
SELECT menu_id INTO @old_sys FROM sys_menu WHERE parent_id=0 AND menu_name='系统管理' LIMIT 1;
SELECT menu_id INTO @old_mon FROM sys_menu WHERE parent_id=0 AND menu_name='系统监控' LIMIT 1;
SELECT menu_id INTO @old_tool FROM sys_menu WHERE parent_id=0 AND menu_name='系统工具' LIMIT 1;

-- Step 1: Insert 13 new top-level menus (use fixed ID range 200-320 to avoid conflicts)
-- Only insert if not already present
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
(200, '慢病路径管理',   0, 1, 'path',      NULL, 'M', '0', '0', '', 'tree',        '1', '0', 'admin', NOW()),
(210, '随访与健康干预', 0, 2, 'followup',  NULL, 'M', '0', '0', '', 'form',        '1', '0', 'admin', NOW()),
(220, '数据可视化',     0, 3, 'datav',     NULL, 'M', '0', '0', '', 'chart',       '1', '0', 'admin', NOW()),
(230, '知识库管理',     0, 4, 'knowledge', NULL, 'M', '0', '0', '', 'education',   '1', '0', 'admin', NOW()),
(240, '基础管理',       0, 5, 'base',      NULL, 'M', '0', '0', '', 'system',      '1', '0', 'admin', NOW()),
(250, '绩效考核',       0, 6, 'perform',   NULL, 'M', '0', '0', '', 'evaluate',    '1', '0', 'admin', NOW()),
(260, '慢病筛查',       0, 7, 'screening', NULL, 'M', '0', '0', '', 'search',      '1', '0', 'admin', NOW()),
(270, '居民管理',       0, 8, 'resident',  NULL, 'M', '0', '0', '', 'people',      '1', '0', 'admin', NOW()),
(280, '分级诊疗',       0, 9, 'referral',  NULL, 'M', '0', '0', '', 'guide',       '1', '0', 'admin', NOW()),
(290, '远程会诊',       0, 10,'telemed',   NULL, 'M', '0', '0', '', 'monitor',     '1', '0', 'admin', NOW()),
(300, 'AI评估分析',     0, 11,'aieval',    NULL, 'M', '0', '0', '', 'cpu',         '1', '0', 'admin', NOW()),
(310, '签约管理',       0, 12,'contract',  NULL, 'M', '0', '0', '', 'document',    '1', '0', 'admin', NOW()),
(320, '设备采集',       0, 13,'device',    NULL, 'M', '0', '0', '', 'device',      '1', '0', 'admin', NOW());

-- Step 2: Disable ALL old top-level menus (mark as hidden/disabled)
UPDATE sys_menu SET status='1' WHERE parent_id=0 AND menu_id NOT BETWEEN 200 AND 320;

-- Step 3: Re-parent existing children under new top-level menus
-- Use the variables captured in Step 0
UPDATE sys_menu SET parent_id=200 WHERE parent_id=@old_queue;
UPDATE sys_menu SET parent_id=200 WHERE parent_id=@old_research;
UPDATE sys_menu SET parent_id=210 WHERE parent_id=@old_follow;
UPDATE sys_menu SET parent_id=220 WHERE parent_id=@old_stat;
UPDATE sys_menu SET parent_id=230 WHERE parent_id=@old_cms;
UPDATE sys_menu SET parent_id=250 WHERE parent_id=@old_eval;

-- Move system management, monitoring, tools under 基础管理 (240)
UPDATE sys_menu SET parent_id=240 WHERE parent_id=@old_sys;
UPDATE sys_menu SET parent_id=240 WHERE parent_id=@old_mon;
UPDATE sys_menu SET parent_id=240 WHERE parent_id=@old_tool;

-- ================================================================
-- Step 4: Insert sub-menus for each top-level menu
-- ================================================================

-- 4a: 慢病路径管理 (200) - Replace old re-parented menus with new ones
DELETE FROM sys_menu WHERE parent_id=200 AND menu_name IN ('队列列表', '队列角色', '入组管理', '周期设置', 'CRF配置');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('队列列表',    200, 1, 'queue',      'queueSettings/queue/index',   'C', '0', '0', 'research:info:list',    'list',     '1', '0', 'admin', NOW()),
('队列角色',    200, 2, 'queueRole',  'researh/role/index',         'C', '0', '0', 'researh:role:list',     'peoples',  '1', '0', 'admin', NOW()),
('入组管理',    200, 3, 'enrollment', 'queueSettings/patient/list', 'C', '0', '0', 'research:patient:list', 'user',     '1', '0', 'admin', NOW()),
('周期设置',    200, 4, 'cycle',      'queueSettings/cycle/index',  'C', '0', '0', 'research:cycle:list',   'date',     '1', '0', 'admin', NOW()),
('CRF配置',     200, 5, 'crf',        'crf/template/index',         'C', '0', '0', 'crf:template:list',    'build',    '1', '0', 'admin', NOW());

-- 4b: 随访与健康干预 (210)
DELETE FROM sys_menu WHERE parent_id=210 AND menu_name IN ('随访工作台', 'EDC工作站', '外呼管理', '预警管理', '短信模板');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('随访工作台',  210, 1, 'followWork', 'workstation/user/index',    'C', '0', '0', 'follow:work:list',      'work',     '1', '0', 'admin', NOW()),
('EDC工作站',   210, 2, 'edcWork',    'workstation/edc/index',     'C', '0', '0', 'edc:work:list',         'data',     '1', '0', 'admin', NOW()),
('外呼管理',    210, 3, 'outbound',   'outbound/index',            'C', '0', '0', 'medical:outbound:list', 'phone',    '1', '0', 'admin', NOW()),
('预警管理',    210, 4, 'warn',       'warn/index',                'C', '0', '0', 'warn:config:list',      'warning',  '1', '0', 'admin', NOW()),
('短信模板',    210, 5, 'sms',        'smsTemplate/index',         'C', '0', '0', 'sms:template:list',     'message',  '1', '0', 'admin', NOW());

-- 4c: 数据可视化 (220)
DELETE FROM sys_menu WHERE parent_id=220 AND menu_name IN ('慢病综合数据分析', '慢病筛查数据分析', '慢病随访数据分析', '慢病监管数据分析');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('慢病综合数据分析', 220, 1, 'overview',    'statistics/overviewHealth',   'C', '0', '0', 'stat:overview:list',    'chart', '1', '0', 'admin', NOW()),
('慢病筛查数据分析', 220, 2, 'screeningDt', 'statistics/screeningData',    'C', '0', '0', 'stat:screening:list',   'chart', '1', '0', 'admin', NOW()),
('慢病随访数据分析', 220, 3, 'followupDt',  'statistics/followup',         'C', '0', '0', 'stat:followup:list',    'chart', '1', '0', 'admin', NOW()),
('慢病监管数据分析', 220, 4, 'monitorDt',   'statistics/monitorData',      'C', '0', '0', 'stat:monitor:list',     'chart', '1', '0', 'admin', NOW());

-- 4d: 知识库管理 (230) - Content side
DELETE FROM sys_menu WHERE parent_id=230 AND menu_name IN ('文章管理', '分类浏览', '评论管理', '留言管理', '轮播管理');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('文章管理',    230, 1, 'article',    'cms/article/index',          'C', '0', '0', 'cms:article:list',     'article',  '1', '0', 'admin', NOW()),
('分类浏览',    230, 2, 'info',       'cms/information/index',      'C', '0', '0', 'cms:information:list',  'list',     '1', '0', 'admin', NOW()),
('评论管理',    230, 3, 'comment',    'cms/comment/index',          'C', '0', '0', 'cms:comment:list',      'comment',  '1', '0', 'admin', NOW()),
('留言管理',    230, 4, 'guestbook',  'cms/guestbook/index',        'C', '0', '0', 'cms:guestbook:list',    'message',  '1', '0', 'admin', NOW()),
('轮播管理',    230, 5, 'banner',     'cms/banner/index',           'C', '0', '0', 'cms:banner:list',       'image',    '1', '0', 'admin', NOW());

-- 4e: 基础管理 (240)
DELETE FROM sys_menu WHERE parent_id=240 AND menu_name IN ('病种管理', '知识库管理', '机构管理', '部门管理', '用户管理', '角色管理', '菜单管理', '字典管理', '参数设置', '通知公告');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('病种管理',    240, 1, 'disease',    'base/disease/index',          'C', '0', '0', 'base:disease:list',      'medical',  '1', '0', 'admin', NOW()),
('知识库管理',  240, 2, 'kbCategory', 'cms/category/index',         'C', '0', '0', 'cms:category:list',      'category', '1', '0', 'admin', NOW()),
('机构管理',    240, 3, 'hospital',   'hospital/hospital/index',    'C', '0', '0', 'base:hospital:list',     'hospital', '1', '0', 'admin', NOW()),
('部门管理',    240, 4, 'dept',       'system/dept/index',          'C', '0', '0', 'system:dept:list',       'dept',     '1', '0', 'admin', NOW()),
('用户管理',    240, 5, 'user',       'system/user/index',          'C', '0', '0', 'system:user:list',       'user',     '1', '0', 'admin', NOW()),
('角色管理',    240, 6, 'role',       'system/role/index',          'C', '0', '0', 'system:role:list',       'peoples',  '1', '0', 'admin', NOW()),
('菜单管理',    240, 7, 'menu',       'system/menu/index',          'C', '0', '0', 'system:menu:list',       'tree-table','1','0', 'admin', NOW()),
('字典管理',    240, 8, 'dict',       'system/dict/index',          'C', '0', '0', 'system:dict:list',       'dict',     '1', '0', 'admin', NOW()),
('参数设置',    240, 9, 'config',     'system/config/index',        'C', '0', '0', 'system:config:list',     'settings', '1', '0', 'admin', NOW()),
('通知公告',    240, 10,'notice',     'system/notice/index',        'C', '0', '0', 'system:notice:list',     'notice',   '1', '0', 'admin', NOW());

-- 4f: 绩效考核 (250)
DELETE FROM sys_menu WHERE parent_id=250 AND menu_name IN ('管理工作统计', '工作进展统计', '随访完成统计');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('管理工作统计',  250, 1, 'manageStat',   'statistics/workload',      'C', '0', '0', 'evaluate:manage:list',    'chart',   '1', '0', 'admin', NOW()),
('工作进展统计',  250, 2, 'progressStat', 'statistics/queuePatient',  'C', '0', '0', 'evaluate:progress:list',  'chart',   '1', '0', 'admin', NOW()),
('随访完成统计',  250, 3, 'followStat',   'statistics/followup',      'C', '0', '0', 'evaluate:follow:list',    'chart',   '1', '0', 'admin', NOW());

-- 4g: 慢病筛查 (260) - NEW module
DELETE FROM sys_menu WHERE parent_id=260 AND menu_name IN ('机构外筛查', '体检筛查', '就诊筛查', '居民申请');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('机构外筛查',  260, 1, 'outside',   'screening/outside/index',   'C', '0', '0', 'screening:outside:list',   'search',  '1', '0', 'admin', NOW()),
('体检筛查',    260, 2, 'physical',  'screening/physical/index',  'C', '0', '0', 'screening:physical:list',  'search',  '1', '0', 'admin', NOW()),
('就诊筛查',    260, 3, 'clinic',    'screening/clinic/index',    'C', '0', '0', 'screening:clinic:list',    'search',  '1', '0', 'admin', NOW()),
('居民申请',    260, 4, 'apply',     'screening/apply/index',     'C', '0', '0', 'screening:apply:list',     'apply',   '1', '0', 'admin', NOW());

-- 4h: 居民管理 (270) - NEW module
DELETE FROM sys_menu WHERE parent_id=270 AND menu_name IN ('档案管理', '高血压患者', '糖尿病患者', '脑卒中患者', '冠心病患者', '慢阻肺患者', '合并症患者');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('档案管理',      270, 1, 'archive',      'resident/archive/index',       'C', '0', '0', 'resident:archive:list',    'file',    '1', '0', 'admin', NOW()),
('高血压患者',    270, 2, 'hypertension', 'resident/hypertension/index',   'C', '0', '0', 'resident:hyper:list',      'patient', '1', '0', 'admin', NOW()),
('糖尿病患者',    270, 3, 'diabetes',     'resident/diabetes/index',      'C', '0', '0', 'resident:diabetes:list',   'patient', '1', '0', 'admin', NOW()),
('脑卒中患者',    270, 4, 'stroke',       'resident/stroke/index',        'C', '0', '0', 'resident:stroke:list',     'patient', '1', '0', 'admin', NOW()),
('冠心病患者',    270, 5, 'chd',          'resident/chd/index',           'C', '0', '0', 'resident:chd:list',        'patient', '1', '0', 'admin', NOW()),
('慢阻肺患者',    270, 6, 'copd',         'resident/copd/index',          'C', '0', '0', 'resident:copd:list',       'patient', '1', '0', 'admin', NOW()),
('合并症患者',    270, 7, 'comorbidity',  'resident/comorbidity/index',   'C', '0', '0', 'resident:comorbid:list',   'patient', '1', '0', 'admin', NOW());

-- 4i: 分级诊疗 (280) - NEW module
DELETE FROM sys_menu WHERE parent_id=280 AND menu_name IN ('转诊管理', '转诊规则');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('转诊管理',    280, 1, 'transfer',   'referral/transfer/index',   'C', '0', '0', 'referral:transfer:list',  'guide',   '1', '0', 'admin', NOW()),
('转诊规则',    280, 2, 'rule',       'referral/rule/index',       'C', '0', '0', 'referral:rule:list',      'rule',    '1', '0', 'admin', NOW());

-- 4j: 远程会诊 (290) - NEW module
DELETE FROM sys_menu WHERE parent_id=290 AND menu_name IN ('会诊管理', '会诊日程');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('会诊管理',    290, 1, 'consultation','telemedicine/consultation/index','C','0','0','telemedicine:consult:list','monitor','1','0','admin',NOW()),
('会诊日程',    290, 2, 'schedule',   'telemedicine/schedule/index',  'C','0','0','telemedicine:schedule:list','date',   '1','0','admin',NOW());

-- 4k: AI评估分析 (300) - NEW module
DELETE FROM sys_menu WHERE parent_id=300 AND menu_name IN ('评估项目', '评估规则');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('评估项目',    300, 1, 'project',    'aieval/project/index',      'C','0','0','ai:evaluation:project:list','project','1','0','admin',NOW()),
('评估规则',    300, 2, 'rule',       'aieval/rule/index',         'C','0','0','ai:evaluation:rule:list',   'rule',   '1','0','admin',NOW());

-- 4l: 签约管理 (310) - NEW module
DELETE FROM sys_menu WHERE parent_id=310 AND menu_name IN ('患者签约', '团队管理', '服务包', '服务项目管理');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('患者签约',      310, 1, 'sign',       'contract/sign/index',       'C','0','0','contract:sign:list',      'sign',   '1','0','admin',NOW()),
('团队管理',      310, 2, 'team',       'contract/team/index',       'C','0','0','contract:team:list',      'peoples','1','0','admin',NOW()),
('服务包',        310, 3, 'package',    'contract/package/index',    'C','0','0','contract:package:list',   'package','1','0','admin',NOW()),
('服务项目管理',  310, 4, 'serviceItem','contract/serviceItem/index', 'C','0','0','contract:service:list',   'service','1','0','admin',NOW());

-- 4m: 设备采集 (320) - NEW module
DELETE FROM sys_menu WHERE parent_id=320 AND menu_name IN ('设备绑定', '设备管理');
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('设备绑定',    320, 1, 'bind',       'device/bind/index',         'C','0','0','device:bind:list',        'link',   '1','0','admin',NOW()),
('设备管理',    320, 2, 'manage',     'device/manage/index',       'C','0','0','device:manage:list',      'device', '1','0','admin',NOW());

-- ================================================================
-- Step 5: Insert button-level permissions (menu_type='F') for new modules
-- ================================================================

-- 慢病筛查 sub-buttons
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '查询', m.menu_id, 1, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':query'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=260 AND m.menu_type='C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '新增', m.menu_id, 2, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':add'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=260 AND m.menu_type='C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '修改', m.menu_id, 3, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':edit'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=260 AND m.menu_type='C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '删除', m.menu_id, 4, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':remove'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=260 AND m.menu_type='C';

-- Repeat for other new modules (resident:270, referral:280, telemedicine:290, aieval:300, contract:310, device:320)
-- Use a stored procedure pattern for brevity
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '查询', m.menu_id, 1, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':query'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id IN (270,280,290,300,310,320) AND m.menu_type='C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '新增', m.menu_id, 2, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':add'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id IN (270,280,290,300,310,320) AND m.menu_type='C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '修改', m.menu_id, 3, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':edit'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id IN (270,280,290,300,310,320) AND m.menu_type='C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '删除', m.menu_id, 4, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':remove'), 'admin', NOW()
FROM sys_menu m WHERE m.parent_id IN (270,280,290,300,310,320) AND m.menu_type='C';

-- ================================================================
-- Step 6: Grant admin role (role_id=1) access to all new menus
-- ================================================================
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id >= 200 AND status='0';

-- Also grant for any other admin roles
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id
FROM sys_role r, sys_menu m
WHERE r.status='0' AND m.menu_id >= 200 AND m.status='0'
  AND r.role_id NOT IN (SELECT DISTINCT role_id FROM sys_role_menu WHERE menu_id >= 200);

-- ================================================================
-- Step 7: Create new database tables for new modules
-- ================================================================

-- Screening: 机构外筛查
CREATE TABLE IF NOT EXISTS screening_outside (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_name VARCHAR(50) COMMENT '居民姓名',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    screening_date DATE COMMENT '筛查日期',
    screening_location VARCHAR(200) COMMENT '筛查地点',
    screening_result VARCHAR(500) COMMENT '筛查结果',
    disease_type VARCHAR(50) COMMENT '疑似病种',
    handler VARCHAR(50) COMMENT '处理人',
    status VARCHAR(2) DEFAULT '0' COMMENT '状态(0正常 1关闭)',
    create_by VARCHAR(64),
    create_time DATETIME,
    update_by VARCHAR(64),
    update_time DATETIME,
    remark VARCHAR(500)
) COMMENT '机构外筛查记录';

-- Screening: 体检筛查
CREATE TABLE IF NOT EXISTS screening_physical (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_name VARCHAR(50) COMMENT '居民姓名',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    exam_date DATE COMMENT '体检日期',
    exam_institution VARCHAR(100) COMMENT '体检机构',
    bmi DECIMAL(5,2) COMMENT 'BMI',
    blood_pressure VARCHAR(20) COMMENT '血压',
    blood_sugar DECIMAL(5,2) COMMENT '血糖',
    blood_lipids VARCHAR(100) COMMENT '血脂',
    exam_result VARCHAR(500) COMMENT '体检结论',
    disease_risk VARCHAR(50) COMMENT '慢病风险',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '体检筛查记录';

-- Screening: 就诊筛查
CREATE TABLE IF NOT EXISTS screening_clinic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_name VARCHAR(50) COMMENT '居民姓名',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    visit_date DATE COMMENT '就诊日期',
    department VARCHAR(50) COMMENT '就诊科室',
    diagnosis VARCHAR(500) COMMENT '诊断结果',
    icd_code VARCHAR(20) COMMENT 'ICD编码',
    disease_type VARCHAR(50) COMMENT '慢病类型',
    doctor_name VARCHAR(50) COMMENT '就诊医生',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '就诊筛查记录';

-- Screening: 居民申请
CREATE TABLE IF NOT EXISTS screening_apply (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_name VARCHAR(50) COMMENT '申请人姓名',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    apply_date DATE COMMENT '申请日期',
    apply_type VARCHAR(2) COMMENT '申请类型(1慢病管理 2健康体检 3签约服务)',
    disease_history VARCHAR(500) COMMENT '既往病史',
    current_medication VARCHAR(500) COMMENT '当前用药',
    apply_reason VARCHAR(500) COMMENT '申请原因',
    audit_status VARCHAR(2) DEFAULT '0' COMMENT '审核状态(0待审 1通过 2驳回)',
    audit_opinion VARCHAR(500) COMMENT '审核意见',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '居民申请记录';

-- Resident: 居民档案
CREATE TABLE IF NOT EXISTS resident_archive (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_name VARCHAR(50) COMMENT '姓名',
    gender VARCHAR(1) COMMENT '性别(0男 1女)',
    birth_date DATE COMMENT '出生日期',
    id_card VARCHAR(18) COMMENT '身份证号',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(200) COMMENT '居住地址',
    community VARCHAR(100) COMMENT '所属社区',
    archive_date DATE COMMENT '建档日期',
    archive_status VARCHAR(2) COMMENT '档案状态',
    disease_types VARCHAR(200) COMMENT '患病类型(多选用逗号分隔)',
    family_history VARCHAR(500) COMMENT '家族史',
    allergy_history VARCHAR(500) COMMENT '过敏史',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '居民健康档案';

-- Referral: 转诊记录
CREATE TABLE IF NOT EXISTS referral_transfer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT COMMENT '患者ID',
    patient_name VARCHAR(50) COMMENT '患者姓名',
    from_institution VARCHAR(100) COMMENT '转出机构',
    to_institution VARCHAR(100) COMMENT '转入机构',
    from_dept VARCHAR(50) COMMENT '转出科室',
    to_dept VARCHAR(50) COMMENT '转入科室',
    transfer_type VARCHAR(2) COMMENT '转诊类型(1上转 2下转 3平转)',
    transfer_date DATE COMMENT '转诊日期',
    reason VARCHAR(500) COMMENT '转诊原因',
    diagnosis VARCHAR(500) COMMENT '诊断摘要',
    accompanying_info TEXT COMMENT '伴随信息(检查报告等)',
    transfer_status VARCHAR(2) COMMENT '转诊状态(0待接收 1已接收 2已拒绝 3已完成)',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '转诊记录';

-- Referral: 转诊规则
CREATE TABLE IF NOT EXISTS referral_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(100) COMMENT '规则名称',
    disease_type VARCHAR(50) COMMENT '适用病种',
    upward_conditions VARCHAR(500) COMMENT '上转条件',
    downward_conditions VARCHAR(500) COMMENT '下转条件',
    emergency_conditions VARCHAR(500) COMMENT '紧急转诊条件',
    priority INT DEFAULT 0 COMMENT '优先级',
    effective_date DATE COMMENT '生效日期',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '转诊规则';

-- Telemedicine: 远程会诊
CREATE TABLE IF NOT EXISTS telemedicine_consultation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    consultation_no VARCHAR(50) COMMENT '会诊编号',
    patient_id BIGINT COMMENT '患者ID',
    patient_name VARCHAR(50) COMMENT '患者姓名',
    applicant_name VARCHAR(50) COMMENT '申请人',
    consultation_type VARCHAR(2) COMMENT '会诊类型(1常规 2紧急 3疑难)',
    scheduled_time DATETIME COMMENT '预约时间',
    actual_time DATETIME COMMENT '实际时间',
    participants TEXT COMMENT '参与人员',
    chief_complaint TEXT COMMENT '主诉',
    summary TEXT COMMENT '会诊摘要',
    conclusion TEXT COMMENT '会诊结论',
    recommendation TEXT COMMENT '治疗建议',
    consultation_status VARCHAR(2) COMMENT '状态(0待会诊 1进行中 2已完成 3已取消)',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '远程会诊记录';

-- Telemedicine: 会诊日程
CREATE TABLE IF NOT EXISTS telemedicine_schedule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT COMMENT '医生ID',
    doctor_name VARCHAR(50) COMMENT '医生姓名',
    dept_name VARCHAR(50) COMMENT '科室',
    schedule_date DATE COMMENT '排班日期',
    time_slot VARCHAR(20) COMMENT '时间段(上午/下午/晚上)',
    max_consultations INT DEFAULT 5 COMMENT '最大会诊数',
    booked_count INT DEFAULT 0 COMMENT '已预约数',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '会诊排班';

-- AI Evaluation: 评估项目
CREATE TABLE IF NOT EXISTS ai_evaluation_project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_name VARCHAR(100) COMMENT '项目名称',
    project_code VARCHAR(50) COMMENT '项目编码',
    disease_type VARCHAR(50) COMMENT '适用病种',
    evaluation_type VARCHAR(2) COMMENT '评估类型(1风险 2疗效 3预后)',
    description VARCHAR(500) COMMENT '项目描述',
    model_config TEXT COMMENT '模型配置(JSON)',
    rule_config TEXT COMMENT '规则配置(JSON)',
    input_fields TEXT COMMENT '输入字段(JSON)',
    output_fields TEXT COMMENT '输出字段(JSON)',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT 'AI评估项目';

-- AI Evaluation: 评估规则
CREATE TABLE IF NOT EXISTS ai_evaluation_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rule_name VARCHAR(100) COMMENT '规则名称',
    rule_code VARCHAR(50) COMMENT '规则编码',
    project_id BIGINT COMMENT '所属项目ID',
    rule_expression TEXT COMMENT '规则表达式',
    risk_level VARCHAR(2) COMMENT '风险等级(1低 2中 3高)',
    score_range VARCHAR(50) COMMENT '评分区间',
    advice TEXT COMMENT '建议措施',
    priority INT DEFAULT 0 COMMENT '优先级',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT 'AI评估规则';

-- Contract: 签约管理
CREATE TABLE IF NOT EXISTS contract_sign (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resident_id BIGINT COMMENT '居民ID',
    resident_name VARCHAR(50) COMMENT '居民姓名',
    team_id BIGINT COMMENT '签约团队ID',
    package_id BIGINT COMMENT '服务包ID',
    sign_date DATE COMMENT '签约日期',
    start_date DATE COMMENT '服务开始日期',
    end_date DATE COMMENT '服务结束日期',
    contract_status VARCHAR(2) COMMENT '签约状态(0待生效 1生效中 2已到期 3已解约)',
    sign_type VARCHAR(2) COMMENT '签约方式(1线上 2线下)',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '居民签约记录';

-- Contract: 签约团队
CREATE TABLE IF NOT EXISTS contract_team (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    team_name VARCHAR(100) COMMENT '团队名称',
    leader_name VARCHAR(50) COMMENT '团队长',
    leader_phone VARCHAR(20) COMMENT '联系方式',
    members TEXT COMMENT '团队成员(JSON)',
    service_area VARCHAR(200) COMMENT '服务区域',
    team_intro VARCHAR(500) COMMENT '团队介绍',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '签约团队';

-- Contract: 服务包
CREATE TABLE IF NOT EXISTS contract_package (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    package_name VARCHAR(100) COMMENT '服务包名称',
    package_type VARCHAR(2) COMMENT '服务包类型(1基础 2增值 3个性化)',
    price DECIMAL(10,2) COMMENT '价格',
    description VARCHAR(500) COMMENT '描述',
    service_ids VARCHAR(500) COMMENT '包含服务项目ID列表',
    applicable_diseases VARCHAR(200) COMMENT '适用病种',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '服务包';

-- Contract: 服务项目
CREATE TABLE IF NOT EXISTS contract_service_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) COMMENT '项目名称',
    item_type VARCHAR(2) COMMENT '项目类型(1诊疗 2检查 3随访 4健康教育 5用药指导)',
    frequency VARCHAR(50) COMMENT '服务频次',
    description VARCHAR(500) COMMENT '项目描述',
    execution_standard VARCHAR(500) COMMENT '执行标准',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '服务项目';

-- Device: 设备绑定
CREATE TABLE IF NOT EXISTS device_bind (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_code VARCHAR(50) COMMENT '设备编码',
    resident_id BIGINT COMMENT '绑定居民ID',
    resident_name VARCHAR(50) COMMENT '居民姓名',
    bind_date DATE COMMENT '绑定日期',
    unbind_date DATE COMMENT '解绑日期',
    bind_status VARCHAR(2) DEFAULT '1' COMMENT '绑定状态(1已绑定 0已解绑)',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '设备绑定记录';

-- Device: 设备管理
CREATE TABLE IF NOT EXISTS device_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_code VARCHAR(50) UNIQUE COMMENT '设备编码',
    device_name VARCHAR(100) COMMENT '设备名称',
    device_type VARCHAR(2) COMMENT '设备类型(1血压计 2血糖仪 3血氧仪 4心电仪 5体脂秤)',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    model VARCHAR(50) COMMENT '设备型号',
    purchase_date DATE COMMENT '购置日期',
    warranty_expire DATE COMMENT '保修截止日期',
    device_status VARCHAR(2) COMMENT '设备状态(0空闲 1使用中 2维修中 3报废)',
    status VARCHAR(2) DEFAULT '0',
    create_by VARCHAR(64), create_time DATETIME, update_by VARCHAR(64), update_time DATETIME, remark VARCHAR(500)
) COMMENT '设备信息';
