package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdHormoneItems;
import com.dchealth.etl.dao.target.TdHormoneItemsMapper;
import com.dchealth.etl.service.target.TdHormoneItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 激素项目表(TdHormoneItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:39:03
 */
@Service("tdHormoneItemsService")
public class TdHormoneItemsServiceImpl implements TdHormoneItemsService {
    @Resource
    private TdHormoneItemsMapper tdHormoneItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdHormoneItems queryById(Integer id) {
        return this.tdHormoneItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdHormoneItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdHormoneItems> queryByPage(TdHormoneItems tdHormoneItems, PageRequest pageRequest) {
        long total = this.tdHormoneItemsDao.count(tdHormoneItems);
        return new PageImpl<>(this.tdHormoneItemsDao.queryAllByLimit(tdHormoneItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdHormoneItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdHormoneItems insert(TdHormoneItems tdHormoneItems) {
        this.tdHormoneItemsDao.insert(tdHormoneItems);
        return tdHormoneItems;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdHormoneItems> entities) {
        return this.tdHormoneItemsDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdHormoneItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdHormoneItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdHormoneItems update(TdHormoneItems tdHormoneItems) {
        this.tdHormoneItemsDao.update(tdHormoneItems);
        return this.queryById(tdHormoneItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdHormoneItemsDao.deleteById(id) > 0;
    }
}
