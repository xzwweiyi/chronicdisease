package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.PatMasterIndex;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 病人主索引患者信息表](PatMasterIndex)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 13:44:51
 */
public interface PatMasterIndexService {

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    PatMasterIndex queryById(String patientId);

    /**
     * 分页查询
     *
     * @param patMasterIndex 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<PatMasterIndex> queryByPage(PatMasterIndex patMasterIndex, PageRequest pageRequest);
    /**
     * 分页查询
     *
     * @param patMasterIndex 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    List<PatMasterIndex> querySyncInfoByPage(PatMasterIndex patMasterIndex, PageRequest pageRequest);
    /**
     * 新增数据
     *
     * @param patMasterIndex 实例对象
     * @return 实例对象
     */
    PatMasterIndex insert(PatMasterIndex patMasterIndex);

    /**
     * 修改数据
     *
     * @param patMasterIndex 实例对象
     * @return 实例对象
     */
    PatMasterIndex update(PatMasterIndex patMasterIndex);

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById(String patientId);

}
