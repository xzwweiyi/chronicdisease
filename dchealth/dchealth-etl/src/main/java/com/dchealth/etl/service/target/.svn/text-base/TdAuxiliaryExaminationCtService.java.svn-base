package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationCt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-ct项目相关表(TdAuxiliaryExaminationCt)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 16:14:32
 */
public interface TdAuxiliaryExaminationCtService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationCt queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationCt 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdAuxiliaryExaminationCt> queryByPage(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationCt 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationCt insert(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt);
    /**
     * 新增数据
     * @param tdAuxiliaryExaminationCts
     * @return
     */
    public int insertBatch(List<TdAuxiliaryExaminationCt> tdAuxiliaryExaminationCts);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationCt 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationCt update(TdAuxiliaryExaminationCt tdAuxiliaryExaminationCt);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
