package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdThyroidFunctionItems;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 甲状腺功能项目表(TdThyroidFunctionItems)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:37:38
 */
public interface TdThyroidFunctionItemsService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdThyroidFunctionItems queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdThyroidFunctionItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdThyroidFunctionItems> queryByPage(TdThyroidFunctionItems tdThyroidFunctionItems, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdThyroidFunctionItems 实例对象
     * @return 实例对象
     */
    TdThyroidFunctionItems insert(TdThyroidFunctionItems tdThyroidFunctionItems);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdThyroidFunctionItems> entities);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdThyroidFunctionItems 实例对象
     * @return 实例对象
     */
    TdThyroidFunctionItems update(TdThyroidFunctionItems tdThyroidFunctionItems);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
