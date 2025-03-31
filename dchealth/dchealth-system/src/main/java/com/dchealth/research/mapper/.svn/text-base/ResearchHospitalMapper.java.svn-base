package com.dchealth.research.mapper;

import com.dchealth.research.domain.ResearchHospital;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResearchHospitalMapper {

    List<ResearchHospital> selectByResearchHospital(ResearchHospital researhHospital);

    int insertResearchHospital(ResearchHospital researchHospital);

    int updateResearchHospital(ResearchHospital researchHospital);

    int deleteResearchHospitalByIds(List<Long> ids);

    List<ResearchHospital> findResearchHospitalByIds(List<Long> ids);

    List<ResearchHospital> findResearchHospitalByResearchIdAndDeptId(@Param("researchId") Long researchId, @Param("deptId") Long deptId);
}
