package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdTestMaster;
import com.dchealth.etl.dao.target.TdTestMasterMapper;
import com.dchealth.etl.service.target.TdTestMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 检验主记录(TdTestMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-03-30 11:23:00
 */
@Service("tdTestMasterService")
public class TdTestMasterServiceImpl implements TdTestMasterService {
    @Resource
    private TdTestMasterMapper tdTestMasterDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdTestMaster queryById(Integer id) {
        return this.tdTestMasterDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdTestMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdTestMaster> queryByPage(TdTestMaster tdTestMaster, PageRequest pageRequest) {
        long total = this.tdTestMasterDao.count(tdTestMaster);
        return new PageImpl<>(this.tdTestMasterDao.queryAllByLimit(tdTestMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdTestMaster 实例对象
     * @return 实例对象
     */
    @Override
    public TdTestMaster insert(TdTestMaster tdTestMaster) {
        this.tdTestMasterDao.insert(tdTestMaster);
        return tdTestMaster;
    }
    /**
     * 新增数据
     *
     * @param tdTestMasters 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdTestMaster> tdTestMasters){
        return this.tdTestMasterDao.insertBatch(tdTestMasters);
    }

    /**
     * 修改数据
     *
     * @param tdTestMaster 实例对象
     * @return 实例对象
     */
    @Override
    public TdTestMaster update(TdTestMaster tdTestMaster) {
        this.tdTestMasterDao.update(tdTestMaster);
        return this.queryById(tdTestMaster.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdTestMasterDao.deleteById(id) > 0;
    }
}
