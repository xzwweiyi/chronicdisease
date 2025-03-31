package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.ExamReport;
import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.dao.record.OrdersMapper;
import com.dchealth.etl.service.record.OrdersService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 医嘱(Orders)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 17:53:20
 */
@Service("ordersService")
public class OrdersServiceImpl implements OrdersService {
    @Resource
    private OrdersMapper ordersDao;

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    @Override
    public Orders queryById( ) {
        return this.ordersDao.queryById();
    }

    /**
     * 分页查询
     *
     * @param orders 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<Orders> queryByPage(Orders orders, PageRequest pageRequest) {
        long total = this.ordersDao.count(orders);
        return new PageImpl<>(this.ordersDao.queryAllByLimit(orders, pageRequest), pageRequest, total);
    }

    @Override
    public int querySyncInfoCount(Orders orders) {
        return this.ordersDao.querySyncInfoCount(orders);
    }

    @Override
    public Page<Orders> querySyncInfoByPage(Orders orders, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.ordersDao.querySyncInfoByPage(orders, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param orders 实例对象
     * @return 实例对象
     */
    @Override
    public Orders insert(Orders orders) {
        this.ordersDao.insert(orders);
        return orders;
    }

    /**
     * 修改数据
     *
     * @param orders 实例对象
     * @return 实例对象
     */
    @Override
    public Orders update(Orders orders) {
        this.ordersDao.update(orders);
        return this.queryById();
    }

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 是否成功
     */
    @Override
    public boolean deleteById( ) {
        return this.ordersDao.deleteById() > 0;
    }
}
