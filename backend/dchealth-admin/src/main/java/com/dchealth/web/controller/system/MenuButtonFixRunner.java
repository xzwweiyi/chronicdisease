package com.dchealth.web.controller.system;

import java.sql.Connection;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Ensure every C-type menu has standard CRUD button permissions (查询/新增/修改/删除).
 * Runs on every startup, idempotent via INSERT IGNORE.
 */
@Component
@Order(4)
public class MenuButtonFixRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MenuButtonFixRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            int total = 0;

            // 1. C型菜单有 :list 权限的，直接替换生成按钮权限
            total += addButtons(conn,
                "SELECT '查询', m.menu_id, 1, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':query'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '查询')");

            total += addButtons(conn,
                "SELECT '新增', m.menu_id, 2, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':add'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '新增')");

            total += addButtons(conn,
                "SELECT '修改', m.menu_id, 3, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':edit'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '修改')");

            total += addButtons(conn,
                "SELECT '删除', m.menu_id, 4, '', 'F', '0', '0', REPLACE(m.perms, ':list', ':remove'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '删除')");

            // 2. C型菜单没有 :list 权限但有其他权限的，用路径生成按钮
            total += addButtons(conn,
                "SELECT '查询', m.menu_id, 1, '', 'F', '0', '0', CONCAT(m.perms, ':query'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != '' AND m.perms NOT LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '查询')");

            total += addButtons(conn,
                "SELECT '新增', m.menu_id, 2, '', 'F', '0', '0', CONCAT(m.perms, ':add'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != '' AND m.perms NOT LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '新增')");

            total += addButtons(conn,
                "SELECT '修改', m.menu_id, 3, '', 'F', '0', '0', CONCAT(m.perms, ':edit'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != '' AND m.perms NOT LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '修改')");

            total += addButtons(conn,
                "SELECT '删除', m.menu_id, 4, '', 'F', '0', '0', CONCAT(m.perms, ':remove'), '1', '0', 'admin', NOW() " +
                "FROM sys_menu m WHERE m.menu_type = 'C' AND m.status = '0' AND m.perms IS NOT NULL AND m.perms != '' AND m.perms NOT LIKE '%:list' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_menu b WHERE b.parent_id = m.menu_id AND b.menu_name = '删除')");

            if (total > 0) {
                log.info("=== Added {} missing menu button permissions ===", total);
            }

            // Grant all F-type menu buttons to all active roles
            int granted = grantAllButtons(conn);
            if (granted > 0) {
                log.info("=== Granted {} button role associations ===", granted);
            }
        } catch (Exception e) {
            log.error("Menu button fix failed: {}", e.getMessage(), e);
        }
    }

    private int addButtons(Connection conn, String selectSql) throws Exception {
        String sql = "INSERT IGNORE INTO sys_menu (menu_name, parent_id, order_num, path, menu_type, visible, status, perms, is_frame, is_cache, create_by, create_time) " + selectSql;
        try (Statement s = conn.createStatement()) {
            return s.executeUpdate(sql);
        }
    }

    private int grantAllButtons(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            return s.executeUpdate(
                "INSERT IGNORE INTO sys_role_menu (role_id, menu_id) " +
                "SELECT r.role_id, m.menu_id FROM sys_role r CROSS JOIN sys_menu m " +
                "WHERE r.status = '0' AND m.status = '0' AND m.menu_type = 'F' " +
                "AND NOT EXISTS (SELECT 1 FROM sys_role_menu rm WHERE rm.role_id = r.role_id AND rm.menu_id = m.menu_id)");
        }
    }

    private int execute(Connection conn, String sql) throws Exception {
        try (Statement s = conn.createStatement()) {
            return s.executeUpdate(sql);
        }
    }
}
