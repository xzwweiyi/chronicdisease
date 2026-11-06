package com.dchealth.medical.domain.form;

import lombok.Data;

import java.util.Date;

/**
 * 超窗处理form
 */
@Data
public class FollowOverTimeHandleForm {

    /**
     * 档案ID
     */
    private Long recordId;

    /**
     * 处理类型
     * 1更新随访时间2忽略本次3失访
     */
    private String handleType;

    /**
     * 随访时间
     */
    private Date visitTime;

    /**
     * 失访原因
     * 1不在参与2失联3其他
     */
    private String lossCause;

    /**
     * 失访原因其他
     */
    private String lossCauseOther;

    private Long userId;

    private String updateBy;

    //0患者本次不随访1患者已经完成随访
    private String ignoreType;

    private Long researchId;
}
