package com.dchealth.web.controller.medical;

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
 * 确保 patient_info 表存在「重点关注」标记列 focus_status。
 */
@Component
@Order(10)
public class PatientFocusStatusRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PatientFocusStatusRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement s = conn.createStatement()) {
            java.sql.ResultSet rs = s.executeQuery(
                "SELECT COLUMN_NAME FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = current_schema() AND TABLE_NAME = 'patient_info' AND COLUMN_NAME = 'focus_status'");
            if (!rs.next()) {
                s.execute("ALTER TABLE patient_info ADD COLUMN focus_status char(1) NOT NULL DEFAULT '0'");
                log.info("=== Added column: patient_info.focus_status ===");
            } else {
                log.debug("=== Column already exists: patient_info.focus_status ===");
            }
            rs.close();
        } catch (Exception e) {
            log.warn("patient_info.focus_status ensure skipped: {}", e.getMessage());
        }
    }
}
