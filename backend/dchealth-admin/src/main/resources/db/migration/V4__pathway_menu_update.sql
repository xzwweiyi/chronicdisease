-- ================================================================
-- V4: Menu Update - Add management pathway sub-menus and elevate modules
-- ================================================================

-- Add sub-menus for 慢病路径管理 (menu_id=200)
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('路径列表',  200, 0, 'pathwayList',  'pathway/list/index',    'C', '0', '0', 'pathway:definition:list', 'list', '1', '0', 'admin', NOW()),
('创建路径',  200, 1, '',             '',                      'F', '0', '0', 'pathway:definition:add',  '',     '1', '0', 'admin', NOW()),
('修改路径',  200, 2, '',             '',                      'F', '0', '0', 'pathway:definition:edit', '',     '1', '0', 'admin', NOW()),
('删除路径',  200, 3, '',             '',                      'F', '0', '0', 'pathway:definition:remove','',    '1', '0', 'admin', NOW()),
('查询路径',  200, 4, '',             '',                      'F', '0', '0', 'pathway:definition:query','',     '1', '0', 'admin', NOW()),
('路径节点',  200, 5, 'pathwayNode',  'pathway/node/index',    'C', '0', '0', 'pathway:node:list',       'tree', '1', '0', 'admin', NOW()),
('入组管理',  200, 6, 'enrollment',   'pathway/enrollment/index','C','0','0', 'pathway:enrollment:list',  'user', '1', '0', 'admin', NOW()),
('执行跟踪',  200, 7, 'execution',    'pathway/execution/index','C', '0','0', 'pathway:execution:list',  'chart', '1', '0', 'admin', NOW()),
('基线配置',  200, 8, 'baseline',     'pathway/baseline/index', 'C','0','0', 'pathway:baseline:config',  'setting','1','0', 'admin', NOW());

-- Add node-level button permissions
INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '新增节点', m.menu_id, 1, '', 'F', '0', '0', 'pathway:node:add', 'admin', NOW()
FROM sys_menu m WHERE m.perms = 'pathway:node:list' AND m.menu_type = 'C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '修改节点', m.menu_id, 2, '', 'F', '0', '0', 'pathway:node:edit', 'admin', NOW()
FROM sys_menu m WHERE m.perms = 'pathway:node:list' AND m.menu_type = 'C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '删除节点', m.menu_id, 3, '', 'F', '0', '0', 'pathway:node:remove', 'admin', NOW()
FROM sys_menu m WHERE m.perms = 'pathway:node:list' AND m.menu_type = 'C';

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '查询节点', m.menu_id, 4, '', 'F', '0', '0', 'pathway:node:query', 'admin', NOW()
FROM sys_menu m WHERE m.perms = 'pathway:node:list' AND m.menu_type = 'C';

-- Elevate 预警管理 from sub-menu to top-level (340)
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
(340, '预警管理',   0, 14, 'warnmgmt',    NULL, 'M', '0', '0', '',             'warning',    '1', '0', 'admin', NOW());

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('预警配置',    340, 1, 'warnConfig',   'warn/config/index',    'C', '0', '0', 'warn:config:list',   'setting', '1', '0', 'admin', NOW()),
('预警记录',    340, 2, 'warnRecords',  'warn/records/index',   'C', '0', '0', 'warn:records:list',   'warning', '1', '0', 'admin', NOW());

-- Elevate 宣教管理 from 知识库管理 to top-level (350)
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
(350, '宣教管理',   0, 15, 'edumgmt',     NULL, 'M', '0', '0', '',             'education',  '1', '0', 'admin', NOW());

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('宣教内容管理', 350, 1, 'eduContent',   'education/content/index',    'C', '0', '0', 'education:content:list', 'article', '1', '0', 'admin', NOW()),
('宣教方案分配', 350, 2, 'eduAssignment','education/assignment/index', 'C', '0', '0', 'education:assign:list',  'link',    '1', '0', 'admin', NOW());

-- Elevate 系统配置 from 基础管理 to top-level (360)
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
(360, '系统配置',   0, 16, 'sysconfig',   NULL, 'M', '0', '0', '',             'settings',   '1', '0', 'admin', NOW());

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('角色管理',    360, 1, 'role',         'system/role/index',      'C', '0', '0', 'system:role:list',    'peoples',  '1', '0', 'admin', NOW()),
('菜单管理',    360, 2, 'menu',         'system/menu/index',      'C', '0', '0', 'system:menu:list',    'tree-table','1','0', 'admin', NOW());

-- New top-level: 慢病门诊 (330)
INSERT IGNORE INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
(330, '慢病门诊',   0, 8,  'outpatient',  NULL, 'M', '0', '0', '',             'clinic',     '1', '0', 'admin', NOW());

INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) VALUES
('信息采集',    330, 1, 'collect',      'outpatient/collect/index',    'C', '0', '0', 'outpatient:collect:list', 'form',    '1', '0', 'admin', NOW()),
('智能分诊',    330, 2, 'triage',       'outpatient/triage/index',     'C', '0', '0', 'outpatient:triage:list',  'guide',   '1', '0', 'admin', NOW()),
('接诊管理',    330, 3, 'reception',    'outpatient/reception/index',  'C', '0', '0', 'outpatient:reception:list','user',  '1', '0', 'admin', NOW()),
('调整分诊',    330, 4, 'adjust',       'outpatient/adjust/index',     'C', '0', '0', 'outpatient:adjust:list',   'edit',    '1', '0', 'admin', NOW());

-- Grant admin role access
INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_id IN (330, 340, 350, 360) AND status = '0';

INSERT IGNORE INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE parent_id IN (200, 330, 340, 350, 360) AND status = '0';
