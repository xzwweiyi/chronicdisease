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
 * Ensure ALL menus are properly granted to admin roles.
 * Fixes 403 errors caused by incomplete role_menu grants after migration.
 */
@Component
@Order(3)
public class MenuPermissionFixRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MenuPermissionFixRunner.class);
    private static final String VERSION = "V4__fix_menu_permissions";

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        log.warn("=== MenuPermissionFixRunner STARTING ===");
        try (Connection conn = dataSource.getConnection()) {
            int count = grantAllMenusToAllRoles(conn);
            log.warn("=== MenuPermissionFixRunner granted {} rows ===", count);
        } catch (Exception e) {
            log.error("Permission fix failed: {}", e.getMessage(), e);
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
            ps.setString(2, "Fix menu permissions for all roles");
            ps.executeUpdate();
        }
    }

    private int grantAllMenusToAllRoles(Connection conn) throws Exception {
        // Insert missing role_menu associations for all active roles and active menus
        String sql =
            "INSERT IGNORE INTO sys_role_menu (role_id, menu_id) " +
            "SELECT r.role_id, m.menu_id " +
            "FROM sys_role r " +
            "CROSS JOIN sys_menu m " +
            "WHERE r.status = '0' AND r.del_flag = '0' " +
            "  AND m.status = '0' AND m.menu_type IN ('M', 'C')";

        try (Statement s = conn.createStatement()) {
            int count = s.executeUpdate(sql);
            return count;
        }
    }
}
