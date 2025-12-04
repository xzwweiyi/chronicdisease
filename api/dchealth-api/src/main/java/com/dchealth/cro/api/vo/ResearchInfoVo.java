package com.dchealth.cro.api.vo;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/28 14:53
 * @Version: 1.0
 */
@Data
public class ResearchInfoVo {

    private String patientId;

    private String researchId;

    private String researchName;

    private String DeptId;

    private String DeptName;

    private String followId;

    private String recordId;

    private Object data;

}
