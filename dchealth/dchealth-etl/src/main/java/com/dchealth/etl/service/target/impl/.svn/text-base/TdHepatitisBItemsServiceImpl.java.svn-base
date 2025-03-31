package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdHepatitisBItems;
import com.dchealth.etl.dao.target.TdHepatitisBItemsMapper;
import com.dchealth.etl.service.target.TdHepatitisBItemsService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 乙肝项目表(TdHepatitisBItems)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 17:03:30
 */
@Service("tdHepatitisBItemsService")
public class TdHepatitisBItemsServiceImpl implements TdHepatitisBItemsService {
    @Resource
    private TdHepatitisBItemsMapper tdHepatitisBItemsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdHepatitisBItems queryById(Integer id) {
        return this.tdHepatitisBItemsDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdHepatitisBItems 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdHepatitisBItems> queryByPage(TdHepatitisBItems tdHepatitisBItems, PageRequest pageRequest) {
        long total = this.tdHepatitisBItemsDao.count(tdHepatitisBItems);
        return new PageImpl<>(this.tdHepatitisBItemsDao.queryAllByLimit(tdHepatitisBItems, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdHepatitisBItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdHepatitisBItems insert(TdHepatitisBItems tdHepatitisBItems) {
        this.tdHepatitisBItemsDao.insert(tdHepatitisBItems);
        return tdHepatitisBItems;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdHepatitisBItemsDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdHepatitisBItems 实例对象
     * @return 实例对象
     */
    @Override
    public TdHepatitisBItems update(TdHepatitisBItems tdHepatitisBItems) {
        this.tdHepatitisBItemsDao.update(tdHepatitisBItems);
        return this.queryById(tdHepatitisBItems.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdHepatitisBItemsDao.deleteById(id) > 0;
    }
}
