package com.dchealth.web.controller.medical;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.dchealth.DchealthApplication;
import com.dchealth.medical.domain.DoctorSatisfactionEvaluation;
import com.dchealth.medical.domain.vo.DoctorSatisfactionEvaluationPage;
import com.dchealth.medical.service.IDoctorSatisfactionEvaluationService;
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
import com.dchealth.common.utils.poi.ExcelUtil;
import com.dchealth.common.core.page.TableDataInfo;


@RestController
@RequestMapping("/medical/evaluation")
public class DoctorSatisfactionEvaluationController extends BaseController
{
    @Autowired
    private IDoctorSatisfactionEvaluationService doctorSatisfactionEvaluationService;

    /**
     * 查询医生满意度评价列表
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:list')")
    @GetMapping("/list")
    public DoctorSatisfactionEvaluationPage list(DoctorSatisfactionEvaluation doctorSatisfactionEvaluation){
        DoctorSatisfactionEvaluationPage page = new DoctorSatisfactionEvaluationPage();
        startPage();
        List<DoctorSatisfactionEvaluation> list = doctorSatisfactionEvaluationService.selectDoctorSatisfactionEvaluationList(doctorSatisfactionEvaluation);
        TableDataInfo tableDataInfo = getDataTable(list);
        page.setTotal(tableDataInfo.getTotal());
        page.setList(list);
        Integer size = doctorSatisfactionEvaluationService.selectDoctorSatisfactionEvaluationList(new DoctorSatisfactionEvaluation()).size();
        Integer totalScore = doctorSatisfactionEvaluationService.getSumScore();
        if(size>0 && totalScore>0){
            page.setSumTotal(size);
            BigDecimal f =new BigDecimal(totalScore).divide(new BigDecimal(size),1, BigDecimal.ROUND_HALF_UP);
            page.setAverage(f.toString());
        }else{
            page.setSumTotal(0);
            page.setAverage("0");
        }
        return page;
    }

    /**
     * 导出医生满意度评价列表
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:export')")
    @Log(title = "医生满意度评价导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DoctorSatisfactionEvaluation doctorSatisfactionEvaluation){
        List<DoctorSatisfactionEvaluation> list = doctorSatisfactionEvaluationService.selectDoctorSatisfactionEvaluationList(doctorSatisfactionEvaluation);
        ExcelUtil<DoctorSatisfactionEvaluation> util = new ExcelUtil<DoctorSatisfactionEvaluation>(DoctorSatisfactionEvaluation.class);
        util.exportExcel(response, list, "医生满意度评价数据");
    }

    /**
     * 获取医生满意度评价详细信息
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id){
        return AjaxResult.success(doctorSatisfactionEvaluationService.selectDoctorSatisfactionEvaluationById(id));
    }

    /**
     * 新增医生满意度评价
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:add')")
    @Log(title = "医生满意度评价新增", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody DoctorSatisfactionEvaluation doctorSatisfactionEvaluation){
        return toAjax(doctorSatisfactionEvaluationService.insertDoctorSatisfactionEvaluation(doctorSatisfactionEvaluation));
    }

    /**
     * 修改医生满意度评价
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:edit')")
    @Log(title = "医生满意度评价编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody DoctorSatisfactionEvaluation doctorSatisfactionEvaluation){
        return toAjax(doctorSatisfactionEvaluationService.updateDoctorSatisfactionEvaluation(doctorSatisfactionEvaluation));
    }

    /**
     * 删除医生满意度评价
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:delete')")
    @Log(title = "医生满意度评价批量删除", businessType = BusinessType.DELETE)
    @GetMapping(value = "delete/{id}")
    public AjaxResult delete(@PathVariable("id") Long id){
        return toAjax(doctorSatisfactionEvaluationService.deleteDoctorSatisfactionEvaluationById(id));
    }

    /**
     * 删除医生满意度评价
     */
    @PreAuthorize("@ss.hasPermi('medical:evaluation:remove')")
    @Log(title = "医生满意度评价批量删除", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(doctorSatisfactionEvaluationService.deleteDoctorSatisfactionEvaluationByIds(ids));
    }
}
