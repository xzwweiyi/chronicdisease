package com.dchealth.etl.service.target;

import com.dchealth.etl.domain.target.TdDrugPrescDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 药品处方明细记录(TdDrugPrescDetail)表服务接口
 *
 * @author makejava
 * @since 2023-04-24 17:43:08
 */
public interface TdDrugPrescDetailService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    TdDrugPrescDetail queryById(Integer id);

    /**
     * 分页查询
     *
     * @param tdDrugPrescDetail 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<TdDrugPrescDetail> queryByPage(TdDrugPrescDetail tdDrugPrescDetail, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param tdDrugPrescDetail 实例对象
     * @return 实例对象
     */
    TdDrugPrescDetail insert(TdDrugPrescDetail tdDrugPrescDetail);
    /**
     * 批量插入
     * @param entities
     * @return
     */
    int insertBatch(List<TdDrugPrescDetail> entities);
    /**
     * 修改数据
     *
     * @param tdDrugPrescDetail 实例对象
     * @return 实例对象
     */
    TdDrugPrescDetail update(TdDrugPrescDetail tdDrugPrescDetail);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

}
