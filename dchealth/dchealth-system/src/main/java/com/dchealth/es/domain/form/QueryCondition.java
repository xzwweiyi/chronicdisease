package com.dchealth.es.domain.form;

import com.dchealth.common.constant.GenConstants;
import com.dchealth.es.constants.RetrievalTypeConstant;
import lombok.Data;

/**
 * 查询条件
 */
public class QueryCondition {
    /**
     * 字段
     */
    private String field;

    /**
     * 检索条件
     */
    private String retrievalType = RetrievalTypeConstant.CONTAIN;

    /**
     * 字段类型
     */
    private String fieldType;

    /**
     * html类型
     */
    private String htmlType = GenConstants.HTML_INPUT;

    /**
     * 值
     */
    private Object value;

    /**
     * 逻辑关系
     */
    private String logicType;
    
    private String from;
    
    private String to;

    public String getField() {
        return field;
    }

    public QueryCondition field(String field) {
        this.field = field;
        return this;
    }

    public String getRetrievalType() {
        return retrievalType;
    }

    public QueryCondition retrievalType(String retrievalType) {
        this.retrievalType = retrievalType;
        return this;
    }

    public String getFieldType() {
        return fieldType;
    }

    public QueryCondition fieldType(String fieldType) {
        this.fieldType = fieldType;
        return this;
    }

    public String getHtmlType() {
        return htmlType;
    }

    public QueryCondition htmlType(String htmlType) {
        this.htmlType = htmlType;
        return this;
    }

    public Object getValue() {
        return value;
    }

    public QueryCondition value(Object value) {
        this.value = value;
        return this;
    }

    public String getLogicType() {
        return logicType;
    }

    public QueryCondition logicType(String logicType) {
        this.logicType = logicType;
        return this;
    }

    public String getFrom() {
        return from;
    }

    public QueryCondition from(String from) {
        this.from = from;
        return this;
    }

    public String getTo() {
        return to;
    }

    public QueryCondition to(String to) {
        this.to = to;
        return this;
    }
}
