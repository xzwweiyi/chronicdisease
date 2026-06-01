package com.dchealth.cro.cms.service;

import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.account.domain.ArticleRequest;
import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.base.mapper.AppBannerMapper;
import com.dchealth.cro.base.mapper.AppDoctorMapper;
import com.dchealth.cro.base.vo.AppDoctorIndexVO;
import com.dchealth.cro.cms.domain.*;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.mapper.*;
import com.dchealth.cro.cms.utils.WordFilter;
import com.dchealth.cro.cms.vo.*;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.PageUtil;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CmsDoctorService {
    private static final Logger L = LoggerFactory.getLogger(CmsDoctorService.class);

    @Autowired
    private AppBannerMapper appBannerMapper;

    @Autowired
    private AppDoctorMapper appDoctorMapper;

    @Autowired
    private CmsArticleMapper cmsArticleMapper;

    @Autowired
    private CmsStatMapper cmsStatMapper;

    @Autowired
    private CmsCollectionMapper cmsCollectionMapper;

    @Autowired
    private CmsCommentMapper cmsCommentMapper;

    @Autowired
    private CmsCommentBlankMapper cmsCommentBlankMapper;

    @Autowired
    private CmsCategoryMapper cmsCategoryMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    /**
     * 查询医生端APP首页轮播
     *
     * @return
     */
    public DchealthCloudResponseEntity<AppDoctorIndexVO> findAppIndex(ArticleRequest articleRequest) {
        DchealthCloudResponseEntity<AppDoctorIndexVO> result = new DchealthCloudResponseEntity<AppDoctorIndexVO>();
        DchealthCloudResponseEntity<AppDoctorIndexVO> result1 = verifyParameter(articleRequest.getAccountId(),
                articleRequest.getPageSize(),
                articleRequest.getPageNo(), result);
        if (result1 != null) return result1;
        AppDoctorIndexVO appDoctorIndexVO = new AppDoctorIndexVO();
        try {
            //通过accountId查询出对应的医生所在的部门ID
            Long deptId;
            deptId = sysUserMapper.getDeptIdsByUserId(articleRequest.getAccountId());
            appDoctorIndexVO.setBanners(appBannerMapper.selectAppBannerByDeptId(deptId,"1", 20, articleRequest.getPositionType()));
            if (Objects.isNull(deptId)) {
                result.setMsg(CroApiCodeConstant.THE_DEPARTMENT_WHERE_THE_ACCOUNT_IS_LOCATED_IS_EMPTY.getMsg());
                result.setCode(CroApiCodeConstant.THE_DEPARTMENT_WHERE_THE_ACCOUNT_IS_LOCATED_IS_EMPTY.getCode());
                result.setData(null);
                return result;
            }

            List<ArticleListVO> articles = appDoctorMapper.selectMobileArticlePageInfo(deptId,
                    articleRequest.getPageNo(),
                    articleRequest.getPageSize(),
                    articleRequest.getPositionType());
            appDoctorIndexVO.setArticles(articles);
            result.setData(appDoctorIndexVO);
            L.info("---------------医生首页接口返回参数-----------:{}", JSON.toJSONString(appDoctorIndexVO));
        } catch (Exception e) {
            L.error("查询APP首页异常:{}", e);
            result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
            return result;
        }
        return result;
    }

    private static DchealthCloudResponseEntity<AppDoctorIndexVO> verifyParameter(Long accountId, Long pageSize,
                                                                                 Long pageNo,
                                                                                 DchealthCloudResponseEntity<AppDoctorIndexVO> result) {
        if (!PageUtil.verifyPage(pageNo, pageSize)) {
            result.setMsg(CroApiCodeConstant.PAGING_PARAMETER_EXCEPTION.getMsg());
            result.setCode(CroApiCodeConstant.PAGING_PARAMETER_EXCEPTION.getCode());
            result.setData(null);
            return result;
        }
        //增加判断账号为空
        if (Objects.isNull(accountId)) {
            result.setMsg(CroApiCodeConstant.ACCOUNT_CANNOT_BE_EMPTY.getMsg());
            result.setCode(CroApiCodeConstant.ACCOUNT_CANNOT_BE_EMPTY.getCode());
            result.setData(null);
            return result;
        }
        return null;
    }

    public DchealthCloudResponseEntity<List<ArticleListVO>> list(ActicleListSearchForm acticleListSearchForm) {
        DchealthCloudResponseEntity<List<ArticleListVO>> result = new DchealthCloudResponseEntity<List<ArticleListVO>>();
        List<ArticleListVO> list = cmsArticleMapper.selectCmsArticleList(acticleListSearchForm);
        List<ArticleListVO> rs = new ArrayList<ArticleListVO>();
        if (list != null && list.size() > 0) {
            for (ArticleListVO articleListVO : list) {
                String createTime = articleListVO.getCreateTime();
                String rsCreateTime = DateUtils.dateTimeToFormatStr(DateUtils.YYYY_MM_DD, createTime);
                articleListVO.setCreateTime(rsCreateTime);
                rs.add(articleListVO);
            }
        }
        result.setData(list);
        return result;

    }

    /**
     * 查询资讯详情
     */
    public DchealthCloudResponseEntity<ArticleDetailVO> detail(Long articleId, Long accountId, String appType) {
        DchealthCloudResponseEntity<ArticleDetailVO> result = new DchealthCloudResponseEntity<ArticleDetailVO>();
        try {
            CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(articleId);
            CmsStat cmsStat = cmsStatMapper.selectCmsStatById(articleId);
            if (cmsArticle == null ) {
                result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
                return result;
            }
            //查询详情时，若统计表无数据
            if(cmsStat == null){
                cmsStat = new CmsStat();
                cmsStat.setId(articleId);
                cmsStat.setViewNum(0);
                cmsStat.setCollectNum(0);
                cmsStat.setCommentNum(0);
                cmsStatMapper.insertCmsStat(cmsStat);
            }
            ArticleDetailVO articleDetailVO = new ArticleDetailVO();
            articleDetailVO.setArticleId(cmsArticle.getId());
            articleDetailVO.setContent(cmsArticle.getContent());
            articleDetailVO.setCopyFrom(cmsArticle.getCopyFrom());
            articleDetailVO.setCreateTime(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD, cmsArticle.getCreateTime()));
            articleDetailVO.setImageUrl(cmsArticle.getImage());
            articleDetailVO.setTitle(cmsArticle.getTitle());
            articleDetailVO.setVideoImage(cmsArticle.getVideoImage());
            articleDetailVO.setVideoUrl(cmsArticle.getVideoUrl());
            if(!Objects.isNull(cmsStat)){
                articleDetailVO.setViewNum(cmsStat.getViewNum());
                articleDetailVO.setCommentNum(cmsStat.getCommentNum());
            }
            String relation = cmsArticle.getRelation();
            if (StringUtils.isNotNullOrEmpty(relation)) {
                String[] relationArr = relation.split(",");
                if (relationArr != null && relationArr.length > 0) {
                    List<ArticleRelationVO> list = new ArrayList<ArticleRelationVO>();
                    for (String relationId : relationArr) {
                        CmsArticle relationCmsArticle = cmsArticleMapper
                                .selectArticleTitleById(Long.valueOf(relationId));
                        if (relationCmsArticle != null) {
                            ArticleRelationVO articleRelationVO = new ArticleRelationVO();
                            articleRelationVO.setArticleId(relationCmsArticle.getId());
                            articleRelationVO.setTitle(relationCmsArticle.getTitle());
                            list.add(articleRelationVO);
                        }

                    }
                    articleDetailVO.setRelationArticles(list);
                }
            }
            if(!Objects.isNull(cmsStat)){
                Integer viewNum = cmsStat.getViewNum() + 1;
                cmsStat.setViewNum(viewNum);
                cmsStatMapper.updateCmsStat(cmsStat);
            }

            if (accountId != null) {
                CmsCollection cmsCollectionSearch = new CmsCollection();
                cmsCollectionSearch.setAccountId(accountId);
                cmsCollectionSearch.setArticleId(articleId);
                List<CmsCollection> list = cmsCollectionMapper.selectCmsCollectionList(cmsCollectionSearch);
                if (list != null && list.size() > 0) {
                    articleDetailVO.setIsCollection("Y");
                }
            }
            result.setData(articleDetailVO);
            return result;
        } catch (Exception e) {
            L.error("查询资讯详情异常{}", articleId, e);
            result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
            return result;
        }
    }

    /**
     * 查询评论列表
     *
     * @param articleId
     * @return
     */
    public DchealthCloudResponseEntity<List<CommentListVO>> detailComment(Long articleId, Long accountId , Integer pageNo,
                                                                          Integer pageSize) {
        DchealthCloudResponseEntity<List<CommentListVO>> result = new DchealthCloudResponseEntity<List<CommentListVO>>();
        List<CommentListVO> list = cmsCommentMapper.selectCommentListByArticleId(articleId, pageNo, pageSize);
        List<CommentListVO> comments = new ArrayList<CommentListVO>();
        if (list != null && list.size() > 0) {
            for (CommentListVO commentListVO : list) {
                String appType = commentListVO.getAppType();
                Long accountId1 = commentListVO.getAccountId();
                if ("1".equals(appType)){
                    SysUser sysUser = sysUserMapper.selectSysUserByUserId(accountId1);
                    if (sysUser !=null){
                        commentListVO.setRealName(sysUser.getUserName());
                        commentListVO.setUserHead(sysUser.getAvatar());
                    }

                }else if ("2".equals(appType)){
                    PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(accountId1);
                    if (patientInfo != null ){
                        commentListVO.setRealName(patientInfo.getPatientName());
                        commentListVO.setUserHead(null);
                    }
                }
                String showTime = DateUtils.getShowTime(commentListVO.getCreateTime());
                commentListVO.setShowTime(showTime);
                commentListVO.setCreateTime(null);
                if(accountId != null && accountId > 0) {
                    CmsCommentBlank cmsCommentBlankSearch = new CmsCommentBlank();
                    cmsCommentBlankSearch.setAccountId(accountId);
                    cmsCommentBlankSearch.setCommentId(commentListVO.getCommentId());
                    List<CmsCommentBlank> cmsCommentList = 	cmsCommentBlankMapper.selectCmsCommentBlankList(cmsCommentBlankSearch);
                    if(cmsCommentList != null && cmsCommentList.size() > 0) {
                        commentListVO.setShow(false);
                        for (CmsCommentBlank cmsCommentBlank : cmsCommentList) {
                            String appType1 = cmsCommentBlank.getAppType();
                            Long accountId2 = cmsCommentBlank.getAccountId();
                            if ("1".equals(appType1)){
                                SysUser sysUser = sysUserMapper.selectSysUserByUserId(accountId2);
                                if (sysUser !=null){
                                    commentListVO.setRealName(sysUser.getUserName());
                                    commentListVO.setUserHead(sysUser.getAvatar());
                                }

                            }else if ("2".equals(appType1)){
                                PatientInfo patientInfo = patientInfoMapper.selectPatientInfoById(accountId2);
                                if (patientInfo != null ){
                                    commentListVO.setRealName(patientInfo.getPatientName());
                                    commentListVO.setUserHead(null);
                                }
                            }
                        }
                    }
                }
                comments.add(commentListVO);
            }
        }
        result.setData(comments);
        return result;
    }


    /**
     * 保存评论
     * @param commentSaveForm
     * @param positionType
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<CommonResult> save(CommentSaveForm commentSaveForm, String positionType) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(commentSaveForm.getArticleId());
        if (commentSaveForm.getAccountId() == null || commentSaveForm.getAccountIds() <1 ){
            result.setCode(CroApiCodeConstant.USER_NOT_LOGIN.getCode());
            return result;
        }
        if (cmsArticle == null) {
            result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
            return result;
        }

        if (Constants.NO.equals(cmsArticle.getAllowComment())) {
            result.setCode(CroApiCodeConstant.ARTICLE_NOT_ALLOWCOMMENT.getCode());
            return result;
        }
        CmsStat cmsStat = cmsStatMapper.selectCmsStatById(commentSaveForm.getArticleId());
        if (cmsStat == null) {
            result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
            return result;
        }
        WordFilter filter = new WordFilter("/sensitivewords/sms.dic");
        Set<String> judge = filter.judge(commentSaveForm.getContent());
        if(judge != null ) {
            for (String word : judge) {
                if(StringUtils.isNotNullOrEmpty(word)) {
                    result.setCode(CroApiCodeConstant.LLLEGAL_CONTENT.getCode());
                    return result;
                }
            }
        }
        CmsComment cmsComment = new CmsComment();
        cmsComment.setAccountId(commentSaveForm.getAccountIds());
        cmsComment.setArticleId(commentSaveForm.getArticleId());
        cmsComment.setContent(commentSaveForm.getContent());
        cmsComment.setCreateTime(DateUtils.getNowDate());
        cmsComment.setDelFlag(Constants.NO);
        cmsComment.setTenantId(commentSaveForm.getTenantId());
        cmsComment.setAppType(commentSaveForm.getAppType());
        int rs = cmsCommentMapper.insertCmsComment(cmsComment);
        CommonResult commonResult = new CommonResult();
        if (rs == 1) {
            int commentNum = cmsStat.getCommentNum() + 1;
            cmsStat.setCommentNum(commentNum);
            cmsStatMapper.updateCmsStat(cmsStat);
            commonResult.setResult(true);
        } else {
            commonResult.setResult(false);
        }
        result.setData(commonResult);
        return result;
    }


    /**
     * 查询资讯分类
     * @param channel
     * @return
     */
    public DchealthCloudResponseEntity<List<CmsCategoryVO>> listCatalog(String positionType) {
        DchealthCloudResponseEntity<List<CmsCategoryVO>> result = new DchealthCloudResponseEntity<List<CmsCategoryVO>>();
        CmsCategory cmsCategorySearch = new CmsCategory();
        cmsCategorySearch.setTenantId("1");
        cmsCategorySearch.setPositionType(positionType);
        List<CmsCategory> list = cmsCategoryMapper.selectCmsCategoryList(cmsCategorySearch);
        List<CmsCategoryVO> catelogs = new ArrayList<CmsCategoryVO>();
        if (list != null && list.size() > 0) {
            for (CmsCategory cmsCategory : list) {
                CmsCategoryVO cmsCategoryVO = new CmsCategoryVO();
                cmsCategoryVO.setCataLogId(cmsCategory.getId());
                cmsCategoryVO.setCatalogName(cmsCategory.getName());
                cmsCategoryVO.setOrderNum(String.valueOf(cmsCategory.getSort()));
                catelogs.add(cmsCategoryVO);
            }
        }
        result.setData(catelogs);
        return result;
    }
}
