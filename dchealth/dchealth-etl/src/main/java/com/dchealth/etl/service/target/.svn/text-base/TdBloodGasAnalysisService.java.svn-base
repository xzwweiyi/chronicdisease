package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdBloodGasAnalysis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 血气分析(TdBloodGasAnalysis)表服务接口
 *
 * @author makejava
 * @since 2023-04-25 18:01:13
 */
public interface TdBloodGasAnalysisService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdBloodGasAnalysis queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdBloodGasAnalysis 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdBloodGasAnalysis> queryByPage(TdBloodGasAnalysis tdBloodGasAnalysis, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdBloodGasAnalysis 实例对象
     * @return 实例对象
     */
    TdBloodGasAnalysis insert(TdBloodGasAnalysis tdBloodGasAnalysis);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdBloodGasAnalysis> entities);
    /**
     * 修改数据
     *
     * @param tdBloodGasAnalysis 实例对象
     * @return 实例对象
     */
    TdBloodGasAnalysis update(TdBloodGasAnalysis tdBloodGasAnalysis);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
