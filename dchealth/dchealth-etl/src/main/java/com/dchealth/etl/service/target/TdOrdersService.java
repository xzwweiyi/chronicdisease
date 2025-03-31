package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdOrders;
import com.dchealth.etl.domain.target.TdStoolRoutine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 医嘱(TdOrders)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 17:41:59
 */
public interface TdOrdersService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdOrders queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdOrders 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdOrders> queryByPage(TdOrders tdOrders, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdOrders 实例对象
     * @return 实例对象
     */
    TdOrders insert(TdOrders tdOrders);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdOrders> entities);
    /**
     * 修改数据
     *
     * @param tdOrders 实例对象
     * @return 实例对象
     */
    TdOrders update(TdOrders tdOrders);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
