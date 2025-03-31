package com.dchealth.cro.researchPatient.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 我的队列
 * @author zhangxiao
 * @Date  2025-1-11
 */
@Data
public class MyQueue extends Model<MyQueue> {
    /**
     * 队列id
     */
    private Integer queueId;
    /**
     * 队列名称
     */
    private String queueName;
    /**
     * 队列状态
     */
    private String status;
    /**
     * 队列状态
     */
    private Integer queueStatus;
    /**
     * 病人状态
     */
    private Integer patientStatus;
    /**
     * 是否退出申请
     */
    private Integer isHaveQuitApply;
    /**
     * 待审核数量
     */
    private Boolean noAuditCount;
    /**
     * 同意数量
     */
    private Boolean agreeCount;

}
