package com.dchealth.web.controller.system;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Ensure the dashboard menu entries exist in sys_menu.
 * Inserts "首页" top-level menu with "工作台" child.
 * Runs once, tracked in sys_migration.
 */
@Component
@Order(2) // Run after DuplicateMenuCleanupRunner and MenuMigrationRunner
public class DashboardMenuRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DashboardMenuRunner.class);
    private static final String VERSION = "V3__dashboard_menu";
    private static final String VERSION_FIX = "V3__dashboard_menu_fix";

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ensureMigrationTable(conn);

            // Always fix the "首页" path to "/" to avoid duplicate tabs
            fixHomePath(conn);

            // Always ensure dashboard menu entries exist
            log.info("=== Ensuring dashboard menu entries ===");
            Long homeMenuId = ensureTopLevelMenu(conn);
            Long workbenchMenuId = ensureWorkbenchMenu(conn, homeMenuId);

            // Always grant admin access (idempotent INSERT IGNORE)
            grantRoleAccess(conn, homeMenuId, workbenchMenuId);
            log.info("=== Dashboard menu verified: home={}, workbench={} ===", homeMenuId, workbenchMenuId);

        } catch (Exception e) {
            log.error("Dashboard menu setup failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Ensure only ONE "首页" top-level menu exists as M-type directory with path="/".
     * The TopNav auto-navigate will redirect to /dashboard when clicked.
     */
    private void fixHomePath(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            // Delete duplicates
            int deleted = s.executeUpdate(
                "DELETE m1 FROM sys_menu m1 " +
                "INNER JOIN (SELECT MIN(menu_id) AS keep_id FROM sys_menu WHERE menu_name = '首页' AND parent_id = 0) m2 " +
                "ON m1.menu_id > m2.keep_id AND m1.menu_name = '首页' AND m1.parent_id = 0");
            if (deleted > 0) log.info("=== Deleted {} duplicate '首页' entries ===", deleted);

            // Fix: M-type directory, path="/", no component
            int updated = s.executeUpdate(
                "UPDATE sys_menu SET menu_type = 'M', path = '', component = NULL, perms = '' " +
                "WHERE menu_name = '首页' AND parent_id = 0");
            if (updated > 0) log.info("=== Fixed '首页' to M-type directory ({} rows) ===", updated);
        }
    }

    private void ensureMigrationTable(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            s.execute(
                "CREATE TABLE IF NOT EXISTS sys_migration (" +
                "    version VARCHAR(64) PRIMARY KEY," +
                "    applied_time DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "    description VARCHAR(255)" +
                ") COMMENT '迁移版本追踪表'"
            );
        }
    }

    private boolean isApplied(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(1) FROM sys_migration WHERE version = ?")) {
            ps.setString(1, VERSION);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void recordApplied(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sys_migration (version, description) VALUES (?, ?)")) {
            ps.setString(1, VERSION);
            ps.setString(2, "Dashboard menu: 首页 + 工作台");
            ps.executeUpdate();
        }
    }

    /**
     * Insert "首页" as the first top-level menu (before 慢病路径管理 at order 1).
     * Uses fixed menu_id=99 to avoid conflicts.
     */
    private Long ensureTopLevelMenu(Connection conn) throws Exception {
        // Check if already exists
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT menu_id FROM sys_menu WHERE menu_name = '首页' AND parent_id = 0")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("menu_id");
                    log.info("Top-level menu '首页' already exists with id={}", id);
                    return id;
                }
            }
        }

        // Insert new top-level M-type directory
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sys_menu (menu_id, menu_name, parent_id, order_num, path, component, " +
                "menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) " +
                "VALUES (99, '首页', 0, 0, '', 'Layout', 'M', '0', '0', '', 'dashboard', '1', '0', 'admin', NOW())",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    log.info("Inserted top-level menu '首页' with id={}", id);
                    return id;
                }
            }
        }

        // Fallback: try to query again
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT menu_id FROM sys_menu WHERE menu_name = '首页' AND parent_id = 0")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("menu_id");
                }
            }
        }

        return 99L;
    }

    /**
     * Insert "工作台" child menu under the "首页" parent.
     */
    private Long ensureWorkbenchMenu(Connection conn, Long parentId) throws Exception {
        // Check if already exists
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT menu_id FROM sys_menu WHERE menu_name = '工作台' AND parent_id = ?")) {
            ps.setLong(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("menu_id");
                    log.info("Child menu '工作台' already exists under parent_id={}, id={}", parentId, id);
                    return id;
                }
            }
        }

        // Delete any old "工作台" entries that might be under different parents
        try (Statement s = conn.createStatement()) {
            s.executeUpdate("DELETE FROM sys_menu WHERE menu_name = '工作台' AND menu_type = 'C' AND parent_id != " + parentId);
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, " +
                "menu_type, visible, status, perms, icon, is_frame, is_cache, create_by, create_time) " +
                "VALUES ('工作台', ?, 1, 'dashboard', 'index', " +
                "'C', '0', '0', 'dashboard:workbench:list', 'dashboard', '1', '0', 'admin', NOW())",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, parentId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    log.info("Inserted child menu '工作台' under parent_id={}, id={}", parentId, id);
                    return id;
                }
            }
        }

        // Fallback query
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT menu_id FROM sys_menu WHERE menu_name = '工作台' AND parent_id = ?")) {
            ps.setLong(1, parentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getLong("menu_id");
            }
        }
        return null;
    }

    /**
     * Grant all active admin roles access to the dashboard menus.
     */
    private void grantRoleAccess(Connection conn, Long homeMenuId, Long workbenchMenuId) throws Exception {
        if (homeMenuId != null) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT IGNORE INTO sys_role_menu (role_id, menu_id) " +
                    "SELECT role_id, ? FROM sys_role WHERE status = '0' AND del_flag = '0'")) {
                ps.setLong(1, homeMenuId);
                ps.executeUpdate();
            }
        }
        if (workbenchMenuId != null) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT IGNORE INTO sys_role_menu (role_id, menu_id) " +
                    "SELECT role_id, ? FROM sys_role WHERE status = '0' AND del_flag = '0'")) {
                ps.setLong(1, workbenchMenuId);
                ps.executeUpdate();
            }
        }
        log.info("Granted role access for dashboard menus: home={}, workbench={}", homeMenuId, workbenchMenuId);
    }
}
