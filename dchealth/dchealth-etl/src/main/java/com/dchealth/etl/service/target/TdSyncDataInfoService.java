package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdSyncDataInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 数据同步信息(TdSyncDataInfo)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 16:17:57
 */
public interface TdSyncDataInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdSyncDataInfo queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdSyncDataInfo 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdSyncDataInfo> queryByPage(TdSyncDataInfo tdSyncDataInfo, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdSyncDataInfo 实例对象
     * @return 实例对象
     */
    TdSyncDataInfo insert(TdSyncDataInfo tdSyncDataInfo);

    /**
     * 修改数据
     *
     * @param tdSyncDataInfo 实例对象
     * @return 实例对象
     */
    TdSyncDataInfo update(TdSyncDataInfo tdSyncDataInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 通过tableName查询单条数据
     *
     * @param tableName
     * @return 实例对象
     */
    TdSyncDataInfo queryByTableName(String tableName);

}
