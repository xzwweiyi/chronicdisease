package com.dchealth.cro.common.enums;

/**
 * 删除状态
 * 
 * @author genemax
 */
public enum DeleteFlag
{
    OK("N", "正常"), DELETED("Y", "删除");

    private final String code;
    private final String info;

    DeleteFlag(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
