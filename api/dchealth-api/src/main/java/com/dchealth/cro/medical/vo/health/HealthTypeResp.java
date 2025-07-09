package com.dchealth.cro.medical.vo.health;


import lombok.Data;

/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/3 18:51
 * @Version: 1.0
 */

@Data
public class HealthTypeResp {

    /**
     * 类型编码
     */
    private Integer healthManageType;

    /**
     * 类型名称
     */
    private String healthManageTypeName;

    /**
     * 编辑标识
     */
    private Boolean isEditable = true;
}
