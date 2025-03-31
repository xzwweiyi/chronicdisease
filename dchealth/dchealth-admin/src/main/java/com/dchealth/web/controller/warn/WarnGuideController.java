package com.dchealth.web.controller.warn;

import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.warn.domain.form.WarnGuideMsgForm;
import com.dchealth.warn.service.IWarnGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 预警指导聊天内容
 */
@RestController
@RequestMapping("/warn/guide")
public class WarnGuideController extends BaseController {

    @Autowired
    private IWarnGuideService warnGuideService;


    /**
     * 预警指导聊天内容
     * @param recordId 档案ID
     * @return
     */
    @PreAuthorize("@ss.hasPermi('warn:guide:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return AjaxResult.success(warnGuideService.guideList(recordId));
    }

    /**
     * 发送预警指导消息
     */
    @PreAuthorize("@ss.hasPermi('warn:guide:send')")
    @PostMapping
    public AjaxResult sendMsg(@RequestBody WarnGuideMsgForm warnGuideMsgForm) {
        warnGuideMsgForm.setFromUserId(getUserId());
        warnGuideMsgForm.setDeptId(getDeptId());
        return AjaxResult.success(warnGuideService.sendMsg(warnGuideMsgForm));
    }

    /**
     * 结束预警指导
     * @param recordId 档案ID
     * @return
     */
    @PreAuthorize("@ss.hasPermi('warn:guide:end')")
    @GetMapping(value = "/end/{recordId}")
    public AjaxResult end(@PathVariable("recordId") Long recordId)
    {
        Long userId = getUserId();
        return AjaxResult.success(warnGuideService.end(recordId,userId));
    }

}
