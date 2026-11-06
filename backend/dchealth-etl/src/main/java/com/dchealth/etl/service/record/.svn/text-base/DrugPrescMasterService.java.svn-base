package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.DrugPrescMaster;
import com.dchealth.etl.domain.record.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 药品处方主记录(DrugPrescMaster)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 17:53:50
 */
public interface DrugPrescMasterService {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    DrugPrescMaster queryById( );

    /**
     * 分页查询
     *
     * @param drugPrescMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<DrugPrescMaster> queryByPage(DrugPrescMaster drugPrescMaster, PageRequest pageRequest);
    /**
     * 查询当天同步数据量
     * @param drugPrescMaster
     * @return
     */
    int querySyncInfoCount(DrugPrescMaster drugPrescMaster);
    /**
     * 分页查询
     * @param drugPrescMaster
     * @param pageRequest
     * @return
     */
    Page<DrugPrescMaster> querySyncInfoByPage(DrugPrescMaster drugPrescMaster, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param drugPrescMaster 实例对象
     * @return 实例对象
     */
    DrugPrescMaster insert(DrugPrescMaster drugPrescMaster);

    /**
     * 修改数据
     *
     * @param drugPrescMaster 实例对象
     * @return 实例对象
     */
    DrugPrescMaster update(DrugPrescMaster drugPrescMaster);

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 是否成功
     */
    boolean deleteById( );

}
