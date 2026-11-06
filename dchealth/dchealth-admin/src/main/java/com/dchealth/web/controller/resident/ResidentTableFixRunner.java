package com.dchealth.web.controller.resident;

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
 * Add missing columns to resident_archive table.
 */
@Component
@Order(4)
public class ResidentTableFixRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ResidentTableFixRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement s = conn.createStatement()) {

            String[][] columns = {
                {"archive_code", "VARCHAR(50) COMMENT '档案编码'"},
                {"age", "INT COMMENT '年龄'"},
                {"managing_institution", "VARCHAR(100) COMMENT '管理机构'"},
                {"managing_doctor", "VARCHAR(50) COMMENT '管理医生'"},
                {"archive_source", "VARCHAR(2) COMMENT '档案来源'"},
                {"transfer_in_date", "DATE COMMENT '转入管理日期'"},
                {"is_signed", "VARCHAR(2) COMMENT '是否签约'"},
            };

            for (String[] col : columns) {
                String colName = col[0];
                String colDef = col[1];
                try {
                    // 使用 information_schema 精确查询当前数据库的列（避免 JDBC metadata 的跨库误报）
                    java.sql.ResultSet rs = s.executeQuery(
                        "SELECT COLUMN_NAME FROM information_schema.COLUMNS " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'resident_archive' AND COLUMN_NAME = '" + colName + "'");
                    if (!rs.next()) {
                        s.execute("ALTER TABLE resident_archive ADD COLUMN " + colName + " " + colDef);
                        log.info("=== Added column: {} ===", colName);
                    } else {
                        log.debug("=== Column already exists: {} ===", colName);
                    }
                    rs.close();
                } catch (Exception e) {
                    if (e.getMessage() != null && e.getMessage().contains("Duplicate column")) {
                        log.debug("ALTER skipped (duplicate): {}", e.getMessage());
                    } else {
                        log.warn("ALTER column {} skipped: {}", colName, e.getMessage());
                    }
                }
            }
            log.info("=== Resident archive table schema verified ===");

            // Also fix menu component paths for resident sub-menus
            fixMenuComponents(conn);

        } catch (Exception e) {
            log.debug("Resident table fix skipped: {}", e.getMessage());
        }
    }

    private void fixMenuComponents(Connection conn) throws Exception {
        try (Statement s = conn.createStatement()) {
            // Unconditionally fix ALL resident sub-menus
            String[][] fixes = {
                {"档案管理",   "resident/archive/index"},
                {"高血压患者", "resident/hypertension/index"},
                {"糖尿病患者", "resident/diabetes/index"},
                {"脑卒中患者", "resident/stroke/index"},
                {"冠心病患者", "resident/chd/index"},
                {"慢阻肺患者", "resident/copd/index"},
                {"合并症患者", "resident/comorbidity/index"},
            };
            int count = 0;
            for (String[] fix : fixes) {
                // Force fix menu_type and component regardless of current values
                int n = s.executeUpdate(
                    "UPDATE sys_menu SET menu_type = 'C', component = '" + fix[1] + "', status = '0', visible = '0' " +
                    "WHERE menu_name = '" + fix[0] + "' AND (menu_type != 'C' OR component != '" + fix[1] + "' OR status != '0')");
                count += n;
            }
            log.info("=== Fixed {} resident menu entries ===", count);
        }
    }
}
