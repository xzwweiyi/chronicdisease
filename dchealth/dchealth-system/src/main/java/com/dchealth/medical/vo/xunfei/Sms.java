package com.dchealth.medical.vo.xunfei;

import lombok.Data;

import java.util.List;

/**
 * 是否启用短信提醒,不启用可不传
 */
@Data
public class Sms {

    /**
     * 短信id
     */
    private String smsId;

    /**
     * 短信提醒条件1-未接通 2-接通 3-全部
     */
    private Integer smsCondition;

    /**
     * 短信动态参数
     */
    private List<Var> smsVars;

}
