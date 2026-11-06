package com.dchealth.medical.vo.xunfei;

import lombok.Data;

import java.util.Map;

/**
 * 电话模版动态参数
 */
@Data
public class Var {

    /**
     * 参数名
     */
    private String remarks;

    /**
     * 变量内容
     */
    private String content;

    private Integer limit;

    private String original;

    public Var(){

    }

    public Var(String remarks,String content){
        this.remarks=remarks;
        this.content=content;
    }
}
