package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jlb;
import com.dchealth.etl.domain.check.ViewJlb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 体检结论(ViewJlb)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 11:34:38
 */
public interface ViewJlbService {

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    ViewJlb queryById(String studyid);

    /**
     * 分页查询
     *
     * @param viewJlb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ViewJlb> queryByPage(ViewJlb viewJlb, PageRequest pageRequest);

    long querySyncInfoCount(ViewJlb jlb);

    /**
     * 分页查询
     *
     * @param jlb 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<ViewJlb> querySyncInfoByPage(ViewJlb jlb, PageRequest pageRequest,long total);

    /**
     * 新增数据
     *
     * @param viewJlb 实例对象
     * @return 实例对象
     */
    ViewJlb insert(ViewJlb viewJlb);

    /**
     * 修改数据
     *
     * @param viewJlb 实例对象
     * @return 实例对象
     */
    ViewJlb update(ViewJlb viewJlb);

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 是否成功
     */
    boolean deleteById(String studyid);

}
