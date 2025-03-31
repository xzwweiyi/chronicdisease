package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdDrugPrescMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 药品处方主记录(TdDrugPrescMaster)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 17:42:39
 */
public interface TdDrugPrescMasterService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdDrugPrescMaster queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdDrugPrescMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdDrugPrescMaster> queryByPage(TdDrugPrescMaster tdDrugPrescMaster, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdDrugPrescMaster 实例对象
     * @return 实例对象
     */
    TdDrugPrescMaster insert(TdDrugPrescMaster tdDrugPrescMaster);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdDrugPrescMaster> entities);
    /**
     * 修改数据
     *
     * @param tdDrugPrescMaster 实例对象
     * @return 实例对象
     */
    TdDrugPrescMaster update(TdDrugPrescMaster tdDrugPrescMaster);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
