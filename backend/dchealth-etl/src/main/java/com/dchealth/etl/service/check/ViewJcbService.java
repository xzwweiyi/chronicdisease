package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jcb;
import com.dchealth.etl.domain.check.ViewJcb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 检查结果(ViewJcb)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public interface ViewJcbService {

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    ViewJcb queryById(String studyid);

    /**
     * 分页查询
     *
     * @param viewJcb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ViewJcb> queryByPage(ViewJcb viewJcb, PageRequest pageRequest);

    long querySyncInfoCount(ViewJcb jcb);

    /**
     * 分页查询
     *
     * @param jcb 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<ViewJcb> querySyncInfoByPage(ViewJcb jcb, PageRequest pageRequest,long total);

    /**
     * 新增数据
     *
     * @param viewJcb 实例对象
     * @return 实例对象
     */
    ViewJcb insert(ViewJcb viewJcb);

    /**
     * 修改数据
     *
     * @param viewJcb 实例对象
     * @return 实例对象
     */
    ViewJcb update(ViewJcb viewJcb);

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 是否成功
     */
    boolean deleteById(String studyid);

}
