package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdOphthalmologicalExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 眼科检查表(TdOphthalmologicalExam)表服务接口
 *
 * @author makejava
 * @since 2023-04-07 14:55:07
 */
public interface TdOphthalmologicalExamService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdOphthalmologicalExam queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdOphthalmologicalExam 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdOphthalmologicalExam> queryByPage(TdOphthalmologicalExam tdOphthalmologicalExam, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdOphthalmologicalExam 实例对象
     * @return 实例对象
     */
    TdOphthalmologicalExam insert(TdOphthalmologicalExam tdOphthalmologicalExam);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdOphthalmologicalExam 实例对象
     * @return 实例对象
     */
    TdOphthalmologicalExam update(TdOphthalmologicalExam tdOphthalmologicalExam);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
