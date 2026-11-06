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
                "  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键'," +
                "  person_id VARCHAR(50) COMMENT '人员标识（身份证号）'," +
                "  assessment_date DATE COMMENT '评估日期'," +
                "  bp_classification VARCHAR(50) COMMENT '血压分级'," +
                "  management_level VARCHAR(50) COMMENT '管理级别'," +
                "  risk_stratification VARCHAR(50) COMMENT '危险分层'," +
                "  cardiovascular_factors TEXT COMMENT '心血管危险因素(JSON数组)'," +
                "  target_organ_damage TEXT COMMENT '靶器官损害(JSON数组)'," +
                "  comorbid_conditions TEXT COMMENT '并存临床情况(JSON数组)'," +
                "  assessor VARCHAR(50) COMMENT '评估人'," +
                "  next_assessment_date DATE COMMENT '下次评估日期'," +
                "  remark VARCHAR(500) COMMENT '备注'," +
                "  create_by VARCHAR(64) COMMENT '创建者'," +
                "  create_time DATETIME COMMENT '创建时间'," +
                "  update_by VARCHAR(64) COMMENT '更新者'," +
                "  update_time DATETIME COMMENT '更新时间'" +
                ") COMMENT '居民分级评估记录'"
            );
            log.info("=== Ensured table resident_grading_record exists ===");

            // 跨机构调取日志表
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS resident_cross_org_access (" +
                "  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键'," +
                "  person_name VARCHAR(50) COMMENT '居民姓名'," +
                "  card_id VARCHAR(18) COMMENT '身份证号'," +
                "  source_institution VARCHAR(100) COMMENT '来源机构'," +
                "  source_doctor VARCHAR(50) COMMENT '来源医生'," +
                "  target_institution VARCHAR(100) COMMENT '目标机构'," +
                "  target_doctor VARCHAR(50) COMMENT '目标医生'," +
                "  access_time DATETIME COMMENT '调取时间'," +
                "  access_reason VARCHAR(255) COMMENT '调取原因'," +
                "  status VARCHAR(20) DEFAULT '1' COMMENT '状态'," +
                "  create_by VARCHAR(64) COMMENT '创建者'," +
                "  create_time DATETIME COMMENT '创建时间'" +
                ") COMMENT '跨机构调取日志'"
            );
            log.info("=== Ensured table resident_cross_org_access exists ===");

        } catch (Exception e) {
            log.error("Failed to create resident tables: {}", e.getMessage());
        }
    }
}
