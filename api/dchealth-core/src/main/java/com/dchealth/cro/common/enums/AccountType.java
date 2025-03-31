package com.dchealth.cro.common.enums;

/**
 * 用户状态
 * 
 * @author genemax
 */
public enum AccountType
{
    PHONE("1"), EMAIL("2");

    private final String code;

    AccountType(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

}
