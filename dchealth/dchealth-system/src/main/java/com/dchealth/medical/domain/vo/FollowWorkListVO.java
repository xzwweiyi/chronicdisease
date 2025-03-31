package com.dchealth.medical.domain.vo;

import com.dchealth.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 随访工作站列表数据vo
 */
@Data
public class FollowWorkListVO {

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
     * 阶段名称
     */
    private String stageName;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 队列名称
     */
    private String projectName;

    //扫码计划加入队列
    private String scanProjectName;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 入组处理结果0无状态1待分配2变更3确认入组
     */
    private String enrollmentResult;

    /**
     * 入组结果文本
     */
    private String enrollmentResultContent;

    /** 队列id */
    private Long researchId;

    /**
     * 入组时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enrollmentTime;

    private String name;

    //随访质疑时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date questionTime;

    //随访完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followCompleteTime;

    //渠道来源1后台2扫码
    private String channelType;

    //随访提交时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date followSubmitTime;

    //外呼详情id
    private Long outboundId;

    //随访工作站查询对应外呼周期记录时查询需要
    private Integer stageAddNum;

    //随访状态0无状态1待处理2进行中3已完成
    private String followStatus;
    private String warnStatus;
    private String overTimeStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTimeCreateTime;

    /**
     * 账号ID
     */
    private Long accountId;

    private String personId;
    private String baseMobile;

    /** 超窗处理状态0无1更新随访时间2忽略本次3失访 */
    private String overTimeHandleStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date overTimeHandleTime;

    /**
     * 患者拒绝次数
     */
    private Integer refuseCount;
    /**
     * 短信发送次数
     */
    private Integer smsSendCount;
    /**
     * 操作的状态
     */
    private Integer enrollmentHandlerStatus;
    /**
     * 当前纳排的状态
     */
    private Integer enrollmentStatus;

    /**
     * 是否提交过数据
     */
    private Boolean haveSubmitted;
    /**
     * 患者状态0未填写1基线2随访3完成4失访 5申请退出 6已退出
     */
    private Integer patientStatus;

    /**
     * 填写状态 0未填写1已填写
     */
    private Integer fillStatus;

    /**
     * 查看/编辑状态  0可以填写 1可查看
     */
    private Integer editFlag;
    /**
     * 创建人用户名
     */
    private String createBy;
    /**
     * 创建人姓名
     */
    private String createName;

}
