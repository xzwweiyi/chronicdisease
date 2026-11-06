package com.dchealth.aieval.mapper;

import java.util.List;
import com.dchealth.aieval.domain.AiEvaluationProject;

public interface AiEvaluationProjectMapper {
    List<AiEvaluationProject> selectAiEvaluationProjectList(AiEvaluationProject entity);
    AiEvaluationProject selectAiEvaluationProjectById(Long id);
    int insertAiEvaluationProject(AiEvaluationProject entity);
    int updateAiEvaluationProject(AiEvaluationProject entity);
    int deleteAiEvaluationProjectById(Long id);
    int deleteAiEvaluationProjectByIds(Long[] ids);
}
