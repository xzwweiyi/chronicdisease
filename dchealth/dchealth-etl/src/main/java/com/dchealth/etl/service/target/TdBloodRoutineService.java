package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdBloodRoutine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 血常规(TdBloodRoutine)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:26:17
 */
public interface TdBloodRoutineService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdBloodRoutine queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdBloodRoutine 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdBloodRoutine> queryByPage(TdBloodRoutine tdBloodRoutine, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdBloodRoutine 实例对象
     * @return 实例对象
     */
    TdBloodRoutine insert(TdBloodRoutine tdBloodRoutine);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdBloodRoutine> entities);
    /**
     * 修改数据
     *
     * @param tdBloodRoutine 实例对象
     * @return 实例对象
     */
    TdBloodRoutine update(TdBloodRoutine tdBloodRoutine);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
