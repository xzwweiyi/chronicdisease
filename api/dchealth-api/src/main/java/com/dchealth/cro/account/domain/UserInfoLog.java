package com.dchealth.cro.account.domain;

import java.util.Date;

import lombok.Data;

/**
 * 医生信息日志表对象 user_info_log
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Data
public class UserInfoLog 
{

    /** $column.columnComment */
    private Long logId;

    /** 用户ID */
    private Long userId;

    /** 出生日期 */
    private Date birthday;

    /** 学历 1：中专及中专以下 2:大专 3:大学本科 4:硕士 5:博士 6:博士后 7:博士导师7:硕士导师' */
    private String education;

    /** 省ID */
    private Integer provinceId;

    /** 市ID */
    private Integer cityId;

    /** 区县ID */
    private Integer areaId;

    /** 街道ID */
    private Integer streetId;

    /** 详情地址 */
    private String detailAddress;

    private String title;

    /** 专长 */
    private String expert;

    /** 个人介绍 */
    private String personalIntro;

    /** 行业开始时间 */
    private String workYear;

    /** 形象照片 */
    private String figureImage;

    /** 审核状态 1 未提交 2 待审核 3 审核通过  4 未通过 */
    private String authStatus;

    /** 审核内容 */
    private String authContext;
    
    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

}
