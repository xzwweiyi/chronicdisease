package com.dchealth.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

public class ListUtils {
    /**
     * 将元素按个数分组
     * @param list 原始list
     * @param quantity 分组条数
     * @return
     */
    public static <T> List<List<T>> splitList(List<T> list, int quantity) {
        List<List<T>> groupList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return groupList;
        }
        int count = 0;
        while (count < list.size()) {
            groupList.add(list.subList(count, (count + quantity) > list.size() ? list.size() : (count + quantity)));
            count += quantity;
        }
        return groupList;
    }
}
