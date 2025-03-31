package com.dchealth.cro.cms.vo;

import lombok.Data;


/**
 *
 */
@Data
public class RecordDetailRes {

    /**
     * 讯飞记录id
     */
    private String recordId;

    //第三方id，周期id
    private String relationId;

    //姓名
    private String dwellerName;

    //手机号
    private String telephone;

    //电话拨打时间
    private Long callTime;

    //任务开始时间
    private String taskTime;

    //方案名称
    private String planName;

    //电话模版名称
    private String speechName;

    //通话时长（秒）
    private Integer callTimeLength;

    //电话任务状态描述
    private String callResult;

    //任务状态枚举值说明0成功2通话中3无法接通4关机5用户正忙6空号7号码错误9停机15客户接听后并主动挂机20用户未接22来电提醒转来电提醒23呼入限制26网络故障28线路故障30呼叫失败300任务执行失败
    private String resultCode;

    //接通状态枚举值说明1正常回答2自动留言3接听不便4居民已死亡5家属不能代答6不愿配合7号码错误8中断
    private String endNode;

    //音频地址
    private String recordUrl;

    //短信模板名称
    private String smsName;

    //短信内容
    private String smsContent;

    //对话内容json字符串,resultCode为0或15时才会有值
    private String dialog;


    private String speechId;
}
