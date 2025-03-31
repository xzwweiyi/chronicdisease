package com.dchealth.cro.warn.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 预警指导聊天对象 warn_leave_message
 * 
 * @author lchbu
 * @date 2023-03-17
 */
@Data
public class WarnLeaveMessageForm
{
    private static final long serialVersionUID = 1L;

    /** 患者随访ID */
    @NotNull(message = "随访id不能为空")
    private Long recordId;

    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    private Long accountId;

    /** 用户类型 1医生 2患者 */
    private String userType;

    /** 留言类型 1文本  2 图片路径 */
    private String contentType;

    /** 留言内容 */
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private List<String> imageList;
}
