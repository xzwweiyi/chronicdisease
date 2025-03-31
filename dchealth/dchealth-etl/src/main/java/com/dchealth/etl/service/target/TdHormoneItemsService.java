package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdHormoneItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 激素项目表(TdHormoneItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:39:03
 */
public interface TdHormoneItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdHormoneItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdHormoneItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdHormoneItems> queryByPage(TdHormoneItems tdHormoneItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdHormoneItems 实例对象
     * @return 实例对象
     */
    TdHormoneItems insert(TdHormoneItems tdHormoneItems);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdHormoneItems> entities);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdHormoneItems 实例对象
     * @return 实例对象
     */
    TdHormoneItems update(TdHormoneItems tdHormoneItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
