package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jcb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 检查表(Jcb)表服务接口
 *
 * @author makejava
 * @since 2023-03-22 17:25:14
 */
public interface JcbService {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    Jcb queryById(String studyId );

    /**
     * 分页查询
     *
     * @param jcb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Jcb> queryByPage(Jcb jcb, PageRequest pageRequest);

    long querySyncInfoCount(Jcb jcb);

    /**
     * 分页查询
     *
     * @param jcb 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<Jcb> querySyncInfoByPage(Jcb jcb, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param jcb 实例对象
     * @return 实例对象
     */
    Jcb insert(Jcb jcb);

    /**
     * 通过主键删除数据
     *
     * @param
     * @return 是否成功
     */
    boolean deleteById( );

}
