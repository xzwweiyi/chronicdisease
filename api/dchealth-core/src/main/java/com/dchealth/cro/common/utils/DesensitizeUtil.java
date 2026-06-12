package com.dchealth.cro.common.utils;


import com.dchealth.cro.common.constant.DesensitizeType;

import java.util.ArrayList;
import java.util.List;

public class DesensitizeUtil {

    public static String mask(String value, DesensitizeType type) {
        switch (type) {
            case NAME:
                return maskName(value);
            case PHONE:
                return maskPhone(value);
            case ID_CARD:
                return maskIdCard(value);
            case ADDRESS:
                return maskAddress(value);
            case SOCIAL_CARD:
                return maskSocialCard(value);
            default:
                return value;
        }
    }

    private static String maskName(String name) {
        return name.length() <= 1 ? name : name.charAt(0) + "**";
    }

    private static String maskPhone(String phone) {
        if (phone.length() < 7) return phone;
        return phone.substring(0, 3) + "*****" + phone.substring(phone.length() - 2);
    }

    /**
     * 身份证号脱敏（兼容15位老证 & 18位新证）
     * 规则：保留前4位 + 后4位，中间全部替换为 '*'
     * 示例：
     *   "110101123456789" (15位) → "1101******789"
     *   "11010119900307235X" (18位) → "1101**********235X"
     *
     * @param id 身份证号（支持 null、空格、大小写 X）
     * @return 脱敏后字符串；非法格式返回 "***"；null/空返回原值
     */
    private static String maskIdCard(String id) {
        // 空值直返（不脱敏，避免 NPE）
        if (id == null || id.trim().isEmpty()) {
            return id;
        }
        String cleanId = id.trim();

        // 长度校验：只接受 15 或 18 位
        int len = cleanId.length();
        if (len != 15 && len != 18) {
            return "***";
        }

        // 格式校验：15位全数字；18位前17位数字 + 第18位数字或X/x
        boolean isValid = false;
        if (len == 15) {
            isValid = cleanId.matches("\\d{15}");
        } else if (len == 18) {
            isValid = cleanId.matches("\\d{17}[\\dXx]");
        }
        if (!isValid) {
            return "***";
        }

        // ✅ 核心脱敏：前4 + 中间* + 后4
        int starCount = len - 8;
        String maskedMiddle = starCount > 0
                ? new String(new char[starCount]).replace("\0", "*")
                : "";

        return cleanId.substring(0, 4) + maskedMiddle + cleanId.substring(len - 4);
    }

    private static String maskSocialCard(String card) {
        if (card.length() < 8) return card;
        return card.substring(0, 2) + "*********" + card.substring(card.length() - 2);
    }

    private static String maskAddress(String address) {
        if (StringUtils.isBlank(address)) {
            return "";
        }

        List<String> masked = new ArrayList<>();

        if (address.contains("省")) {
            masked.add("***省");
        }
        if (address.contains("市")) {
            masked.add("**市");
        }
        if (address.contains("区") || address.contains("县")) {
            masked.add("****区");
        }
        if (address.contains("街") || address.contains("乡") || address.contains("镇")) {
            masked.add("***街道");
        }
        if (address.contains("号") || address.contains("楼")) {
            masked.add("***号楼");
        }
        if (address.contains("单元")) {
            masked.add("**单元");
        }

        return String.join("，", masked);
    }
}
