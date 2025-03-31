package com.dchealth.cro.medical.vo;

import lombok.Data;
@Data
public class ResearchListVO {
	
	  /** 主图 */
    private String imageUrl;

    private String projectName;

    private Integer hospitalNum;//中心数
    
    private Integer totalNum;//目标入组数
    
    private Integer patientNum;//当前入组数
    private Integer crcNum;//CRC数量
    
    private Long researchId;
    
    private String templateType;
    private String researchSynopsis;
    private String joinRate;//招募进度
    private String authType;//2有权限3无权限

    private String tips="0";//0没有待处理1有待处理
	private String isStandardExist = "1";// 0 = 不存在 1 = 存在 
}
