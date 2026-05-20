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

    private static String maskIdCard(String id) {
        if (id.length() < 8) return id;
        return id.substring(0, 2) + "*********" + id.substring(id.length() - 2);
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
