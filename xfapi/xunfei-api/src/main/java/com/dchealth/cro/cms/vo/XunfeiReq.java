package com.dchealth.cro.cms.vo;

import lombok.Data;

import java.util.List;

/**
 * 讯飞请求体
 */
@Data
public class XunfeiReq {

    /**
     * 要外呼的居民信息（居民数不超过500）
     */
    private List<Dweller> dwellers;

    /**
     * 电话模版id
     */
    private Long speechId;

    //短信id
    private String smsId;

    /**
     * 任务名称
     */
    private String planName;

    /**
     * 电话模版动态参数
     */
    private List<Var> vars;

    /**
     * 时间类型(1：即呼，2：指定日期)
     */
    private Integer type;

    //指定日期(type = 2 时生效)
    //格式：yyyy-MM-dd HH:mm:ss
    private String date;

    //是否启用重拨提醒,不启用可不传
    private Recall recall;

    //是否启用短信提醒,不启用可不传
    private Sms sms;

}
