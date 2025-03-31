package com.dchealth.medical.domain.form;

import lombok.Data;

/**
 * 退出队列表单
 * @author zhangxiao
 */
@Data
public class QuitRecordForm {
    /**
     * 队列ID
     */
    private String researchId;
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 页大小
     */
    private Integer pageSize;
    /**
     * 状态 1完成 0未完成
     */
    private String status;
    /**
     * 病人姓名
     */
    private String patientName;
    /**
     * 患者id
     */
    private String personId;
}
