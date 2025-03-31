package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdOrders;
import com.dchealth.etl.dao.target.TdOrdersMapper;
import com.dchealth.etl.service.target.TdOrdersService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 医嘱(TdOrders)表服务实现类
 *
 * @author makejava
 * @since 2023-04-24 17:41:59
 */
@Service("tdOrdersService")
public class TdOrdersServiceImpl implements TdOrdersService {
    @Resource
    private TdOrdersMapper tdOrdersDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdOrders queryById(Integer id) {
        return this.tdOrdersDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdOrders 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdOrders> queryByPage(TdOrders tdOrders, PageRequest pageRequest) {
        long total = this.tdOrdersDao.count(tdOrders);
        return new PageImpl<>(this.tdOrdersDao.queryAllByLimit(tdOrders, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdOrders 实例对象
     * @return 实例对象
     */
    @Override
    public TdOrders insert(TdOrders tdOrders) {
        this.tdOrdersDao.insert(tdOrders);
        return tdOrders;
    }

    @Override
    public int insertBatch(List<TdOrders> entities) {
        return this.tdOrdersDao.insertBatch(entities);
    }

    /**
     * 修改数据
     *
     * @param tdOrders 实例对象
     * @return 实例对象
     */
    @Override
    public TdOrders update(TdOrders tdOrders) {
        this.tdOrdersDao.update(tdOrders);
        return this.queryById(tdOrders.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdOrdersDao.deleteById(id) > 0;
    }
}
