package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationUltrasound;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-超声项目相关表(TdAuxiliaryExaminationUltrasound)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 16:20:37
 */
public interface TdAuxiliaryExaminationUltrasoundService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationUltrasound queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationUltrasound 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdAuxiliaryExaminationUltrasound> queryByPage(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationUltrasound 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationUltrasound insert(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    public int insertBatch(List<TdAuxiliaryExaminationUltrasound> entities);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationUltrasound 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationUltrasound update(TdAuxiliaryExaminationUltrasound tdAuxiliaryExaminationUltrasound);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
