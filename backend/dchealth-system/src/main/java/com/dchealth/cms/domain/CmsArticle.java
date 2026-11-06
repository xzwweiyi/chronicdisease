package com.dchealth.cms.domain;

import java.util.List;
import java.util.Map;

import com.dchealth.common.annotation.Excel;
import com.dchealth.common.core.domain.BaseEntity;

import com.dchealth.template.domain.FMedicalExamForm;


/**
 * 【请填写功能名称】对象 cms_article
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
//@Data
//@EqualsAndHashCode(callSuper=false)
public class CmsArticle extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long id;

    private Long articleId;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

    /** 栏目编号 */
    @Excel(name = "栏目编号")
    private Long categoryId;

    @Excel(name = "分类名称")
    private String categoryName;

    /** 宣教分类编号 */
    @Excel(name = "宣教分类编号")
    private Long eduCategoryId;

    /** 宣教分类名称 */
    @Excel(name = "宣教分类名称")
    private String eduCategoryName;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 文章链接 */
    @Excel(name = "文章链接")
    private String link;

    //上传方式1本地上传2转发3云视频
    private String uploadType;
    //转发路径
    private String forwardLink;

    /** 视频路径 */
    @Excel(name = "视频路径")
    private String videoUrl;

    /** 视频显示图 */
    @Excel(name = "视频显示图")
    private String videoImage;

    /** 文章图片 */
    @Excel(name = "文章图片")
    private String image;

    /** 关键字 */
    @Excel(name = "关键字")
    private String keywords;

    /** 描述、摘要 */
    @Excel(name = "描述、摘要")
    private String description;

    /** 列表排序 */
    @Excel(name = "列表排序")
    private Long listOrderNum;

    /** 首页list显示 */
    @Excel(name = "首页list显示")
    private String listShow;

    /** 显示类型 1 医生端 2 患者端 */
    @Excel(name = "显示类型 1 医生端 2 患者端 ")
    private String positionType;

    /** 删除标记 */
    private String delFlag;

    /** 文章内容 */
    private String content;

    /** 文章来源 */
    private String copyFrom;

    /** 相关文章 */
    private String relation;

    /** 是否允许评论 */
    private String allowComment;

    private String tenantName;
  
    /** 请求参数 */
    private Map<String, Object> params;

    /** 科室id */
    private Long deptId;

    //科室id集合
    private String deptIds;

    /** 病种id */
    private Long diseaseId;

    /** 病种名称 */
    private String diseaseName;

    /** 队列id */
    private Long researchId;

    /** 类型（1-文章，2-问卷） */
    private String type;

    /** 是否发布（问卷专用：0-未发布，1-已发布） */
    private String articleStatus;

    /** 问卷排序值 */
    private Integer quesNum;

    /** 问卷模板信息 */

    private FMedicalExamForm fmedicalExamForm;

    private Long createId;

    private String articleType;

    private Long parentId;

    /** 1 是公共库  0否**/
    private Integer commonFlag;

    /** 0 未加入 1 已加入  2不需要显示**/
    private Integer isJoin;

    /**
     * 1 展示移除 0不展示
     *
     */
    private Integer isShowRemove;


    /**
     * 首页类型
     */
    private String indexType;

    /**
     * 是否展示轮播 （1展示 0 不展示）
     */
    private String isShow;

    private String scoresTotalVal;


    private String updateTimeStr;

    private String createTimeStr;


    private List<Long> categoryIdList;

    private List<Long> deptCategoryIdList;

    public List<Long> getCategoryIdList() {
        return categoryIdList;
    }

    public void setCategoryIdList(List<Long> categoryIdList) {
        this.categoryIdList = categoryIdList;
    }

    public List<Long> getDeptCategoryIdList() {
        return deptCategoryIdList;
    }

    public void setDeptCategoryIdList(List<Long> deptCategoryIdList) {
        this.deptCategoryIdList = deptCategoryIdList;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getEduCategoryId() {
        return eduCategoryId;
    }

    public void setEduCategoryId(Long eduCategoryId) {
        this.eduCategoryId = eduCategoryId;
    }

    public String getEduCategoryName() {
        return eduCategoryName;
    }

    public void setEduCategoryName(String eduCategoryName) {
        this.eduCategoryName = eduCategoryName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getForwardLink() {
        return forwardLink;
    }

    public void setForwardLink(String forwardLink) {
        this.forwardLink = forwardLink;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getListOrderNum() {
        return listOrderNum;
    }

    public void setListOrderNum(Long listOrderNum) {
        this.listOrderNum = listOrderNum;
    }

    public String getListShow() {
        return listShow;
    }

    public void setListShow(String listShow) {
        this.listShow = listShow;
    }

    public String getPositionType() {
        return positionType;
    }

    public void setPositionType(String positionType) {
        this.positionType = positionType;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCopyFrom() {
        return copyFrom;
    }

    public void setCopyFrom(String copyFrom) {
        this.copyFrom = copyFrom;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAllowComment() {
        return allowComment;
    }

    public void setAllowComment(String allowComment) {
        this.allowComment = allowComment;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public Map<String, Object> getParams() {
        return params;
    }

    @Override
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptIds() {
        return deptIds;
    }

    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }

    public Long getDiseaseId() {
        return diseaseId;
    }

    public void setDiseaseId(Long diseaseId) {
        this.diseaseId = diseaseId;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public Long getResearchId() {
        return researchId;
    }

    public void setResearchId(Long researchId) {
        this.researchId = researchId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArticleStatus() {
        return articleStatus;
    }

    public void setArticleStatus(String articleStatus) {
        this.articleStatus = articleStatus;
    }

    public Integer getQuesNum() {
        return quesNum;
    }

    public void setQuesNum(Integer quesNum) {
        this.quesNum = quesNum;
    }

    public FMedicalExamForm getFmedicalExamForm() {
        return fmedicalExamForm;
    }

    public void setFmedicalExamForm(FMedicalExamForm fmedicalExamForm) {
        this.fmedicalExamForm = fmedicalExamForm;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getCommonFlag() {
        return commonFlag;
    }

    public void setCommonFlag(Integer commonFlag) {
        this.commonFlag = commonFlag;
    }

    public Integer getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(Integer isJoin) {
        this.isJoin = isJoin;
    }

    public Integer getIsShowRemove() {
        return isShowRemove;
    }

    public void setIsShowRemove(Integer isShowRemove) {
        this.isShowRemove = isShowRemove;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getScoresTotalVal() {
        return scoresTotalVal;
    }

    public void setScoresTotalVal(String scoresTotalVal) {
        this.scoresTotalVal = scoresTotalVal;
    }
}
