package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.DrugPrescDetail;
import com.dchealth.etl.domain.record.DrugPrescMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (DrugPrescDetail)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 17:54:22
 */
public interface DrugPrescDetailService {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    DrugPrescDetail queryById( );

    /**
     * 分页查询
     *
     * @param drugPrescDetail 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<DrugPrescDetail> queryByPage(DrugPrescDetail drugPrescDetail, PageRequest pageRequest);
    /**
     * 查询当天同步数据量
     * @param drugPrescDetail
     * @return
     */
    int querySyncInfoCount(DrugPrescDetail drugPrescDetail);
    /**
     * 分页查询
     * @param drugPrescDetail
     * @param pageRequest
     * @return
     */
    Page<DrugPrescDetail> querySyncInfoByPage(DrugPrescDetail drugPrescDetail, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param drugPrescDetail 实例对象
     * @return 实例对象
     */
    DrugPrescDetail insert(DrugPrescDetail drugPrescDetail);

    /**
     * 修改数据
     *
     * @param drugPrescDetail 实例对象
     * @return 实例对象
     */
    DrugPrescDetail update(DrugPrescDetail drugPrescDetail);

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 是否成功
     */
    boolean deleteById( );

}
