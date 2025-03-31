package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdSurgicalExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 外科检查表(TdSurgicalExam)表服务接口
 *
 * @author makejava
 * @since 2023-04-07 14:31:50
 */
public interface TdSurgicalExamService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdSurgicalExam queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdSurgicalExam 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdSurgicalExam> queryByPage(TdSurgicalExam tdSurgicalExam, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdSurgicalExam 实例对象
     * @return 实例对象
     */
    TdSurgicalExam insert(TdSurgicalExam tdSurgicalExam);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdSurgicalExam 实例对象
     * @return 实例对象
     */
    TdSurgicalExam update(TdSurgicalExam tdSurgicalExam);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
