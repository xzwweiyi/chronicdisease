package com.dchealth.cms.service.impl;

import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsCategoryTree;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.cms.mapper.CmsCategoryMapper;
import com.dchealth.cms.mapper.CmsDeptArticleMapper;
import com.dchealth.cms.service.ICmsCategoryService;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.research.mapper.ResearchInfoArticleMapper;
import com.dchealth.system.mapper.SysRoleDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 资讯分类Service业务层处理
 *
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
public class CmsCategoryServiceImpl implements ICmsCategoryService {
    @Autowired
    private CmsCategoryMapper cmsCategoryMapper;

    @Autowired
    private CmsArticleMapper cmsArticleMapper;
    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;


    @Autowired
    private CmsDeptArticleMapper cmsDeptArticleMapper;


    @Autowired
    private ResearchInfoArticleMapper researchInfoArticleMapper;

    /**
     * 查询资讯分类
     *
     * @param id 资讯分类主键
     * @return 资讯分类
     */
    @Override
    public CmsCategory selectCmsCategoryById(Long id) {
        return cmsCategoryMapper.selectCmsCategoryById(id);
    }

    /**
     * 查询资讯分类列表
     *
     * @param cmsCategory 资讯分类
     * @return 资讯分类
     */
    @Override
    public List<CmsCategory> selectCmsCategoryList(CmsCategory cmsCategory) {
        cmsCategory.setParentId(0l);
        //一级分类列表
        List<CmsCategory> oneLevelCategoryList = cmsCategoryMapper.selectCmsCategoryList(cmsCategory);
        //所有分类统计
        List<CmsCategory> allCategoryList;
        if (cmsCategory.getCommonFlag() == 1) {
            allCategoryList = cmsCategoryMapper.selectCmsCategoryStatistics(cmsCategory.getCommonFlag());
        } else {
            allCategoryList = cmsCategoryMapper.selectCmsDeptCategoryStatistics(cmsCategory.getCommonFlag(), cmsCategory.getDeptId());
        }

        for (CmsCategory oneLevelCategory : oneLevelCategoryList) {
            for (CmsCategory allCmsCategory : allCategoryList) {
                if (allCmsCategory.getParentId() == 0 && oneLevelCategory.getId() == allCmsCategory.getId()) {
                    oneLevelCategory.setArticleCount(allCmsCategory.getArticleCount());
                    continue;
                }
                if (Arrays.asList(allCmsCategory.getParentIds().split(",")).contains(oneLevelCategory.getId().toString())) {
                    long articleCount = oneLevelCategory.getArticleCount() == null ? 0 : oneLevelCategory.getArticleCount();
                    oneLevelCategory.setArticleCount(articleCount + allCmsCategory.getArticleCount());
                }
            }
        }
        return oneLevelCategoryList;
    }


    public List<CmsCategory> selectCmsCategoryListByIds(Set<String> ids) {
        CmsCategory cmsCategory = new CmsCategory();
        cmsCategory.setIds(ids);
        return cmsCategoryMapper.selectCmsCategoryList(cmsCategory);
    }

    /**
     * 查询资讯分类列表
     *
     * @param cmsCategory 资讯分类
     * @return 资讯分类
     */
    @Override
    public List<CmsCategoryTree> selectCmsCategoryTree(CmsCategory cmsCategory, Map<String, List<CmsArticleCateGoryVo>> categotyArticleMap) {
        List<CmsCategory> categoryList = cmsCategoryMapper.selectCmsCategoryList(cmsCategory);
        if (StringUtils.isNotEmpty(cmsCategory.getTitle()) && !CollectionUtils.isEmpty(categoryList)) {
            Set<String> set = new HashSet<>();
            Set<String> idSet = categotyArticleMap.keySet();
            idSet.stream().forEach(s -> {
                String[] split = s.split(",");
                for (int i = 0; i < split.length; i++) {
                    set.add(split[i]);
                }
            });
            categoryList = categoryList.stream()
                    .filter(s -> set.contains(s.getId().toString()))
                    .collect(Collectors.toList());
        }
        List<CmsCategoryTree> cmsCategoryTreeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categoryList)) {
            categoryList.stream().forEach(cmsCate -> {
                CmsCategoryTree cmsCategoryTree = new CmsCategoryTree(cmsCate.getId(), cmsCate.getParentId(), cmsCate.getName(),cmsCate.getDeptId());
                cmsCategoryTree.setCmsArticleList(categotyArticleMap.get(cmsCate.getId() + "," + cmsCate.getParentIds()));
                cmsCategoryTreeList.add(cmsCategoryTree);
            });
        }
        return buildTree(cmsCategoryTreeList);
    }

    /**
     * 资讯分类树查询
     *
     * @param cmsCategory 资讯分类
     * @return 资讯分类
     */
    @Override
    public List<CmsCategoryTree> selectCmsCategoryTree(CmsCategory cmsCategory) {
        if (cmsCategory.getCommonFlag() == 0 ){
            if (Objects.nonNull(cmsCategory.getCategoryId())){
                CmsDeptArticle cmsDeptArticle = new CmsDeptArticle();
                cmsDeptArticle.setArticleId(cmsCategory.getArticleId());
                cmsDeptArticle.setCategoryId(cmsCategory.getCategoryId());
                List<CmsDeptArticle> cmsDeptArticleList = cmsDeptArticleMapper.selectCmsDeptArticle(cmsDeptArticle);
                cmsCategory.setDeptId(cmsDeptArticleList.get(0).getDeptId());
            }
            CmsCategory cmsCategoryParam = new CmsCategory();
            cmsCategoryParam.setCommonFlag(cmsCategory.getCommonFlag());
            cmsCategoryParam.setDeptId(cmsCategory.getDeptId());
        }
        List<CmsCategory> categoryList = cmsCategoryMapper.selectCmsCategoryList(cmsCategory);
        List<CmsCategoryTree> cmsCategoryTreeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(categoryList)) {
            categoryList.stream().forEach(cmsCate -> {
                CmsCategoryTree cmsCategoryTree = new CmsCategoryTree(cmsCate.getId(), cmsCate.getParentId(), cmsCate.getName(),cmsCate.getDeptId());
                cmsCategoryTreeList.add(cmsCategoryTree);
            });
        }
        return buildTree(cmsCategoryTreeList);
    }



    private List<CmsCategoryTree> buildTree(List<CmsCategoryTree> nodeList) {
        List<CmsCategoryTree> rootNode = nodeList.stream().filter(i -> i.getParentId() == 0).collect(Collectors.toList());
        for (CmsCategoryTree cmsCategoryTree : rootNode) {
            buildChildTree(nodeList, cmsCategoryTree);

        }
        return rootNode;
    }

    private CmsCategoryTree buildChildTree(List<CmsCategoryTree> nodeList, CmsCategoryTree pNode) {
        List<CmsCategoryTree> childTree = new ArrayList<CmsCategoryTree>();
        List<CmsCategoryTree> treeNodeStream = nodeList.stream()
                .filter(i -> i.getParentId().equals(pNode.getId())).collect(Collectors.toList());

        for (CmsCategoryTree treeNode : treeNodeStream) {
            childTree.add(buildChildTree(nodeList, treeNode));
        }
        pNode.setChildren(childTree);
        return pNode;
    }


    /**
     * 新增资讯分类
     *
     * @param cmsCategory 资讯分类
     * @return 结果
     */
    @Override
    public int insertCmsCategory(CmsCategory cmsCategory) {
        CmsCategory cmsCategoryVO = cmsCategoryMapper.selectCmsCategoryById(cmsCategory.getParentId());
        cmsCategory.setCreateTime(DateUtils.getNowDate());
        cmsCategory.setUpdateTime(DateUtils.getNowDate());
        cmsCategory.setParentId(cmsCategory.getParentId() == null ? 0 : cmsCategory.getParentId());
        cmsCategory.setTenantId("1");
        cmsCategory.setCommonFlag(cmsCategory.getCommonFlag());
        cmsCategory.setParentIds(cmsCategoryVO == null ? "0" : cmsCategoryVO.getParentIds() + "," + cmsCategory.getParentId());
        return cmsCategoryMapper.insertCmsCategory(cmsCategory);
    }

    /**
     * 修改资讯分类
     *
     * @param cmsCategory 资讯分类
     * @return 结果
     */
    @Override
    public int updateCmsCategory(CmsCategory cmsCategory) {
        cmsCategory.setUpdateTime(DateUtils.getNowDate());
        return cmsCategoryMapper.updateCmsCategory(cmsCategory);
    }

    /**
     * 批量删除资讯分类
     *
     * @param ids 需要删除的资讯分类主键
     * @return 结果
     */
    @Override
    public int deleteCmsCategoryByIds(Long[] ids) {
        return cmsCategoryMapper.deleteCmsCategoryByIds(ids);
    }

    /**
     * 删除资讯分类信息
     *
     * @param id 资讯分类主键
     * @return 结果
     */
    @Override
    public int deleteCmsCategoryById(Long id) {
        return cmsCategoryMapper.deleteCmsCategoryById(id);
    }

    /**
     * 逻辑删除分类
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int updateCmsCategoryById(Long id) {
        //逻辑删除分类底下的患教内容
        cmsArticleMapper.updateCmsArticleDelByCategoryId(id);
        //删除队列患教关系
        researchInfoArticleMapper.deleteResearchInfoArticleByCategoryId(id);
        //逻辑删除分类
        return cmsCategoryMapper.updateCmsCategoryDelById(id);
    }


    /**
     * 逻辑删除分类
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int updateDeptCmsCategoryById(Long id) {
        //逻辑删除分类底下的患教内容
        cmsDeptArticleMapper.deleteCmsDeptArticleByCategoryId(id);
        //删除队列患教关系
        researchInfoArticleMapper.deleteResearchInfoArticleByCategoryId(id);
        //逻辑删除分类
        return cmsCategoryMapper.updateCmsCategoryDelById(id);
    }

    /**
     * 根据科室id查询分类
     *
     * @param deptId
     * @return
     */
    @Override
    public List<CmsCategory> selectCmsCategoryByDeptId(Long deptId) {
        return cmsCategoryMapper.selectCmsCategoryByDeptId(deptId);
    }

    @Override
    public List<CmsCategory> selectAllChildCategoryById(Long deptCategoryId) {
        return cmsCategoryMapper.selectAllChildCategoryById(deptCategoryId);
    }

}
