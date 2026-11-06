package com.dchealth.web.controller.contract;

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
import com.dchealth.contract.domain.ContractSign;
import com.dchealth.contract.service.IContractSignService;

/**
 * 患者签约 — 患者数据来源于 mp_person + resident_archive
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/contract/sign")
public class ContractSignController extends BaseController {

    @Autowired
    private IContractSignService service;
    @Autowired
    private DataSource dataSource;

    /**
     * 签约列表（mp_person + resident_archive + 团队 + 服务包）
     */
    @PreAuthorize("@ss.hasPermi('contract:sign:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String residentName,
            @RequestParam(required = false) String contractStatus,
            @RequestParam(required = false) String signType,
            @RequestParam(required = false) String beginSignDate,
            @RequestParam(required = false) String endSignDate,
            @RequestParam(required = false) String managingInstitution,
            @RequestParam(required = false) String managingDoctor,
            @RequestParam(required = false) String diseaseTypes,
            @RequestParam(required = false) Long teamId) {
        startPage();
        Map<String, Object> params = new HashMap<>();
        if (residentName != null && !residentName.isEmpty()) params.put("residentName", residentName);
        if (contractStatus != null && !contractStatus.isEmpty()) params.put("contractStatus", contractStatus);
        if (signType != null && !signType.isEmpty()) params.put("signType", signType);
        if (beginSignDate != null && !beginSignDate.isEmpty()) params.put("beginSignDate", beginSignDate);
        if (endSignDate != null && !endSignDate.isEmpty()) params.put("endSignDate", endSignDate);
        if (managingInstitution != null && !managingInstitution.isEmpty()) params.put("managingInstitution", managingInstitution);
        if (managingDoctor != null && !managingDoctor.isEmpty()) params.put("managingDoctor", managingDoctor);
        if (diseaseTypes != null && !diseaseTypes.isEmpty()) params.put("diseaseTypes", diseaseTypes);
        if (teamId != null) params.put("teamId", teamId);
        return getDataTable(service.selectContractSignVOList(params));
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

    @PreAuthorize("@ss.hasPermi('contract:sign:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectContractSignVOById(id));
    }

    @PreAuthorize("@ss.hasPermi('contract:sign:add')")
    @Log(title = "患者签约", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ContractSign entity) {
        // 校验 resident_id 关联的居民存在于 resident_archive 和 mp_person
        if (entity.getResidentId() != null) {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            try {
                Map<String, Object> resident = jdbc.queryForMap(
                    "SELECT ra.resident_name, ra.id_card, mp.person_name " +
                    "FROM resident_archive ra " +
                    "LEFT JOIN mp_person mp ON ra.id_card = mp.card_id " +
                    "WHERE ra.id = ? LIMIT 1",
                    entity.getResidentId());
                // 优先用 mp_person 中的真实姓名，其次是 resident_archive 中的
                String mpName = (String) resident.get("person_name");
                if (mpName != null && !mpName.isEmpty()) {
                    entity.setResidentName(mpName);
                }
            } catch (Exception e) {
                return AjaxResult.error("居民不存在，请先选择已建档的居民（数据来源于mp_person）");
            }
        }
        entity.setCreateBy(getUsername());
        return toAjax(service.insertContractSign(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:sign:edit')")
    @Log(title = "患者签约", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ContractSign entity) {
        // 编辑时若 resident_id 变更，重新校验 mp_person
        if (entity.getResidentId() != null) {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            try {
                Map<String, Object> resident = jdbc.queryForMap(
                    "SELECT ra.resident_name, ra.id_card, mp.person_name " +
                    "FROM resident_archive ra " +
                    "LEFT JOIN mp_person mp ON ra.id_card = mp.card_id " +
                    "WHERE ra.id = ? LIMIT 1",
                    entity.getResidentId());
                String mpName = (String) resident.get("person_name");
                if (mpName != null && !mpName.isEmpty()) {
                    entity.setResidentName(mpName);
                }
            } catch (Exception e) {
                return AjaxResult.error("居民不存在，请先选择已建档的居民（数据来源于mp_person）");
            }
        }
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateContractSign(entity));
    }

    @PreAuthorize("@ss.hasPermi('contract:sign:remove')")
    @Log(title = "患者签约", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteContractSignByIds(ids));
    }

    @GetMapping("/institutions")
    public AjaxResult institutions() {
        return AjaxResult.success(service.selectDistinctInstitutions());
    }

    @GetMapping("/doctors")
    public AjaxResult doctors() {
        return AjaxResult.success(service.selectDistinctDoctors());
    }
}
