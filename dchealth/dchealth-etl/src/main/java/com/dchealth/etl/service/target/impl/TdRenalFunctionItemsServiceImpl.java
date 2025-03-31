package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdRenalFunctionItems;
import com.dchealth.etl.dao.target.TdRenalFunctionItemsMapper;
import com.dchealth.etl.service.target.TdRenalFunctionItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 肾功能项目表(TdRenalFunctionItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 17:05:33
 */
@Service("tdRenalFunctionItemsService")
public class TdRenalFunctionItemsServiceImpl implements TdRenalFunctionItemsService {
    @Resource
    private TdRenalFunctionItemsMapper tdRenalFunctionItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdRenalFunctionItems queryById(Integer id) {
        return this.tdRenalFunctionItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdRenalFunctionItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdRenalFunctionItems> queryByPage(TdRenalFunctionItems tdRenalFunctionItems, PageRequest pageRequest) {
        long total = this.tdRenalFunctionItemsDao.count(tdRenalFunctionItems);
        return new PageImpl<>(this.tdRenalFunctionItemsDao.queryAllByLimit(tdRenalFunctionItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdRenalFunctionItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdRenalFunctionItems insert(TdRenalFunctionItems tdRenalFunctionItems) {
        this.tdRenalFunctionItemsDao.insert(tdRenalFunctionItems);
        return tdRenalFunctionItems;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdRenalFunctionItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdRenalFunctionItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdRenalFunctionItems update(TdRenalFunctionItems tdRenalFunctionItems) {
        this.tdRenalFunctionItemsDao.update(tdRenalFunctionItems);
        return this.queryById(tdRenalFunctionItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdRenalFunctionItemsDao.deleteById(id) > 0;
    }
}
