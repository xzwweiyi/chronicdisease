package com.dchealth.cro.medical.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.base.common.FileCommon;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.form.domain.FMedicalExamForm;
import com.dchealth.cro.form.domain.FMedicalExamRes;
import com.dchealth.cro.form.service.MedicalExamService;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

/**
 * 问卷相关
 */
@Controller
@RequestMapping(value = "/medical/exam")
public class MedicalExamController extends ApiBaseController {

    private static final Logger L = LoggerFactory.getLogger(MedicalExamController.class);

    @Autowired
    private MedicalExamService medicalExamService;
    @Autowired
    private FileCommon fileCommon;
    @Autowired
    private RsaUtil rsaUtil;
    /**
     * 问卷详情
     * @param fMedicalExamRes
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "examFromInfo", method = RequestMethod.POST)
    public String examFromDetailInfo(FMedicalExamRes fMedicalExamRes) {
        try {
            if(fMedicalExamRes.getAccountId() !=null){
                fMedicalExamRes.setAccountId(Long.valueOf(rsaUtil.decryptValue(fMedicalExamRes.getAccountId().toString())));
            }
            L.info("问卷查看详情json{}", JSON.toJSONString(fMedicalExamRes));
            return buildSuccessMView(medicalExamService.selectExamFromDetailInfo(fMedicalExamRes));
        } catch (Exception e) {
            L.info("问卷详情异常{}", JSON.toJSONString(fMedicalExamRes), e);
            return buildExceptionMView();
        }
    }

    /**
     * 问卷填报(新增、编辑)
     * @param examFromJson
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdateExamFrom", method = RequestMethod.POST)
    public String saveOrUpdateExamFrom(@RequestParam(value = "examFromJson", required = true) String examFromJson ,
                                       @RequestParam(value = "imageUrlsJson", required = false) String imageUrlsJson
            , MultipartHttpServletRequest request) {
        try {
            L.info("问卷填报的json{}", JSON.toJSONString(examFromJson));
            FMedicalExamForm fmedicalexamform = new FMedicalExamForm();
            if (StringUtils.isNotBlankAndNotEmpty(examFromJson)) {
                fmedicalexamform = JSON.parseObject(examFromJson, FMedicalExamForm.class);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(imageUrlsJson)) {
                List<PatientRecordFilesForm> list = JSON.parseArray(imageUrlsJson,
                        PatientRecordFilesForm.class);
                fmedicalexamform.setImageUrls(list);
            }
            fmedicalexamform.setImageUrls(fileCommon.uploadExamFiles(fmedicalexamform.getImageUrls(), request));
            return buildSuccessMView(medicalExamService.newSaveOrUpdateExamFrom2(fmedicalexamform));
        } catch (Exception e) {
            L.info("问卷填报异常{}", JSON.toJSONString(examFromJson), e);
            return buildExceptionMView();
        }
    }


    /**
     * 问卷填报(新增、编辑)
     * @param examFromJson
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdateExamFromMin", method = RequestMethod.POST)
    public String saveOrUpdateExamFromMin (@RequestParam(value = "examFromJson", required = true) String examFromJson ,
                                       @RequestParam(value = "imageUrlsJson", required = false) String imageUrlsJson
//            , MultipartHttpServletRequest request
    ) {
        try {
            L.info("问卷填报的json{}", JSON.toJSONString(examFromJson));
            FMedicalExamForm fmedicalexamform = new FMedicalExamForm();
            if (StringUtils.isNotBlankAndNotEmpty(examFromJson)) {
                fmedicalexamform = JSON.parseObject(examFromJson, FMedicalExamForm.class);
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(imageUrlsJson)) {
                List<PatientRecordFilesForm> list = JSON.parseArray(imageUrlsJson,
                        PatientRecordFilesForm.class);
                fmedicalexamform.setImageUrls(list);
            }
//            fmedicalexamform.setImageUrls(fileCommon.uploadExamFiles(fmedicalexamform.getImageUrls(), null));
            return buildSuccessMView(medicalExamService.newSaveOrUpdateExamFrom2(fmedicalexamform));
        } catch (Exception e) {
            L.info("问卷填报异常{}", JSON.toJSONString(examFromJson), e);
            return buildExceptionMView();
        }
    }

}
