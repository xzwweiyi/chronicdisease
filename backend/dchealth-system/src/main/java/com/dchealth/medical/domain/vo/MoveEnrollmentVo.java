package com.dchealth.medical.domain.vo;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/9/3 14:00
 * @Version: 1.0
 */
@Data
public class MoveEnrollmentVo {

    private Long researchId;

    private String cardId;

    private String mobile;

    private Long accountId;

    private String handleType;

    private String deptId;
}
