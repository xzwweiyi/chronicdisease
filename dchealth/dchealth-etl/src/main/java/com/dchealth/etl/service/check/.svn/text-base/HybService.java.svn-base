package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Hyb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 化验结果表（HYB）(Hyb)表服务接口
 *
 * @author makejava
 * @since 2023-03-27 10:46:29
 */
public interface HybService {


    /**
     * 分页查询
     *
     * @param hyb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Hyb> queryByPage(Hyb hyb, PageRequest pageRequest);


    long querySyncInfoCount(Hyb hyb);

    /**
     * 分页查询
     *
     * @param hyb 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<Hyb> querySyncInfoByPage(Hyb hyb, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param hyb 实例对象
     * @return 实例对象
     */
    Hyb insert(Hyb hyb);



}
