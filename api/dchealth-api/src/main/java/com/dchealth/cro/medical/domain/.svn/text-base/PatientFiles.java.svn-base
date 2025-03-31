package com.dchealth.cro.medical.domain;

import java.util.Date;

import lombok.Data;

/**
 * 档案文件对象 patient_files
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Data
public class PatientFiles
{

    /** $column.columnComment */
    private Long id;

    /** 图片地址 */
    private String imageUrl;

    /** 患者ID */
    private Long patientId;

    /** 记录ID */
    private Long archiveId;

    /** 二级分类 1=知情同意书 2=人口学统计 3=全面体格检查  4= 生命体征 5=体重 6=研究者决定的针对性体格检查 7=对症体格检查 */
    private String imageType;

    /** 1=没有压缩  2 =有压缩 默认返回都是压缩后图片 */
    private String isCompress;

    /** 图片一级类型 1=受试者基本信息  2=既往病史及SLE治疗史 */
    private String type;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

}
