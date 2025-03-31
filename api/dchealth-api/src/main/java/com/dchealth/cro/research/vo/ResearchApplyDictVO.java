package com.dchealth.cro.research.vo;

import lombok.Data;

import java.util.List;

/**
 * 队列申请字典VO
 */
@Data
public class ResearchApplyDictVO {

    /**
     * 科室名称
      */
    private String deptName;

    /**
     * 病种
     */
    private List<ResearchDiseasesDictVO> researchDiseasesDictVOS;

}
