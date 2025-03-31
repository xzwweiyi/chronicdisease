package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.dao.target.TdUrineRoutineMapper;
import com.dchealth.etl.domain.target.TdUrineRoutine;
import com.dchealth.etl.service.target.TdUrineRoutineService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 尿常规(TdUrineRoutine)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:31:23
 */
@Service("tdUrineRoutineService")
public class TdUrineRoutineServiceImpl implements TdUrineRoutineService {
    @Resource
    private TdUrineRoutineMapper tdUrineRoutineDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdUrineRoutine queryById(Integer id) {
        return this.tdUrineRoutineDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdUrineRoutine 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdUrineRoutine> queryByPage(TdUrineRoutine tdUrineRoutine, PageRequest pageRequest) {
        long total = this.tdUrineRoutineDao.count(tdUrineRoutine);
        return new PageImpl<>(this.tdUrineRoutineDao.queryAllByLimit(tdUrineRoutine, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdUrineRoutine 实例对象
     * @return 实例对象
     */
    @Override
    public TdUrineRoutine insert(TdUrineRoutine tdUrineRoutine) {
        this.tdUrineRoutineDao.insert(tdUrineRoutine);
        return tdUrineRoutine;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdUrineRoutine> entities) {
        return this.tdUrineRoutineDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdUrineRoutineDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdUrineRoutine 实例对象
     * @return 实例对象
     */
    @Override
    public TdUrineRoutine update(TdUrineRoutine tdUrineRoutine) {
        this.tdUrineRoutineDao.update(tdUrineRoutine);
        return this.queryById(tdUrineRoutine.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdUrineRoutineDao.deleteById(id) > 0;
    }
}
