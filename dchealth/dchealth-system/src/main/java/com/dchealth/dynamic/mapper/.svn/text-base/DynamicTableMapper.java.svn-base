package com.dchealth.dynamic.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 单配置Mapper接口
 *
 * @author ruoyi
 * @date 2022-03-23
 */
@Repository
public interface DynamicTableMapper {

    void createTable(String createTableSql);

    /**
     * 根据表前缀查询表顺序
     * @param tableNamePre
     * @return
     */
    String queryTableOrder(String tableNamePre);

    List<String> querySamePreTable(String tableNamePre);
}
