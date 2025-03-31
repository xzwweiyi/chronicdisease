package com.dchealth.cro.researchPatient.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @description: 退出记录
 * @author zhangxiao
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("quit_record")
public class QuitRecord extends Model<QuitRecord> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 队列id
     */
    @TableField("research_id")
    private String queueId;
    /**
     * 患者id
     */
    private String personId;
    /**
     * 退出原因
     */
    private String quitReason;
    /**
     * 退出时间
     */
    private Date quitTime;
    /**
     * 是否同意
     */
    private Boolean isAgree;
    /**
     * 同意时间
     */
    private Date agreeTime;
    /**
     * 拒绝原因
     */
    private String rejectReason;
    /**
     * 拒绝时间
     */
    private Date rejectTime;
}
