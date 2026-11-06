package com.dchealth.cms.service.impl;

import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.CmsDeptArticleParam;
import com.dchealth.cms.mapper.CmsDeptArticleMapper;
import com.dchealth.cms.service.ICmsDeptArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.common.utils.SecurityUtils.getUsername;

@Service
public class CmsDeptArticleServiceImpl implements ICmsDeptArticleService {

    @Autowired
    private CmsDeptArticleMapper cmsDeptArticleMapper;

    @Override
    public List<CmsDeptArticle> batchSelectCmsDeptArticle(Long deptId,List<Long> articleIdList) {
        return cmsDeptArticleMapper.batchSelectCmsDeptArticle(deptId, articleIdList);
    }

    @Override
    public List<CmsDeptArticle> selectCmsDeptArticle(CmsDeptArticle cmsDeptArticle) {
        return cmsDeptArticleMapper.selectCmsDeptArticle(cmsDeptArticle);
    }

    @Override
    public int insertCmsDeptArticle(CmsDeptArticle cmsDeptArticle) {
        return cmsDeptArticleMapper.insertCmsDeptArticle(cmsDeptArticle);
    }

    @Override
    public int deleteCmsDeptArticleById(Long deptId, Long articleId) {
        return cmsDeptArticleMapper.deleteCmsDeptArticleById(deptId,articleId);
    }


    @Transactional
    @Override
    public int batchInsertCmsDeptArticle(CmsDeptArticleParam cmsDeptArticleParam, CmsCategory cmsCategory, List<CmsArticle> cmsArticles) {
        List<CmsDeptArticle> cmsDeptArticleList = new ArrayList<>();
        List<Long> articleList = new ArrayList<>();
        for (CmsArticle cmsArticleVo : cmsArticles) {
            articleList.add(cmsArticleVo.getId());
            CmsDeptArticle cmsDeptArticle = getCmsDeptArticle(cmsDeptArticleParam, cmsArticleVo, cmsCategory);
            cmsDeptArticleList.add(cmsDeptArticle);
        }
        List<CmsDeptArticle> updateCmsDeptArticleList = batchSelectCmsDeptArticle(cmsCategory.getDeptId(), articleList);
        if (!CollectionUtils.isEmpty(updateCmsDeptArticleList)) {
            cmsDeptArticleMapper.updateCmsDeptArticleCategory(cmsDeptArticleList);
        }
        List<Long> updateArticleIds = updateCmsDeptArticleList.stream().map(CmsDeptArticle::getArticleId).collect(Collectors.toList());
        List<CmsDeptArticle> insertCmsDeptArticelList = cmsDeptArticleList.stream().filter(cmsDeptArticle -> !updateArticleIds.contains(cmsDeptArticle.getArticleId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(insertCmsDeptArticelList)){
            cmsDeptArticleMapper.batchInsertCmsDeptArticle(insertCmsDeptArticelList);
        }
        return 1;
    }

    private CmsDeptArticle getCmsDeptArticle(CmsDeptArticleParam cmsDeptArticleParam, CmsArticle cmsArticleVo, CmsCategory cmsCategory) {
        String userName = getUsername();
        CmsDeptArticle cmsDeptArticle = new CmsDeptArticle();
        cmsDeptArticle.setDeptId(cmsCategory.getDeptId());
        cmsDeptArticle.setArticleId(cmsArticleVo.getId());
        cmsDeptArticle.setCategoryId(cmsDeptArticleParam.getDeptCategoryId());
        cmsDeptArticle.setCreateBy(userName);
        cmsDeptArticle.setUpdateBy(userName);
        return cmsDeptArticle;
    }
}
