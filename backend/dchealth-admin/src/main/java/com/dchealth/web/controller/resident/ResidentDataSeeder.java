package com.dchealth.web.controller.resident;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化居民档案种子数据。
 *
 * 重要原则：resident_archive 中的患者数据来源于 mp_person，不凭空创造患者。
 * 仅当 mp_person 中已有患者数据时，才为其创建档案管理元数据。
 */
@Component
@Order(5)
public class ResidentDataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ResidentDataSeeder.class);

    @Autowired
    private DataSource dataSource;

    // 几种常见慢病标签（档案层面标注）
    private static final String[] DISEASE_TAGS = {"高血压", "糖尿病", "冠心病", "脑卒中", "慢阻肺"};

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            // 已有档案数据则跳过
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery("SELECT COUNT(*) FROM resident_archive")) {
                if (rs.next() && rs.getInt(1) > 0) {
                    log.info("=== resident_archive already has {} records, skipping seed ===", rs.getInt(1));
                    return;
                }
            } catch (Exception e) {
                log.info("=== resident_archive table may not exist, skipping seed ===");
                return;
            }

            // Step 1: 从 mp_person 获取真实患者列表（患者数据来源必须是 mp_person）
            List<Map<String, String>> realPatients = new ArrayList<>();
            try (Statement s = conn.createStatement();
                 ResultSet rs = s.executeQuery(
                     "SELECT person_id, person_name, gender_source_value, sex, card_id, phone_number, address " +
                     "FROM mp_person ORDER BY id ASC LIMIT 12")) {
                while (rs.next()) {
                    Map<String, String> p = new LinkedHashMap<>();
                    p.put("personName", rs.getString("person_name"));
                    p.put("gender", getOrDefault(rs, "gender_source_value", "未知"));
                    p.put("sex", getOrDefault(rs, "sex", "0"));
                    p.put("cardId", rs.getString("card_id"));
                    p.put("phone", getOrDefault(rs, "phone_number", ""));
                    p.put("address", getOrDefault(rs, "address", ""));
                    realPatients.add(p);
                }
            }

            // 若 mp_person 无数据，则无法创建档案（患者数据不能凭空产生）
            if (realPatients.isEmpty()) {
                log.info("=== mp_person has no records, cannot seed resident_archive (patient data must come from mp_person) ===");
                return;
            }

            log.info("=== Seeding resident_archive from {} real mp_person records ===", realPatients.size());

            // Step 2: 为真实患者创建档案管理元数据
            // 注意：resident_name, gender, phone, address 等基本信息从 mp_person 同步，
            // resident_archive 表只额外存储管理元数据 (archive_code, managing_institution, managing_doctor 等)
            String[] institutions = {"心内科", "内分泌科", "神经内科", "呼吸科", "社区全科"};
            String[] doctors = {"王医生", "赵医生", "陈医生", "刘医生", "张医生"};

            // 检测 archive_code 列是否存在，动态调整 INSERT 列
            boolean hasArchiveCode = false;
            try {
                java.sql.DatabaseMetaData meta = conn.getMetaData();
                java.sql.ResultSet colRs = meta.getColumns(null, null, "resident_archive", "archive_code");
                if (colRs.next()) { hasArchiveCode = true; }
                colRs.close();
            } catch (Exception ignored) {}

            String sql;
            if (hasArchiveCode) {
                sql = "INSERT INTO resident_archive " +
                    "(archive_code, resident_name, gender, id_card, phone, address, " +
                    " community, managing_institution, managing_doctor, disease_types, " +
                    " archive_source, archive_date, archive_status, is_signed, create_by, create_time) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,'1',NOW(),'0','1','admin',NOW())";
            } else {
                log.warn("=== resident_archive missing archive_code column, seeding without it ===");
                sql = "INSERT INTO resident_archive " +
                    "(resident_name, gender, id_card, phone, address, " +
                    " community, managing_institution, managing_doctor, disease_types, " +
                    " archive_source, archive_date, archive_status, is_signed, create_by, create_time) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,'1',NOW(),'0','1','admin',NOW())";
            }

            int count = 0;
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int i = 0; i < realPatients.size(); i++) {
                    Map<String, String> p = realPatients.get(i);
                    String diseaseTag = DISEASE_TAGS[i % DISEASE_TAGS.length];
                    String inst = institutions[i % institutions.length];
                    String doctor = doctors[i % doctors.length];

                    int idx = 1;
                    if (hasArchiveCode) {
                        String archiveCode = "DA" + String.format("%06d", i + 1);
                        ps.setString(idx++, archiveCode);
                    }
                    ps.setString(idx++, p.get("personName"));            // 来源于 mp_person
                    ps.setString(idx++, p.get("gender"));                // 来源于 mp_person
                    ps.setString(idx++, p.get("cardId"));                // FK 关联 mp_person
                    ps.setString(idx++, p.get("phone"));                 // 来源于 mp_person
                    ps.setString(idx++, p.get("address"));               // 来源于 mp_person
                    ps.setString(idx++, "社区");                          // 默认社区
                    ps.setString(idx++, inst);                           // 管理机构（管理元数据）
                    ps.setString(idx++, doctor);                         // 管理医生（管理元数据）
                    ps.setString(idx++, diseaseTag);                    // 病种标签（管理元数据）
                    ps.addBatch();
                    count++;
                }
                ps.executeBatch();
            } catch (Exception e) {
                log.warn("=== Seed resident_archive failed (table schema mismatch), skipping: {} ===", e.getMessage());
                return;
            }

            log.info("=== Seeded {} resident archive records from mp_person ===", count);
        }
    }

    private static String getOrDefault(ResultSet rs, String column, String defaultValue) throws java.sql.SQLException {
        String val = rs.getString(column);
        return val != null ? val : defaultValue;
    }
}
