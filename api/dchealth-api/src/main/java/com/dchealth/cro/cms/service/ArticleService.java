package com.dchealth.cro.cms.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.dchealth.cro.account.domain.SysUser;
import com.dchealth.cro.account.mapper.SysUserMapper;
import com.dchealth.cro.cms.domain.CmsArticle;
import com.dchealth.cro.cms.domain.CmsCategory;
import com.dchealth.cro.cms.domain.CmsCollection;
import com.dchealth.cro.cms.domain.CmsComment;
import com.dchealth.cro.cms.domain.CmsCommentBlank;
import com.dchealth.cro.cms.domain.CmsStat;
import com.dchealth.cro.cms.form.ActicleListSearchForm;
import com.dchealth.cro.cms.form.CollectionActicleSearchForm;
import com.dchealth.cro.cms.form.CommentSaveForm;
import com.dchealth.cro.cms.mapper.CmsArticleMapper;
import com.dchealth.cro.cms.mapper.CmsCategoryMapper;
import com.dchealth.cro.cms.mapper.CmsCollectionMapper;
import com.dchealth.cro.cms.mapper.CmsCommentBlankMapper;
import com.dchealth.cro.cms.mapper.CmsCommentMapper;
import com.dchealth.cro.cms.mapper.CmsStatMapper;
import com.dchealth.cro.cms.utils.WordFilter;
import com.dchealth.cro.cms.vo.ArticleDetailVO;
import com.dchealth.cro.cms.vo.ArticleListVO;
import com.dchealth.cro.cms.vo.ArticleRelationVO;
import com.dchealth.cro.cms.vo.ArticleVO;
import com.dchealth.cro.cms.vo.CmsCategoryVO;
import com.dchealth.cro.cms.vo.CommentListVO;
import com.dchealth.cro.cms.vo.QuestionnaireVO;
import com.dchealth.cro.common.base.CommonResult;
import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.constant.Constants;
import com.dchealth.cro.common.constant.CroApiCodeConstant;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.form.domain.FMedicalExamItem;
import com.dchealth.cro.form.domain.PatientExamRecord;
import com.dchealth.cro.form.mapper.FMedicalExamItemMapper;
import com.dchealth.cro.form.mapper.PatientExamRecordMapper;
import com.dchealth.cro.health.domain.MpPerson;
import com.dchealth.cro.health.mapper.MpPersonMapper;
import com.dchealth.cro.medical.domain.PatientInfo;
import com.dchealth.cro.medical.mapper.PatientInfoMapper;
import com.dchealth.cro.research.domain.ResearchInfo;
import com.dchealth.cro.research.domain.ResearchInfoArticlePatient;
import com.dchealth.cro.research.mapper.ResearchInfoArticlePatientMapper;
import com.dchealth.cro.research.mapper.ResearchInfoMapper;

@Service
public class ArticleService {
    private static final Logger L = LoggerFactory.getLogger(ArticleService.class);
    @Autowired
    private CmsArticleMapper cmsArticleMapper;
    @Autowired
    private CmsStatMapper cmsStatMapper;
    @Autowired
    private CmsCollectionMapper cmsCollectionMapper;
    @Autowired
    private CmsCommentMapper cmsCommentMapper;
    @Autowired
    private CmsCategoryMapper cmsCategoryMapper;
    @Autowired
    private CmsCommentBlankMapper cmsCommentBlankMapper;

    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;
    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private MpPersonMapper mpPersonMapper;
    @Autowired
    private ResearchInfoArticlePatientMapper researchInfoArticlePatientMapper;

    @Autowired
    private  ResearchInfoMapper researchInfoMapper;
    
	@Autowired
	private CroCoreConfig croCoreConfig;
	
	@Autowired
	private PatientInfoMapper patientInfoMapper;
    /**
     * 查询资讯分类
     *
     * @param channel
     * @return
     */
    public DchealthCloudResponseEntity<List<CmsCategoryVO>> listCatalog(String channel, String positionType) {
        DchealthCloudResponseEntity<List<CmsCategoryVO>> result =
                new DchealthCloudResponseEntity<List<CmsCategoryVO>>();
        //
        CmsCategory cmsCategorySearch = new CmsCategory();
        cmsCategorySearch.setTenantId(channel);
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

    /**
     * 综合查询资讯列表
     *
     * @param acticleListSearchForm
     * @return
     */
    public DchealthCloudResponseEntity<List<ArticleListVO>> list(ActicleListSearchForm acticleListSearchForm) {
        DchealthCloudResponseEntity<List<ArticleListVO>> result =
                new DchealthCloudResponseEntity<List<ArticleListVO>>();
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
     * 查询用户收藏信息
     *
     * @param collectionActicleSearchForm
     * @return
     */
    public DchealthCloudResponseEntity<List<ArticleListVO>> collenctionList(
            CollectionActicleSearchForm collectionActicleSearchForm) {
        DchealthCloudResponseEntity<List<ArticleListVO>> result =
                new DchealthCloudResponseEntity<List<ArticleListVO>>();
        List<ArticleListVO> list = cmsArticleMapper.selectCollectArticleList(collectionActicleSearchForm);
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
     *
     * @param articleId
     * @return
     */
    public DchealthCloudResponseEntity<ArticleDetailVO> detail(Long articleId, Long accountId, String appType) {
        DchealthCloudResponseEntity<ArticleDetailVO> result = new DchealthCloudResponseEntity<ArticleDetailVO>();
        try {
            CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(articleId);
            CmsStat cmsStat = cmsStatMapper.selectCmsStatById(articleId);
            if (cmsArticle == null) {
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
            articleDetailVO.setForwardLink(cmsArticle.getForwardLink());
            articleDetailVO.setUploadType(cmsArticle.getUploadType());
            if (!Objects.isNull(cmsStat)) {
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
            if (!Objects.isNull(cmsStat)) {
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
     * 查询资讯详情
     *
     * @param articleId
     * @return
     */
    public DchealthCloudResponseEntity<String> detailArticle(Long articleId) {
        DchealthCloudResponseEntity<String> result = new DchealthCloudResponseEntity<String>();
        try {
            CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(articleId);
            if (cmsArticle == null) {
                result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
                return result;
            }
            result.setData(cmsArticle.getContent());
            return result;
        } catch (Exception e) {
            L.error("查询资讯详情异常{}", articleId, e);
            result.setCode(CroApiCodeConstant.SYSTEM_ERROR.getCode());
            return result;
        }
    }

    @Transactional
    public DchealthCloudResponseEntity<CommonResult> update(Long accountId, Long articleId, String tenantId,
                                                            String positionType) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(articleId);
        CmsStat cmsStat = cmsStatMapper.selectCmsStatById(articleId);
        if (cmsArticle == null || cmsStat == null) {
            result.setCode(CroApiCodeConstant.ARTICLE_NOT_EXIST.getCode());
            return result;
        }
        CommonResult commonResult = new CommonResult();
        CmsCollection cmsCollectionSearch = new CmsCollection();
        cmsCollectionSearch.setAccountId(accountId);
        cmsCollectionSearch.setArticleId(articleId);
        List<CmsCollection> list = cmsCollectionMapper.selectCmsCollectionList(cmsCollectionSearch);

        if (list == null || list.size() == 0) {
            CmsCollection cmsCollection = new CmsCollection();
            cmsCollection.setAccountId(accountId);
            cmsCollection.setTenantId(tenantId);
            cmsCollection.setArticleId(articleId);
            cmsCollection.setCreateTime(DateUtils.getNowDate());
            int rs = cmsCollectionMapper.insertCmsCollection(cmsCollection);
            if (rs == 1) {
                Integer collectNum = cmsStat.getCollectNum() + 1;
                cmsStat.setCollectNum(collectNum);
                cmsStatMapper.updateCmsStat(cmsStat);
                commonResult.setResult(true);
                result.setData(commonResult);
                return result;
            }
        } else {
            for (CmsCollection cmsCollection : list) {
                int rs = cmsCollectionMapper.deleteCmsCollectionById(cmsCollection.getId());
                if (rs == 1) {
                    Integer collectNum = cmsStat.getCollectNum() - 1;
                    if (collectNum >= 0) {
                        cmsStat.setCollectNum(collectNum);
                        cmsStatMapper.updateCmsStat(cmsStat);
                        commonResult.setResult(true);
                        result.setData(commonResult);
                        return result;
                    }
                }
            }
        }
        commonResult.setResult(false);
        result.setData(commonResult);
        return result;
    }

    /**
     * 查询评论列表
     *
     * @param articleId
     * @return
     */
    public DchealthCloudResponseEntity<List<CommentListVO>> detailComment(Long articleId, Long accountId,
                                                                          Integer pageNo,
                                                                          Integer pageSize) {
        DchealthCloudResponseEntity<List<CommentListVO>> result =
                new DchealthCloudResponseEntity<List<CommentListVO>>();
        List<CommentListVO> list = cmsCommentMapper.selectCommentListByArticleId(articleId, pageNo, pageSize);
        List<CommentListVO> comments = new ArrayList<CommentListVO>();
        if (list != null && list.size() > 0) {
            for (CommentListVO commentListVO : list) {
                String appType = commentListVO.getAppType();
                Long accountId1 = commentListVO.getAccountId();
                if ("1".equals(appType)) {
                    SysUser sysUser = sysUserMapper.selectSysUserByUserId(accountId1);
                    if (sysUser != null) {
                        commentListVO.setRealName(sysUser.getUserName());
                        commentListVO.setUserHead(sysUser.getAvatar());
                    }

                } else if ("2".equals(appType)) {
                	MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(accountId1);
                    if (mpPerson != null) {
                        commentListVO.setRealName(mpPerson.getPersonName());
                        commentListVO.setUserHead(croCoreConfig.getUserHead());
                    }
                }
                String showTime = DateUtils.getShowTime(commentListVO.getCreateTime());
                commentListVO.setShowTime(showTime);
                commentListVO.setCreateTime(null);
                if (accountId != null && accountId > 0) {
                    CmsCommentBlank cmsCommentBlankSearch = new CmsCommentBlank();
                    cmsCommentBlankSearch.setAccountId(accountId);
                    cmsCommentBlankSearch.setCommentId(commentListVO.getCommentId());
                    List<CmsCommentBlank> cmsCommentList =
                            cmsCommentBlankMapper.selectCmsCommentBlankList(cmsCommentBlankSearch);
                    if (cmsCommentList != null && cmsCommentList.size() > 0) {
                        commentListVO.setShow(false);
                        for (CmsCommentBlank cmsCommentBlank : cmsCommentList) {
                            String appType1 = cmsCommentBlank.getAppType();
                            Long accountId2 = cmsCommentBlank.getAccountId();
                            if ("1".equals(appType1)) {
                                SysUser sysUser = sysUserMapper.selectSysUserByUserId(accountId2);
                                if (sysUser != null) {
                                    commentListVO.setRealName(sysUser.getUserName());
                                    commentListVO.setUserHead(sysUser.getAvatar());
                                }

                            } else if ("2".equals(appType1)) {
                            	MpPerson mpPerson = mpPersonMapper.selectPersonByAccountId(accountId1);
                                if (mpPerson != null) {
                                    commentListVO.setRealName(mpPerson.getPersonName());
                                    commentListVO.setUserHead(croCoreConfig.getUserHead());
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


    public DchealthCloudResponseEntity<ArticleVO> latestPatientArticle(ResearchInfoArticlePatient researchInfoArticlePatient) {
        DchealthCloudResponseEntity<ArticleVO> result = new DchealthCloudResponseEntity<ArticleVO>();
        //对于没有关联队列的患者直接返回null，这里的查询和患者基础信息接口保持一致
        List<PatientInfo> patientInfos =
                patientInfoMapper.selectPatientInfoByAccountIdEnrollment(researchInfoArticlePatient.getAccountIds());
        //已入组的队列
        List<PatientInfo> patientInfoList = patientInfos.stream().filter(x -> "1".equals(x.getEnrollmentStatus())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(patientInfoList)){
            //展示默认资讯
            List<ArticleListVO> articleListVOS = cmsArticleMapper.selectAllDefaultCmsArticlePatientList(researchInfoArticlePatient);
//            List<ArticleListVO> rs = articleListVOS.stream().limit(5).collect(Collectors.toList());
            ArticleVO articleVO = new ArticleVO();
            articleVO.setArticleList(articleListVOS);
            result.setData(articleVO);
            return result;
        }
        //过滤掉已经删除的队列所绑定的患教
        Optional<String> survivalResearchIdsOptional = filterDeadResearch(patientInfoList);
        researchInfoArticlePatient.setSurvivalResearchIds(survivalResearchIdsOptional.get());
        ArticleVO articleVO = new ArticleVO();
        researchInfoArticlePatient.setInfoType("2");//0 无 1 轮播 2首页列表
        List<ArticleListVO> list = cmsArticleMapper.selectAllCmsArticlePatientList(researchInfoArticlePatient);
        researchInfoArticlePatient.setInfoType("1");//0 无 1 轮播 2首页列表
        List<ArticleListVO> bannerTypeList = cmsArticleMapper.selectAllCmsArticlePatientList(researchInfoArticlePatient);

//        List<ArticleListVO> rs = list.stream().limit(5).collect(Collectors.toList());

        CmsCollection cmsCollectionSearch = new CmsCollection();
        cmsCollectionSearch.setAccountId(researchInfoArticlePatient.getAccountIds());
        List<CmsCollection> cmsCollectionList = cmsCollectionMapper.selectCmsCollectionList(cmsCollectionSearch);
        Map<Long,CmsCollection> collectionMap;
        if (cmsCollectionList != null && !cmsCollectionList.isEmpty()) {
            collectionMap = cmsCollectionList.stream().collect(Collectors.toMap(CmsCollection::getArticleId, Function.identity(), (key1, key2) -> key2));
        } else {
            collectionMap = new HashMap<>();
        }

        if (list != null && list.size() > 0) {
            for (ArticleListVO articleListVO : list) {
                String createTime = articleListVO.getCreateTime();
                String rsCreateTime = DateUtils.dateTimeToFormatStr(DateUtils.YYYY_MM_DD, createTime);
                articleListVO.setCreateTime(rsCreateTime);
                if(collectionMap.containsKey(articleListVO.getArticleId())){
                    articleListVO.setIsCollection("Y");
                }
            }
        }
//        List<ArticleListVO> bannerList = bannerTypeList.stream().limit(5).collect(Collectors.toList());
        List<ArticleListVO> collect = bannerTypeList.stream().map(ba ->
        {
            ArticleListVO articleListVO = new ArticleListVO();
            articleListVO.setImageUrl(ba.getImageUrl());
            articleListVO.setLink(ba.getLink());
            articleListVO.setArticleId(ba.getArticleId());
            articleListVO.setTitle(ba.getTitle());
            articleListVO.setDataId(ba.getArticleId());
            articleListVO.setForwardLink(ba.getForwardLink());
            articleListVO.setUploadType(ba.getUploadType());
            if(collectionMap.containsKey(articleListVO.getArticleId())){
                articleListVO.setIsCollection("Y");
            }
            //1：资讯
            articleListVO.setType("1");
            return articleListVO;
        }).collect(Collectors.toList());
        articleVO.setBannerList(collect);
        articleVO.setArticleList(list);
        result.setData(articleVO);
        return result;
    }

    public DchealthCloudResponseEntity<ArticleVO> allPatientArticle(ResearchInfoArticlePatient researchInfoArticlePatient) {
        DchealthCloudResponseEntity<ArticleVO> result = new DchealthCloudResponseEntity<ArticleVO>();
        List<PatientInfo> patientInfos =
        		patientInfoMapper.selectPatientInfoByAccountIdEnrollment(researchInfoArticlePatient.getAccountIds());
        if(CollectionUtils.isEmpty(patientInfos)){
            result.setData(new ArticleVO());
            return result;
        }
        ArticleVO articleVO = new ArticleVO();
        //过滤掉已经删除的队列所绑定的患教
        Optional<String> survivalResearchIdsOptional = filterDeadResearch(patientInfos);
        researchInfoArticlePatient.setSurvivalResearchIds(survivalResearchIdsOptional.get());
        for (PatientInfo patientInfo : patientInfos) {
            if ("1".equals(patientInfo.getEnrollmentStatus())) {
                if (researchInfoArticlePatient.getSurvivalResearchIds().contains(String.valueOf(patientInfo.getResearchId()))) {
                    articleVO.getResearchIdList().add(String.valueOf(patientInfo.getResearchId()));
                }
            }
        }
        //文章处理
        List<ArticleListVO> list = cmsArticleMapper.selectAllCmsArticlePatientList(researchInfoArticlePatient);
        if (list != null && list.size() > 0) {
            for (ArticleListVO articleListVO : list) {
                String createTime = articleListVO.getCreateTime();
                String rsCreateTime = DateUtils.dateTimeToFormatStr(DateUtils.YYYY_MM_DD, createTime);
                articleListVO.setCreateTime(rsCreateTime);
            }
        }
        articleVO.setArticleList(list);
        //获取问卷列表
        List<ArticleListVO> articleListVOList =
                cmsArticleMapper.selectWriteQuestionnaireList(researchInfoArticlePatient);//待填写列表
        if (articleListVOList.size() > 0) {
            articleVO.setQuestionnaireVoList(articleListVOList);
            articleVO.setQuestionnaireNum(articleListVOList.size());
        }
        result.setData(articleVO);
        return result;
    }

    private Optional<String> filterDeadResearch(List<PatientInfo> patientInfos) {
        List<Long> researchIds = patientInfos.stream().filter(patientInfo -> {
            Long researchId =
                    Optional.ofNullable(patientInfo.getResearchId()).<RuntimeException>orElseThrow(() -> new RuntimeException("队列ID为空"));
            ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchId);
            AtomicReference<String> delFlag = new AtomicReference<>("0");
            Optional.ofNullable(researchInfo).ifPresent(research -> {
                delFlag.set(research.getDelFlag());
            });
            return delFlag.get().equals("0");
        }).map(PatientInfo::getResearchId).collect(Collectors.toList());
        //List中的数据转换为以 , 分割的字符串
        return Optional.ofNullable(Optional.of(researchIds.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .<RuntimeException>orElseThrow(() -> new RuntimeException("存活的队列数据异常")));
    }

    public DchealthCloudResponseEntity<QuestionnaireVO> questionnaireList(ResearchInfoArticlePatient researchInfoArticlePatient) {
        DchealthCloudResponseEntity<QuestionnaireVO> result = new DchealthCloudResponseEntity<QuestionnaireVO>();
        List<PatientInfo> patientInfos =
        		patientInfoMapper.selectPatientInfoByAccountIdEnrollment(researchInfoArticlePatient.getAccountIds());
        if(CollectionUtils.isEmpty(patientInfos)){
            return result;
        }
        //过滤掉已经删除的队列所绑定的患教
        Optional<String> survivalResearchIdsOptional = filterDeadResearch(patientInfos);
        String survivalResearchIds = survivalResearchIdsOptional.orElse(null);
        researchInfoArticlePatient.setSurvivalResearchIds(survivalResearchIds);

        //获取问卷列表
        QuestionnaireVO questionnaireVO = new QuestionnaireVO();
        List<ArticleListVO> list = new ArrayList<>();
        List<ArticleListVO> finalArticleListVOS =new ArrayList<>();
        // 0待填写1进行中2已完成
        int questionnaireNum =0;
        if ("0".equals(researchInfoArticlePatient.getFillStatus())) {//待填写
            list = cmsArticleMapper.selectWriteQuestionnaireList(researchInfoArticlePatient);
        } else {
            researchInfoArticlePatient.setPageNo((researchInfoArticlePatient.getPageNo()-1)*researchInfoArticlePatient.getPageSize());
            list = cmsArticleMapper.selectProgressQuestionnaireListByPage(researchInfoArticlePatient);
            questionnaireNum = cmsArticleMapper.selectProgressQuestionnaireCountByPage(researchInfoArticlePatient);
        }
        if ("1".equals(researchInfoArticlePatient.getFillStatus())) {
            if (!CollectionUtils.isEmpty(list)) {
                list = list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                        -> new TreeSet<>(Comparator.comparing(ArticleListVO::getArticleId))), ArrayList::new));
            }
        }
            if (list.size() > 0) {
            for (ArticleListVO articleListVO : list) {
                if ("2".equals(researchInfoArticlePatient.getFillStatus())) {//已完成
                    finalArticleListVOS.add(articleListVO);
                }
                //进行中
                else if ("1".equals(researchInfoArticlePatient.getFillStatus())) {

                    researchInfoArticlePatient.setArticleId(articleListVO.getArticleId());
                    researchInfoArticlePatient.setFillStatus("1");
                    researchInfoArticlePatient.setSurvivalResearchIds(null);
                    List<ResearchInfoArticlePatient> researchInfoArticlePatientList =
                            researchInfoArticlePatientMapper.selectResearchInfoArticlePatientByArticleIdAndAccountId(researchInfoArticlePatient);
                    List<ArticleListVO> tempArticleList =new ArrayList<>();
                    if (!CollectionUtils.isEmpty(researchInfoArticlePatientList)) {

                        researchInfoArticlePatientList.forEach(r -> {
                            articleListVO.setFillStatus(r.getFillStatus());
                            articleListVO.setExamRecordId(r.getExamRecordId());
                            articleListVO.setIsFillAgain(r.getIsFillAgain());
                            ArticleListVO articleListVO1 = new ArticleListVO();
                            BeanUtils.copyProperties(articleListVO, articleListVO1);
                            tempArticleList.add(articleListVO1);
                        });

                    }
                    finalArticleListVOS.addAll(tempArticleList);
                } else if ("0".equals(researchInfoArticlePatient.getFillStatus())) {
                    // 待处理
                    articleListVO.setFillStatus("0");
                    articleListVO.setIsFillAgain(false);
                    finalArticleListVOS.add(articleListVO);
                }
            }
        }
        List<ArticleListVO> collect = new ArrayList<>();
        if("2".equals(researchInfoArticlePatient.getFillStatus())){
            collect = finalArticleListVOS;
            questionnaireVO.setQuestionnaireNum(questionnaireNum);
        }else{
            collect = orderArticleList(researchInfoArticlePatient, finalArticleListVOS);
            questionnaireVO.setQuestionnaireNum(finalArticleListVOS.size());
        }
        questionnaireVO.setQuestionnaireVoList(collect);

        result.setData(questionnaireVO);
        return result;
    }


    private static List<ArticleListVO> orderArticleList(ResearchInfoArticlePatient researchInfoArticlePatient,
                                                        List<ArticleListVO> finalArticleListVOS) {
        List<ArticleListVO> collect;
        if (Integer.parseInt(researchInfoArticlePatient.getFillStatus()) > 0) {
            // 按问卷ID倒序排序
            collect =
                    finalArticleListVOS.stream().sorted(Comparator.comparing(ArticleListVO::getExamRecordId).reversed()).collect(Collectors.toList());
        } else {
            collect =
                    finalArticleListVOS.stream().sorted(Comparator.comparing(ArticleListVO::getArticleId).reversed()).collect(Collectors.toList());
        }
        return collect;
    }

  

    public DchealthCloudResponseEntity<QuestionnaireVO> questionnaireList1(ResearchInfoArticlePatient researchInfoArticlePatient) {
        DchealthCloudResponseEntity<QuestionnaireVO> result = new DchealthCloudResponseEntity<QuestionnaireVO>();
        //获取问卷列表
        QuestionnaireVO questionnaireVO = new QuestionnaireVO();
        List<ArticleListVO> list = cmsArticleMapper.selectAllQuestionnaireList(researchInfoArticlePatient);
        if (list.size() > 0) {
            for (ArticleListVO articleListVO : list
            ) {
                PatientExamRecord patientExamRecord =
                        patientExamRecordMapper.selectPatientExamRecordByArticleId(articleListVO.getArticleId());
                if (null != patientExamRecord) {
                    FMedicalExamItem item = new FMedicalExamItem();
                    item.setTableName(patientExamRecord.getExamTable());
                    item.setMedicalFormId(patientExamRecord.getMedicalFormId());
                    List<FMedicalExamItem> fMedicalExamItems =
                            fMedicalExamItemMapper.selectFMedicalExamItemScoreList(item);
                    Long sum = fMedicalExamItems.stream().mapToLong(a -> a.getItemScores()).sum();
                    articleListVO.setScore(sum);
                }
            }
        }
        questionnaireVO.setQuestionnaireVoList(list);
        questionnaireVO.setQuestionnaireNum(list.size());
        result.setData(questionnaireVO);
        return result;
    }

    public DchealthCloudResponseEntity<QuestionnaireVO> ownQuestionnaire(ResearchInfoArticlePatient researchInfoArticlePatient) {
        DchealthCloudResponseEntity<QuestionnaireVO> result = new DchealthCloudResponseEntity<QuestionnaireVO>();
        //获取问卷列表
        QuestionnaireVO questionnaireVO = new QuestionnaireVO();
        List<ArticleListVO> list = cmsArticleMapper.selectOwnQuestionnaireList(researchInfoArticlePatient);
        questionnaireVO.setQuestionnaireVoList(list);
        questionnaireVO.setQuestionnaireNum(list.size());
        result.setData(questionnaireVO);
        return result;
    }


    /**
     * 保存评论
     *
     * @param commentSaveForm
     * @param positionType
     * @return
     */
    @Transactional
    public DchealthCloudResponseEntity<CommonResult> save(CommentSaveForm commentSaveForm, String positionType) {
        DchealthCloudResponseEntity<CommonResult> result = new DchealthCloudResponseEntity<CommonResult>();
        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(commentSaveForm.getArticleId());
        if (commentSaveForm.getAccountIds() == null || commentSaveForm.getAccountIds() < 1) {
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
        if (judge != null) {
            for (String word : judge) {
                if (StringUtils.isNotNullOrEmpty(word)) {
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
}
