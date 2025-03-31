package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchSourceMapper {

    int insertResearchSource(ResearchSource researchSource);

    int updateResearchSource(ResearchSource researchSource);

    List<ResearchSource> selectResearchSourceList(ResearchSource researchSource);

    ResearchSource findOneResearchSource(Long sourceId);

    int isHaveChildResearchSource(Long sourceId);

    int deleteResearchSource(Long sourceId);

    List<ResearchSource> findResearchSourceBySourceIds(@Param("sources") List<Long> sources);

    List<ResearchSource> findAllResearchSource();
}
