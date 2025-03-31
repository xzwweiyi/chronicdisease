package com.dchealth.research.service;

import com.alibaba.fastjson2.JSONObject;
import com.dchealth.research.domain.ResearchHospital;

import java.util.List;

public interface IResearchHospitalService {

    List<ResearchHospital> selectResearchHospital(ResearchHospital researchHospital);

    int insertResearchHospital(ResearchHospital researchHospital);

    int updateResearchHospital(ResearchHospital researchHospital);

    int deleteResearchHospitalByIds(List<Long> ids);

    List<JSONObject> findHospitalDict();

    List<JSONObject> findResearchInfoDict();
}
