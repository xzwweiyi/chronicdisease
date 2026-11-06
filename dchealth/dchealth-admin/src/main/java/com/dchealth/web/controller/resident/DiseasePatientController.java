package com.dchealth.web.controller.resident;

import java.util.*;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.resident.service.IResidentArchiveService;

/**
 * 慢病专项患者管理。
 *
 * 数据来源：mp_person（患者基本信息）+ resident_archive（管理元数据，含 disease_types 病种标签）。
 * 患者列表与档案管理的121例患者保持一致，不依赖HIS mp_diagnosis 表。
 */
@RestController
@RequestMapping("/resident/disease")
public class DiseasePatientController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(DiseasePatientController.class);

    @Autowired
    private IResidentArchiveService residentArchiveService;
    @Autowired
    private DataSource dataSource;

    /** 疾病名称映射 */
    private static final Map<String, String> DISEASE_MAP = new LinkedHashMap<>();
    /** 5种慢病关键词 */
    private static final List<String> CHRONIC_DISEASES = Arrays.asList(
        "高血压", "糖尿病", "脑卒中", "冠心病", "慢阻肺"
    );
    static {
        DISEASE_MAP.put("hypertension", "高血压");
        DISEASE_MAP.put("diabetes", "糖尿病");
        DISEASE_MAP.put("stroke", "脑卒中");
        DISEASE_MAP.put("chd", "冠心病");
        DISEASE_MAP.put("copd", "慢阻肺");
    }

    /**
     * 慢病专项患者列表。
     * 直接通过 resident_archive.disease_types 过滤，数据来源于 mp_person + resident_archive，
     * 与档案管理中的121例患者保持一致。
     */
    @PreAuthorize("@ss.hasPermi('resident:archive:list')")
    @GetMapping("/{disease}/list")
    public TableDataInfo list(
            @PathVariable String disease,
            @RequestParam(required = false) String personName,
            @RequestParam(required = false) String cardId) {

        startPage();
        Map<String, Object> params = new HashMap<>();
        if (personName != null && !personName.isEmpty()) params.put("personName", personName);
        if (cardId != null && !cardId.isEmpty()) params.put("cardId", cardId);

        // 通过 resident_archive.disease_types 过滤，确保与档案数据一致
        if ("comorbidity".equals(disease)) {
            // 合并症：由 Service 层特殊处理（resident_archive.disease_types 含多种病种标签）
            return getDataTable(residentArchiveService.selectComorbidityPatientList(params));
        } else {
            String diseaseName = DISEASE_MAP.getOrDefault(disease, disease);
            params.put("diseaseTypes", diseaseName);
            return getDataTable(residentArchiveService.selectArchivedPatientList(params));
        }
    }

    /**
     * 慢病患者统计。
     * 直接从 resident_archive.disease_types 统计，与患者列表口径一致。
     */
    @GetMapping("/{disease}/stats")
    public AjaxResult stats(@PathVariable String disease) {
        Map<String, Object> result = new LinkedHashMap<>();
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);

        try {
            String diseaseName = DISEASE_MAP.get(disease);
            String diseaseFilter;

            if ("comorbidity".equals(disease)) {
                // 合并症：统计 disease_types 含2种及以上慢病的患者
                diseaseFilter = buildComorbidityWhere();
            } else if (diseaseName != null) {
                diseaseFilter = "ra.disease_types LIKE '%" + diseaseName + "%'";
            } else {
                diseaseFilter = "1=0"; // 未知病种，返回空
            }

            // 总患者数（按 card_id 去重，一人多号只计一次）
            Long total = jdbc.queryForObject(
                "SELECT COUNT(DISTINCT mp.card_id) FROM mp_person mp " +
                "INNER JOIN resident_archive ra ON mp.card_id = ra.id_card " +
                "WHERE " + diseaseFilter, Long.class);
            result.put("totalPatients", total != null ? total : 0L);

            // 本月新增
            if (total != null && total > 0) {
                Long newThisMonth = jdbc.queryForObject(
                    "SELECT COUNT(DISTINCT mp.card_id) FROM mp_person mp " +
                    "INNER JOIN resident_archive ra ON mp.card_id = ra.id_card " +
                    "WHERE " + diseaseFilter +
                    " AND to_char(ra.create_time, 'YYYY-MM') = to_char(NOW(), 'YYYY-MM')", Long.class);
                result.put("newThisMonth", newThisMonth != null ? newThisMonth : 0L);
            } else {
                result.put("newThisMonth", 0L);
            }

            result.put("controlRate", 0.0);
            result.put("interventionRate", 0.0);
        } catch (Exception e) {
            log.error("Failed to get stats for disease: {}", disease, e);
            result.put("totalPatients", 0L);
            result.put("newThisMonth", 0L);
            result.put("controlRate", 0.0);
            result.put("interventionRate", 0.0);
        }
        return AjaxResult.success(result);
    }

    /** 构建合并症 WHERE 条件：disease_types 含2种及以上慢病关键词 */
    private String buildComorbidityWhere() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < CHRONIC_DISEASES.size(); i++) {
            if (i > 0) sb.append(" + ");
            sb.append("CASE WHEN ra.disease_types LIKE '%").append(CHRONIC_DISEASES.get(i)).append("%' THEN 1 ELSE 0 END");
        }
        sb.append(") >= 2");
        return sb.toString();
    }
}
