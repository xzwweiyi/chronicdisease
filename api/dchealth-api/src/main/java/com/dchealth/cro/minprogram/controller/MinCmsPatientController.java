package com.dchealth.cro.minprogram.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.service.ArticleService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.research.domain.ResearchInfoArticlePatient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 患者端患教
 */
@Controller
@RequestMapping(value = "/min_program/cmsPatient")
public class MinCmsPatientController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(MinCmsPatientController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RsaUtil rsaUtil;
    /**
     * 最新患教文章
     *
     * @param researchInfoArticlePatient
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "latestPatientArticle", method = RequestMethod.POST)
    public String latestPatientArticle(@Validated ResearchInfoArticlePatient researchInfoArticlePatient, BindingResult bind) {
        L.info("researchInfoArticlePatient:{}",researchInfoArticlePatient);
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(researchInfoArticlePatient.getAccountId())){
                researchInfoArticlePatient.setAccountIds(Long.valueOf(rsaUtil.decryptValue(researchInfoArticlePatient.getAccountId())));
            }
            return buildSuccessMView(articleService.latestPatientArticle(researchInfoArticlePatient));
        } catch (Exception e) {
            L.info("最新患教接口异常{}", JSON.toJSONString(researchInfoArticlePatient), e);
            return buildExceptionMView();
        }
    }

    /**
     * 患教天地
     *
     * @param researchInfoArticlePatient
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "allPatientArticle", method = RequestMethod.POST)
    public String allPatientArticle(@Validated ResearchInfoArticlePatient researchInfoArticlePatient, BindingResult bind) {
        L.info("researchInfoArticlePatient:{}",JSON.toJSONString(researchInfoArticlePatient));
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(researchInfoArticlePatient.getAccountId())){
                researchInfoArticlePatient.setAccountIds(Long.valueOf(rsaUtil.decryptValue(researchInfoArticlePatient.getAccountId())));
            }
            return buildSuccessMView(articleService.allPatientArticle(researchInfoArticlePatient));
        } catch (Exception e) {
            L.info("患教天地接口异常{}", JSON.toJSONString(researchInfoArticlePatient), e);
            return buildExceptionMView();
        }
    }

    /**
     * 自评问卷(暂时弃用)
     */
    @ResponseBody
    @RequestMapping(value = "ownQuestionnaire", method = RequestMethod.POST)
    public String ownQuestionnaire(@Validated ResearchInfoArticlePatient researchInfoArticlePatient, BindingResult bind) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(researchInfoArticlePatient.getAccountId())){
                researchInfoArticlePatient.setAccountIds(Long.valueOf(rsaUtil.decryptValue(researchInfoArticlePatient.getAccountId())));
            }
            return buildSuccessMView(articleService.ownQuestionnaire(researchInfoArticlePatient));
        } catch (Exception e) {
            L.info("自评接口异常{}", JSON.toJSONString(researchInfoArticlePatient), e);
            return buildExceptionMView();
        }
    }

    /**
     * 问卷列表
     */
    @ResponseBody
    @RequestMapping(value = "questionnaireList", method = RequestMethod.POST)
    public String questionnaireList(@Validated ResearchInfoArticlePatient researchInfoArticlePatient, BindingResult bind) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(researchInfoArticlePatient.getAccountId())){
                researchInfoArticlePatient.setAccountIds(Long.valueOf(rsaUtil.decryptValue(researchInfoArticlePatient.getAccountId())));
            }
            return buildSuccessMView(articleService.questionnaireList(researchInfoArticlePatient));
        } catch (Exception e) {
            L.info("问卷列表接口异常{}", JSON.toJSONString(researchInfoArticlePatient), e);
            return buildExceptionMView();
        }
    }

    /**
     * 资讯列表
     * @param acticleListSearchForm
     * @param bind
     * @param positionType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public String list(@Validated ActicleListSearchForm acticleListSearchForm, BindingResult bind,
                       @RequestParam(value = "positionType",required = false, defaultValue="2") String positionType) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            acticleListSearchForm.setPositionType(positionType);
            return buildSuccessMView(articleService.list(acticleListSearchForm));
        } catch (Exception e) {
            L.info("综合查询资讯信息异常{}", JSON.toJSONString(acticleListSearchForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 资讯详情
     * @param articleId
     * @param accountId
     * @param appType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public String detail(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) String accountId,@RequestParam("appType") String appType) {
        try {
            if (articleId < 1) {
                return buildErrorParamMView();
            }
            Long accountIds = null;
            if(StringUtils.isNotEmpty(accountId)){
                accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
            }
            return buildSuccessMView(articleService.detail(articleId,accountIds,appType));
        } catch (Exception e) {
            L.info("查询资讯详情异常{}", articleId, e);
            return buildExceptionMView();
        }
    }

    /**
     * 保存评论
     */
    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String save(@Validated CommentSaveForm commentSaveForm, BindingResult bind,
                       @RequestAttribute("channel") String channel, @RequestAttribute("appType") String appType) {
        try {
            commentSaveForm.setTenantId(channel);
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(commentSaveForm.getAccountId())){
                commentSaveForm.setAccountIds(Long.valueOf(rsaUtil.decryptValue(commentSaveForm.getAccountId())));
            }
            return buildSuccessMView(articleService.save(commentSaveForm,appType));
        } catch (Exception e) {
            L.info("文章评论意见异常{}", JSON.toJSONString(commentSaveForm), e);
            return buildExceptionMView();
        }
    }

    /**
     * 查询资讯分类信息
     */
    @ResponseBody
    @RequestMapping(value = "catalog", method = RequestMethod.POST)
    public String catalog(@RequestParam(value = "positionType",required = false, defaultValue="2") String positionType) {
        try {
            return buildSuccessMView(articleService.listCatalog("1",positionType));
        } catch (Exception e) {
            L.info(" 查询资讯分类异常{}", positionType, e);
            return buildExceptionMView();
        }
    }

    /**
     * 查询资讯信息评论
     */
    @ResponseBody
    @RequestMapping(value = "detailArticleComment", method = RequestMethod.POST)
    public String detailComment(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) String accountId,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        try {
            if (articleId < 1) {
                return buildErrorParamMView();
            }
            Long accountIds = null;
            if(StringUtils.isNotEmpty(accountId)){
                accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
            }
            return buildSuccessMView(articleService.detailComment(articleId,accountIds, pageNo, pageSize));
        } catch (Exception e) {
            L.info("查询资讯详情异常{}", articleId, e);
            return buildExceptionMView();
        }
    }

}
