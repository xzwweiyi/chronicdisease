package com.dchealth.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import co.elastic.clients.elasticsearch.sql.QueryResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import co.elastic.clients.util.ObjectBuilder;
import com.dchealth.elasticsearch.domain.ElasticsearchResult;
import com.dchealth.elasticsearch.exception.ElasticsearchException;
import com.dchealth.elasticsearch.service.ElasticsearchTemplate;
import com.dchealth.elasticsearch.utils.IndexTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class ElasticsearchTemplateImpl<T> implements ElasticsearchTemplate<T> {

    private static final Logger log = LoggerFactory.getLogger(ElasticsearchTemplateImpl.class);
    
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private IndexTools indexTools;

    @Override
    public void create1(T t) throws IOException {
        CreateRequest.Builder builder = new CreateRequest.Builder();
        builder.id("906");
        builder.document(t);
        elasticsearchClient.create(builder.build()).version();
    }

    @Override
    public void createIndices(String index,
                              Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> settingFn,
                              Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn) throws IOException {
        CreateIndexResponse createIndexResponse = elasticsearchClient
                .indices()
                .create(c->c
                        .index(index)
                        .settings(settingFn)
                        .mappings(mappingFn)
                );
    }

    @Override
    public boolean createIndices(String index, String jsonContent) throws IOException {
        // 根据Reader创建请求对象
        CreateIndexRequest request = CreateIndexRequest.of(builder -> builder
                .index(index)
                .withJson(new StringReader(jsonContent)));

        CreateIndexResponse response = elasticsearchClient.indices().create(request);
        return response.acknowledged();
    }

    @Override
    public boolean deleteIndices(String index, String... indexs) throws IOException {
        DeleteIndexResponse response = elasticsearchClient.indices().delete(fn->fn.index(getIndexs(index, indexs)));
        return response.acknowledged();
    }

    @Override
    public boolean exists(String index, String... indexs) throws IOException {
        BooleanResponse response = elasticsearchClient.indices().exists(fn->fn.index(getIndexs(index, indexs)));
        return response.value();
    }

    @Override
    public Map<String, IndexMappingRecord> getMapping(String index) throws IOException {
        GetMappingResponse response = elasticsearchClient.indices().getMapping(fn->fn.index(index));
        return response.result();
    }

    @Override
    public boolean putMapping(String index, String jsonContent) throws IOException {
        PutMappingResponse response = elasticsearchClient.indices().putMapping(fn->fn.index(index).withJson(new StringReader(jsonContent)));
        return response.acknowledged();
    }

    @Override
    public Long create(String index, String id, T t) throws IOException {
        CreateRequest.Builder builder = new CreateRequest.Builder();
        builder.index(index);
        if (id != null){
            builder.id(id);
        }
        builder.document(t);
        return elasticsearchClient.create(builder.build()).version();
    }

    @Override
    public Long create(T t) throws IOException, NoSuchFieldException, IllegalAccessException {
        CreateRequest.Builder builder = new CreateRequest.Builder();
        builder.index(indexTools.getIndex(t.getClass()));
        builder.id(indexTools.getIdValue(t));
        builder.document(t);
        return elasticsearchClient.create(builder.build()).version();
    }

    @Override
    public Long create(String index, String id, String jsonContent) throws IOException {
        CreateRequest.Builder builder = new CreateRequest.Builder();
        builder.index(index);
        if (id != null){
            builder.id(id);
        }
        builder.withJson(new StringReader(jsonContent));
        return elasticsearchClient.create(builder.build()).version();
    }

    @Override
    public Long index(IndexRequest<T> request) throws IOException {
        return elasticsearchClient.index(request).version();
    }

    @Override
    public Long index(Function<IndexRequest.Builder<T>, ObjectBuilder<IndexRequest<T>>> fn) throws IOException {
        return elasticsearchClient.index(fn).version();
    }

    @Override
    public Integer bulkSaveOrUpdate(BulkRequest bulkRequest) throws IOException {
        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);
        return bulkResponse.items().size();
    }

    @Override
    public Integer bulkSaveOrUpdate(Function<BulkRequest.Builder, ObjectBuilder<BulkRequest>> fn) throws IOException {
        BulkResponse bulkResponse = elasticsearchClient.bulk(fn);
        return bulkResponse.items().size();
    }

    @Override
    public Integer bulkSaveOrUpdate(String index, List<BulkOperation> opts) throws IOException {
        BulkRequest bulkRequest = BulkRequest.of(b->b
                .index(index)
                .operations(opts));
        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);
        return bulkResponse.items().size();
    }
    
    @Override
    public Integer bulkSaveOrUpdate(String index, Map<String, T> docMap) throws IOException {
        List<BulkOperation> opts = new ArrayList<>();
        docMap.entrySet().stream().forEach(doc -> {
            IndexOperation<T> indexOpe = new IndexOperation.Builder<T>()
                    .id(doc.getKey())
                    .document(doc.getValue())
                    .build();
            BulkOperation opt = new BulkOperation.Builder().index(indexOpe).build();
            opts.add(opt);
        });
        BulkRequest bulkRequest = BulkRequest.of(b->b
                .index(index)
                .operations(opts));
        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);
        return bulkResponse.items().size();
    }

    @Override
    public Integer bulkSaveOrUpdate(List<T> list) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (list == null || list.isEmpty()){
            return 0;
        }
        T t = list.get(0);
        List<BulkOperation> opts = new ArrayList<>();
        for (T obj : list){
            IndexOperation<T> indexOpe = new IndexOperation.Builder<T>()
                    .id(indexTools.getIdValue(obj))
                    .document(obj)
                    .build();
            BulkOperation opt = new BulkOperation.Builder().index(indexOpe).build();
            opts.add(opt);
        }
        BulkRequest bulkRequest = BulkRequest.of(b->b
                .index(indexTools.getIndex(t))
                .operations(opts));
        BulkResponse bulkResponse = elasticsearchClient.bulk(bulkRequest);
        return bulkResponse.items().size();
    }

    @Override
    public Long saveOrUpdate(String index, String id, T t) throws Exception {
        IndexRequest.Builder builder = new IndexRequest.Builder();
        if (index == null){
            throw new ElasticsearchException("index is null");
        }
        if (t == null){
            throw new ElasticsearchException(t.getClass() + " is null");
        }
        builder.index(index);
        if (id != null){
            builder.id(id);
        }
        builder.document(t);
        return elasticsearchClient.index(builder.build()).version();
    }

    @Override
    public Long saveOrUpdate(String index, String id, String jsonContent) throws IOException {
        if (index == null){
            throw new ElasticsearchException("index name is null");
        }
        IndexRequest.Builder builder = new IndexRequest.Builder();
        builder.index(index);
        if (id != null){
            builder.id(id);
        }
        builder.withJson(new StringReader(jsonContent));
        return elasticsearchClient.index(builder.build()).version();
    }

    @Override
    public Long saveOrUpdate(T t) throws NoSuchFieldException, IllegalAccessException, IOException {
        IndexRequest.Builder builder = new IndexRequest.Builder();
        builder.index(indexTools.getIndex(t));
        builder.id(indexTools.getIdValue(t));
        builder.document(t);
        return elasticsearchClient.index(builder.build()).version();
    }

    @Override
    public Long deleteById(String index, String id) throws IOException {
        DeleteRequest.Builder builder = new DeleteRequest.Builder();
        builder.index(index);
        builder.id(id);
        return elasticsearchClient.delete(builder.build()).version();
    }

    @Override
    public Integer bulkDeleteByIds(String index, String... ids) throws IOException {
        List<BulkOperation> opts = new ArrayList<>();
        for (String id : ids){
            BulkOperation bulkOperation = new BulkOperation.Builder()
                    .delete(x->x
                            .id(id))
                    .build();
            opts.add(bulkOperation);
        }
        BulkRequest.Builder bulkRequest = new BulkRequest.Builder()
                .index(index)
                .operations(opts);
        return elasticsearchClient.bulk(bulkRequest.build()).items().size();
    }

    @Override
    public Long deleteById(Class<T> clazz, String id) throws IOException {
        return deleteById(indexTools.getIndex(clazz), id);
    }

    @Override
    public Integer bulkDeleteByIds(Class<T> clazz, String... ids) throws IOException {
        return bulkDeleteByIds(indexTools.getIndex(clazz), ids);
    }

    @Override
    public T selectById(String index, String id, Class<T> clazz) throws IOException, NoSuchFieldException, IllegalAccessException {
        GetResponse<JsonData> getResponse = elasticsearchClient.get(g->g.index(index).id(id), JsonData.class);
        if (getResponse.found()){
            T t = getResponse.source().to(clazz);
            indexTools.setIdValue(t, id);
        }
        return null;
    }

    @Override
    public T selectById(String id, Class<T> clazz) throws IOException, NoSuchFieldException, IllegalAccessException {
        return selectById(indexTools.getIndex(clazz), id, clazz);
    }

    @Override
    public List<T> selectByQuery(Class<T> clazz, Query query, String... indexs) throws IOException, NoSuchFieldException, IllegalAccessException {
        if (indexs == null){
            indexs = new String[]{indexTools.getIndex(clazz)};
        }
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(Arrays.asList(indexs))
                .query(query)
                .build();
        return selectBySearchRequest(clazz,searchRequest).getList();
    }

    @Override
    public ElasticsearchResult<T> selectBySearchRequest(Class<T> clazz, SearchRequest searchRequest) throws IOException, NoSuchFieldException, IllegalAccessException {
        SearchResponse<JsonData> searchResponse = elasticsearchClient.search(searchRequest, JsonData.class);
        ElasticsearchResult<T> result = new ElasticsearchResult();
        result.setCardinalities(searchResponse.aggregations());
        result.setTotal(searchResponse.hits().total().value());
        List<Hit<JsonData>> hits = searchResponse.hits().hits();
        if (hits == null){
            return null;
        }
        List<T> list = new ArrayList<>();
        for (Hit<JsonData> hit : hits){
            JsonData jd = hit.source();
            if(jd != null){
                T t = jd.to(clazz);
                indexTools.setValue(t, hit);
                list.add(t);
            }
        }
        result.setList(list);
        return result;
    }

    @Override
    public ElasticsearchResult<T> selectByPage(Class<T> clazz, Query query, Integer pageNum, 
                                Integer pageSize, List<SortOptions> sorts,
                                Map<String, Aggregation> cardinalities, Highlight highlight) throws IllegalAccessException, NoSuchFieldException, IOException {
        SearchRequest.Builder builder = new SearchRequest.Builder()
                .index(indexTools.getIndex(clazz))
                .query(query);
        
        if (query != null){
            builder.query(query);
        }
        
        if (cardinalities != null){
            builder.aggregations(cardinalities);
        }

        //查询数量
        /*CountRequest.Builder countRequest = new CountRequest.Builder()
                .index(indexTools.getIndex(clazz));
        if (query != null){
            countRequest.query(query);
        }
        CountResponse countResponse = elasticsearchClient.count(countRequest.build());
        long total = countResponse.count();*/
        builder.from((pageNum - 1)*pageSize);
        builder.size(pageSize);
        if (sorts != null){
            builder.sort(sorts);
        }
        if (highlight != null){
            builder.highlight(highlight);
        }
        return selectBySearchRequest(clazz, builder.build());
    }

    /**
     * 索引refresh
     *
     * @param indexName 索引名
     * @return refresh是否成功，true成功 false失败
     * @throws IOException 异常信息
     */
    @Override
    public boolean refresh(String indexName) throws IOException {
        RefreshResponse response = elasticsearchClient.indices().refresh(request -> request.index(indexName));
        return doOperation(response.shards());
    }

    /**
     * 索引flush
     *
     * @param indexName 索引名
     * @return flush是否成功，true成功 false失败
     * @throws IOException 异常信息
     */
    @Override
    public boolean flush(String indexName) throws IOException {
        FlushResponse response = elasticsearchClient.indices().flush(request -> request.index(indexName));
        return doOperation(response.shards());
    }
    
    /**
     * 创建索引示例
     * @throws IOException
     */
    private void testCreateIndices() throws IOException {
        // 索引名
        String indexName = "product002";

        // 构建setting时，builder用到的lambda
        Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> settingFn = sBuilder -> sBuilder
                .index(iBuilder -> iBuilder
                        // 三个分片
                        .numberOfShards("3")
                        // 一个副本
                        .numberOfReplicas("1")
                );

        // 新的索引有三个字段，每个字段都有自己的property，这里依次创建
        Property keywordProperty = Property.of(pBuilder -> pBuilder.keyword(kBuilder -> kBuilder.ignoreAbove(256)));
        Property textProperty = Property.of(pBuilder -> pBuilder.text(tBuilder -> tBuilder));
        Property integerProperty = Property.of(pBuilder -> pBuilder.integer(iBuilder -> iBuilder));

        // // 构建mapping时，builder用到的lambda
        Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn = mBuilder -> mBuilder
                .properties("name", keywordProperty)
                .properties("description", textProperty)
                .properties("price", integerProperty);

        // 创建索引，并且指定了setting和mapping
        createIndices(indexName, settingFn, mappingFn);
    }

    private void testCreateIndicesJsonContent() throws IOException {
        // 文件名
        String jsonContent = "{" +
                "  \"settings\": {" +
                "    \"number_of_shards\": 3," +
                "    \"number_of_replicas\": 1" +
                "  }," +
                "  \"mappings\": {" +
                "    \"properties\": {" +
                "      \"name\": {" +
                "        \"type\": \"keyword\"," +
                "        \"ignore_above\": 256" +
                "      }," +
                "      \"description\": {" +
                "        \"type\": \"text\"" +
                "      }," +
                "      \"price\": {" +
                "        \"type\": \"integer\"" +
                "      }" +
                "    }" +
                "  }" +
                "}";

        // 索引名
        String indexName = "product005";

        // 通过InputStream创建索引
        createIndices(indexName, jsonContent);
    }

    public static void main(String[] args) {
        /*Map<String, String> map = new HashMap<>();
        map.put(null,"1");
        map.put(null,"2");
        System.out.println("args = " + Arrays.deepToString(args));*/
        String s = null;
        System.out.println("args = " + s.length());
    }
}
