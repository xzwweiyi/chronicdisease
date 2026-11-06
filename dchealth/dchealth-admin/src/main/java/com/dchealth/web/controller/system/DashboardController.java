package com.dchealth.web.controller.system;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页工作台仪表盘
 *
 * 数据源说明：
 * - mp_person:      HIS患者主索引表，存储所有医院患者基本信息
 * - patient_info:   平台管理患者表（入组到队列的患者）
 * - patient_record: 随访记录表（每次随访填写的表单记录）
 * - research_info:  队列/研究项目表
 * - research_diseases: 病种分类表
 * - referral_transfer: 转诊记录表
 * - sys_dept:       科室/机构表
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate getJdbc() {
        return new JdbcTemplate(dataSource);
    }

    @GetMapping("/index")
    public Map<String, Object> index() {
        Map<String, Object> result = new LinkedHashMap<>();
        try {
            result.put("summary", getSummaryCards());
            result.put("todoList", getTodoList());
            result.put("focusPatients", getFocusPatients());
            result.put("diseaseDistribution", getDiseaseDistribution());
            result.put("trendAnalysis", getTrendAnalysis());
            result.put("institutionRanking", getInstitutionRanking());
            result.put("code", 200);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "仪表盘数据加载失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 顶部统计卡片：管理患者总数、本月随访完成率、控制达标率、待处理转诊
     */
    private Map<String, Object> getSummaryCards() {
        JdbcTemplate jdbc = getJdbc();
        Map<String, Object> cards = new LinkedHashMap<>();

        // 管理患者总数：从 mp_person 表汇总（按身份证号去重）
        try {
            Long totalPatients = jdbc.queryForObject(
                "SELECT COUNT(DISTINCT card_id) FROM mp_person WHERE card_id IS NOT NULL AND card_id != ''", Long.class);
            cards.put("totalPatients", totalPatients != null ? totalPatients : 0L);
        } catch (Exception e) {
            cards.put("totalPatients", 0L);
        }

        // 本月随访完成率
        try {
            Map<String, Object> followRate = jdbc.queryForMap(
                "SELECT " +
                "  COUNT(*) AS total, " +
                "  COALESCE(SUM(CASE WHEN pr.fill_status = '1' AND pr.follow_status = '1' THEN 1 ELSE 0 END), 0) AS completed " +
                "FROM patient_record pr " +
                "WHERE DATE_FORMAT(pr.visit_time, '%Y-%m') = DATE_FORMAT(NOW(), '%Y-%m')");
            long total = ((Number) followRate.get("total")).longValue();
            long completed = ((Number) followRate.get("completed")).longValue();
            double rate = total > 0 ? Math.round(completed * 10000.0 / total) / 100.0 : 0;
            cards.put("followRate", rate);
            cards.put("followCompleted", completed);
            cards.put("followTotal", total);
        } catch (Exception e) {
            cards.put("followRate", 0);
            cards.put("followCompleted", 0);
            cards.put("followTotal", 0);
        }

        // 控制达标率（基于有随访记录的已完成随访率）
        try {
            Map<String, Object> controlRateMap = jdbc.queryForMap(
                "SELECT " +
                "  COUNT(*) AS total, " +
                "  COALESCE(SUM(CASE WHEN pr.follow_status = '1' THEN 1 ELSE 0 END), 0) AS controlled " +
                "FROM patient_record pr " +
                "WHERE pr.fill_status = '1'");
            long total = ((Number) controlRateMap.get("total")).longValue();
            long controlled = ((Number) controlRateMap.get("controlled")).longValue();
            double controlRate = total > 0 ? Math.round(controlled * 10000.0 / total) / 100.0 : 0;
            cards.put("controlRate", controlRate);
        } catch (Exception e) {
            cards.put("controlRate", 0);
        }

        // 待处理转诊
        try {
            Long pendingReferrals = jdbc.queryForObject(
                "SELECT COUNT(*) FROM referral_transfer WHERE transfer_status = '0'",
                Long.class);
            cards.put("pendingReferrals", pendingReferrals != null ? pendingReferrals : 0L);
        } catch (Exception e) {
            cards.put("pendingReferrals", 0L);
        }

        return cards;
    }

    /**
     * 待办事项列表
     */
    private List<Map<String, Object>> getTodoList() {
        JdbcTemplate jdbc = getJdbc();
        List<Map<String, Object>> todos = new ArrayList<>();

        // 即将到期的随访（patient_record 中 visit_time 在未来3天内且未填写）
        try {
            List<Map<String, Object>> upcomingFollows = jdbc.queryForList(
                "SELECT CONCAT('随访计划：', COALESCE(p.patient_name, mp.person_name), '') AS title, " +
                "       pr.visit_time AS dueDate, 'follow' AS type, 'warning' AS priority " +
                "FROM patient_record pr " +
                "LEFT JOIN patient_info p ON pr.patient_id = p.patient_id " +
                "LEFT JOIN mp_person mp ON p.person_id = mp.person_id " +
                "WHERE pr.fill_status = '0' " +
                "  AND pr.visit_time BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 3 DAY) " +
                "ORDER BY pr.visit_time ASC LIMIT 5");
            for (Map<String, Object> item : upcomingFollows) {
                item.put("tag", "随访");
                item.put("tagType", "warning");
                todos.add(item);
            }
        } catch (Exception e) {
            // skip
        }

        // 待处理的转诊
        try {
            List<Map<String, Object>> pendingTransfers = jdbc.queryForList(
                "SELECT CONCAT('转诊待处理：', patient_name) AS title, " +
                "       create_time AS dueDate, 'transfer' AS type, 'danger' AS priority " +
                "FROM referral_transfer WHERE transfer_status = '0' " +
                "ORDER BY create_time ASC LIMIT 5");
            for (Map<String, Object> item : pendingTransfers) {
                item.put("tag", "转诊");
                item.put("tagType", "danger");
                todos.add(item);
            }
        } catch (Exception e) {
            // skip
        }

        // 超窗未处理的随访记录
        try {
            List<Map<String, Object>> overtimeRecords = jdbc.queryForList(
                "SELECT CONCAT('超窗提醒：', COALESCE(p.patient_name, '未知患者')) AS title, " +
                "       pr.visit_time AS dueDate, 'overdue' AS type, 'warning' AS priority " +
                "FROM patient_record pr " +
                "LEFT JOIN patient_info p ON pr.patient_id = p.patient_id " +
                "WHERE pr.over_time_status = '1' AND COALESCE(pr.over_time_handle_status, '0') = '0' " +
                "ORDER BY pr.visit_time ASC LIMIT 5");
            for (Map<String, Object> item : overtimeRecords) {
                item.put("tag", "超窗");
                item.put("tagType", "warning");
                todos.add(item);
            }
        } catch (Exception e) {
            // skip
        }

        return todos;
    }

    /**
     * 重点关注患者列表（随访工作台手动打标签的重点关注患者 patient_info.focus_status='1'）
     */
    private List<Map<String, Object>> getFocusPatients() {
        JdbcTemplate jdbc = getJdbc();
        List<Map<String, Object>> patients;
        try {
            patients = jdbc.queryForList(
                "SELECT " +
                "  COALESCE(p.patient_name, mp.person_name) AS name, " +
                "  CASE WHEN mp.sex = '1' THEN '男' WHEN mp.sex = '2' THEN '女' ELSE COALESCE(mp.gender_source_value, '未知') END AS gender, " +
                "  CASE WHEN CHAR_LENGTH(COALESCE(mp.card_id, '')) >= 18 THEN YEAR(CURDATE()) - CAST(SUBSTRING(mp.card_id, 7, 4) AS UNSIGNED) ELSE NULL END AS age, " +
                "  COALESCE(rd.disease_name, '未分类') AS disease, " +
                "  COALESCE(mp.phone_number, p.patient_mobile) AS phone, " +
                "  CASE WHEN pr.warn_status = '1' THEN '高危' " +
                "       WHEN pr.over_time_status = '1' THEN '中危' " +
                "       ELSE '关注' END AS riskLevel, " +
                "  COALESCE(DATE_FORMAT(pr.visit_time, '%Y-%m-%d'), DATE_FORMAT(pr.create_time, '%Y-%m-%d')) AS lastFollowDate, " +
                "  CASE p.patient_status " +
                "       WHEN '3' THEN '已完成' " +
                "       WHEN '4' THEN '失访' " +
                "       WHEN '5' THEN '退出申请' " +
                "       WHEN '6' THEN '已退出' " +
                "       WHEN '2' THEN '随访中' " +
                "       WHEN '1' THEN '基线' " +
                "       ELSE '未填写' END AS followStatus " +
                "FROM patient_info p " +
                "LEFT JOIN mp_person mp ON p.person_id = mp.person_id " +
                "LEFT JOIN research_info ri ON p.research_id = ri.id " +
                "LEFT JOIN research_diseases rd ON ri.disease_type_id = rd.disease_id AND rd.is_deleted = '0' " +
                "LEFT JOIN patient_record pr ON pr.patient_id = p.patient_id " +
                "  AND pr.record_id = (SELECT MAX(pr2.record_id) FROM patient_record pr2 WHERE pr2.patient_id = p.patient_id) " +
                "WHERE p.focus_status = '1' " +
                "ORDER BY pr.warn_status DESC, pr.visit_time ASC");
        } catch (Exception e) {
            // Real data unavailable — fetch from mp_person
            try {
                patients = jdbc.queryForList(
                    "SELECT " +
                    "  mp.person_name AS name, " +
                    "  CASE WHEN mp.sex = '1' THEN '男' WHEN mp.sex = '2' THEN '女' ELSE COALESCE(mp.gender_source_value, '未知') END AS gender, " +
                    "  CASE WHEN CHAR_LENGTH(COALESCE(mp.card_id, '')) >= 18 THEN YEAR(CURDATE()) - CAST(SUBSTRING(mp.card_id, 7, 4) AS UNSIGNED) ELSE NULL END AS age, " +
                    "  '未入组' AS disease, " +
                    "  COALESCE(mp.phone_number, mp.contacts_phone) AS phone, " +
                    "  '关注' AS riskLevel, " +
                    "  DATE_FORMAT(mp.create_time, '%Y-%m-%d') AS lastFollowDate " +
                    "FROM mp_person mp " +
                    "ORDER BY mp.create_time DESC LIMIT 8");
            } catch (Exception e2) {
                patients = new ArrayList<>();
            }
        }
        return patients;
    }

    /**
     * 慢病病种分布（饼图数据），基于居民档案 disease_types + mp_person 主索引（按身份证号去重）
     */
    private List<Map<String, Object>> getDiseaseDistribution() {
        JdbcTemplate jdbc = getJdbc();
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            Map<String, Object> row = jdbc.queryForMap(
                "SELECT " +
                "  SUM(CASE WHEN r.disease_types LIKE '%高血压%' THEN 1 ELSE 0 END) AS hypertension, " +
                "  SUM(CASE WHEN r.disease_types LIKE '%糖尿病%' THEN 1 ELSE 0 END) AS diabetes, " +
                "  SUM(CASE WHEN r.disease_types LIKE '%脑卒中%' THEN 1 ELSE 0 END) AS stroke, " +
                "  SUM(CASE WHEN r.disease_types LIKE '%冠心病%' THEN 1 ELSE 0 END) AS chd, " +
                "  SUM(CASE WHEN r.disease_types LIKE '%慢阻肺%' THEN 1 ELSE 0 END) AS copd " +
                "FROM ( " +
                "  SELECT ra.id_card AS card_id, MAX(ra.disease_types) AS disease_types " +
                "  FROM resident_archive ra " +
                "  WHERE ra.archive_status = '0' " +
                "  GROUP BY ra.id_card " +
                ") r " +
                "JOIN (SELECT card_id, MAX(person_id) AS person_id FROM mp_person GROUP BY card_id) m " +
                "  ON m.card_id = r.card_id");
            String[] names = {"高血压", "糖尿病", "脑卒中", "冠心病", "慢阻肺"};
            String[] keys  = {"hypertension", "diabetes", "stroke", "chd", "copd"};
            for (int i = 0; i < names.length; i++) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("name", names[i]);
                Object v = row.get(keys[i]);
                item.put("value", v == null ? 0 : ((Number) v).intValue());
                result.add(item);
            }
            result.sort((a, b) -> Integer.compare((Integer) b.get("value"), (Integer) a.get("value")));
        } catch (Exception e) {
            // return empty
        }
        return result;
    }

    /**
     * 慢病趋势分析（近12个月每月新增管理居民数，按病种），基于居民档案 + mp_person
     */
    private Map<String, Object> getTrendAnalysis() {
        JdbcTemplate jdbc = getJdbc();
        Map<String, Object> trend = new LinkedHashMap<>();
        List<Map<String, Object>> series = new ArrayList<>();

        // 生成近12个月标签
        String[] monthLabels = new String[12];
        LocalDate now = LocalDate.now();
        for (int i = 11; i >= 0; i--) {
            LocalDate d = now.minusMonths(i);
            monthLabels[11 - i] = d.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        }
        trend.put("months", monthLabels);

        String[] diseaseNames = {"高血压", "糖尿病", "脑卒中", "冠心病", "慢阻肺"};
        String[] diseaseCols  = {"hypertension", "diabetes", "stroke", "chd", "copd"};

        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT " +
                "  DATE_FORMAT(ra.create_time, '%Y-%m') AS month, " +
                "  SUM(CASE WHEN ra.disease_types LIKE '%高血压%' THEN 1 ELSE 0 END) AS hypertension, " +
                "  SUM(CASE WHEN ra.disease_types LIKE '%糖尿病%' THEN 1 ELSE 0 END) AS diabetes, " +
                "  SUM(CASE WHEN ra.disease_types LIKE '%脑卒中%' THEN 1 ELSE 0 END) AS stroke, " +
                "  SUM(CASE WHEN ra.disease_types LIKE '%冠心病%' THEN 1 ELSE 0 END) AS chd, " +
                "  SUM(CASE WHEN ra.disease_types LIKE '%慢阻肺%' THEN 1 ELSE 0 END) AS copd " +
                "FROM resident_archive ra " +
                "WHERE ra.archive_status = '0' " +
                "  AND ra.create_time >= DATE_SUB(NOW(), INTERVAL 12 MONTH) " +
                "GROUP BY DATE_FORMAT(ra.create_time, '%Y-%m') " +
                "ORDER BY month");

            for (int d = 0; d < diseaseNames.length; d++) {
                int[] data = new int[12];
                for (Map<String, Object> row : rows) {
                    String month = (String) row.get("month");
                    int idx = Arrays.asList(monthLabels).indexOf(month);
                    if (idx >= 0) {
                        Object v = row.get(diseaseCols[d]);
                        data[idx] = v == null ? 0 : ((Number) v).intValue();
                    }
                }
                Map<String, Object> s = new LinkedHashMap<>();
                s.put("name", diseaseNames[d]);
                s.put("data", data);
                series.add(s);
            }
        } catch (Exception e) {
            // return empty series, frontend will use fallback
        }

        trend.put("series", series);
        return trend;
    }

    /**
     * 机构管理居民排名（柱状图数据），基于居民档案 managing_institution + mp_person（按身份证号去重）
     */
    private Map<String, Object> getInstitutionRanking() {
        JdbcTemplate jdbc = getJdbc();
        Map<String, Object> ranking = new LinkedHashMap<>();

        try {
            List<Map<String, Object>> rows = jdbc.queryForList(
                "SELECT " +
                "  ra.managing_institution AS name, " +
                "  COUNT(DISTINCT ra.id_card) AS patientCount " +
                "FROM resident_archive ra " +
                "JOIN (SELECT card_id, MAX(person_id) AS person_id FROM mp_person GROUP BY card_id) m " +
                "  ON m.card_id = ra.id_card " +
                "WHERE ra.archive_status = '0' " +
                "  AND ra.managing_institution IS NOT NULL AND ra.managing_institution != '' " +
                "GROUP BY ra.managing_institution " +
                "ORDER BY patientCount DESC LIMIT 10");
            ranking.put("institutions", rows);
        } catch (Exception e) {
            ranking.put("institutions", new ArrayList<>());
        }

        return ranking;
    }
}
