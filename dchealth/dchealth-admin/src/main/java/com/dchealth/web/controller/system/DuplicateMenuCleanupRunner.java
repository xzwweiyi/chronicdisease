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
 * Aggressive duplicate menu cleanup runner.
 * Removes all duplicate menus, keeping the record with the smallest menu_id.
 * Runs once, tracked in sys_migration.
 */
@Component
@Order(0) // Run before MenuMigrationRunner
public class DuplicateMenuCleanupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DuplicateMenuCleanupRunner.class);
    private static final String CLEANUP_VERSION = "V1__cleanup_duplicate_menus";

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            ensureMigrationTable(conn);

            if (isCleanupApplied(conn)) {
                log.info("=== Duplicate menu cleanup already applied, skipping ===");
                return;
            }

            logDuplicateStats(conn, "BEFORE cleanup");

            // Step 1: Remove duplicate M/C type menus by (menu_name, parent_id, menu_type)
            // This catches cases like two "队列列表" under the same parent
            int deletedMC = deleteDuplicateMC(conn);

            // Step 2: Remove duplicate F type buttons by (menu_name, parent_id, menu_type, perms)
            int deletedF = deleteDuplicateF(conn);

            log.info("=== Cleaned up duplicate menus: M/C={}, F={} ===", deletedMC, deletedF);
            logDuplicateStats(conn, "AFTER cleanup");

            // Reset V2 migration tracking so it re-runs with updated DELETE-before-INSERT SQL
            resetV2MigrationTracking(conn);

            recordCleanup(conn);

        } catch (Exception e) {
            log.error("Duplicate menu cleanup failed: {}", e.getMessage(), e);
        }
    }

    private void ensureMigrationTable(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            s.execute(
                "CREATE TABLE IF NOT EXISTS sys_migration (" +
                "    version VARCHAR(64) PRIMARY KEY," +
                "    applied_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "    description VARCHAR(255)" +
                ")"
            );
        }
    }

    private boolean isCleanupApplied(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(1) FROM sys_migration WHERE version = ?")) {
            ps.setString(1, CLEANUP_VERSION);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private void recordCleanup(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sys_migration (version, description) VALUES (?, ?)")) {
            ps.setString(1, CLEANUP_VERSION);
            ps.setString(2, "Aggressive duplicate menu cleanup");
            ps.executeUpdate();
        }
    }

    /**
     * Delete duplicate menus of type M/C.
     * Groups by (menu_name, parent_id, menu_type), keeps the smallest menu_id.
     */
    private int deleteDuplicateMC(Connection conn) throws Exception {
        String sql =
            "DELETE m1 FROM sys_menu m1 " +
            "INNER JOIN (" +
            "    SELECT menu_name, parent_id, menu_type, MIN(menu_id) as keep_id " +
            "    FROM sys_menu " +
            "    WHERE menu_type IN ('M', 'C') " +
            "    GROUP BY menu_name, parent_id, menu_type " +
            "    HAVING COUNT(*) > 1" +
            ") m2 ON m1.menu_name = m2.menu_name " +
            "    AND m1.parent_id = m2.parent_id " +
            "    AND m1.menu_type = m2.menu_type " +
            "    AND m1.menu_id > m2.keep_id";

        try (Statement s = conn.createStatement()) {
            int deleted = s.executeUpdate(sql);
            return Math.max(deleted, 0);
        }
    }

    /**
     * Delete duplicate button-permission menus of type F.
     * Groups by (menu_name, parent_id, menu_type, COALESCE(perms, '')),
     * keeps the smallest menu_id.
     */
    private int deleteDuplicateF(Connection conn) throws Exception {
        String sql =
            "DELETE m1 FROM sys_menu m1 " +
            "INNER JOIN (" +
            "    SELECT menu_name, parent_id, menu_type, COALESCE(perms, '') as perms_key, MIN(menu_id) as keep_id " +
            "    FROM sys_menu " +
            "    WHERE menu_type = 'F' " +
            "    GROUP BY menu_name, parent_id, menu_type, COALESCE(perms, '') " +
            "    HAVING COUNT(*) > 1" +
            ") m2 ON m1.menu_name = m2.menu_name " +
            "    AND m1.parent_id = m2.parent_id " +
            "    AND m1.menu_type = m2.menu_type " +
            "    AND COALESCE(m1.perms, '') = m2.perms_key " +
            "    AND m1.menu_id > m2.keep_id";

        try (Statement s = conn.createStatement()) {
            int deleted = s.executeUpdate(sql);
            return Math.max(deleted, 0);
        }
    }

    /**
     * Remove the V2 migration tracking record so it re-runs with updated
     * DELETE-before-INSERT logic that properly replaces old re-parented menus.
     */
    private void resetV2MigrationTracking(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            int deleted = s.executeUpdate(
                "DELETE FROM sys_migration WHERE version = 'V2__menu_restructure'");
            if (deleted > 0) {
                log.info("=== Reset V2 migration tracking - will re-run with updated SQL ===");
            }
        }
    }

    private void logDuplicateStats(Connection conn, String label) throws Exception {
        try (Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery(
                 "SELECT COUNT(*) as dup_groups, COALESCE(SUM(cnt - 1), 0) as dup_records " +
                 "FROM (SELECT COUNT(*) as cnt FROM sys_menu " +
                 "      GROUP BY menu_name, parent_id, menu_type HAVING cnt > 1) t")) {
            if (rs.next()) {
                log.info("=== [{}] Duplicate groups: {}, Duplicate records: {} ===",
                    label, rs.getInt(1), rs.getInt(2));
            }
        }
    }
}
