package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdCoagulationItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 凝血项目表(TdCoagulationItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 17:11:42
 */
public interface TdCoagulationItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdCoagulationItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdCoagulationItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdCoagulationItems> queryByPage(TdCoagulationItems tdCoagulationItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdCoagulationItems 实例对象
     * @return 实例对象
     */
    TdCoagulationItems insert(TdCoagulationItems tdCoagulationItems);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdCoagulationItems 实例对象
     * @return 实例对象
     */
    TdCoagulationItems update(TdCoagulationItems tdCoagulationItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
