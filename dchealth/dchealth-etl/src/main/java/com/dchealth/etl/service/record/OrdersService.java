package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.ExamReport;
import com.dchealth.etl.domain.record.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 医嘱(Orders)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 17:53:20
 */
public interface OrdersService {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    Orders queryById( );

    /**
     * 分页查询
     *
     * @param orders 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Orders> queryByPage(Orders orders, PageRequest pageRequest);
    /**
     * 查询当天同步数据量
     * @param orders
     * @return
     */
    int querySyncInfoCount(Orders orders);
    /**
     * 分页查询
     * @param orders
     * @param pageRequest
     * @return
     */
    Page<Orders> querySyncInfoByPage(Orders orders, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param orders 实例对象
     * @return 实例对象
     */
    Orders insert(Orders orders);

    /**
     * 修改数据
     *
     * @param orders 实例对象
     * @return 实例对象
     */
    Orders update(Orders orders);

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 是否成功
     */
    boolean deleteById( );

}
