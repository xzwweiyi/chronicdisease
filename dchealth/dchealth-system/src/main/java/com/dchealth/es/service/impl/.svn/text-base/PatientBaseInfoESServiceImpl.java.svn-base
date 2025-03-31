package com.dchealth.es.service.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.dchealth.elasticsearch.domain.ElasticsearchResult;
import com.dchealth.elasticsearch.service.ElasticsearchTemplate;
import com.dchealth.es.domain.form.QueryCondition;
import com.dchealth.es.service.IPatientBaseInfoESService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PatientBaseInfoESServiceImpl<T> implements IPatientBaseInfoESService<T> {
    
    private static final Logger log = LoggerFactory.getLogger(PatientBaseInfoESServiceImpl.class);
    
    @Autowired
    private ElasticsearchTemplate<T> elasticsearchTemplate;


    @Override
    public ElasticsearchResult<T> findByQueryConditionByPage(String type, Class<T> clazz, List<QueryCondition> list, 
                                                          Integer pageNum, Integer pageSize) throws IllegalAccessException, NoSuchFieldException, IOException {
        if (list == null || list.isEmpty()){
            return elasticsearchTemplate.selectByPage(clazz, null, pageNum, pageSize, null, null, null);
        }
        String queryType = QueryType.INCLUSION.toUpperCase().equals(type.toUpperCase()) ?
                QueryType.MUST : QueryType.MUST_NOT;
        Map<String, List<Query>> map = buildQuerys(queryType, list);
        List<Query> must = map.get(queryType);
        List<Query> should = map.get("should");
        
        Query.Builder builder = new Query.Builder();
        BoolQuery.Builder bool = new BoolQuery.Builder();
        if (queryType.toUpperCase().equals(QueryType.MUST.toUpperCase())){
            if (must != null && !must.isEmpty()){
                bool.must(must);
            }
            if (should != null && !should.isEmpty()){
                bool.must(m->m.bool(b->b.should(should)));
            }
        }else if (queryType.toUpperCase().equals(QueryType.MUST_NOT.toUpperCase())){
            if (must != null && !must.isEmpty()){
                bool.mustNot(must);
            }
            if (should != null && !should.isEmpty()){
                bool.mustNot(m->m.bool(b->b.should(should)));
                bool.minimumShouldMatch("1");
            }
        }else{
            log.info("没有可选查询类型,敬请等候,开发中...");
        }
        
        builder.bool(bool.build());
        return elasticsearchTemplate.selectByPage(clazz, builder.build(), pageNum, pageSize, null, null, null);
    }
    
    static class QueryType{
        public static String MUST = "must";
        public static String MUST_NOT = "mustNot";
        public static String SHOULD = "should";
        public static String INCLUSION = "inclusion";//纳入条件
        public static String EXCLUSION = "exclusion";//排除条件
    }
}
