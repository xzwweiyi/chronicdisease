package com.dchealth.cro.medical.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * app 满意度评价(AppSatisfactionEvaluation)实体类
 *
 * @author makejava
 * @since 2023-05-26 14:53:42
 */
public class AppSatisfactionEvaluation implements Serializable {
    private static final long serialVersionUID = 440628988325488661L;

    private Integer pageNo =1;
    private Integer pageSize =20;

    private Integer id;
    /**
     * 1:医生；2：患者
     */
    private String type;
    /**
     * 医生或患者id
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 评分 1~5 分
     */
    private Long evaluationScores;
    /**
     * 评价描述
     */
    private String evaluationDesc;
    /**
     * 提交时间
     */
    private Date createTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getEvaluationScores() {
        return evaluationScores;
    }

    public void setEvaluationScores(Long evaluationScores) {
        this.evaluationScores = evaluationScores;
    }

    public String getEvaluationDesc() {
        return evaluationDesc;
    }

    public void setEvaluationDesc(String evaluationDesc) {
        this.evaluationDesc = evaluationDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}

