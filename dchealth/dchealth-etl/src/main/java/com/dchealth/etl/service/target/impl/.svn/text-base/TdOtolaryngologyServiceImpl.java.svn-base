package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdOtolaryngology;
import com.dchealth.etl.dao.target.TdOtolaryngologyMapper;
import com.dchealth.etl.service.target.TdOtolaryngologyService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 唐都-耳鼻喉相关表(TdOtolaryngology)表服务实现类
 *
 * @author makejava
 * @since 2023-04-07 14:32:36
 */
@Service("tdOtolaryngologyService")
public class TdOtolaryngologyServiceImpl implements TdOtolaryngologyService {
    @Resource
    private TdOtolaryngologyMapper tdOtolaryngologyDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdOtolaryngology queryById(Integer id) {
        return this.tdOtolaryngologyDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdOtolaryngology 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdOtolaryngology> queryByPage(TdOtolaryngology tdOtolaryngology, PageRequest pageRequest) {
        long total = this.tdOtolaryngologyDao.count(tdOtolaryngology);
        return new PageImpl<>(this.tdOtolaryngologyDao.queryAllByLimit(tdOtolaryngology, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdOtolaryngology 实例对象
     * @return 实例对象
     */
    @Override
    public TdOtolaryngology insert(TdOtolaryngology tdOtolaryngology) {
        this.tdOtolaryngologyDao.insert(tdOtolaryngology);
        return tdOtolaryngology;
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdOtolaryngologyDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdOtolaryngology 实例对象
     * @return 实例对象
     */
    @Override
    public TdOtolaryngology update(TdOtolaryngology tdOtolaryngology) {
        this.tdOtolaryngologyDao.update(tdOtolaryngology);
        return this.queryById(tdOtolaryngology.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdOtolaryngologyDao.deleteById(id) > 0;
    }
}
