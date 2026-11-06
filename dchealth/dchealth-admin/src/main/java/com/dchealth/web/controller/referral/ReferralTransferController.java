package com.dchealth.web.controller.referral;

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
import com.dchealth.referral.domain.ReferralTransfer;
import com.dchealth.referral.service.IReferralTransferService;

/**
 * 转诊管理 — 患者数据来源于 mp_person（通过 patient_info 桥接）
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/referral/transfer")
public class ReferralTransferController extends BaseController {

    @Autowired
    private IReferralTransferService service;
    @Autowired
    private DataSource dataSource;

    /**
     * 转诊列表（LEFT JOIN patient_info + mp_person）
     */
    @PreAuthorize("@ss.hasPermi('referral:transfer:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String transferStatus,
            @RequestParam(required = false) String transferType,
            @RequestParam(required = false) String fromInstitution,
            @RequestParam(required = false) String toInstitution) {
        startPage();
        Map<String, Object> params = new HashMap<>();
        if (patientName != null && !patientName.isEmpty()) params.put("patientName", patientName);
        if (transferStatus != null && !transferStatus.isEmpty()) params.put("transferStatus", transferStatus);
        if (transferType != null && !transferType.isEmpty()) params.put("transferType", transferType);
        if (fromInstitution != null && !fromInstitution.isEmpty()) params.put("fromInstitution", fromInstitution);
        if (toInstitution != null && !toInstitution.isEmpty()) params.put("toInstitution", toInstitution);
        return getDataTable(service.selectReferralTransferWithPatient(params));
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

    @PreAuthorize("@ss.hasPermi('referral:transfer:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectReferralTransferById(id));
    }

    @PreAuthorize("@ss.hasPermi('referral:transfer:add')")
    @Log(title = "转诊管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ReferralTransfer entity) {
        // 校验 patient_id 关联的患者存在于 patient_info 和 mp_person
        // 链接路径: referral_transfer.patient_id → patient_info.patient_id → patient_info.person_id → mp_person.person_id
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
        return toAjax(service.insertReferralTransfer(entity));
    }

    @PreAuthorize("@ss.hasPermi('referral:transfer:edit')")
    @Log(title = "转诊管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ReferralTransfer entity) {
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
        return toAjax(service.updateReferralTransfer(entity));
    }

    @PreAuthorize("@ss.hasPermi('referral:transfer:remove')")
    @Log(title = "转诊管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteReferralTransferByIds(ids));
    }
}
