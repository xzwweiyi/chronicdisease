package com.dchealth.cro.medical.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 随访工作站列表数据vo
 */
@Data
public class OutboundWorkListVO {

    private Long id;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 档案ID
     */
    private Long recordId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 队列名称
     */
    private String projectName;

    /** 队列id */
    private Long researchId;

    private String name;

    private Integer stageNum;

    //计划随访日期
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planVisitTime;

    /**
     * 计划提醒时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outboundTime;

    /** 电话任务状态描述 */
    private String xunfeiCallResult;

    /** 任务状态枚举值说明0成功2通话中3无法接通4关机5用户正忙6空号7号码错误9停机15客户接听后并主动挂机20用户未接22来电提醒转来电提醒23呼入限制26网络故障28线路故障30呼叫失败300任务执行失败 */
    private String xunfeiResultCode;

    /** 接通状态枚举值说明1正常回答2自动留言3接听不便4居民已死亡5家属不能代答6不愿配合7号码错误8中断 */
    private String xunfeiEndNode;


}
