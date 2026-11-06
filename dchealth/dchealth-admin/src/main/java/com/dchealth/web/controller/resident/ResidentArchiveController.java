package com.dchealth.web.controller.resident;

import java.util.*;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.service.IMpPersonService;
import com.dchealth.resident.domain.ResidentArchive;
import com.dchealth.resident.domain.ResidentGradingRecord;
import com.dchealth.resident.service.IResidentArchiveService;
import com.dchealth.resident.service.IResidentGradingService;

@RestController
@RequestMapping("/resident/archive")
public class ResidentArchiveController extends BaseController {

    @Autowired
    private IResidentArchiveService residentArchiveService;
    @Autowired
    private IResidentGradingService gradingService;
    @Autowired
    private IMpPersonService mpPersonService;
    @Autowired
    private DataSource dataSource;

    // ============================================================
    // 列表 & 统计
    // ============================================================

    /**
     * 档案管理列表 — LEFT JOIN mp_person + resident_archive
     */
    @PreAuthorize("@ss.hasPermi('resident:archive:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String personName,
            @RequestParam(required = false) String cardId,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String diseaseTypes,
            @RequestParam(required = false) String managingInstitution,
            @RequestParam(required = false) String archiveStatus,
            @RequestParam(required = false) String isSigned,
            @RequestParam(required = false) String beginCreateTime,
            @RequestParam(required = false) String endCreateTime) {
        startPage();
        Map<String, Object> params = new HashMap<>();
        if (personName != null && !personName.isEmpty()) params.put("personName", personName);
        if (cardId != null && !cardId.isEmpty()) params.put("cardId", cardId);
        if (phoneNumber != null && !phoneNumber.isEmpty()) params.put("phoneNumber", phoneNumber);
        if (diseaseTypes != null && !diseaseTypes.isEmpty()) params.put("diseaseTypes", diseaseTypes);
        if (managingInstitution != null && !managingInstitution.isEmpty()) params.put("managingInstitution", managingInstitution);
        if (archiveStatus != null && !archiveStatus.isEmpty()) params.put("archiveStatus", archiveStatus);
        if (isSigned != null && !isSigned.isEmpty()) params.put("isSigned", isSigned);
        if (beginCreateTime != null && !beginCreateTime.isEmpty()) params.put("beginCreateTime", beginCreateTime);
        if (endCreateTime != null && !endCreateTime.isEmpty()) params.put("endCreateTime", endCreateTime);
        return getDataTable(residentArchiveService.selectArchivedPatientList(params));
    }

    /**
     * 统计卡片数据
     */
    @GetMapping("/stats")
    public AjaxResult stats() {
        return AjaxResult.success(residentArchiveService.getStats());
    }

    /**
     * 各机构慢病分布
     */
    @GetMapping("/distribution")
    public AjaxResult distribution() {
        return AjaxResult.success(residentArchiveService.getCrossOrgDistribution());
    }

    /**
     * 跨机构调取日志
     */
    @GetMapping("/cross-org-log")
    public AjaxResult crossOrgLog() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        List<Map<String, Object>> list;
        try {
            list = jdbc.queryForList(
                "SELECT mp.person_name, mp.card_id, " +
                "       ra.managing_institution AS source_institution, " +
                "       ra.managing_doctor AS source_doctor, " +
                "       ra.managing_institution AS target_institution, " +
                "       ra.managing_doctor AS target_doctor, " +
                "       ra.create_time AS access_time " +
                "FROM mp_person mp " +
                "INNER JOIN resident_archive ra ON mp.card_id = ra.id_card " +
                "WHERE ra.managing_institution IS NOT NULL LIMIT 20");
        } catch (Exception e) {
            list = new ArrayList<>();
        }
        return AjaxResult.success(list);
    }

    // ============================================================
    // 详情
    // ============================================================

    /**
     * 档案详情（单条 mp_person 数据）
     */
    @PreAuthorize("@ss.hasPermi('resident:archive:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(mpPersonService.selectMpPersonById(id));
    }

    /**
     * 档案详情 — 通过 cardId（身份证号）查询 resident_archive 数据
     */
    @PreAuthorize("@ss.hasPermi('resident:archive:query')")
    @GetMapping("/detail/{cardId}")
    public AjaxResult getDetail(@PathVariable String cardId) {
        return AjaxResult.success(residentArchiveService.selectArchivedPatientByPersonId(cardId));
    }

    /**
     * 患者诊断记录（cardId → personId → mp_diagnosis）
     */
    @GetMapping("/detail/{cardId}/diagnosis")
    public AjaxResult getDiagnosis(@PathVariable String cardId) {
        return AjaxResult.success(queryHisByCardId(cardId,
            "SELECT diagnosis_desc, diagnosis_date, condition_name, department_name, final_check_doctor FROM mp_diagnosis WHERE person_id = ? ORDER BY diagnosis_date DESC LIMIT 20"));
    }

    /**
     * 患者用药记录
     */
    @GetMapping("/detail/{cardId}/medications")
    public AjaxResult getMedications(@PathVariable String cardId) {
        return AjaxResult.success(queryHisByCardId(cardId,
            "SELECT drug_name, drug_spec, dosage_form, drug_exposure_start_date, days_supply FROM mp_drug_exposure WHERE person_id = ? ORDER BY drug_exposure_start_date DESC LIMIT 20"));
    }

    /**
     * 患者就诊记录
     */
    @GetMapping("/detail/{cardId}/visits")
    public AjaxResult getVisits(@PathVariable String cardId) {
        return AjaxResult.success(queryHisByCardId(cardId,
            "SELECT visit_occurrence_id, visit_type, visit_start_date, visit_end_date, discharge_department, ward FROM mp_visit_occurrence WHERE person_id = ? ORDER BY visit_start_date DESC LIMIT 20"));
    }

    /**
     * 通过 cardId 查询 HIS 数据：先查 mp_person 获取 person_id，再查目标表
     */
    private List<Map<String, Object>> queryHisByCardId(String cardId, String sql) {
        List<Map<String, Object>> list;
        try {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            // Step 1: 通过 card_id 查 mp_person 获取 person_id
            String personId = null;
            try {
                personId = jdbc.queryForObject(
                    "SELECT person_id FROM mp_person WHERE card_id = ? LIMIT 1",
                    String.class, cardId);
            } catch (Exception e) {
                // mp_person 可能在 HIS 库，查询失败则返回空
            }
            if (personId == null) {
                return new ArrayList<>();
            }
            // Step 2: 用 person_id 查询目标表
            list = jdbc.queryForList(sql, personId);
        } catch (Exception e) {
            list = new ArrayList<>();
        }
        return list;
    }

    // ============================================================
    // CRUD (resident_archive 管理元数据)
    // ============================================================

    /**
     * 搜索 mp_person 中的患者（用于建档前查找患者）
     */
    @GetMapping("/searchPerson")
    public AjaxResult searchPerson(@RequestParam String keyword) {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        try {
            List<Map<String, Object>> list = jdbc.queryForList(
                "SELECT id, person_id, person_name, card_id, phone_number, " +
                "       gender_source_value, sex, address, location_id " +
                "FROM mp_person " +
                "WHERE person_name LIKE ? OR card_id = ? OR phone_number LIKE ? " +
                "LIMIT 20",
                "%" + keyword + "%", keyword, "%" + keyword + "%");
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("搜索患者失败: " + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('resident:archive:add')")
    @Log(title = "居民档案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ResidentArchive entity) {
        // 1. 身份证号（卡号）为必填，用于关联 mp_person
        if (entity.getIdCard() == null || entity.getIdCard().isEmpty()) {
            return AjaxResult.error("身份证号不能为空，请先选择患者");
        }

        // 2. 验证患者必须已存在于 mp_person 中
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        Map<String, Object> mpPerson;
        try {
            mpPerson = jdbc.queryForMap(
                "SELECT person_id, person_name, gender_source_value, sex, card_id, " +
                "       phone_number, address " +
                "FROM mp_person WHERE card_id = ? LIMIT 1",
                entity.getIdCard());
        } catch (Exception e) {
            return AjaxResult.error("患者不存在于居民档案库(mp_person)中，无法建档");
        }

        // 3. 从 mp_person 同步患者基本信息（不从前端接收的值取，保证数据一致性）
        entity.setResidentName((String) mpPerson.get("person_name"));
        entity.setGender((String) mpPerson.get("gender_source_value"));
        entity.setPhone((String) mpPerson.get("phone_number"));
        entity.setAddress((String) mpPerson.get("address"));
        entity.setIdCard((String) mpPerson.get("card_id"));

        // 4. 设置默认值
        if (entity.getArchiveStatus() == null) entity.setArchiveStatus("0");
        if (entity.getArchiveDate() == null) entity.setArchiveDate(new java.util.Date());

        entity.setCreateBy(getUsername());
        return toAjax(residentArchiveService.insertResidentArchive(entity));
    }

    @PreAuthorize("@ss.hasPermi('resident:archive:edit')")
    @Log(title = "居民档案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ResidentArchive entity) {
        // 如果修改了 id_card，验证新患者存在于 mp_person 并同步最新信息
        if (entity.getIdCard() != null && !entity.getIdCard().isEmpty()) {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            try {
                Map<String, Object> mpPerson = jdbc.queryForMap(
                    "SELECT person_name, gender_source_value, phone_number, address, card_id " +
                    "FROM mp_person WHERE card_id = ? LIMIT 1",
                    entity.getIdCard());
                entity.setResidentName((String) mpPerson.get("person_name"));
                entity.setGender((String) mpPerson.get("gender_source_value"));
                entity.setPhone((String) mpPerson.get("phone_number"));
                entity.setAddress((String) mpPerson.get("address"));
                entity.setIdCard((String) mpPerson.get("card_id"));
            } catch (Exception e) {
                return AjaxResult.error("该患者不存在于mp_person中，请确认身份证号正确");
            }
        }

        entity.setUpdateBy(getUsername());
        return toAjax(residentArchiveService.updateResidentArchive(entity));
    }

    @PreAuthorize("@ss.hasPermi('resident:archive:remove')")
    @Log(title = "居民档案", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(residentArchiveService.deleteResidentArchiveByIds(ids));
    }

    // ============================================================
    // 分级管理
    // ============================================================

    @GetMapping("/detail/{cardId}/grading")
    public AjaxResult getGradingRecords(@PathVariable String cardId) {
        return AjaxResult.success(gradingService.selectGradingRecordByPersonId(cardId));
    }

    @PreAuthorize("@ss.hasPermi('resident:archive:edit')")
    @Log(title = "分级评估", businessType = BusinessType.INSERT)
    @PostMapping("/detail/{cardId}/grading")
    public AjaxResult saveGrading(@PathVariable String cardId, @RequestBody ResidentGradingRecord record) {
        record.setPersonId(cardId);
        record.setCreateBy(getUsername());
        return toAjax(gradingService.insertGradingRecord(record));
    }
}
