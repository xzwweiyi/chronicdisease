package com.dchealth.etl.service.record;

import com.dchealth.etl.domain.record.Orders;
import com.dchealth.etl.domain.record.OutpMedrec;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 门诊诊断表(OutpMedrec)表服务接口
 *
 * @author makejava
 * @since 2023-05-18 15:33:58
 */
public interface OutpMedrecService {


    /**
     * 分页查询
     *
     * @param outpMedrec 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<OutpMedrec> queryByPage(OutpMedrec outpMedrec, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param outpMedrec 实例对象
     * @return 实例对象
     */
    OutpMedrec insert(OutpMedrec outpMedrec);

    /**
     * 修改数据
     *
     * @param outpMedrec 实例对象
     * @return 实例对象
     */
    OutpMedrec update(OutpMedrec outpMedrec);

    /**
     * 查询当天同步数据量
     * @param outpMedrec
     * @return
     */
    int querySyncInfoCount(OutpMedrec outpMedrec);
    /**
     * 分页查询
     * @param outpMedrec
     * @param pageRequest
     * @return
     */
    Page<OutpMedrec> querySyncInfoByPage(OutpMedrec outpMedrec, PageRequest pageRequest,long total);


}
