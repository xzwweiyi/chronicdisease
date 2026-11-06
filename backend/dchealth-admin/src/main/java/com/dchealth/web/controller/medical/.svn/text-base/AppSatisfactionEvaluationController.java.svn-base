package com.dchealth.web.controller.medical;



import com.dchealth.common.annotation.Log;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.core.domain.entity.SysRole;
import com.dchealth.common.core.page.TableDataInfo;
import com.dchealth.common.enums.BusinessType;
import com.dchealth.medical.domain.AppSatisfactionEvaluation;
import com.dchealth.medical.service.AppSatisfactionEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author caihe
 * @since 2023/5/29
 */
@RestController
@RequestMapping("/evaluation")
public class AppSatisfactionEvaluationController extends BaseController {
    @Autowired
    AppSatisfactionEvaluationService appSatisfactionEvaluationService;
    /**
     * 评价记录列表
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:select')")
    @PostMapping("/queryList")
    public AjaxResult list(AppSatisfactionEvaluation evaluation){
        startPage();
        List<AppSatisfactionEvaluation> list = appSatisfactionEvaluationService.selectEvaluationList(evaluation);
        Map rtMap = new HashMap<>();
        rtMap.put("total",getDataTable(list).getTotal());
        rtMap.put("rows",list);
        //查询综合满意度
        rtMap.putAll(appSatisfactionEvaluationService.queryEvaluation(evaluation));

        return AjaxResult.success(rtMap);
    }

}
