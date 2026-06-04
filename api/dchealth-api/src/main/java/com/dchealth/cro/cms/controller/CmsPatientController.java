package com.dchealth.cro.cms.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.PatientAccount;
import com.dchealth.cro.account.mapper.PatientAccountMapper;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.service.ArticleService;
import com.dchealth.cro.cms.vo.ArticleDetailVO;
import com.dchealth.cro.cms.vo.ArticleVO;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
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

import javax.servlet.http.HttpServletRequest;

/**
 * 患者端患教
 */
@Controller
@RequestMapping(value = "/cmsPatient")
public class CmsPatientController extends ApiBaseController {
    private static final Logger L = LoggerFactory.getLogger(CmsPatientController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private PatientAccountMapper patientAccountMapper;
    @Autowired
    private RsaUtil rsaUtil;

    /**
     * 权限校验：验证 accountId 是否属于当前登录用户
     */
    private boolean validateAccountIdAccess(Long accountId, HttpServletRequest request) {
        if (accountId == null) {
            return true; // accountId为可选参数，为空时不校验
        }
        if(accountId != null){
            accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
        }
        String deviceToken = request.getHeader("deviceToken");
        if (!StringUtils.isNotBlankAndNotEmpty(deviceToken)) {
            return true; // 无deviceToken时跳过校验（兼容旧逻辑）
        }
        PatientAccount currentAccount = patientAccountMapper.selectPatientAccountByOpenId(deviceToken);
        if (currentAccount == null) {
            L.warn("越权访问尝试: deviceToken={}, 用户不存在", deviceToken);
            return false;
        }
        if (!currentAccount.getAccountId().equals(accountId)) {
            L.warn("越权访问尝试: deviceToken={}, 请求accountId={}, 实际accountId={}",
                deviceToken, accountId, currentAccount.getAccountId());
            return false;
        }
        return true;
    }

    /**
     * 构建权限拒绝响应
     */
    private String buildAccessDeniedResponse() {
        DchealthCloudResponseEntity<Object> result = new DchealthCloudResponseEntity<>();
        result.setCode(CroApiCodeConstant.ERROR_PARAM.getCode());
        result.setMsg("无权访问该用户信息");
        return JSON.toJSONString(result);
    }

    /**
     * 最新患教文章
     *
     * @param researchInfoArticlePatient
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "latestPatientArticle", method = RequestMethod.POST)
    public String latestPatientArticle(HttpServletRequest request,
                                       @Validated ResearchInfoArticlePatient researchInfoArticlePatient, BindingResult bind) {
        L.info("researchInfoArticlePatient:{}",researchInfoArticlePatient);
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(researchInfoArticlePatient.getAccountId())){
                researchInfoArticlePatient.setAccountIds(Long.valueOf(rsaUtil.decryptValue(researchInfoArticlePatient.getAccountId())));
            }
            DchealthCloudResponseEntity<ArticleVO> result = articleService.latestPatientArticle(researchInfoArticlePatient);
            if (!"3".equals(request.getHeader("osType"))) {
                result.getData().getArticleList().forEach(article -> {
                    if("3".equals(article.getUploadType()) && "1".equals(article.getType())){
                        article.setUploadType("2");
                    }
                });
            }
            return buildSuccessMView(result);
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
    public String allPatientArticle(HttpServletRequest request,
            @Validated ResearchInfoArticlePatient researchInfoArticlePatient, BindingResult bind) {
        L.info("researchInfoArticlePatient:{}",JSON.toJSONString(researchInfoArticlePatient));
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            if(StringUtils.isNotEmpty(researchInfoArticlePatient.getAccountId())){
                researchInfoArticlePatient.setAccountIds(Long.valueOf(rsaUtil.decryptValue(researchInfoArticlePatient.getAccountId())));
            }
            DchealthCloudResponseEntity<ArticleVO> result = articleService.allPatientArticle(researchInfoArticlePatient);
            if (!"3".equals(request.getHeader("osType"))) {
                result.getData().getArticleList().forEach(article -> {
                    if("3".equals(article.getUploadType()) && "1".equals(article.getType())){
                        article.setUploadType("2");
                    }
                });
            }
            return buildSuccessMView(result);
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
    public String detail(HttpServletRequest request,
            @RequestParam(value = "articleId", required = true) Long articleId,
            @RequestParam(value = "accountId", required = false) String accountId,
            @RequestParam("appType") String appType) {
        try {
            if (articleId < 1) {
                return buildErrorParamMView();
            }
            Long accountIds = null;
            if(StringUtils.isNotEmpty(accountId)){
                accountIds = Long.valueOf(rsaUtil.decryptValue(accountId));
            }
            // 权限校验
            if (!validateAccountIdAccess(accountIds, request)) {
                return buildAccessDeniedResponse();
            }
            String osType = request.getHeader("osType");
            DchealthCloudResponseEntity<ArticleDetailVO> result = articleService.detail(articleId, accountIds, appType);
            ArticleDetailVO data = result.getData();
            if((!"3".equals(osType)) && "3".equals(data.getUploadType())
            && StringUtils.isBlank(data.getVideoUrl())){
                data.setUploadType("2");
            }
            return buildSuccessMView(result);
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
    public String detailComment(HttpServletRequest request,
            @RequestParam(value = "articleId", required = true) Long articleId,
            @RequestParam(value = "accountId", required = false) Long accountId,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        try {
            if (articleId < 1) {
                return buildErrorParamMView();
            }
            if(accountId != null){
                accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
            }
            // 权限校验
            if (!validateAccountIdAccess(accountId, request)) {
                return buildAccessDeniedResponse();
            }
            return buildSuccessMView(articleService.detailComment(articleId,accountId, pageNo, pageSize));
        } catch (Exception e) {
            L.info("查询资讯详情异常{}", articleId, e);
            return buildExceptionMView();
        }
    }

}