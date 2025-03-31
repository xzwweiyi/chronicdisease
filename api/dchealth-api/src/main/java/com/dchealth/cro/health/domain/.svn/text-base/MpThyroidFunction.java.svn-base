package com.dchealth.cro.health.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * 甲状腺功能对象 mp_thyroid_function
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpThyroidFunction {
	  /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;

    /** 自增主键 */
    private Long id;

    /** 医疗机构id */
    private String careSiteId;

    /** 患者id */
    private String personId;

    /** 就诊id */
    private String visitOccurrenceId;

    /** 就诊类型名称 */
    private String visitType;

    /** 报告时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date resultTime;

    /** 三碘甲状腺原氨酸(t3) */
    private String thyroidT3;

    /** 甲状腺素（t4） */
    private String thyroidT4;

    /** 促甲状腺激素(tsh) */
    private String thyroidTsh;

    /** 游离三碘甲状腺原氨酸(t3) */
    private String thyroidFt3;

    /** 游离甲状腺素（ft4） */
    private String thyroidFt4;

    /** 抗甲状腺球蛋白抗体 */
    private String thyroidTgab;

    /** 抗甲状腺过氧化物酶抗体 */
    private String thyroidTpoAb;

    /** 促甲状腺素受体抗体 */
    private String thyroidTrAb;

    /** 抗甲状腺微粒体抗体 */
    private String thyroidTmAb;

    /** 甲状腺球蛋白 */
    private String thyroidGlobulin;

   
}
