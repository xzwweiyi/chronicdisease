package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.dao.target.TdStoolRoutineMapper;
import com.dchealth.etl.domain.target.TdStoolRoutine;
import com.dchealth.etl.service.target.TdStoolRoutineService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 便常规(TdStoolRoutine)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:33:49
 */
@Service("tdStoolRoutineService")
public class TdStoolRoutineServiceImpl implements TdStoolRoutineService {
    @Resource
    private TdStoolRoutineMapper tdStoolRoutineDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdStoolRoutine queryById(Integer id) {
        return this.tdStoolRoutineDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdStoolRoutine 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdStoolRoutine> queryByPage(TdStoolRoutine tdStoolRoutine, PageRequest pageRequest) {
        long total = this.tdStoolRoutineDao.count(tdStoolRoutine);
        return new PageImpl<>(this.tdStoolRoutineDao.queryAllByLimit(tdStoolRoutine, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdStoolRoutine 实例对象
     * @return 实例对象
     */
    @Override
    public TdStoolRoutine insert(TdStoolRoutine tdStoolRoutine) {
        this.tdStoolRoutineDao.insert(tdStoolRoutine);
        return tdStoolRoutine;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdStoolRoutine> entities) {
        return this.tdStoolRoutineDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdStoolRoutineDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdStoolRoutine 实例对象
     * @return 实例对象
     */
    @Override
    public TdStoolRoutine update(TdStoolRoutine tdStoolRoutine) {
        this.tdStoolRoutineDao.update(tdStoolRoutine);
        return this.queryById(tdStoolRoutine.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdStoolRoutineDao.deleteById(id) > 0;
    }
}
