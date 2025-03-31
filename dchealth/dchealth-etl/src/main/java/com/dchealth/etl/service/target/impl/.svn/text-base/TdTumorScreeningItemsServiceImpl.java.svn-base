package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdTumorScreeningItems;
import com.dchealth.etl.dao.target.TdTumorScreeningItemsMapper;
import com.dchealth.etl.service.target.TdTumorScreeningItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-肿瘤项目相关表(TdTumorScreeningItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:38:02
 */
@Service("tdTumorScreeningItemsService")
public class TdTumorScreeningItemsServiceImpl implements TdTumorScreeningItemsService {
    @Resource
    private TdTumorScreeningItemsMapper tdTumorScreeningItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdTumorScreeningItems queryById(Integer id) {
        return this.tdTumorScreeningItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdTumorScreeningItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdTumorScreeningItems> queryByPage(TdTumorScreeningItems tdTumorScreeningItems, PageRequest pageRequest) {
        long total = this.tdTumorScreeningItemsDao.count(tdTumorScreeningItems);
        return new PageImpl<>(this.tdTumorScreeningItemsDao.queryAllByLimit(tdTumorScreeningItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdTumorScreeningItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdTumorScreeningItems insert(TdTumorScreeningItems tdTumorScreeningItems) {
        this.tdTumorScreeningItemsDao.insert(tdTumorScreeningItems);
        return tdTumorScreeningItems;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdTumorScreeningItems> entities) {
        return this.tdTumorScreeningItemsDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdTumorScreeningItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdTumorScreeningItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdTumorScreeningItems update(TdTumorScreeningItems tdTumorScreeningItems) {
        this.tdTumorScreeningItemsDao.update(tdTumorScreeningItems);
        return this.queryById(tdTumorScreeningItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdTumorScreeningItemsDao.deleteById(id) > 0;
    }
}
