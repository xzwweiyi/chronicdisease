package com.dchealth.web.controller.redis;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.redis.domain.SysRedisConfig;
import com.dchealth.redis.exception.RedisException;
import com.dchealth.redis.service.ISysRedisConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.common.core.page.TableDataInfo;

/**
 * 【请填写功能名称】Controller
 *
 * @author ruoyi
 * @date 2022-06-20
 */
@RestController
@RequestMapping("/redis/config")
public class SysRedisConfigController extends BaseController {
    @Autowired
    private ISysRedisConfigService sysRedisConfigService;

    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysRedisConfig sysRedisConfig) {
        startPage();
        List<SysRedisConfig> list = sysRedisConfigService.selectSysRedisConfigList(sysRedisConfig);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysRedisConfig sysRedisConfig) {
        List<SysRedisConfig> list = sysRedisConfigService.selectSysRedisConfigList(sysRedisConfig);
        ExcelUtil<SysRedisConfig> util = new ExcelUtil<SysRedisConfig>(SysRedisConfig.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysRedisConfigService.selectSysRedisConfigById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "添加缓存配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysRedisConfig sysRedisConfig) throws RedisException {
        return toAjax(sysRedisConfigService.insertSysRedisConfig(sysRedisConfig));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "编辑缓存配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysRedisConfig sysRedisConfig) throws RedisException {
        return toAjax(sysRedisConfigService.updateSysRedisConfig(sysRedisConfig));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysRedisConfigService.deleteSysRedisConfigByIds(ids));
    }
}

