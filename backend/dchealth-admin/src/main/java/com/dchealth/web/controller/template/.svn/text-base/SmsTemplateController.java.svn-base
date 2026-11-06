package com.dchealth.web.controller.template;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.template.domain.SmsTemplate;
import com.dchealth.template.service.ISmsTemplateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;


@RestController
@RequestMapping("/sms/template")
public class SmsTemplateController extends BaseController
{
    @Autowired
    private ISmsTemplateService smsTemplateService;

    /**
     * 查询短信模板列表
     */
    @PreAuthorize("@ss.hasPermi('sms:template:list')")
    @GetMapping("/list")
    public TableDataInfo list(SmsTemplate smsTemplate){
        startPage();
        List<SmsTemplate> list = smsTemplateService.selectSmsTemplateList(smsTemplate);
        return getDataTable(list);
    }

    /**
     * 导出短信模板列表
     */
    @PreAuthorize("@ss.hasPermi('sms:template:export')")
    @Log(title = "短信模板导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SmsTemplate smsTemplate){
        List<SmsTemplate> list = smsTemplateService.selectSmsTemplateList(smsTemplate);
        ExcelUtil<SmsTemplate> util = new ExcelUtil<SmsTemplate>(SmsTemplate.class);
        util.exportExcel(response, list, "短信模板数据");
    }

    /**
     * 获取短信模板详细信息
     */
    @PreAuthorize("@ss.hasPermi('sms:template:detail')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id){
        return AjaxResult.success(smsTemplateService.selectSmsTemplateById(id));
    }

    /**
     * 新增短信模板
     */
    @PreAuthorize("@ss.hasPermi('sms:template:add')")
    @Log(title = "短信模板添加", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody SmsTemplate smsTemplate){
        return toAjax(smsTemplateService.insertSmsTemplate(smsTemplate));
    }

    /**
     * 获取短信模板详细信息
     */
    @GetMapping(value = "/getDetailByType/{type}")
    public AjaxResult getInfo(@PathVariable("type") String type){
        return AjaxResult.success(smsTemplateService.selectSmsTemplateByType(type));
    }

    /**
     * 修改短信模板
     */
    @PreAuthorize("@ss.hasPermi('sms:template:edit')")
    @Log(title = "短信模板编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody SmsTemplate smsTemplate){
        return toAjax(smsTemplateService.updateSmsTemplate(smsTemplate));
    }

    /**
     * 根据ID删除
     */
    @PreAuthorize("@ss.hasPermi('sms:template:delete')")
    @Log(title = "短信模板删除", businessType = BusinessType.UPDATE)
    @GetMapping(value = "delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(smsTemplateService.deleteSmsTemplateById(id));
    }

    /**
     * 删除短信模板
     */
    @PreAuthorize("@ss.hasPermi('sms:template:remove')")
    @Log(title = "短信模板批量删除", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(smsTemplateService.deleteSmsTemplateByIds(ids));
    }
}
