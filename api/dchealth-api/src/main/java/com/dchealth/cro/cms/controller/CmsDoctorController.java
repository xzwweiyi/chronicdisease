package com.dchealth.cro.cms.controller;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.ArticleRequest;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.service.CmsDoctorService;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 医生端cms
 */
@Controller
@RequestMapping(value = "/cmsDoctor")
public class CmsDoctorController extends ApiBaseController {

    private static final Logger L = LoggerFactory.getLogger(CmsDoctorController.class);

    @Autowired
    CmsDoctorService cmsDoctorService;
    @Autowired
    private RsaUtil rsaUtil;


    /**
     * APP端首页
     * "positionType 显示类型 1 医生端 2 患者端 "
     */
    @ResponseBody
    @RequestMapping(value = "findAppIndex", method = RequestMethod.POST)
    public String findLiverGallDoctorIndex(@RequestBody ArticleRequest articleRequest) {
        try {
            L.info("infoForm:{}",JSON.toJSONString(articleRequest));
            if(articleRequest.getAccountId() != null){
                articleRequest.setAccountId(Long.valueOf(rsaUtil.decryptValue(articleRequest.getAccountId().toString())));
            }
            return buildSuccessMView(cmsDoctorService.findAppIndex(articleRequest));
        } catch (Exception e) {
            L.error("查询首页信息异常{}", e);
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
                       @RequestParam(value = "positionType",required = false, defaultValue="1") String positionType) {
        try {
            if (bind.hasErrors()) {
                return buildBindErrorParamMView(bind);
            }
            acticleListSearchForm.setPositionType(positionType);
            return buildSuccessMView(cmsDoctorService.list(acticleListSearchForm));
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
    public String detail(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) Long accountId,@RequestParam("appType") String appType) {
        try {
            if (articleId < 1) {
                return buildErrorParamMView();
            }
            if(accountId != null){
                accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
            }
            L.info("articleId:{},accountId:{}",articleId,accountId);
            return buildSuccessMView(cmsDoctorService.detail(articleId,accountId,appType));
        } catch (Exception e) {
            L.info("查询资讯详情异常{}", articleId, e);
            return buildExceptionMView();
        }
    }

    /**
     * 查询资讯信息评论
     */
    @ResponseBody
    @RequestMapping(value = "detailArticleComment", method = RequestMethod.POST)
    public String detailComment(@RequestParam(value = "articleId", required = true) Long articleId,@RequestParam(value = "accountId", required = false) Long accountId,@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        try {
            if (articleId < 1) {
                return buildErrorParamMView();
            }
            if(accountId != null){
                accountId = Long.valueOf(rsaUtil.decryptValue(accountId.toString()));
            }
            return buildSuccessMView(cmsDoctorService.detailComment(articleId,accountId, pageNo, pageSize));
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
            if(commentSaveForm.getAccountId() !=null){
                commentSaveForm.setAccountId(Long.valueOf(rsaUtil.decryptValue(commentSaveForm.getAccountId().toString())));
            }
            return buildSuccessMView(cmsDoctorService.save(commentSaveForm,appType));
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
    public String catalog(@RequestParam(value = "positionType",required = false, defaultValue="1") String positionType) {
        try {
            return buildSuccessMView(cmsDoctorService.listCatalog(positionType));
        } catch (Exception e) {
            L.info(" 查询资讯分类异常{}", positionType, e);
            return buildExceptionMView();
        }
    }

}
