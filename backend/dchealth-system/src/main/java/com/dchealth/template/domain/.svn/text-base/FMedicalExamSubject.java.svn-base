package com.dchealth.template.domain;

import com.dchealth.common.annotation.Excel;
import com.dchealth.template.domain.vo.FMedicalValueVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 属性题目对象 f_medical_exam_subject
 * 
 * @author lchbu
 * @date 2022-10-31
 */
@Data
public class FMedicalExamSubject
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 属性编码 */
    @Excel(name = "属性编码")
    private String propertyCode;

    /** 属性名称 */
    @Excel(name = "属性名称")
    private String propertyName;

    /** 属性父id */
    @Excel(name = "属性父id")
    private Long parentId;

    /** 属性类型 */
    @Excel(name = "属性类型")
    private String propertyType;

    /** 字典表类型 */
    @Excel(name = "字典表类型")
    private String dictType;

    /** 模板ID */
    @Excel(name = "模板ID")
    private Long medicalFormId;

    /** 是否开启APP端属性填写 0未开启 1 开启  */
    @Excel(name = "是否开启APP端属性填写 0未开启 1 开启 ")
    private String openProperty;

    /** 参考卷王里的数据 */
    @Excel(name = "参考卷王里的数据")
    private String attribute;

    /** 排序 */
    @Excel(name = "排序")
    private Integer sortNum;

    /** 是否参与记分（0-否，1-是） */
    @Excel(name = "是否参与记分", readConverterExp = "0=-否，1-是")
    private String ifScore;

    /** 题干分数 */
    private Long subjectScores;

    private List<FMedicalExamSubject> examSubjects;

    /** 题目选项 */
    private List<FMedicalExamItem> itemList;

    /** 题目提交属性值 */
    private List<FMedicalValueVo> propertyValues;

    /**
     * 图片
     */
    private List<String> imageUrls;

    /** cms关联id */
    private Long articleId;

    private String name;

    private Long value;

    private String tableName;

    private String propertyValue;

    /** 其它属性值 */
    private String other;

    private Integer pageNum =1;

    private Integer pageSize =10;

    private Date createTime;

    private Date updateTime;

    /** 创建者 */
    private String createBy;

    private Long followId;


    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPropertyCode(String propertyCode) 
    {
        this.propertyCode = propertyCode;
    }

    public String getPropertyCode() 
    {
        return propertyCode;
    }
    public void setPropertyName(String propertyName) 
    {
        this.propertyName = propertyName;
    }

    public String getPropertyName() 
    {
        return propertyName;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setPropertyType(String propertyType) 
    {
        this.propertyType = propertyType;
    }

    public String getPropertyType() 
    {
        return propertyType;
    }
    public void setDictType(String dictType) 
    {
        this.dictType = dictType;
    }

    public String getDictType() 
    {
        return dictType;
    }
    public void setMedicalFormId(Long medicalFormId) 
    {
        this.medicalFormId = medicalFormId;
    }

    public Long getMedicalFormId() 
    {
        return medicalFormId;
    }
    public void setOpenProperty(String openProperty) 
    {
        this.openProperty = openProperty;
    }

    public String getOpenProperty() 
    {
        return openProperty;
    }
    public void setAttribute(String attribute) 
    {
        this.attribute = attribute;
    }

    public String getAttribute() 
    {
        return attribute;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public void setIfScore(String ifScore)
    {
        this.ifScore = ifScore;
    }

    public String getIfScore() 
    {
        return ifScore;
    }

    public List<FMedicalExamItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<FMedicalExamItem> itemList) {
        this.itemList = itemList;
    }


}
