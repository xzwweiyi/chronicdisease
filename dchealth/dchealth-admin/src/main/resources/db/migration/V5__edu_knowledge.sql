-- ================================================================
-- V5: 宣教知识库 — 文章管理改造为宣教知识库 + 新增宣教分类管理
-- Run this SQL against chronic_disease_new database
-- ================================================================

-- Step 1: 宣教分类表
CREATE TABLE IF NOT EXISTS cms_edu_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类编号',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    sort INT DEFAULT 0 COMMENT '显示排序',
    status CHAR(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
    create_by VARCHAR(64) DEFAULT '' COMMENT '创建者',
    create_time DATETIME COMMENT '创建时间',
    update_by VARCHAR(64) DEFAULT '' COMMENT '更新者',
    update_time DATETIME COMMENT '更新时间',
    remark VARCHAR(500) DEFAULT NULL COMMENT '备注',
    del_flag CHAR(1) DEFAULT 'N' COMMENT '删除标志（N未删除 Y已删除）'
) COMMENT '宣教分类';

-- Step 2: cms_article 增加宣教分类字段（若已存在请跳过，由执行脚本做存在性检查）
-- ALTER TABLE cms_article ADD COLUMN edu_category_id BIGINT DEFAULT NULL COMMENT '宣教分类编号' AFTER category_id;

-- Step 3: 宣教分类种子数据
INSERT IGNORE INTO cms_edu_category (id, name, sort, status, create_by, create_time, remark, del_flag) VALUES
(1, '术后宣教', 1, '0', 'admin', NOW(), NULL, 'N'),
(2, '用药指导', 2, '0', 'admin', NOW(), NULL, 'N'),
(3, '门诊宣教', 3, '0', 'admin', NOW(), NULL, 'N'),
(4, '护理宣教', 4, '0', 'admin', NOW(), NULL, 'N'),
(5, '饮食指导', 5, '0', 'admin', NOW(), NULL, 'N'),
(6, '术前宣教', 6, '0', 'admin', NOW(), NULL, 'N'),
(7, '出院宣教', 7, '0', 'admin', NOW(), NULL, 'N'),
(8, '入院宣教', 8, '0', 'admin', NOW(), NULL, 'N'),
(9, '其他宣教', 9, '0', 'admin', NOW(), NULL, 'N');

-- Step 4: 菜单「文章管理」→「宣教知识库」（知识库管理 230 下）
UPDATE sys_menu SET menu_name='宣教知识库' WHERE parent_id=230 AND path='article';

-- Step 5: 新增「宣教分类」菜单（知识库管理 230 下）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time)
SELECT '宣教分类', 230, 6, 'eduCategory', 'cms/eduCategory/index', 'C', '0', '0', 'cms:eduCategory:list', 'list', '1', '0', 'admin', NOW()
WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE parent_id=230 AND path='eduCategory');

-- Step 6: 宣教分类按钮权限（查询/新增/修改/删除）
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '查询', m.menu_id, 1, '', 'F', '0', '0', 'cms:eduCategory:query', 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=230 AND m.path='eduCategory'
AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms='cms:eduCategory:query');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '新增', m.menu_id, 2, '', 'F', '0', '0', 'cms:eduCategory:add', 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=230 AND m.path='eduCategory'
AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms='cms:eduCategory:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '修改', m.menu_id, 3, '', 'F', '0', '0', 'cms:eduCategory:edit', 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=230 AND m.path='eduCategory'
AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms='cms:eduCategory:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, create_by, create_time)
SELECT '删除', m.menu_id, 4, '', 'F', '0', '0', 'cms:eduCategory:remove', 'admin', NOW()
FROM sys_menu m WHERE m.parent_id=230 AND m.path='eduCategory'
AND NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms='cms:eduCategory:remove');

-- Step 7: 授权给所有启用角色
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT r.role_id, m.menu_id FROM sys_role r, sys_menu m
WHERE r.status='0' AND m.perms IN ('cms:eduCategory:list','cms:eduCategory:query','cms:eduCategory:add','cms:eduCategory:edit','cms:eduCategory:remove')
AND NOT EXISTS (SELECT 1 FROM sys_role_menu rm WHERE rm.role_id=r.role_id AND rm.menu_id=m.menu_id);
