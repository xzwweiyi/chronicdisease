package com.dchealth.cro.api.enums;


/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/30 16:53
 * @Version: 1.0
 */
public enum BindDeptEnum {

    NEIFENMI("369", "369", "");

    private final String tdCode;
    private final String szCode;
    private final String name;

    BindDeptEnum(String tdCode, String szCode, String name) {
        this.tdCode = tdCode;
        this.szCode = szCode;
        this.name = name;
    }

    public String getSzCode(String tdCode) {
        for (BindDeptEnum value : values()) {
            if (value.tdCode.equals(tdCode)) {
                return value.szCode;
            }
        }
        return null;
    }
}
