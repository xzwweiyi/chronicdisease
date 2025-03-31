package com.dchealth.cro.medical.vo.xunfei;

import lombok.Data;

import java.util.Map;

/**
 * 讯飞要外呼的居民信息
 */
@Data
public class Dweller {

    /**
     * 姓名
     */
    private String dwellerName;

    /**
     * 号码
     */
    private String telephone;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 第三方ID
     */
    private String relationId;

    /**
     * 居民扩展信息
     */
    private Map<String,String> extProperty;

}
