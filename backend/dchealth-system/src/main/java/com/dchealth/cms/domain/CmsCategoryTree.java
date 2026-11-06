package com.dchealth.cms.domain;

import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;
import lombok.Data;

import java.util.List;

@Data
public class CmsCategoryTree {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    private String name;

    private List<CmsArticleCateGoryVo> cmsArticleList;

    private List<CmsCategoryTree> children;

    private Long deptId;
    public CmsCategoryTree(long id, long parentId, String name,Long deptId) {
        this.id=id;
        this.parentId=parentId;
        this.name=name;
        this.deptId = deptId;
    }
}
