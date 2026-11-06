package com.dchealth.medical.domain;

import com.github.pagehelper.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 那排条件检索树(PatientEnrollmentConditionTree)实体类
 *
 * @author makejava
 * @since 2023-05-05 15:17:56
 */
public class PatientEnrollmentConditionTree implements Serializable {
    private static final long serialVersionUID = -49219514986819096L;
    
    private Integer id;
    /**
     * 级别
     */
    private Integer level;
    /**
     * 数据表
     */
    private String tableCode;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 字段
     */
    private String field;
    /**
     * 字段名称
     */
    private String fieldName;
    /**
     * 字段类型
     */
    private String fieldType;
    /**
     * 父表
     */
    private String parentTable;
    /**
     * 状态 1：有效
     */
    private Integer state;
    /**
     * 排序号
     */
    private Integer orderNum;
    
    private Date createTime;
    /**
     * 是否具体字段 1：是
     */
    private Integer isField;
    /**
     * 子节点
     */
    private List<PatientEnrollmentConditionTree> childrenList;

    public PatientEnrollmentConditionTree(){}

    public List<PatientEnrollmentConditionTree> getTree (List<PatientEnrollmentConditionTree> list){
        List<PatientEnrollmentConditionTree> firstLevelList = new ArrayList<>();
        if(list!=null && list.size()>0){
            //循环第一级树节点
            list.stream().filter(t->t.getLevel()==1&&"diagnosis".equalsIgnoreCase(t.getTableCode())).forEach(t1->{

                //循环第二级树节点
                List<PatientEnrollmentConditionTree> childList2 = new ArrayList<>();
                list.stream().filter(t2->t2.getLevel()==2).forEach(t2->{

                    List<PatientEnrollmentConditionTree> childList3 = new ArrayList<>();
                    if(StringUtil.isNotEmpty(t1.getTableCode()) && t1.getTableCode().equalsIgnoreCase(t2.getParentTable()) ){

                        //循环第三级树节点
                        list.stream().filter(t3->t3.getLevel()==3).forEach(t3->{
                            if(StringUtil.isNotEmpty(t2.getTableCode()) && t2.getTableCode().equalsIgnoreCase(t3.getParentTable()) ){
                                childList3.add(t3);
                            }
                        });

                        childList2.add(t2);
                    }
                    t2.setChildrenList(childList3);
                });

                t1.setChildrenList(childList2);
                firstLevelList.add(t1);

            });
        }
        return firstLevelList;
    }

    public List<PatientEnrollmentConditionTree> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<PatientEnrollmentConditionTree> childrenList) {
        this.childrenList = childrenList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getParentTable() {
        return parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsField() {
        return isField;
    }

    public void setIsField(Integer isField) {
        this.isField = isField;
    }

}

