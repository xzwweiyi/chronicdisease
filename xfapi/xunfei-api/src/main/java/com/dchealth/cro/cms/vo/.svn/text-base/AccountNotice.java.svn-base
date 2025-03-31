package com.dchealth.cro.cms.vo;

import com.dchealth.cro.common.annotation.Excel;
import lombok.Data;

import java.util.Date;


@Data
public class AccountNotice {

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long accountId;

    private Long recordId;

    private String title;

    /** 类型 */
    @Excel(name = "类型")
    private String type;

    /** 读取状态 */
    @Excel(name = "读取状态")
    private String isRead;

    private String extendJson;

    /** 通知内容 */
    private String content;

    /** 租户ID */
    @Excel(name = "租户ID")
    private String tenantId;

   private Date createTime;
   private Date updateTime;

   //是否发送友盟推送0否1是
   private String isSendPush;
   
   private Long noticeId;

   private Long researchId;
   private Long patientId;

   private String name;
   private String followTipStatus;
   private Date visitTime;
   private Date recordCreateTime;
   private String sex;
   private String warnStatus;

   private String createTimeStr;

}
