package com.dchealth.elasticsearch.utils;

import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caihe
 * @since 2023/4/26
 */
@Slf4j
public class ESQueryUtils {



    /**
     * 获取Hit对象列表
     *
     * @param response SearchResponse对象
     * @return 文档对象Hit泛型列表
     * @param <T> 文档对象泛型
     */
    public static <T> List<Hit<T>> getHitList(SearchResponse response) {
        log.info("consume times {} mill second", response);

        List<Hit<T>> hitList = new ArrayList<>();

        if (CollectionUtils.isEmpty(hitList)) {
            return new ArrayList<>();
        }

        return hitList;
    }
}
