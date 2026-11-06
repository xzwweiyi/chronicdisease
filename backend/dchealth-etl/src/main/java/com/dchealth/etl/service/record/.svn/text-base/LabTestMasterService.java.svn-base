package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.LabTestMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 检验主记录表
注释：此表描述每张检验申请单。一个检验申请与一个标本必须一一对应。此表记录由开单程序生成（病房或门诊或检验科室），由检验科室修改。
以1000张床位计，每天全院的检验申请约为2000件。每月的数据增长量约为6万条。
主键：申请序号(LabTestMaster)表服务接口
 *
 * @author makejava
 * @since 2023-03-30 11:34:06
 */
public interface LabTestMasterService {

    /**
     * 通过ID查询单条数据
     *
     * @param testNo 主键
     * @return 实例对象
     */
    LabTestMaster queryById(String testNo);

    /**
     * 分页查询
     *
     * @param labTestMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<LabTestMaster> queryByPage(LabTestMaster labTestMaster, PageRequest pageRequest);
    /**
     * 查询当天同步数据量
     * @param labTestMaster
     * @return
     */
    int querySyncInfoCount(LabTestMaster labTestMaster);
    /**
     * 分页查询
     *
     * @param labTestMaster 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<LabTestMaster> querySyncInfoByPage(LabTestMaster labTestMaster, PageRequest pageRequest,long total);

    /**
     * 新增数据
     *
     * @param labTestMaster 实例对象
     * @return 实例对象
     */
    LabTestMaster insert(LabTestMaster labTestMaster);

    /**
     * 修改数据
     *
     * @param labTestMaster 实例对象
     * @return 实例对象
     */
    LabTestMaster update(LabTestMaster labTestMaster);

    /**
     * 通过主键删除数据
     *
     * @param testNo 主键
     * @return 是否成功
     */
    boolean deleteById(String testNo);

}
