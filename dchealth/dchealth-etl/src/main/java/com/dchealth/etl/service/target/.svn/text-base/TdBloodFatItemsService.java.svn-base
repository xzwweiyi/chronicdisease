package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdBloodFatItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 血脂项目表(TdBloodFatItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 17:06:28
 */
public interface TdBloodFatItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdBloodFatItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdBloodFatItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdBloodFatItems> queryByPage(TdBloodFatItems tdBloodFatItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdBloodFatItems 实例对象
     * @return 实例对象
     */
    TdBloodFatItems insert(TdBloodFatItems tdBloodFatItems);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdBloodFatItems 实例对象
     * @return 实例对象
     */
    TdBloodFatItems update(TdBloodFatItems tdBloodFatItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
