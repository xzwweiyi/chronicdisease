package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdLiverFunctionItems;
import com.dchealth.etl.dao.target.TdLiverFunctionItemsMapper;
import com.dchealth.etl.service.target.TdLiverFunctionItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 肝功能项目表(TdLiverFunctionItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 17:05:03
 */
@Service("tdLiverFunctionItemsService")
public class TdLiverFunctionItemsServiceImpl implements TdLiverFunctionItemsService {
    @Resource
    private TdLiverFunctionItemsMapper tdLiverFunctionItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdLiverFunctionItems queryById(Integer id) {
        return this.tdLiverFunctionItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdLiverFunctionItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdLiverFunctionItems> queryByPage(TdLiverFunctionItems tdLiverFunctionItems, PageRequest pageRequest) {
        long total = this.tdLiverFunctionItemsDao.count(tdLiverFunctionItems);
        return new PageImpl<>(this.tdLiverFunctionItemsDao.queryAllByLimit(tdLiverFunctionItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdLiverFunctionItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdLiverFunctionItems insert(TdLiverFunctionItems tdLiverFunctionItems) {
        this.tdLiverFunctionItemsDao.insert(tdLiverFunctionItems);
        return tdLiverFunctionItems;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdLiverFunctionItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdLiverFunctionItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdLiverFunctionItems update(TdLiverFunctionItems tdLiverFunctionItems) {
        this.tdLiverFunctionItemsDao.update(tdLiverFunctionItems);
        return this.queryById(tdLiverFunctionItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdLiverFunctionItemsDao.deleteById(id) > 0;
    }
}
