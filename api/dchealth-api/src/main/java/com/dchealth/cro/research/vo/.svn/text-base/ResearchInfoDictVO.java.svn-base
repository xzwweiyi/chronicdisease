package com.dchealth.cro.research.vo;

import com.dchealth.cro.research.domain.ResearchInfo;
import lombok.Data;

/**
 * 队列字典
 */
@Data
public class ResearchInfoDictVO {

    /**
     * 队列id
     */
    private Long researchId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 病种id
     */
    private Long diseaseId;

    public ResearchInfoDictVO(){}

    public ResearchInfoDictVO(ResearchInfo researchInfo){
        this.researchId = researchInfo.getId();
        this.projectName = researchInfo.getProjectName();
        this.diseaseId = researchInfo.getDiseaseTypeId();
    }

}
