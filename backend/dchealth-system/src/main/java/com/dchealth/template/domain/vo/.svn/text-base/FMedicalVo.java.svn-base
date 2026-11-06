package com.dchealth.template.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FMedicalVo {
    private Long researchId;       //队列id
    private Long recordId;         //档案Id
    private Long formId;           //表单ID
    private Long propertyId;     //属性id
    private String propertyCode;   //属性code
    private String propertyType;   //属性类型

    private String propertyValue;  //属性值
    private String createBy;      //操作人

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;      //操作时间
}
