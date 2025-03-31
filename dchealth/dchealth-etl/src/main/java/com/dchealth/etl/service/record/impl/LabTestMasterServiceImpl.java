package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.dao.record.LabTestMasterMapper;
import com.dchealth.etl.domain.record.LabTestMaster;
import com.dchealth.etl.service.record.LabTestMasterService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;

/**
 * 检验主记录表
注释：此表描述每张检验申请单。一个检验申请与一个标本必须一一对应。此表记录由开单程序生成（病房或门诊或检验科室），由检验科室修改。
以1000张床位计，每天全院的检验申请约为2000件。每月的数据增长量约为6万条。
主键：申请序号(LabTestMaster)表服务实现类
 *
 * @author makejava
 * @since 2023-03-30 11:34:06
 */
@Service("labTestMasterService")
public class LabTestMasterServiceImpl implements LabTestMasterService {
    @Resource
    private LabTestMasterMapper labTestMasterDao;

    /**
     * 通过ID查询单条数据
     *
     * @param testNo 主键
     * @return 实例对象
     */
    @Override
    public LabTestMaster queryById(String testNo) {
        return this.labTestMasterDao.queryById(testNo);
    }

    /**
     * 分页查询
     *
     * @param labTestMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<LabTestMaster> queryByPage(LabTestMaster labTestMaster, PageRequest pageRequest) {
        long total = this.labTestMasterDao.count(labTestMaster);
        return new PageImpl<LabTestMaster>(this.labTestMasterDao.queryAllByLimit(labTestMaster, pageRequest), pageRequest, total);
    }
    /**
     * 查询当天同步数据量
     * @param labTestMaster
     * @return
     */
    @Override
    public int querySyncInfoCount(LabTestMaster labTestMaster) {
        return this.labTestMasterDao.querySyncInfoCount(labTestMaster);
    }
    /**
     * 分页查询
     *
     * @param labTestMaster 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<LabTestMaster> querySyncInfoByPage(LabTestMaster labTestMaster, PageRequest pageRequest,long total) {
        return new PageImpl<>(this.labTestMasterDao.querySyncInfoByPage(labTestMaster, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param labTestMaster 实例对象
     * @return 实例对象
     */
    @Override
    public LabTestMaster insert(LabTestMaster labTestMaster) {
        this.labTestMasterDao.insert(labTestMaster);
        return labTestMaster;
    }

    /**
     * 修改数据
     *
     * @param labTestMaster 实例对象
     * @return 实例对象
     */
    @Override
    public LabTestMaster update(LabTestMaster labTestMaster) {
        this.labTestMasterDao.update(labTestMaster);
        return this.queryById(labTestMaster.getTestNo());
    }

    /**
     * 通过主键删除数据
     *
     * @param testNo 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String testNo) {
        return this.labTestMasterDao.deleteById(testNo) > 0;
    }
}
