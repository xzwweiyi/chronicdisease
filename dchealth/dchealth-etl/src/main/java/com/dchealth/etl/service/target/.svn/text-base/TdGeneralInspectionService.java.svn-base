package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdGeneralInspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 一般检查表(TdGeneralInspection)表服务接口
 *
 * @author makejava
 * @since 2023-03-28 13:42:42
 */
public interface TdGeneralInspectionService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdGeneralInspection queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdGeneralInspection 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdGeneralInspection> queryByPage(TdGeneralInspection tdGeneralInspection, PageRequest pageRequest);
    /**
     * 查询数据列表
     *
     * @param tdGeneralInspection 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    List<TdGeneralInspection> queryInfoList(TdGeneralInspection tdGeneralInspection, PageRequest pageRequest);
    /**
     * 新增数据
     *
     * @param tdGeneralInspection 实例对象
     * @return 实例对象
     */
    TdGeneralInspection insert(TdGeneralInspection tdGeneralInspection);

    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatch(List<TdGeneralInspection> entities);

    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdGeneralInspection 实例对象
     * @return 实例对象
     */
    TdGeneralInspection update(TdGeneralInspection tdGeneralInspection);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
