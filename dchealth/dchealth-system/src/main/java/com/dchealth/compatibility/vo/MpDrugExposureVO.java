package com.dchealth.compatibility.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/8/8 10:40
 * @Version: 1.0
 */
@Data
public class MpDrugExposureVO {

    /**
     * 就诊类型
     */
    @NotNull(message = "personId不能为空")
    private Long personId;

    /**
     * 就诊id
     */
//    @NotNull(message = "visitOccurrenceId不能为空")
    private Long visitOccurrenceId;
}
