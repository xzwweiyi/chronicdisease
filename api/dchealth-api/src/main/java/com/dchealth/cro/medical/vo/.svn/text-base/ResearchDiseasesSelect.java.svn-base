package com.dchealth.cro.medical.vo;

import com.dchealth.cro.research.domain.ResearchDiseases;
import com.dchealth.cro.research.domain.ResearchInfo;
import lombok.Data;

import java.util.List;

/**
 * 病种队列下拉
 */
@Data
public class ResearchDiseasesSelect {

    private Long nodeId;

    private String nodeName;

    private List<ResearchDiseasesSelect> researchDiseasesSelects;

    public ResearchDiseasesSelect(){}


    public ResearchDiseasesSelect(ResearchDiseases researchDiseases, List<ResearchDiseasesSelect> researchDiseasesSelects){
        this.nodeId = researchDiseases.getDiseaseId();
        this.nodeName = researchDiseases.getDiseaseName();
        this.researchDiseasesSelects = researchDiseasesSelects;
    }


    public ResearchDiseasesSelect(ResearchInfo researchInfo){
        this.nodeId = researchInfo.getId();
        this.nodeName = researchInfo.getProjectName();
    }

}
