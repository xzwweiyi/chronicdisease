package com.dchealth.etl.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zjl
 * @since 2023/3/28
 */
public class BaseEntity implements Serializable{
    private static final long serialVersionUID = 1L;

    /** 搜索值 */
    private String searchValue;

    /** 备注 */
    private String remark;

    /** 请求参数 */
    private Map<String, Object> params;

    public String getSearchValue()
    {
        return searchValue;
    }

    public void setSearchValue(String searchValue)
    {
        this.searchValue = searchValue;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public Map<String, Object> getParams()
    {
        if (params == null)
        {
            params = new HashMap<>();
        }
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }


}
