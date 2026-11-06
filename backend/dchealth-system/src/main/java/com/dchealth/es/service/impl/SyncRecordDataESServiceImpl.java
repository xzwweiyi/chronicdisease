package com.dchealth.es.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.es.domain.form.TdHealthIndex;
import com.dchealth.es.service.SyncRecordDataESService;
import com.dchealth.research.mapper.MpESDataQueryMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @author zjl
 * @since 2023/4/18
 */
@Service("SyncRecordDataESServiceImpl")
@Slf4j
public class SyncRecordDataESServiceImpl<T> implements SyncRecordDataESService<T> {

    @Autowired
    MpESDataQueryMapper mpESDataQueryMapper;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    public static String formatStrShort = "yyyy-MM-dd";
    private static double pageSize = 300.00;


    /**
     * 获取日期偏移后日期字符串
     *
     * @param recordDateStr yyyy-MM-dd
     * @param offset
     * @return
     */
    private String getDateStr(String recordDateStr, int offset) {
        Date rDate = DateUtil.parse(recordDateStr, formatStrShort);
        return DateUtil.format(DateUtil.offsetDay(rDate, offset), formatStrShort);
    }
    public List<TdHealthIndex> getSources(SearchResponse searchResponse) {
        log.info("consume times {} mill second", searchResponse.getTook());
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<TdHealthIndex> ls = new ArrayList<>();
        if (searchHits != null && searchHits.length > 0) {
            // 将hits中的hits中的值放在对象中
            for (SearchHit hit : searchHits) {
                String str = hit.getSourceAsString();
                TdHealthIndex tTdHealthIndex = JSONObject.parseObject(str, TdHealthIndex.class);
                ls.add(tTdHealthIndex);
            }
        }
        return ls;
    }


    /**
     * 同步患者诊断信息，es中字段平铺
     * 诊断记录依次从数据库查询后插入es即可，不存在更新数据的情况，也和其他表不做关联
     * @param id
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    @Override
    public int syncMpDiagnosisInfos(String id, String startDate, String endDate) throws Exception {
        boolean isNext = true;
        try{
            SearchRequest searchRequest =
                    new SearchRequest("td_health_index");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            //基础条件-健康档案信息
            BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
            boolQueryBuilder.filter(QueryBuilders.termQuery("esDataType","1"));
            searchSourceBuilder.query(boolQueryBuilder);
            searchSourceBuilder.sort("diagnosisDate", SortOrder.DESC);
            searchSourceBuilder.size(3);
            searchSourceBuilder.from(0);
            searchRequest.source(searchSourceBuilder);
            // 执行查询，然后处理响应结果
            SearchResponse searchResponse
                    = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<TdHealthIndex> list = getSources(searchResponse);
            if(list!=null && list.size()>0 && StringUtils.isBlank(startDate)){
                startDate = DateUtil.format(list.get(0).getDiagnosisDate(),formatStrShort);
            }

            while (isNext){
                Date rDate = DateUtil.parse(startDate, formatStrShort);
                if (DateUtil.compare(rDate, new Date(), formatStrShort) >= 0 || startDate.equals(endDate)) {
                    break;
                }

                Map params = new HashMap();
                params.put("startDate",startDate);
                Integer count = mpESDataQueryMapper.queryDiagnosisInfosCount2(params);
                if (count == 0) {
                    log.info("syncTdDiagnosisInfos当天无数据={}", JSONUtil.toJsonStr(params));
                    //循环进行下一天的数据
                    startDate = getDateStr(startDate, 1);
                    continue;
                }
                //计算总页数
                Double num = Math.ceil(count / pageSize);
                int totalPage = num.intValue();
                int saveNum =0;
                for (int i = 0; i < totalPage; i++) {
                    params.put("offset",(int)(i*pageSize));
                    params.put("pageSize",(int)pageSize);
                    log.info("params={}",JSON.toJSONString(params));
                    List<TdHealthIndex> ls = mpESDataQueryMapper.queryDiagnosisInfos2(params);
                    //保存数据到es
                    saveNum += this.esBulk(ls);
                    log.info("保存患者诊断信息成功：最后一条数据,i={},TdHealthIndex={}",i,JSON.toJSONString(ls.get(ls.size()-1)));
                }
                log.info("{}当天保存诊断信息数据{}条",startDate,saveNum);
                //循环进行下一天的数据
                startDate = getDateStr(startDate, 1);
            }
        }catch (Exception e){
            log.info("同步syncTdDiagnosisInfos异常："+e.getMessage());
            e.printStackTrace();
            throw e;
        }

        return 0;
    }
    /**
     * 保存数据到es
     * @param ls
     * @return
     */
    private int esBulk(List<TdHealthIndex> ls) throws IOException {
        int rsNum = 0;
        if(ls!=null && ls.size()>0){
            BulkRequest bulkRequest = new BulkRequest();
            for(TdHealthIndex hl : ls){
                if(StringUtils.isNotBlank(hl.getPatientId())){
                    String hlStr = JSON.toJSONString(hl);
                    bulkRequest.add(new IndexRequest("td_health_index").source(hlStr, XContentType.JSON));
                }
            }
            BulkItemResponse[] items = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT).getItems();
            rsNum = items.length;
        }
        return rsNum;
    }

}
