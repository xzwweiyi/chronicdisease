package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdAuxiliaryExaminationNmr;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 唐都-辅助检查-核磁项目相关表(TdAuxiliaryExaminationNmr)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 16:16:56
 */
public interface TdAuxiliaryExaminationNmrService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdAuxiliaryExaminationNmr queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdAuxiliaryExaminationNmr 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdAuxiliaryExaminationNmr> queryByPage(TdAuxiliaryExaminationNmr tdAuxiliaryExaminationNmr, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdAuxiliaryExaminationNmr 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationNmr insert(TdAuxiliaryExaminationNmr tdAuxiliaryExaminationNmr);
    /**
     * 新增数据
     * @param tdAuxiliaryExaminationNmrs
     * @return
     */
    public int insertBatch(List<TdAuxiliaryExaminationNmr> tdAuxiliaryExaminationNmrs);
    /**
     * 新增数据
     * @param entities
     * @return
     */
    int insertBatchByMaps(List<Map> entities);
    /**
     * 修改数据
     *
     * @param tdAuxiliaryExaminationNmr 实例对象
     * @return 实例对象
     */
    TdAuxiliaryExaminationNmr update(TdAuxiliaryExaminationNmr tdAuxiliaryExaminationNmr);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
