package com.dchealth.es.service;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;
import com.dchealth.common.constant.GenConstants;
import com.dchealth.elasticsearch.domain.ElasticsearchResult;
import com.dchealth.es.constants.RetrievalTypeConstant;
import com.dchealth.es.domain.form.QueryCondition;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 患者信息es
 */
public interface IPatientBaseInfoESService<T> {

    /**
     * 根据动态条件查询
     * @param type 纳排类型inclusion、exclusion
     * @param clazz
     * @param queryConditions 检索条件
     * @param pageNum
     * @param pageSize
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws IOException
     */
    public ElasticsearchResult<T> findByQueryConditionByPage(String type, Class<T> clazz, List<QueryCondition> queryConditions, 
                                                             Integer pageNum, Integer pageSize) throws IllegalAccessException, NoSuchFieldException, IOException;

    /**
     * 创建query语句
     * @param queryType must,should,mustNot
     * @param queryConditions
     * @return key: must/mustNot/should
     */
    default Map<String, List<Query>> buildQuerys(String queryType, List<QueryCondition> queryConditions){
        Map<String, List<Query>> map = new HashMap<>();
        if (queryConditions != null && !queryConditions.isEmpty()){
            List<Query> should = new ArrayList<>();
            List<Query> must = new ArrayList<>();
            for (QueryCondition queryCondition : queryConditions){
                Query query = null;
                if (RetrievalTypeConstant.CONTAIN.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    if (GenConstants.HTML_INPUT.toUpperCase().equals(queryCondition.getHtmlType().toUpperCase())
                        || GenConstants.HTML_TEXTAREA.toUpperCase().equals(queryCondition.getHtmlType().toUpperCase())){
                        query = MatchQuery.of(m->m.field(queryCondition.getField()).query(String.valueOf(queryCondition.getValue())))._toQuery();
                    }else {
                        query = TermQuery.of(t->t.field(queryCondition.getField()).value(String.valueOf(queryCondition.getValue())))._toQuery();
                    }
                }else if (RetrievalTypeConstant.INTERVAL.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    query = RangeQuery.of(r->r.field(queryCondition.getField()).from(queryCondition.getFrom()).to(queryCondition.getTo()))._toQuery();
                }else if (RetrievalTypeConstant.GT.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    query = RangeQuery.of(r->r.field(queryCondition.getField()).gt(JsonData.of(queryCondition.getValue())))._toQuery();
                }else if (RetrievalTypeConstant.GTE.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    query = RangeQuery.of(r->r.field(queryCondition.getField()).gte(JsonData.of(queryCondition.getValue())))._toQuery();
                }else if (RetrievalTypeConstant.Lt.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    query = RangeQuery.of(r->r.field(queryCondition.getField()).lt(JsonData.of(queryCondition.getValue())))._toQuery();
                }else if (RetrievalTypeConstant.LTE.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    query = RangeQuery.of(r->r.field(queryCondition.getField()).lte(JsonData.of(queryCondition.getValue())))._toQuery();
                }else if (RetrievalTypeConstant.EQ.toUpperCase().equals(queryCondition.getRetrievalType().toUpperCase())){
                    query = TermQuery.of(t->t.field(queryCondition.getField()).value(String.valueOf(queryCondition.getValue())))._toQuery();
                }
                if (Operator.Or.jsonValue().toUpperCase().equals(queryCondition.getLogicType().toUpperCase())){
                    should.add(query);
                }else{
                    must.add(query);
                }
            }
            map.put("should", should);
            map.put(queryType, must);
        }
        return map;
    }
}
