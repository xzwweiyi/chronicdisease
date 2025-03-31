package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.PatMasterIndex;
import com.dchealth.etl.domain.record.VitalSignsRec;
import com.dchealth.etl.dao.record.VitalSignsRecMapper;
import com.dchealth.etl.service.record.VitalSignsRecService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 病人体症记录(VitalSignsRec)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 13:58:50
 */
@Service("vitalSignsRecService")
public class VitalSignsRecServiceImpl implements VitalSignsRecService {
    @Resource
    private VitalSignsRecMapper vitalSignsRecDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  patientId 主键
     * @return 实例对象
     */
    @Override
    public VitalSignsRec queryById(String patientId) {
        return this.vitalSignsRecDao.queryById(patientId);
    }

    /**
     * 分页查询
     *
     * @param vitalSignsRec 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<VitalSignsRec> queryByPage(VitalSignsRec vitalSignsRec, PageRequest pageRequest) {
        long total = this.vitalSignsRecDao.count(vitalSignsRec);
        return new PageImpl<>(this.vitalSignsRecDao.queryAllByLimit(vitalSignsRec, pageRequest), pageRequest, total);
    }

    /**
     * 查询总数
     * @param vitalSignsRec
     * @return
     */
    @Override
    public long querySyncInfoCount(VitalSignsRec vitalSignsRec){
        return this.vitalSignsRecDao.querySyncInfoCount(vitalSignsRec);
    }
    /**
     * 分页查询
     *
     * @param vitalSignsRec 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public List<VitalSignsRec> querySyncInfoByPage(VitalSignsRec vitalSignsRec, PageRequest pageRequest){
        return this.vitalSignsRecDao.querySyncInfoByPage(vitalSignsRec, pageRequest);
    }
    /**
     * 新增数据
     *
     * @param vitalSignsRec 实例对象
     * @return 实例对象
     */
    @Override
    public VitalSignsRec insert(VitalSignsRec vitalSignsRec) {
        this.vitalSignsRecDao.insert(vitalSignsRec);
        return vitalSignsRec;
    }

    /**
     * 修改数据
     *
     * @param vitalSignsRec 实例对象
     * @return 实例对象
     */
    @Override
    public VitalSignsRec update(VitalSignsRec vitalSignsRec) {
        this.vitalSignsRecDao.update(vitalSignsRec);
        return this.queryById(vitalSignsRec.getPatientId());
    }

    /**
     * 通过主键删除数据
     *
     * @param patientId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String patientId) {
        return this.vitalSignsRecDao.deleteById(patientId) > 0;
    }
}
