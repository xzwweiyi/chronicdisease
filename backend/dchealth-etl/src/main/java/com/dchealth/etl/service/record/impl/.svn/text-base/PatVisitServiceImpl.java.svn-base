package com.dchealth.etl.service.record.impl;

import com.dchealth.etl.domain.record.PatVisit;
import com.dchealth.etl.dao.record.PatVisitMapper;
import com.dchealth.etl.service.record.PatVisitService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 病人住院主记录(PatVisit)表服务实现类
 *
 * @author makejava
 * @since 2023-04-13 09:26:57
 */
@Service("patVisitService")
public class PatVisitServiceImpl implements PatVisitService {
    @Resource
    private PatVisitMapper patVisitDao;


    /**
     * 查询记录
     * @param patientId
     * @return
     */
    @Override
    public PatVisit queryPatVisit(String patientId){
        List<PatVisit> list = this.patVisitDao.queryPatVisit(patientId);
        PatVisit pv =null;
        if(list!=null && list.size()>0){
            pv = list.get(0);
        }
        return pv;
    }
    /**
     * 分页查询
     *
     * @param patVisit 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    @Override
    public Page<PatVisit> queryByPage(PatVisit patVisit, PageRequest pageRequest) {
        long total = this.patVisitDao.count(patVisit);
        return new PageImpl<>(this.patVisitDao.queryAllByLimit(patVisit, pageRequest), pageRequest, total);
    }

    /**
     * 新增数据
     *
     * @param patVisit 实例对象
     * @return 实例对象
     */
    @Override
    public PatVisit insert(PatVisit patVisit) {
        this.patVisitDao.insert(patVisit);
        return patVisit;
    }

    /**
     * 修改数据
     *
     * @param patVisit 实例对象
     * @return 实例对象
     */
    @Override
    public PatVisit update(PatVisit patVisit) {
        this.patVisitDao.update(patVisit);
        return patVisit;
    }

    @Override
    public int querySyncInfoCount(PatVisit patVisit) {
        return this.patVisitDao.querySyncInfoCount(patVisit);
    }

    @Override
    public Page<PatVisit> querySyncInfoByPage(PatVisit patVisit, PageRequest pageRequest, long total) {
        return new PageImpl<>(this.patVisitDao.querySyncInfoByPage(patVisit, pageRequest), pageRequest, total);
    }
}
