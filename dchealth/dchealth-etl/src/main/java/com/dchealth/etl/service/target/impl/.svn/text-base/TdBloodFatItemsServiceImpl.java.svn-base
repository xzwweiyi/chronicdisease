package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdBloodFatItems;
import com.dchealth.etl.dao.target.TdBloodFatItemsMapper;
import com.dchealth.etl.service.target.TdBloodFatItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 血脂项目表(TdBloodFatItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 17:06:28
 */
@Service("tdBloodFatItemsService")
public class TdBloodFatItemsServiceImpl implements TdBloodFatItemsService {
    @Resource
    private TdBloodFatItemsMapper tdBloodFatItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdBloodFatItems queryById(Integer id) {
        return this.tdBloodFatItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdBloodFatItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdBloodFatItems> queryByPage(TdBloodFatItems tdBloodFatItems, PageRequest pageRequest) {
        long total = this.tdBloodFatItemsDao.count(tdBloodFatItems);
        return new PageImpl<>(this.tdBloodFatItemsDao.queryAllByLimit(tdBloodFatItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdBloodFatItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodFatItems insert(TdBloodFatItems tdBloodFatItems) {
        this.tdBloodFatItemsDao.insert(tdBloodFatItems);
        return tdBloodFatItems;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdBloodFatItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdBloodFatItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodFatItems update(TdBloodFatItems tdBloodFatItems) {
        this.tdBloodFatItemsDao.update(tdBloodFatItems);
        return this.queryById(tdBloodFatItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdBloodFatItemsDao.deleteById(id) > 0;
    }
}
