package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdCoagulationItems;
import com.dchealth.etl.dao.target.TdCoagulationItemsMapper;
import com.dchealth.etl.service.target.TdCoagulationItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 凝血项目表(TdCoagulationItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 17:11:42
 */
@Service("tdCoagulationItemsService")
public class TdCoagulationItemsServiceImpl implements TdCoagulationItemsService {
    @Resource
    private TdCoagulationItemsMapper tdCoagulationItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdCoagulationItems queryById(Integer id) {
        return this.tdCoagulationItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdCoagulationItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdCoagulationItems> queryByPage(TdCoagulationItems tdCoagulationItems, PageRequest pageRequest) {
        long total = this.tdCoagulationItemsDao.count(tdCoagulationItems);
        return new PageImpl<>(this.tdCoagulationItemsDao.queryAllByLimit(tdCoagulationItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdCoagulationItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdCoagulationItems insert(TdCoagulationItems tdCoagulationItems) {
        this.tdCoagulationItemsDao.insert(tdCoagulationItems);
        return tdCoagulationItems;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdCoagulationItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdCoagulationItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdCoagulationItems update(TdCoagulationItems tdCoagulationItems) {
        this.tdCoagulationItemsDao.update(tdCoagulationItems);
        return this.queryById(tdCoagulationItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdCoagulationItemsDao.deleteById(id) > 0;
    }
}
