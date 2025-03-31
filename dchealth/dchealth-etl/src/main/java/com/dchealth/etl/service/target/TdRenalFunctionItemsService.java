package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdRenalFunctionItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 肾功能项目表(TdRenalFunctionItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 17:05:33
 */
public interface TdRenalFunctionItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdRenalFunctionItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdRenalFunctionItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdRenalFunctionItems> queryByPage(TdRenalFunctionItems tdRenalFunctionItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdRenalFunctionItems 实例对象
     * @return 实例对象
     */
    TdRenalFunctionItems insert(TdRenalFunctionItems tdRenalFunctionItems);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdRenalFunctionItems 实例对象
     * @return 实例对象
     */
    TdRenalFunctionItems update(TdRenalFunctionItems tdRenalFunctionItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
