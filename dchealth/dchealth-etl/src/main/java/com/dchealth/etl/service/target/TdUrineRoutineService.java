package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdUrineRoutine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 尿常规(TdUrineRoutine)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 09:31:23
 */
public interface TdUrineRoutineService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdUrineRoutine queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdUrineRoutine 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdUrineRoutine> queryByPage(TdUrineRoutine tdUrineRoutine, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdUrineRoutine 实例对象
     * @return 实例对象
     */
    TdUrineRoutine insert(TdUrineRoutine tdUrineRoutine);
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
    int insertBatch(List<TdUrineRoutine> entities);
    /**
     * 修改数据
     *
     * @param tdUrineRoutine 实例对象
     * @return 实例对象
     */
    TdUrineRoutine update(TdUrineRoutine tdUrineRoutine);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
