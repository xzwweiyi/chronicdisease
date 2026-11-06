package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.VitalSignsRec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 病人体症记录(VitalSignsRec)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 13:58:50
 */
public interface VitalSignsRecService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    VitalSignsRec queryById(String patientId);

    /**
     * 分页查询
     *
     * @param vitalSignsRec 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<VitalSignsRec> queryByPage(VitalSignsRec vitalSignsRec, PageRequest pageRequest);
    /**
     * 查询总数
     * @param vitalSignsRec
     * @return
     */
    long querySyncInfoCount(VitalSignsRec vitalSignsRec);
    /**
     * 分页查询
     *
     * @param vitalSignsRec 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    List<VitalSignsRec> querySyncInfoByPage(VitalSignsRec vitalSignsRec, PageRequest pageRequest);
    /**
     * 新增数据
     *
     * @param vitalSignsRec 实例对象
     * @return 实例对象
     */
    VitalSignsRec insert(VitalSignsRec vitalSignsRec);

    /**
     * 修改数据
     *
     * @param vitalSignsRec 实例对象
     * @return 实例对象
     */
    VitalSignsRec update(VitalSignsRec vitalSignsRec);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById(String patientId);

}
