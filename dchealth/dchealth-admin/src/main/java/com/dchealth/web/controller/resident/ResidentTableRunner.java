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
 * Ensure resident module tables exist (resident_grading_record, resident_cross_org_access).
 */
@Component
@Order(10)
public class ResidentTableRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ResidentTableRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // 分级评估记录表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS resident_grading_record (" +
                "  id bigserial PRIMARY KEY," +
                "  person_id VARCHAR(50)," +
                "  assessment_date DATE," +
                "  bp_classification VARCHAR(50)," +
                "  management_level VARCHAR(50)," +
                "  risk_stratification VARCHAR(50)," +
                "  cardiovascular_factors TEXT," +
                "  target_organ_damage TEXT," +
                "  comorbid_conditions TEXT," +
                "  assessor VARCHAR(50)," +
                "  next_assessment_date DATE," +
                "  remark VARCHAR(500)," +
                "  create_by VARCHAR(64)," +
                "  create_time TIMESTAMP," +
                "  update_by VARCHAR(64)," +
                "  update_time TIMESTAMP" +
                ")"
            );
            log.info("=== Ensured table resident_grading_record exists ===");

            // 跨机构调取日志表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS resident_cross_org_access (" +
                "  id bigserial PRIMARY KEY," +
                "  person_name VARCHAR(50)," +
                "  card_id VARCHAR(18)," +
                "  source_institution VARCHAR(100)," +
                "  source_doctor VARCHAR(50)," +
                "  target_institution VARCHAR(100)," +
                "  target_doctor VARCHAR(50)," +
                "  access_time TIMESTAMP," +
                "  access_reason VARCHAR(255)," +
                "  status VARCHAR(20) DEFAULT '1'," +
                "  create_by VARCHAR(64)," +
                "  create_time TIMESTAMP" +
                ")"
            );
            log.info("=== Ensured table resident_cross_org_access exists ===");

        } catch (Exception e) {
            log.error("Failed to create resident tables: {}", e.getMessage());
        }
    }
}
