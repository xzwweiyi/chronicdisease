package com.dchealth.cro.core.domain;

import java.util.Date;

import com.dchealth.cro.common.annotation.Excel;

import lombok.Data;

/**
 * 医院信息对象 hospital
 * 
 * @author genemax1107@aliyun.com
 * @date 2019-12-13
 */
@Data
public class Hospital 
{

    /** 主键 */
    private Integer id;

    /** 医院名称 */
    @Excel(name = "医院名称")
    private String hospitalName;
    private String tenantId;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createDate;

    /** 修改时间 */
    private Date updateDate;
    private String hospitalIntro;
    private String hospitalImage;
    private String hospitalCode;


}
