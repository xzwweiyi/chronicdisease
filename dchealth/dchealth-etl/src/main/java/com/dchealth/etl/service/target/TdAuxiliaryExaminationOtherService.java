package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationOther;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-心电图等其他项目表(TdAuxiliaryExaminationOther)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 16:20:11
 */
public interface TdAuxiliaryExaminationOtherService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationOther queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationOther 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdAuxiliaryExaminationOther> queryByPage(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationOther 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationOther insert(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther);
    /**
     * 新增数据
     * @param tdAuxiliaryExaminationOthers
     * @return
     */
    public int insertBatch(List<TdAuxiliaryExaminationOther> tdAuxiliaryExaminationOthers);

    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationOther 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationOther update(TdAuxiliaryExaminationOther tdAuxiliaryExaminationOther);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
