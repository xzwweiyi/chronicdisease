package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdOral;
import com.dchealth.etl.dao.target.TdOralMapper;
import com.dchealth.etl.service.target.TdOralService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-口腔科相关表(TdOral)表服务实现类
 *
 * @author makejava
 * @since 2023-04-07 14:56:03
 */
@Service("tdOralService")
public class TdOralServiceImpl implements TdOralService {
    @Resource
    private TdOralMapper tdOralDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdOral queryById(Integer id) {
        return this.tdOralDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdOral 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdOral> queryByPage(TdOral tdOral, PageRequest pageRequest) {
        long total = this.tdOralDao.count(tdOral);
        return new PageImpl<>(this.tdOralDao.queryAllByLimit(tdOral, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdOral 实例对象
     * @return 实例对象
     */
    @Override
    public TdOral insert(TdOral tdOral) {
        this.tdOralDao.insert(tdOral);
        return tdOral;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdOralDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdOral 实例对象
     * @return 实例对象
     */
    @Override
    public TdOral update(TdOral tdOral) {
        this.tdOralDao.update(tdOral);
        return this.queryById(tdOral.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdOralDao.deleteById(id) > 0;
    }
}
