package com.dchealth.etl.service.target.impl;

import com.dchealth.etl.domain.target.TdBloodRoutine;
import com.dchealth.etl.dao.target.TdBloodRoutineMapper;
import com.dchealth.etl.service.target.TdBloodRoutineService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 血常规(TdBloodRoutine)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 09:26:17
 */
@Service("tdBloodRoutineService")
public class TdBloodRoutineServiceImpl implements TdBloodRoutineService {
    @Resource
    private TdBloodRoutineMapper tdBloodRoutineDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public TdBloodRoutine queryById(Integer id) {
        return this.tdBloodRoutineDao.queryById(id);
    }

    /**
     * 分页查询
     *
     * @param tdBloodRoutine 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<TdBloodRoutine> queryByPage(TdBloodRoutine tdBloodRoutine, PageRequest pageRequest) {
        long total = this.tdBloodRoutineDao.count(tdBloodRoutine);
        return new PageImpl<>(this.tdBloodRoutineDao.queryAllByLimit(tdBloodRoutine, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param tdBloodRoutine 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodRoutine insert(TdBloodRoutine tdBloodRoutine) {
        this.tdBloodRoutineDao.insert(tdBloodRoutine);
        return tdBloodRoutine;
    }
    /**
     * 新增数据
     *
     * @param entities 实例对象
     * @return 实例对象
     */
    @Override
    public int insertBatch(List<TdBloodRoutine> entities) {
        return this.tdBloodRoutineDao.insertBatch(entities);
    }
    /**
     * 新增数据
     * @param entities
     * @return
     */
    @Override
    public int insertBatchByMaps(List<Map> entities){
        return this.tdBloodRoutineDao.insertBatchByMaps(entities);
    }
    /**
     * 修改数据
     *
     * @param tdBloodRoutine 实例对象
     * @return 实例对象
     */
    @Override
    public TdBloodRoutine update(TdBloodRoutine tdBloodRoutine) {
        this.tdBloodRoutineDao.update(tdBloodRoutine);
        return this.queryById(tdBloodRoutine.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Integer id) {
        return this.tdBloodRoutineDao.deleteById(id) > 0;
    }
}
