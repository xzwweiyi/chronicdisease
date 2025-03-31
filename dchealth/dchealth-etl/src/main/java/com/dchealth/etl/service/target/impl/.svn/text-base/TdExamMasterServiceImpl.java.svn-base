package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdExamMaster;
import com.dchealth.etl.dao.target.TdExamMasterMapper;
import com.dchealth.etl.service.target.TdExamMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 检查主记录表(TdExamMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 18:04:02
 */
@Service("tdExamMasterService")
public class TdExamMasterServiceImpl implements TdExamMasterService {
    @Resource
    private TdExamMasterMapper tdExamMasterDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdExamMaster queryById(Integer id) {
        return this.tdExamMasterDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdExamMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdExamMaster> queryByPage(TdExamMaster tdExamMaster, PageRequest pageRequest) {
        long total = this.tdExamMasterDao.count(tdExamMaster);
        return new PageImpl<>(this.tdExamMasterDao.queryAllByLimit(tdExamMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdExamMaster 实例对象
     * @return 实例对象
     */
    @Override
    public TdExamMaster insert(TdExamMaster tdExamMaster) {
        this.tdExamMasterDao.insert(tdExamMaster);
        return tdExamMaster;
    }
    /**
     * 新增数据
     *
     * @param tdExamMasters 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdExamMaster> tdExamMasters) {

        return this.tdExamMasterDao.insertBatch(tdExamMasters);
    }

    /**
     * 修改数据
     *
     * @param tdExamMaster 实例对象
     * @return 实例对象
     */
    @Override
    public TdExamMaster update(TdExamMaster tdExamMaster) {
        this.tdExamMasterDao.update(tdExamMaster);
        return this.queryById(tdExamMaster.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdExamMasterDao.deleteById(id) > 0;
    }
}
