package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdLiverFunctionItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 肝功能项目表(TdLiverFunctionItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 17:05:03
 */
public interface TdLiverFunctionItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdLiverFunctionItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdLiverFunctionItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdLiverFunctionItems> queryByPage(TdLiverFunctionItems tdLiverFunctionItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdLiverFunctionItems 实例对象
     * @return 实例对象
     */
    TdLiverFunctionItems insert(TdLiverFunctionItems tdLiverFunctionItems);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdLiverFunctionItems 实例对象
     * @return 实例对象
     */
    TdLiverFunctionItems update(TdLiverFunctionItems tdLiverFunctionItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
