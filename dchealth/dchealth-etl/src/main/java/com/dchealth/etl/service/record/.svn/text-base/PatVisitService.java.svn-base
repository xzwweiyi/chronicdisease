package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.PatVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 病人住院主记录(PatVisit)表服务接口
 *
 * @author makejava
 * @since 2023-04-13 09:26:57
 */
public interface PatVisitService {

    /**
     * 查询记录
     * @param patientId
     * @return
     */
    PatVisit queryPatVisit(String patientId);
    /**
     * 分页查询
     *
     * @param patVisit 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<PatVisit> queryByPage(PatVisit patVisit, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param patVisit 实例对象
     * @return 实例对象
     */
    PatVisit insert(PatVisit patVisit);

    /**
     * 修改数据
     *
     * @param patVisit 实例对象
     * @return 实例对象
     */
    PatVisit update(PatVisit patVisit);

    /**
     * 查询当天同步数据量
     * @param patVisit
     * @return
     */
    int querySyncInfoCount(PatVisit patVisit);

    /**
     * 分页查询
     * @param patVisit
     * @param pageRequest
     * @return
     */
    Page<PatVisit> querySyncInfoByPage(PatVisit patVisit, PageRequest pageRequest,long total);
}
