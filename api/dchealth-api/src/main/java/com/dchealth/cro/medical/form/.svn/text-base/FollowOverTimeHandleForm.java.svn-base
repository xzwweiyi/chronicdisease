package com.dchealth.cro.medical.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
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
}
