package com.dchealth.web.controller.screening;

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
import com.dchealth.screening.domain.ScreeningPhysical;
import com.dchealth.screening.service.IScreeningPhysicalService;

/**
 * 体检筛查 — 患者数据来源于 mp_person
 *
 * @author dchealth
 */
@RestController
@RequestMapping("/screening/physical")
public class ScreeningPhysicalController extends BaseController {

    @Autowired
    private IScreeningPhysicalService service;
    @Autowired
    private DataSource dataSource;

    @PreAuthorize("@ss.hasPermi('screening:physical:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) String personName,
            @RequestParam(required = false) String cardId,
            @RequestParam(required = false) String examInstitution) {
        startPage();
        Map<String, Object> params = new HashMap<>();
        if (personName != null && !personName.isEmpty()) params.put("personName", personName);
        if (cardId != null && !cardId.isEmpty()) params.put("cardId", cardId);
        if (examInstitution != null && !examInstitution.isEmpty()) params.put("examInstitution", examInstitution);
        return getDataTable(service.selectScreeningPatientList(params));
    }

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

    @PreAuthorize("@ss.hasPermi('screening:physical:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(service.selectScreeningPhysicalById(id));
    }

    @PreAuthorize("@ss.hasPermi('screening:physical:add')")
    @Log(title = "体检筛查", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ScreeningPhysical entity) {
        if (entity.getIdCard() == null || entity.getIdCard().isEmpty()) {
            return AjaxResult.error("身份证号不能为空，请先选择患者");
        }
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        try {
            Map<String, Object> mp = jdbc.queryForMap(
                "SELECT person_name, gender_source_value, phone_number, card_id FROM mp_person WHERE card_id = ? LIMIT 1",
                entity.getIdCard());
            entity.setResidentName((String) mp.get("person_name"));
            entity.setPhone((String) mp.get("phone_number"));
        } catch (Exception e) {
            return AjaxResult.error("患者不存在于mp_person中，请确认身份证号正确");
        }
        entity.setCreateBy(getUsername());
        return toAjax(service.insertScreeningPhysical(entity));
    }

    @PreAuthorize("@ss.hasPermi('screening:physical:edit')")
    @Log(title = "体检筛查", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ScreeningPhysical entity) {
        // 若 idCard 变更，重新校验 mp_person 并同步姓名和手机号
        if (entity.getIdCard() != null && !entity.getIdCard().isEmpty()) {
            JdbcTemplate jdbc = new JdbcTemplate(dataSource);
            try {
                Map<String, Object> mp = jdbc.queryForMap(
                    "SELECT person_name, gender_source_value, phone_number, card_id FROM mp_person WHERE card_id = ? LIMIT 1",
                    entity.getIdCard());
                entity.setResidentName((String) mp.get("person_name"));
                entity.setPhone((String) mp.get("phone_number"));
            } catch (Exception e) {
                return AjaxResult.error("患者不存在于mp_person中，请确认身份证号正确");
            }
        }
        entity.setUpdateBy(getUsername());
        return toAjax(service.updateScreeningPhysical(entity));
    }

    @PreAuthorize("@ss.hasPermi('screening:physical:remove')")
    @Log(title = "体检筛查", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(service.deleteScreeningPhysicalByIds(ids));
    }
}
