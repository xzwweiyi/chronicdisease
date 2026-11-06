package com.dchealth.web.controller.telemedicine;

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
import com.dchealth.telemedicine.domain.TelemedicineConsultation;
import com.dchealth.telemedicine.service.ITelemedicineConsultationService;

/**
 * 远程会诊 — 患者数据来源于 mp_person（通过 patient_info 桥接）
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/telemedicine/consultation")
public class TelemedicineConsultationController extends BaseController {

    @Autowired
    private ITelemedicineConsultationService service;
    @Autowired
    private DataSource dataSource;

    /**
     * 会诊列表（LEFT JOIN patient_info + mp_person）
     */
    @PreAuthorize("@ss.hasPermi('telemedicine:consult:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String consultationStatus,
            @RequestParam(required = false) String consultationType,
            @RequestParam(required = false) String consultationNo) {
        startPage();
        Map<String, Object> params = new HashMap<>();
        if (patientName != null && !patientName.isEmpty()) params.put("patientName", patientName);
        if (consultationStatus != null && !consultationStatus.isEmpty()) params.put("consultationStatus", consultationStatus);
        if (consultationType != null && !consultationType.isEmpty()) params.put("consultationType", consultationType);
        if (consultationNo != null && !consultationNo.isEmpty()) params.put("consultationNo", consultationNo);
        return getDataTable(service.selectTelemedicineConsultationWithPatient(params));
    }

    /** 搜索 mp_person 中的患者 */
    @GetMapping("/searchPerson")
    public AjaxResult searchPerson(@RequestParam String keyword) {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        try {
            List<Map<String, Object>> list = jdbc.queryForList(
                "SELECT id, person_id, person_name, card_id, phone_number, gender_source_value, sex, address " +
                "FROM mp_person WHERE person_name LIKE ? OR card_id = ? OR phone_number LIKE ? LIMIT 20",
                "%" + keyword + "%", keyword, "%" + keyword + "%");
            return AjaxResult.success(list);
        } catch (Exception e) {
            return AjaxResult.error("搜索患者失败: " + e.getMessage());
        }
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:consult:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectTelemedicineConsultationById(id));
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:consult:add')")
    @Log(title = "远程会诊", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TelemedicineConsultation entity) {
        // 校验 patient_id 关联的患者存在于 patient_info 和 mp_person
        // 链接路径: telemedicine_consultation.patient_id → patient_info.patient_id → patient_info.person_id → mp_person.person_id
        if (entity.getPatientId() == null) {
            return AjaxResult.error("患者ID不能为空，请先选择患者");
        }
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        try {
            Map<String, Object> patient = jdbc.queryForMap(
                "SELECT pi.patient_id, mp.person_name " +
                "FROM patient_info pi " +
                "LEFT JOIN mp_person mp ON pi.person_id = mp.person_id " +
                "WHERE pi.patient_id = ? LIMIT 1",
                entity.getPatientId());
            // 从 mp_person 获取真实患者姓名
            String mpName = (String) patient.get("person_name");
            if (mpName != null && !mpName.isEmpty()) {
                entity.setPatientName(mpName);
            }
        } catch (Exception e) {
            return AjaxResult.error("患者不存在于平台中（数据来源于mp_person），请先选择正确的患者");
        }
        entity.setCreateBy(getUsername());
        return toAjax(service.insertTelemedicineConsultation(entity));
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:consult:edit')")
    @Log(title = "远程会诊", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TelemedicineConsultation entity) {
        // 编辑时若 patient_id 变更，重新校验 mp_person
        if (entity.getPatientId() != null) {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            try {
                Map<String, Object> patient = jdbc.queryForMap(
                    "SELECT pi.patient_id, mp.person_name " +
                    "FROM patient_info pi " +
                    "LEFT JOIN mp_person mp ON pi.person_id = mp.person_id " +
                    "WHERE pi.patient_id = ? LIMIT 1",
                    entity.getPatientId());
                String mpName = (String) patient.get("person_name");
                if (mpName != null && !mpName.isEmpty()) {
                    entity.setPatientName(mpName);
                }
            } catch (Exception e) {
                return AjaxResult.error("患者不存在于平台中（数据来源于mp_person），请先选择正确的患者");
            }
        }
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateTelemedicineConsultation(entity));
    }

    @PreAuthorize("@ss.hasPermi('telemedicine:consult:remove')")
    @Log(title = "远程会诊", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteTelemedicineConsultationByIds(ids));
    }
}
