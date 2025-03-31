package com.dchealth.research.service;

import com.dchealth.research.domain.ResearchSource;
import com.dchealth.research.domain.vo.ResearchIdsVo;

import java.util.List;

public interface IResearchSourceService {

    int insertResearchSource(ResearchSource researchSource);

    int updateResearchSource(ResearchSource researchSource);

    List<ResearchSource> selectResearchSourceList(ResearchSource researchSource);

    ResearchSource findOneResearchSource(Long sourceId);

    int deleteResearchSource(ResearchSource researchSource);

    boolean isHaveChildResearchSource(ResearchSource researchSource);

    boolean isDistributionResearchSource(ResearchSource researchSource);

    List<ResearchSource> findResearchSourceByUserId(ResearchIdsVo researchIdsVo);

    List<Long> findResearchSourceByRoleId(ResearchIdsVo researchIdsVo);

    List<ResearchSource> treeResearchSource();

    int batchInsertResearchSource(Long roleId, List<Long> sourceIds);
}
