package com.dchealth.cms.domain.vo;

import com.dchealth.cms.domain.CmsArticle;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CmsArticleVo {

    private String categoryName;

    private List<CmsArticle> articleList;

    private Integer articleNum;
}
