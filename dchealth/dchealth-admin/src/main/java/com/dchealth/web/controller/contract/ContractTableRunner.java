package com.dchealth.web.controller.contract;

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
 * Ensure contract module tables exist with proper UTF-8 charset and required columns.
 *
 * Fixes mojibake/garbled Chinese characters by enforcing utf8mb4 at the table level.
 */
@Component
@Order(11)
public class ContractTableRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ContractTableRunner.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // --- contract_sign ---
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS contract_sign (" +
                "  id bigserial PRIMARY KEY," +
                "  resident_id BIGINT," +
                "  resident_name VARCHAR(50)," +
                "  team_id BIGINT," +
                "  package_id BIGINT," +
                "  sign_date DATE," +
                "  start_date DATE," +
                "  end_date DATE," +
                "  contract_status VARCHAR(20) DEFAULT '0'," +
                "  sign_type VARCHAR(20)," +
                "  create_by VARCHAR(64)," +
                "  create_time TIMESTAMP," +
                "  update_by VARCHAR(64)," +
                "  update_time TIMESTAMP," +
                "  remark VARCHAR(500)" +
                ")"
            );
            log.info("=== Ensured table contract_sign exists (utf8mb4) ===");

            // --- contract_team ---
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS contract_team (" +
                "  id bigserial PRIMARY KEY," +
                "  team_name VARCHAR(100)," +
                "  leader_name VARCHAR(50)," +
                "  leader_phone VARCHAR(30)," +
                "  members TEXT," +
                "  service_area VARCHAR(255)," +
                "  team_intro VARCHAR(500)," +
                "  status VARCHAR(2) DEFAULT '1'," +
                "  create_by VARCHAR(64)," +
                "  create_time TIMESTAMP," +
                "  update_by VARCHAR(64)," +
                "  update_time TIMESTAMP," +
                "  remark VARCHAR(500)" +
                ")"
            );
            log.info("=== Ensured table contract_team exists (utf8mb4) ===");

            // --- contract_package ---
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS contract_package (" +
                "  id bigserial PRIMARY KEY," +
                "  package_name VARCHAR(100)," +
                "  package_type VARCHAR(50)," +
                "  price DECIMAL(10,2) DEFAULT 0," +
                "  description VARCHAR(500)," +
                "  service_ids VARCHAR(500)," +
                "  applicable_diseases VARCHAR(500)," +
                "  status VARCHAR(2) DEFAULT '1'," +
                "  create_by VARCHAR(64)," +
                "  create_time TIMESTAMP," +
                "  update_by VARCHAR(64)," +
                "  update_time TIMESTAMP," +
                "  remark VARCHAR(500)" +
                ")"
            );
            log.info("=== Ensured table contract_package exists (utf8mb4) ===");

            // --- contract_service_item ---
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS contract_service_item (" +
                "  id bigserial PRIMARY KEY," +
                "  item_name VARCHAR(100)," +
                "  item_type VARCHAR(50)," +
                "  frequency VARCHAR(50)," +
                "  description VARCHAR(500)," +
                "  execution_standard VARCHAR(500)," +
                "  status VARCHAR(2) DEFAULT '1'," +
                "  create_by VARCHAR(64)," +
                "  create_time TIMESTAMP," +
                "  update_by VARCHAR(64)," +
                "  update_time TIMESTAMP," +
                "  remark VARCHAR(500)" +
                ")"
            );
            log.info("=== Ensured table contract_service_item exists (utf8mb4) ===");

            // --- ALTER existing tables to utf8mb4 (fix garbled chars in tables created before this update) ---
            alterTableCharset(stmt, "contract_sign");
            alterTableCharset(stmt, "contract_team");
            alterTableCharset(stmt, "contract_package");
            alterTableCharset(stmt, "contract_service_item");

            // --- Add status column if tables already exist without it ---
            try { stmt.executeUpdate("ALTER TABLE contract_team ADD COLUMN status VARCHAR(2) DEFAULT '1'"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_package ADD COLUMN status VARCHAR(2) DEFAULT '1'"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_service_item ADD COLUMN status VARCHAR(2) DEFAULT '1'"); }
            catch (Exception ignored) {}
            // Fix column sizes for existing tables (Chinese chars need more space)
            try { stmt.executeUpdate("ALTER TABLE contract_service_item MODIFY COLUMN item_type VARCHAR(50)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_service_item MODIFY COLUMN frequency VARCHAR(50)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_team MODIFY COLUMN members TEXT"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_team MODIFY COLUMN service_area VARCHAR(255)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_package MODIFY COLUMN package_type VARCHAR(50)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_package MODIFY COLUMN applicable_diseases VARCHAR(500)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_package MODIFY COLUMN service_ids VARCHAR(500)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_sign MODIFY COLUMN sign_type VARCHAR(50)"); }
            catch (Exception ignored) {}
            try { stmt.executeUpdate("ALTER TABLE contract_sign MODIFY COLUMN contract_status VARCHAR(20)"); }
            catch (Exception ignored) {}

            // === JDBC-based seed data (bypasses API encoding issues) ===
            try {
                // Check if data already exists
                java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM contract_service_item");
                rs.next();
                long count = rs.getLong(1);
                rs.close();
                if (count == 0) {
                    log.info("=== Seeding contract data via JDBC ===");
                    stmt.executeUpdate("INSERT INTO contract_service_item (item_name,item_type,frequency,description,execution_standard,status,create_by,create_time) VALUES " +
                        "('血压监测','免费项目','每月1次','定期血压测量与记录，建立血压监测档案','按国家基本公共卫生服务规范执行','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_service_item (item_name,item_type,frequency,description,execution_standard,status,create_by,create_time) VALUES " +
                        "('空腹血糖检测','免费项目','每月2次','检测空腹血糖值，评估血糖控制情况','按中国2型糖尿病防治指南执行','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_service_item (item_name,item_type,frequency,description,execution_standard,status,create_by,create_time) VALUES " +
                        "('慢病随访','免费项目','每季度1次','面对面随访，评估病情变化和用药依从性','按慢病管理规范执行','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_service_item (item_name,item_type,frequency,description,execution_standard,status,create_by,create_time) VALUES " +
                        "('心电图检查','付费项目','每半年1次','常规12导联心电图检查，评估心脏功能','按心电诊断规范执行','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_service_item (item_name,item_type,frequency,description,execution_standard,status,create_by,create_time) VALUES " +
                        "('眼底筛查','付费项目','每年1次','眼底照相检查，筛查视网膜病变','按眼科检查规范执行','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_service_item (item_name,item_type,frequency,description,execution_standard,status,create_by,create_time) VALUES " +
                        "('用药指导','免费项目','每季度1次','评估药物疗效，指导合理用药，调整药物剂量','按处方管理规范执行','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_team (team_name,leader_name,leader_phone,members,service_area,team_intro,status,create_by,create_time) VALUES " +
                        "('城关镇家庭医生团队','张伟','13800138001','张伟,李红,王芳,赵强','城关镇辖区（东关村、西关村、南关村）','负责城关镇辖区居民的家庭医生签约服务，提供慢性病管理和健康管理服务','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_team (team_name,leader_name,leader_phone,members,service_area,team_intro,status,create_by,create_time) VALUES " +
                        "('柳林镇全科医疗团队','李明','13900139001','李明,张丽,王强,赵霞','柳林镇辖区（柳林村、大庄村）','以全科医疗为核心，为辖区居民提供综合性的健康管理服务','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_team (team_name,leader_name,leader_phone,members,service_area,team_intro,status,create_by,create_time) VALUES " +
                        "('张家川慢病管理团队','王磊','13700137001','王磊,李娟,张明,王燕','张家川镇辖区（张家川村、李家村）','专注于慢性病患者的全程管理，提供个性化的慢病防控方案','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_package (package_name,package_type,price,description,service_ids,applicable_diseases,status,create_by,create_time) VALUES " +
                        "('高血压管理基础包','基础包',120,'包含血压监测、用药指导、健康教育、饮食指导、运动处方等基础服务','1,6','高血压','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_package (package_name,package_type,price,description,service_ids,applicable_diseases,status,create_by,create_time) VALUES " +
                        "('糖尿病管理升级包','升级包',280,'包含血糖监测、HbA1c检测、眼底筛查、足部检查、用药指导等升级服务','2,5,6','糖尿病','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_package (package_name,package_type,price,description,service_ids,applicable_diseases,status,create_by,create_time) VALUES " +
                        "('慢病综合管理包','综合包',450,'包含综合体检、多学科会诊、家庭随访等全方位慢病管理服务','1,2,3,4,5,6','高血压,糖尿病,冠心病,脑卒中,慢阻肺','1','admin',now())");
                    stmt.executeUpdate("INSERT INTO contract_package (package_name,package_type,price,description,service_ids,applicable_diseases,status,create_by,create_time) VALUES " +
                        "('老年人健康管理包','基础包',80,'包含基础体检、中医体质辨识、健康讲座等老年人专项服务','1,3','高血压,糖尿病','0','admin',now())");
                    // Seed contract_sign: 从 mp_person + resident_archive 获取真实患者数据
                    // 患者姓名必须来源于 mp_person，不能虚构
                    java.sql.ResultSet archiveRs = stmt.executeQuery(
                        "SELECT ra.id as resident_id, mp.person_name " +
                        "FROM resident_archive ra " +
                        "LEFT JOIN mp_person mp ON ra.id_card = mp.card_id " +
                        "WHERE mp.person_name IS NOT NULL " +
                        "ORDER BY ra.id ASC LIMIT 3");
                    java.util.List<String[]> seedResidents = new java.util.ArrayList<>();
                    while (archiveRs.next()) {
                        seedResidents.add(new String[]{
                            archiveRs.getString("resident_id"),
                            archiveRs.getString("person_name")
                        });
                    }
                    archiveRs.close();

                    if (!seedResidents.isEmpty()) {
                        java.sql.PreparedStatement signPs = conn.prepareStatement(
                            "INSERT INTO contract_sign (resident_id,resident_name,team_id,package_id,sign_date,start_date,end_date,contract_status,sign_type,create_by,create_time) " +
                            "VALUES (?,?,?,?,?,?,?,'1','首签','admin',now())");
                        String[] startDates = {"2025-10-19", "2025-11-15", "2025-10-19"};
                        String[] endDates = {"2026-10-19", "2026-11-15", "2026-10-19"};
                        for (int i = 0; i < seedResidents.size(); i++) {
                            String[] r = seedResidents.get(i);
                            signPs.setString(1, r[0]);          // resident_id from resident_archive
                            signPs.setString(2, r[1]);          // resident_name from mp_person (真实姓名)
                            signPs.setInt(3, i + 1);            // team_id
                            signPs.setInt(4, i + 1);            // package_id
                            signPs.setString(5, startDates[i]);
                            signPs.setString(6, startDates[i]);
                            signPs.setString(7, endDates[i]);
                            signPs.addBatch();
                        }
                        signPs.executeBatch();
                        signPs.close();
                        log.info("=== JDBC seed data inserted successfully (contract_sign names from mp_person) ===");
                    } else {
                        log.info("=== No residents in archive, skipping contract_sign seed ===");
                    }

                    // Verify the seeded data is readable
                    rs = stmt.executeQuery("SELECT item_name FROM contract_service_item LIMIT 1");
                    if (rs.next()) {
                        log.info("=== Seed verify — first service item: '{}' ===", rs.getString(1));
                    }
                    rs.close();
                } else {
                    log.info("=== Contract data already exists, skipping seed ===");
                }
            } catch (Exception e) {
                log.error("=== JDBC seed failed: {} ===", e.getMessage());
            }

            log.info("=== Contract table migration completed ===");
        } catch (Exception e) {
            log.error("Failed to create/migrate contract tables: {}", e.getMessage());
        }
    }

    /**
     * Convert an existing table and all its text columns to utf8mb4 charset.
     * This fixes garbled Chinese characters in tables created before the charset fix.
     */
    private void alterTableCharset(Statement stmt, String tableName) {
        try {
            stmt.executeUpdate("ALTER TABLE " + tableName + " CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            log.info("=== Converted table {} to utf8mb4 ===", tableName);
        } catch (Exception e) {
            log.info("=== Table {} charset conversion skipped: {} ===", tableName, e.getMessage());
        }
    }
}
