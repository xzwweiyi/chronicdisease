package com.dchealth.web.controller.system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * One-time menu migration runner. Executes V2__menu_restructure.sql on startup.
 * Tracks execution in sys_migration table to ensure idempotency.
 * Remove this class after successful migration.
 */
@Component
@Order(1)
public class MenuMigrationRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(MenuMigrationRunner.class);
    private static final String MIGRATION_VERSION = "V2__menu_restructure";

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            // Ensure migration tracking table exists
            ensureMigrationTable(conn);

            // Check if this migration has already been applied
            if (isMigrationApplied(conn)) {
                log.info("=== Migration {} already applied, skipping ===", MIGRATION_VERSION);
                return;
            }

            log.info("=== Starting menu migration {} ===", MIGRATION_VERSION);

            // Step 0: Clean up duplicate menus before migration
            cleanupDuplicateMenus(conn);

            // Execute the migration SQL
            ClassPathResource resource = new ClassPathResource("db/migration/V2__menu_restructure.sql");
            if (!resource.exists()) {
                log.info("Migration SQL file not found, skipping");
                return;
            }

            int count = 0;
            try (InputStream is = resource.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {

                StringBuilder sql = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    // Skip comments and empty lines
                    String trimmed = line.trim();
                    if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                        continue;
                    }
                    sql.append(line).append("\n");
                    // Execute when semicolon found
                    if (trimmed.endsWith(";")) {
                        String stmt = sql.toString();
                        sql = new StringBuilder();
                        if (stmt.trim().isEmpty()) continue;

                        try (Statement s = conn.createStatement()) {
                            s.execute(stmt);
                            count++;
                        } catch (Exception e) {
                            log.debug("SQL statement skipped (may be expected): {}", e.getMessage());
                        }
                    }
                }

                // Execute remaining SQL without trailing semicolon
                if (sql.length() > 0 && sql.toString().trim().length() > 0) {
                    try (Statement s = conn.createStatement()) {
                        s.execute(sql.toString());
                        count++;
                    } catch (Exception e) {
                        log.debug("Final SQL statement skipped: {}", e.getMessage());
                    }
                }
            }

            // Record migration as applied
            recordMigration(conn);
            log.info("=== Menu migration {} completed. {} statements executed. ===", MIGRATION_VERSION, count);

        } catch (Exception e) {
            log.error("Menu migration failed: {}", e.getMessage(), e);
        }
    }

    /**
     * Ensure the migration tracking table exists.
     */
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

    /**
     * Check if the migration has already been applied.
     */
    private boolean isMigrationApplied(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(1) FROM sys_migration WHERE version = ?")) {
            ps.setString(1, MIGRATION_VERSION);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Record this migration as applied.
     */
    private void recordMigration(Connection conn) throws Exception {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO sys_migration (version, description) VALUES (?, ?)")) {
            ps.setString(1, MIGRATION_VERSION);
            ps.setString(2, "Menu restructure: transform from sidebar to horizontal top-nav");
            ps.executeUpdate();
        }
    }

    /**
     * Clean up duplicate menus created by repeated migration runs.
     * Keeps the first inserted menu (smallest menu_id) and removes duplicates
     * that have the same (menu_name, parent_id, menu_type, perms).
     */
    private void cleanupDuplicateMenus(Connection conn) throws Exception {
        log.info("=== Cleaning up duplicate menus ===");

        // Remove duplicate menus: keep the one with smallest menu_id per (menu_name, parent_id, menu_type)
        // For menus without perms, match on (menu_name, parent_id, menu_type)
        String cleanupSql =
            "DELETE m1 FROM sys_menu m1 " +
            "INNER JOIN (" +
            "    SELECT menu_name, parent_id, menu_type, " +
            "           COALESCE(perms, '') as perms_key, " +
            "           MIN(menu_id) as keep_id " +
            "    FROM sys_menu " +
            "    GROUP BY menu_name, parent_id, menu_type, COALESCE(perms, '') " +
            "    HAVING COUNT(*) > 1" +
            ") m2 ON m1.menu_name = m2.menu_name " +
            "    AND m1.parent_id = m2.parent_id " +
            "    AND m1.menu_type = m2.menu_type " +
            "    AND COALESCE(m1.perms, '') = m2.perms_key " +
            "    AND m1.menu_id > m2.keep_id";

        try (Statement s = conn.createStatement()) {
            int deleted = s.executeUpdate(cleanupSql);
            if (deleted > 0) {
                log.info("=== Cleaned up {} duplicate menu records ===", deleted);
            } else {
                log.info("=== No duplicate menus found ===");
            }
        } catch (Exception e) {
            log.warn("Duplicate menu cleanup failed (may be expected): {}", e.getMessage());
        }
    }
}
