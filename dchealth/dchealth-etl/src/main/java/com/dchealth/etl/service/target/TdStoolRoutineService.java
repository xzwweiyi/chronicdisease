package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdStoolRoutine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 便常规(TdStoolRoutine)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:33:49
 */
public interface TdStoolRoutineService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdStoolRoutine queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdStoolRoutine 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdStoolRoutine> queryByPage(TdStoolRoutine tdStoolRoutine, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdStoolRoutine 实例对象
     * @return 实例对象
     */
    TdStoolRoutine insert(TdStoolRoutine tdStoolRoutine);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdStoolRoutine> entities);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdStoolRoutine 实例对象
     * @return 实例对象
     */
    TdStoolRoutine update(TdStoolRoutine tdStoolRoutine);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
