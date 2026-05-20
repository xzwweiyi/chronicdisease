package com.dchealth.cro.api.vo;


import com.dchealth.cro.common.annoation.Desensitize;
import com.dchealth.cro.common.constant.DesensitizeType;
import com.dchealth.cro.common.serializers.DesensitizeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @Desensitize(type = DesensitizeType.PHONE)
    @JsonSerialize(using = DesensitizeSerializer.class)
    private String mobile;

    private List<ResearchInfoVo> researchInfoVos;



}
