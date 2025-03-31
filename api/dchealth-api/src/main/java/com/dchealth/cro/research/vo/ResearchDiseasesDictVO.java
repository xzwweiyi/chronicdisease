package com.dchealth.cro.research.vo;

import com.dchealth.cro.research.domain.ResearchDiseases;
import lombok.Data;

import java.util.List;

/**
 * 病种vo
 */

@Data
public class ResearchDiseasesDictVO {

    /** 病种id */
    private Long diseaseId;

    /** 病种名称 */
    private String diseaseName;

    /**
     * 队列字典
     */
    private List<ResearchInfoDictVO> researchInfoDictVOS;

    public ResearchDiseasesDictVO(){}

    public ResearchDiseasesDictVO(ResearchDiseases researchDiseases, List<ResearchInfoDictVO> researchInfoDictVOS){
        this.diseaseId = researchDiseases.getDiseaseId();
        this.diseaseName = researchDiseases.getDiseaseName();
        this.researchInfoDictVOS = researchInfoDictVOS;
    }

}
