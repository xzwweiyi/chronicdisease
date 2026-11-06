package com.dchealth.elasticsearch.domain;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;

import java.util.List;
import java.util.Map;

/**
 * 结果
 */
public class ElasticsearchResult<T> {

    /**
     * 聚合
     */
    private Map<String, Aggregate> cardinalities;

    /**
     * 总数
     */
    private Long total;

    /**
     * 数据
     */
    private List<T> list;

    public Map<String, Aggregate> getCardinalities() {
        return cardinalities;
    }

    public void setCardinalities(Map<String, Aggregate> cardinalities) {
        this.cardinalities = cardinalities;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
