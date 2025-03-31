package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdExamMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 检查主记录表(TdExamMaster)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 18:04:02
 */
public interface TdExamMasterService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdExamMaster queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdExamMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdExamMaster> queryByPage(TdExamMaster tdExamMaster, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdExamMaster 实例对象
     * @return 实例对象
     */
    TdExamMaster insert(TdExamMaster tdExamMaster);
    /**
     * 新增数据
     *
     * @param tdExamMasters 实例对象
     * @return 实例对象
     */
    public int insertBatch(List<TdExamMaster> tdExamMasters);
    /**
     * 修改数据
     *
     * @param tdExamMaster 实例对象
     * @return 实例对象
     */
    TdExamMaster update(TdExamMaster tdExamMaster);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
