package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.target.TdSyncDataInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * 数据同步信息(TdSyncDataInfo)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-27 16:17:57
 */
public interface TdSyncDataInfoMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdSyncDataInfo queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param tdSyncDataInfo 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<TdSyncDataInfo> queryAllByLimit(TdSyncDataInfo tdSyncDataInfo, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param tdSyncDataInfo 查询条件
     * @return 总行数
     */
    long count(TdSyncDataInfo tdSyncDataInfo);

    /**
     * 新增数据
     *
     * @param tdSyncDataInfo 实例对象
     * @return 影响行数
     */
    int insert(TdSyncDataInfo tdSyncDataInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdSyncDataInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TdSyncDataInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TdSyncDataInfo> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<TdSyncDataInfo> entities);

    /**
     * 修改数据
     *
     * @param tdSyncDataInfo 实例对象
     * @return 影响行数
     */
    int update(TdSyncDataInfo tdSyncDataInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

    /**
     * 通过tableName查询单条数据
     *
     * @param tableName
     * @return 实例对象
     */
    TdSyncDataInfo queryByTableName(String tableName);
}

