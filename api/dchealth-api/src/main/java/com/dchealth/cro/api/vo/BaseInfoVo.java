package com.dchealth.cro.api.vo;


import lombok.Data;

import java.util.List;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/10/28 13:56
 * @Version: 1.0
 */

@Data
public class BaseInfoVo {

    private String accountId;

    private String personId;

    private String patientName;

    private String mobile;

    private List<ResearchInfoVo> researchInfoVos;



}
