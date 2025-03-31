package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.ClinicMaster;
import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.domain.record.OutpMedrec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 就诊记录(ClinicMaster)表服务接口
 *
 * @author makejava
 * @since 2023-05-18 15:34:47
 */
public interface ClinicMasterService {


    /**
     * 分页查询
     *
     * @param clinicMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ClinicMaster> queryByPage(ClinicMaster clinicMaster, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param clinicMaster 实例对象
     * @return 实例对象
     */
    ClinicMaster insert(ClinicMaster clinicMaster);

    /**
     * 修改数据
     *
     * @param clinicMaster 实例对象
     * @return 实例对象
     */
    ClinicMaster update(ClinicMaster clinicMaster);

    /**
     * 查询当天同步数据量
     * @param clinicMaster
     * @return
     */
    int querySyncInfoCount(ClinicMaster clinicMaster);
    /**
     * 分页查询
     * @param clinicMaster
     * @param pageRequest
     * @return
     */
    Page<ClinicMaster> querySyncInfoByPage(ClinicMaster clinicMaster, PageRequest pageRequest, long total);
}
