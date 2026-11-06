package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jcxx;
import com.dchealth.etl.domain.check.ViewJcxx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 患者基本信息(ViewJcxx)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public interface ViewJcxxService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ViewJcxx queryById(String id);

    /**
     * 分页查询
     *
     * @param viewJcxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ViewJcxx> queryByPage(ViewJcxx viewJcxx, PageRequest pageRequest);
    /**
     * 分页查询
     *
     * @param viewJcxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    List<ViewJcxx> querySyncInfoByPage(ViewJcxx viewJcxx, PageRequest pageRequest);
    /**
     * 新增数据
     *
     * @param viewJcxx 实例对象
     * @return 实例对象
     */
    ViewJcxx insert(ViewJcxx viewJcxx);

    /**
     * 修改数据
     *
     * @param viewJcxx 实例对象
     * @return 实例对象
     */
    ViewJcxx update(ViewJcxx viewJcxx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}
