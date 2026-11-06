package com.dchealth.research.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.mapper.ResearchInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dchealth.research.mapper.ResearchInfoArticleMapper;
import com.dchealth.research.domain.ResearchInfoArticle;
import com.dchealth.research.service.IResearchInfoArticleService;

/**
 * 队列-患教关系Service业务层处理
 * 
 * @author lchbu
 * @date 2022-08-25
 */
@Service
public class ResearchInfoArticleServiceImpl implements IResearchInfoArticleService 
{
    @Autowired
    private ResearchInfoArticleMapper researchInfoArticleMapper;

    @Autowired
    private CmsArticleMapper cmsArticleMapper;

    @Autowired
    private ResearchInfoMapper researchInfoMapper;

    /**
     * 查询队列-患教关系
     * 
     * @param researchId 队列-患教关系主键
     * @return 队列-患教关系
     */
    @Override
    public List<ResearchInfoArticle> selectResearchInfoArticleByResearchId(Long researchId)
    {
        return researchInfoArticleMapper.selectResearchInfoArticleByResearchId(researchId);
    }

    /**
     * 查询队列-患教关系列表
     * 
     * @param researchInfoArticle 队列-患教关系
     * @return 队列-患教关系
     */
    @Override
    public List<ResearchInfoArticle> selectResearchInfoArticleList(ResearchInfoArticle researchInfoArticle)
    {
        return researchInfoArticleMapper.selectResearchInfoArticleList(researchInfoArticle);
    }

    /**
     * 新增队列-患教关系
     * 
     * @param researchInfoArticles 队列-患教关系
     * @return 结果
     */
    @Override
    public int insertResearchInfoArticle(List<ResearchInfoArticle> researchInfoArticles)
    {
        int num = 0;
        for (ResearchInfoArticle researchInfoArticle : researchInfoArticles) {
            //验证队列-病种-患教是否存在，存在则不做处理
            List<ResearchInfoArticle> researchInfoArticleList = researchInfoArticleMapper.selectResearchInfoArticleList(researchInfoArticle);
            if(researchInfoArticleList == null || researchInfoArticleList.size() == 0){
                ResearchInfo researchInfo = researchInfoMapper.selectResearchInfoById(researchInfoArticle.getResearchId());
                if(researchInfo != null){
                    researchInfoArticle.setDiseaseId(researchInfo.getDiseaseTypeId());
                }
                researchInfoArticleMapper.insertResearchInfoArticle(researchInfoArticle);
            }
            num += 1;
        }
        return num;
    }

    /**
     * 修改队列-患教关系
     * 
     * @param researchInfoArticle 队列-患教关系
     * @return 结果
     */
    @Override
    public int updateResearchInfoArticle(ResearchInfoArticle researchInfoArticle)
    {
        return researchInfoArticleMapper.updateResearchInfoArticle(researchInfoArticle);
    }

    /**
     * 批量删除队列-患教关系
     * 
     * @param researchIds 需要删除的队列-患教关系主键
     * @return 结果
     */
    @Override
    public int deleteResearchInfoArticleByResearchIds(Long[] researchIds)
    {
        return researchInfoArticleMapper.deleteResearchInfoArticleByResearchIds(researchIds);
    }

    /**
     * 删除队列-患教关系信息
     * 
     * @param researchId 队列-患教关系主键
     * @return 结果
     */
    @Override
    public int deleteResearchInfoArticleByResearchId(Long researchId)
    {
        return researchInfoArticleMapper.deleteResearchInfoArticleByResearchId(researchId);
    }

    /**
     * 查询队列-病种下的患教列表
     * @param researchInfoArticle
     * @return
     */
    @Override
    public List<CmsArticle> selectListArticle(ResearchInfoArticle researchInfoArticle) {
        List<CmsArticle> list = new ArrayList<>();
        //查询患教列表
        List<ResearchInfoArticle> researchInfoArticles = researchInfoArticleMapper.selectResearchInfoArticleList2(researchInfoArticle);
        //根据标题刷选
        if(researchInfoArticles != null && researchInfoArticles.size() > 0){
            researchInfoArticles.stream().forEach(researchArticle -> {
                CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(researchArticle.getArticleId());
                cmsArticle.setCategoryId(researchArticle.getCategoryId());
                cmsArticle.setIndexType(researchArticle.getType());
                if(researchInfoArticle.getTitle() != null && cmsArticle.getTitle() != null && cmsArticle.getTitle().contains(researchInfoArticle.getTitle())){
                    list.add(cmsArticle);
                } else {
                    list.add(cmsArticle);
                }
            });
        }
        return list;
    }

    /**
     * 移除队列患教
     * @param researchInfoArticle
     * @return
     */
    @Override
    public int removeArticle(ResearchInfoArticle researchInfoArticle) {
        return researchInfoArticleMapper.deleteResearchArticle(researchInfoArticle);
    }



    @Override
    public int updateResearchArticleType(ResearchInfoArticle researchInfoArticle) {
        return researchInfoArticleMapper.updateResearchArticleType(researchInfoArticle);
    }

}
