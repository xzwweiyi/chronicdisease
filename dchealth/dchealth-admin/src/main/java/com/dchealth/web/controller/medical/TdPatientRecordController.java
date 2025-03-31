package com.dchealth.web.controller.medical;

import com.alibaba.fastjson.JSON;
import com.dchealth.common.FtpUtils;
import com.dchealth.common.core.controller.BaseController;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.compatibility.domain.MpElectrocardiogramExam;
import com.dchealth.compatibility.from.DetailForm;
import com.dchealth.compatibility.from.HealthListSearchForm;
import com.dchealth.compatibility.from.MobileForm;
import com.dchealth.medical.service.impl.HealthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author lzx
 * @Date 2023/4/17
 * @Description: 健康档案接口
 */
@RestController
@RequestMapping(value = "/medical")
@Slf4j
public class TdPatientRecordController extends BaseController {
    @Autowired
    private HealthService healthService;


    /**
     * 查询患者健康档案列表
     *
     * @param healthListSearchForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/health/index", method = RequestMethod.POST)
    public AjaxResult healthIndex(  HealthListSearchForm  healthListSearchForm) {
        try {
            healthListSearchForm.setType("1");
        	return AjaxResult.success(healthService.index(healthListSearchForm));
        } catch (Exception e) {
        	 log.error("查询患者健康档案和体检报告列表异常：{},{}", JSON.toJSONString(healthListSearchForm),e);
        	 return AjaxResult.error(e.getMessage());
        }
       
    }

    /**
     * 查询患者体检报告列表
     *
     * @param healthListSearchForm
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/exam/index", method = RequestMethod.POST)
    public AjaxResult examIndex(  HealthListSearchForm  healthListSearchForm) {
        try {
            healthListSearchForm.setType("2");
            return AjaxResult.success(healthService.index(healthListSearchForm));
        } catch (Exception e) {
            log.error("查询患者健康档案和体检报告列表异常：{},{}", JSON.toJSONString(healthListSearchForm),e);
            return AjaxResult.error(e.getMessage());
        }

    }

    /**
     * 查询患者基础信息
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryBaseInfo", method = RequestMethod.POST)
    public AjaxResult queryBaseInfo(DetailForm detail) {
        try {
        	return AjaxResult.success(healthService.queryBaseInfo(detail));
        } catch (Exception e) {
        	log.error("查询患者基本信息，诊断信息异常：{}", e);
        	 return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 更新手机号 （此处更新患者的手机号mp_person和本次随访的手机号patient_info）
     * @param form 表单信息
     * @return AjaxResult
     */
    @PostMapping("updateMobile")
    public AjaxResult updateMobile(MobileForm form) {
        try {
            return AjaxResult.success(healthService.updateMobile(form));
        } catch (Exception e) {
            log.error("更新患者手机号异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询一般检查
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryGeneralInfo", method = RequestMethod.POST)
    public AjaxResult queryGeneralInfo( DetailForm detail) {
        try {
        	return AjaxResult.success(healthService.queryGeneralInfo(detail));
        } catch (Exception e) {
        	log.error("查询患者基本信息，诊断信息异常：{}", e);
        	 return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询诊断信息-健康档案
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryDiagnosis", method = RequestMethod.POST)
    public AjaxResult queryDiagnosis( DetailForm detail) {
        try {
        	return AjaxResult.success(healthService.queryDiagnosis(detail));
        } catch (Exception e) {
        	log.error("查询患者基本信息，诊断信息异常：{}", e);
        	 return AjaxResult.error(e.getMessage());
        }
    }

    
    /**
     * 检查项目列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkExamItemList", method = RequestMethod.POST)
    public AjaxResult checkExamItemList( DetailForm detail) {
        try {
        	return AjaxResult.success(healthService.checkExamItemList(detail));
        } catch (Exception e) {
        	log.info("查询体检报告检查项目列表异常：{}", e);
        	 return AjaxResult.error(e.getMessage());
        }
    }
  


    /**
     * 查询详情页 - 检验项目列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkTestItemList", method = RequestMethod.POST)
    public AjaxResult queryTestItemList( DetailForm detail) {
        try {
        	return AjaxResult.success(healthService.queryCheckTestItemList(detail));
        } catch (Exception e) {
        	log.info("查询体检报告检验项目列表异常：{}", e);
        	 return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 检验项目列表---门诊
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mzTestItemList", method = RequestMethod.POST)
    public AjaxResult mzTestItemList(DetailForm detail) {
        try {
            detail.setVisitType("2");
            return AjaxResult.success(healthService.mzTestItemList(detail));
        } catch (Exception e) {
            log.info("查询门诊检验项目列表异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 检验项目列表---预住院, 检验
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/preAdmissionTestItemList", method = RequestMethod.POST)
    public AjaxResult preAdmissionTestItemList(DetailForm detail) {
        try {
            detail.setVisitType("4");
            return AjaxResult.success(healthService.mzTestItemList(detail));
        } catch (Exception e) {
            log.info("查询门诊检验项目列表异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 检查项目列表--门诊
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mzExamItemList", method = RequestMethod.POST)
    public AjaxResult mzExamItemList(DetailForm detail) {
        try {
            detail.setVisitType("2");
            return AjaxResult.success(healthService.mzExamItemList(detail));
        } catch (Exception e) {
            log.info("查询门诊检查项目列表异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 检查项目列表--预住院检查
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/preAdmissionExamItemList", method = RequestMethod.POST)
    public AjaxResult preAdmissionExamItemList(DetailForm detail) {
        try {
            detail.setVisitType("4");
            return AjaxResult.success(healthService.mzExamItemList(detail));
        } catch (Exception e) {
            log.info("查询门诊检查项目列表异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询诊断信息-门诊
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryMzDiagnosis", method = RequestMethod.POST)
    public AjaxResult queryMzDiagnosis( DetailForm detail) {
        try {
            detail.setVisitType("2");
            return AjaxResult.success(healthService.queryMzDiagnosis(detail));
        } catch (Exception e) {
            log.error("查询患者基本信息，诊断信息异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 查询诊断信息-诊断预住院
     *
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/preAdmissionDiagnosis", method = RequestMethod.POST)
    public AjaxResult preAdmissionDiagnosis( DetailForm detail) {
        try {
            detail.setVisitType("4");
            return AjaxResult.success(healthService.queryMzDiagnosis(detail));
        } catch (Exception e) {
            log.error("查询患者基本信息，诊断信息异常：{}", e);
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping(value ="/preview/electrocardiogram", method = RequestMethod.POST)
    public void previewElectrocardiogramReport(MpElectrocardiogramExam mpElectrocardiogramExam,HttpServletResponse response){
        try {
            FTPClient ftpClient = FtpUtils.login();
            healthService.previewArea(ftpClient, response, mpElectrocardiogramExam.getPdfReport());
            FtpUtils.ftpFinally(ftpClient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询健康档案树形结构
     *
     * @param id
     * @return
     */
//    @GetMapping("/tree")
//    public AjaxResult recordTree(String  personId) {
//
//        try {
//            TdPatientRecordTree tree = tdPatientRecordService.recordTree(id);
//            return AjaxResult.success(tree);
//        } catch (Exception e) {
//            log.info("查询健康档案树形结构异常:{}", e.getMessage());
//            return AjaxResult.error(e.getMessage());
//        }
//    }

  
}
