package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdThyroidFunctionItems;
import com.dchealth.etl.dao.target.TdThyroidFunctionItemsMapper;
import com.dchealth.etl.service.target.TdThyroidFunctionItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 甲状腺功能项目表(TdThyroidFunctionItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:37:38
 */
@Service("tdThyroidFunctionItemsService")
public class TdThyroidFunctionItemsServiceImpl implements TdThyroidFunctionItemsService {
    @Resource
    private TdThyroidFunctionItemsMapper tdThyroidFunctionItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdThyroidFunctionItems queryById(Integer id) {
        return this.tdThyroidFunctionItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdThyroidFunctionItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdThyroidFunctionItems> queryByPage(TdThyroidFunctionItems tdThyroidFunctionItems, PageRequest pageRequest) {
        long total = this.tdThyroidFunctionItemsDao.count(tdThyroidFunctionItems);
        return new PageImpl<>(this.tdThyroidFunctionItemsDao.queryAllByLimit(tdThyroidFunctionItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdThyroidFunctionItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdThyroidFunctionItems insert(TdThyroidFunctionItems tdThyroidFunctionItems) {
        this.tdThyroidFunctionItemsDao.insert(tdThyroidFunctionItems);
        return tdThyroidFunctionItems;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdThyroidFunctionItems> entities) {
        return this.tdThyroidFunctionItemsDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdThyroidFunctionItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdThyroidFunctionItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdThyroidFunctionItems update(TdThyroidFunctionItems tdThyroidFunctionItems) {
        this.tdThyroidFunctionItemsDao.update(tdThyroidFunctionItems);
        return this.queryById(tdThyroidFunctionItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdThyroidFunctionItemsDao.deleteById(id) > 0;
    }
}
