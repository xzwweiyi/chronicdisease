package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdDiagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 诊断记录(TdDiagnosis)表服务接口
 *
 * @author makejava
 * @since 2023-04-04 16:33:42
 */
public interface TdDiagnosisService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdDiagnosis queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdDiagnosis 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdDiagnosis> queryByPage(TdDiagnosis tdDiagnosis, PageRequest pageRequest);

    List<TdDiagnosis> queryByPage(TdDiagnosis tdDiagnosis);
    /**
     * 新增数据
     *
     * @param tdDiagnosis 实例对象
     * @return 实例对象
     */
    TdDiagnosis insert(TdDiagnosis tdDiagnosis);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdDiagnosis> entities);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdDiagnosis 实例对象
     * @return 实例对象
     */
    TdDiagnosis update(TdDiagnosis tdDiagnosis);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
