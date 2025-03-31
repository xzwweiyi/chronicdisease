package com.dchealth.cro.health.domain;

import java.util.Date;

import lombok.Data;

/**
 * 血液生化对象 mp_blood_biochemistry
 * 
 * @author GeneMax
 * @date 2023-11-04
 */
@Data
public class MpBloodBiochemistry {
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
    private Date resultTime;

    /** 总蛋白 */
    private String bloodTp;

    /** 白蛋白 */
    private String bloodAlb;

    /** 球蛋白 */
    private String bloodGlubolin;

    /** 白蛋白/球蛋白 */
    private String bloodAToG;

    /** 天门冬氨酸氨基转移酶 */
    private String bloodAst;

    /** 丙氨酸氨基转移酶 */
    private String bloodAlt;

    /** ast/alt比值 */
    private String astToAlt;

    /** 总胆汁酸 */
    private String bloodTba;

    /** 总胆红素 */
    private String bloodTbil;

    /** 直接胆红素 */
    private String bloodDbil;

    /** 间接胆红素 */
    private String bloodIbil;

    /** 碱性磷酸酶 */
    private String bloodAlp;

    /** ꝩ-谷氨酰转移酶 */
    private String bloodYGt;

    /** 胆碱酯酶 */
    private String bloodChe;

    /** 乳酸脱氢酶 */
    private String bloodLdh;

    /** α-羟丁酸脱氢酶 */
    private String bloodHbdh;

    /** 肌酸激酶 */
    private String bloodCk;

    /** 肌酸激酶同工酶 */
    private String bloodCkMb;

    /** 尿素 */
    private String bloodBun;

    /** 肌酐 */
    private String bloodCr;

    /** 尿酸 */
    private String bloodUa;

    /** 空腹血葡萄糖 */
    private String bloodGlucose;

    /** 血清胱抑素c */
    private String bloodCpi;

    /** β2-微球蛋白 */
    private String bloodΒ2Mg;

    /** 钾 */
    private String bloodK;

    /** 钠 */
    private String bloodNa;

    /** 氯 */
    private String bloodCl;

    /** 钙 */
    private String bloodCa;

    /** 磷 */
    private String bloodIp;

    /** 镁 */
    private String bloodMg;

    /** 总二氧化碳总量 */
    private String bloodTco2;

    /** 甘油三酯 */
    private String bloodTg;

    /** 总胆固醇 */
    private String bloodCho;

    /** 高密度脂蛋白胆固醇 */
    private String bloodHdlC;

    /** 低密度脂蛋白胆固醇 */
    private String bloodLdlC;

    /** 载脂蛋白a-1 */
    private String bloodApoA1;

    /** 载脂蛋白b */
    private String bloodApoB;

    /** 载脂蛋白a-1/b */
    private String bloodApoA1ToB;

    /** 脂蛋白a */
    private String bloodLpa;

   
}
