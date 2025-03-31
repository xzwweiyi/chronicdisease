package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.Diagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 诊断记录(Diagnosis)表服务接口
 *
 * @author makejava
 * @since 2023-04-04 16:23:44
 */
public interface DiagnosisService {


    /**
     * 分页查询
     *
     * @param diagnosis 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Diagnosis> queryByPage(Diagnosis diagnosis, PageRequest pageRequest);
    /**
     * 查询当天同步数据量
     * @param diagnosis
     * @return
     */
    int querySyncInfoCount(Diagnosis diagnosis);

    /**
     * 分页查询
     * @param diagnosis
     * @param pageRequest
     * @return
     */
    Page<Diagnosis> querySyncInfoByPage(Diagnosis diagnosis, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param diagnosis 实例对象
     * @return 实例对象
     */
    Diagnosis insert(Diagnosis diagnosis);

    /**
     * 修改数据
     *
     * @param diagnosis 实例对象
     * @return 实例对象
     */
    Diagnosis update(Diagnosis diagnosis);


}
