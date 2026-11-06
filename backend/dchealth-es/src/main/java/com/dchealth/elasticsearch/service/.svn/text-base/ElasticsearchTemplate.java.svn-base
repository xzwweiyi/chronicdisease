package com.dchealth.elasticsearch.service;

import co.elastic.clients.elasticsearch._types.ShardStatistics;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.indices.IndexSettings;
import co.elastic.clients.elasticsearch.indices.get_mapping.IndexMappingRecord;
import co.elastic.clients.util.ObjectBuilder;
import com.dchealth.elasticsearch.domain.ElasticsearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * ES接口
 * @param <T>
 */
public interface ElasticsearchTemplate<T> {

    static final Logger log = LoggerFactory.getLogger(ElasticsearchTemplate.class);
    
    public void create1(T t) throws IOException;
    
    /**
     * 创建索引
     * @param index 索引名称
     * @param settingFn 索引setting
     * @param mappingFn 索引mapping
     * @throws IOException
     */
    public void createIndices(String index,
                              Function<IndexSettings.Builder, ObjectBuilder<IndexSettings>> settingFn,
                              Function<TypeMapping.Builder, ObjectBuilder<TypeMapping>> mappingFn) throws IOException;

    /**
     * Json格式创建索引
     * @param index
     * @param jsonContent
     * @throws IOException
     */
    public boolean createIndices(String index, String jsonContent) throws IOException;

    /**
     * 删除索引
     * @param indexs
     * @return
     * @throws IOException
     */
    public boolean deleteIndices(String index, String... indexs) throws IOException;

    /**
     * 索引是否存在
     * @param indexs
     * @return
     * @throws IOException
     */
    public boolean exists(String index, String... indexs) throws IOException;

    /**
     * 查询索引字段信息
     * @param index
     * @return
     * @throws IOException
     */
    public Map<String, IndexMappingRecord> getMapping(String index) throws IOException;
    
    /**
     * 更新索引字段
     * @param index
     * @param jsonContent
     * @return
     * @throws IOException
     */
    public boolean putMapping(String index, String jsonContent) throws IOException;
    
    /**
     * 新增
     * @param index
     * @param id
     * @param t
     * @throws IOException
     */
    public Long create(String index, String id, T t) throws IOException;

    /**
     * 新增
     * @param t
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public Long create(T t) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 新增
     * @param index
     * @param id
     * @param jsonContent json格式
     * @throws IOException
     */
    public Long create(String index, String id, String jsonContent) throws IOException;

    /**
     * index执行
     * @param request
     * @return
     * @throws IOException
     */
    public Long index(IndexRequest<T> request) throws IOException;

    /**
     * index执行
     * @param fn
     * @return
     * @throws IOException
     */
    public Long index(Function<IndexRequest.Builder<T>, ObjectBuilder<IndexRequest<T>>> fn) throws IOException;

    /**
     * 批量执行
     * @param bulkRequest
     * @return
     * @throws IOException
     */
    public Integer bulkSaveOrUpdate(BulkRequest bulkRequest) throws IOException;

    /**
     * 批量执行
     * @param fn
     * @return
     * @throws IOException
     */
    public Integer bulkSaveOrUpdate(Function<BulkRequest.Builder, ObjectBuilder<BulkRequest>> fn) throws IOException;

    /**
     * 批量执行
     * @param index
     * @param opts
     * @return
     * @throws IOException
     */
    public Integer bulkSaveOrUpdate(String index, List<BulkOperation> opts) throws IOException;

    /**
     * 批量执行
     * @param index
     * @param docMap
     * @return
     * @throws IOException
     */
    public Integer bulkSaveOrUpdate(String index, Map<String, T> docMap) throws IOException;

    /**
     * 批量执行
     * @param list
     * @return
     * @throws IOException
     */
    public Integer bulkSaveOrUpdate(List<T> list) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 新增更新
     * @param index
     * @param id
     * @param t
     * @return
     * @throws Exception
     */
    public Long saveOrUpdate(String index, String id, T t) throws Exception;

    /**
     * 新增更新
     * @param index
     * @param id
     * @param jsonContent
     * @return
     * @throws Exception
     */
    public Long saveOrUpdate(String index, String id, String jsonContent) throws Exception;

    /**
     * 新增更新
     * @param t
     * @return
     */
    public Long saveOrUpdate(T t) throws NoSuchFieldException, IllegalAccessException, IOException;

    /**
     * 删除
     * @param index
     * @param id
     * @throws IOException
     */
    public Long deleteById(String index, String id) throws IOException;

    /**
     * 批量删除
     * @param index
     * @param ids
     * @return
     * @throws IOException
     */
    public Integer bulkDeleteByIds(String index, String... ids) throws IOException;

    /**
     * 批量删除
     * @param clazz
     * @param id
     * @return
     */
    public Long deleteById(Class<T> clazz, String id) throws IOException;

    /**
     * 批量删除
     * @param clazz
     * @return
     */
    public Integer bulkDeleteByIds(Class<T> clazz, String... ids) throws IOException;

    /**
     * 根据id查询
     * @param index
     * @param id
     * @param clazz
     * @return
     * @throws IOException
     */
    public T selectById(String index, String id, Class<T> clazz) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 根据id查询
     * @param id
     * @param clazz
     * @return
     */
    public T selectById(String id, Class<T> clazz) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 查询
     * @param clazz
     * @param query
     * @return
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public List<T> selectByQuery(Class<T> clazz, Query query, String... indexs) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 查询
     * @param clazz
     * @param searchRequest
     * @return
     * @throws IOException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public ElasticsearchResult<T> selectBySearchRequest(Class<T> clazz, SearchRequest searchRequest) throws IOException, NoSuchFieldException, IllegalAccessException;

    /**
     * 查询
     * @param clazz
     * @param query
     * @param pageNum
     * @param pageSize
     * @param sorts
     * @param cardinalities
     * @param highlight
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws IOException
     */
    public ElasticsearchResult<T> selectByPage(Class<T> clazz, Query query, Integer pageNum,
                                      Integer pageSize, List<SortOptions> sorts,
                                      Map<String, Aggregation> cardinalities, Highlight highlight) throws IllegalAccessException, NoSuchFieldException, IOException;

    /**
     * refresh
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean refresh(String indexName) throws IOException;

    /**
     * flush
     * @param indexName
     * @return
     * @throws IOException
     */
    public boolean flush(String indexName) throws IOException;
    
    /**
     * 索引集合
     * @param index
     * @param indexs
     * @return
     */
    default List<String> getIndexs(String index, String... indexs){
        List<String> list = new ArrayList<>();
        if (index != null){
            list.add(index);
        }
        if (indexs != null){
            list.addAll(Arrays.asList(indexs));
        }
        return list;
    }

    /**
     * 判断索引refresh/flush是否成功
     * @param response
     * @return
     */
    default boolean doOperation(ShardStatistics response) {
        int failedShards = response.failures().size();
        if (failedShards == response.total().intValue()) {
            log.error("ES索引刷新失败 {}", response.failures());
            return false;
        } else if (failedShards > 0) {
            log.error("ES索引刷新部分分片失败 {}", response.failures());
        }
        log.info("ES索引刷新成功");
        return true;
    }

}
