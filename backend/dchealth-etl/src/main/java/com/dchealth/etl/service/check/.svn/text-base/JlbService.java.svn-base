package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jlb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

/**
 * 结论表(Jlb)表服务接口
 *
 * @author makejava
 * @since 2023-03-22 17:46:18
 */
public interface JlbService {


    /**
     * 分页查询
     *
     * @param jlb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Jlb> queryByPage(Jlb jlb, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param jlb 实例对象
     * @return 实例对象
     */
    Jlb insert(Jlb jlb);

    long querySyncInfoCount(Jlb jlb);

    /**
     * 分页查询
     *
     * @param jlb 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<Jlb> querySyncInfoByPage(Jlb jlb, PageRequest pageRequest,long total);

    public List querySyncInfoByPage(Map params);
}
