package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdInternalMedicineExam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 内科检查表(TdInternalMedicineExam)表服务接口
 *
 * @author makejava
 * @since 2023-04-07 14:08:44
 */
public interface TdInternalMedicineExamService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdInternalMedicineExam queryById(String id);

    /**
     * 分页查询
     *
     * @param tdInternalMedicineExam 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdInternalMedicineExam> queryByPage(TdInternalMedicineExam tdInternalMedicineExam, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdInternalMedicineExam 实例对象
     * @return 实例对象
     */
    TdInternalMedicineExam insert(TdInternalMedicineExam tdInternalMedicineExam);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdInternalMedicineExam 实例对象
     * @return 实例对象
     */
    TdInternalMedicineExam update(TdInternalMedicineExam tdInternalMedicineExam);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}
