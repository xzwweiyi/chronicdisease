package com.dchealth.cro.api.enums;


/**
 * @Author: wangpf
 * @Description: TODO
 * @Date: 2025/7/30 16:53
 * @Version: 1.0
 */
public enum BindDeptEnum {

    FEI_PANG_MZ("150002", "266", "肥胖门诊");

    private final String tdCode;
    private final String szCode;
    private final String name;

    BindDeptEnum(String tdCode, String szCode, String name) {
        this.tdCode = tdCode;
        this.szCode = szCode;
        this.name = name;
    }

    // 通过tdCode获取对应的szCode
    public static String getSzCode(String tdCode) {
        for (BindDeptEnum bindDept : BindDeptEnum.values()) {
            if (bindDept.tdCode.equals(tdCode)) {
                return bindDept.szCode;
            }
        }
        return null; // 或者抛出自定义异常，根据业务需求决定
    }

    // 也可以直接获取整个枚举实例
    public static BindDeptEnum getByTdCode(String tdCode) {
        for (BindDeptEnum bindDept : BindDeptEnum.values()) {
            if (bindDept.tdCode.equals(tdCode)) {
                return bindDept;
            }
        }
        return null;
    }
}
