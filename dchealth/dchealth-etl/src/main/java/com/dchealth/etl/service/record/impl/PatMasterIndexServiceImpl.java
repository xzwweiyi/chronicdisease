package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.PatMasterIndex;
import com.dchealth.etl.dao.record.PatMasterIndexMapper;
import com.dchealth.etl.service.record.PatMasterIndexService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 病人主索引患者信息表](PatMasterIndex)表服务实现类
 *
 * @author makejava
 * @since 2023-03-27 13:44:51
 */
@Service("patMasterIndexService")
public class PatMasterIndexServiceImpl implements PatMasterIndexService {
    @Resource
    private PatMasterIndexMapper patMasterIndexDao;

    /**
     * 通过ID查询单条数据
     *
     * @param  主键
     * @return 实例对象
     */
    @Override
    public PatMasterIndex queryById(String patientId) {
        return this.patMasterIndexDao.queryById(patientId);
    }

    /**
     * 分页查询
     *
     * @param patMasterIndex 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<PatMasterIndex> queryByPage(PatMasterIndex patMasterIndex, PageRequest pageRequest) {
        long total = this.patMasterIndexDao.count(patMasterIndex);
        return new PageImpl<>(this.patMasterIndexDao.queryAllByLimit(patMasterIndex, pageRequest), pageRequest, total);
    }

    /**
     * 分页查询
     *
     * @param patMasterIndex 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public List<PatMasterIndex> querySyncInfoByPage(PatMasterIndex patMasterIndex, PageRequest pageRequest){
        return this.patMasterIndexDao.querySyncInfoByPage(patMasterIndex, pageRequest);
    }

    /**
     * 新增数据
     *
     * @param patMasterIndex 实例对象
     * @return 实例对象
     */
    @Override
    public PatMasterIndex insert(PatMasterIndex patMasterIndex) {
        this.patMasterIndexDao.insert(patMasterIndex);
        return patMasterIndex;
    }

    /**
     * 修改数据
     *
     * @param patMasterIndex 实例对象
     * @return 实例对象
     */
    @Override
    public PatMasterIndex update(PatMasterIndex patMasterIndex) {
        this.patMasterIndexDao.update(patMasterIndex);
        return this.queryById(patMasterIndex.getPatientId());
    }

    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(String patientId) {
        return this.patMasterIndexDao.deleteById(patientId) > 0;
    }
}
