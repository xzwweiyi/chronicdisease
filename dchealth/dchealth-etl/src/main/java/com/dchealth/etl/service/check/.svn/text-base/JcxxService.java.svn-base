package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jcxx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * 检查信息表(Jcxx)表服务接口
 *
 * @author makejava
 * @since 2023-03-22 17:44:08
 */
public interface JcxxService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Jcxx queryById(String id);

    /**
     * 分页查询
     *
     * @param jcxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Jcxx> queryByPage(Jcxx jcxx, PageRequest pageRequest);

    /**
     * 分页查询
     *
     * @param jcxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    List<Jcxx> querySyncInfoByPage(Jcxx jcxx, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param jcxx 实例对象
     * @return 实例对象
     */
    Jcxx insert(Jcxx jcxx);

    /**
     * 修改数据
     *
     * @param jcxx 实例对象
     * @return 实例对象
     */
    Jcxx update(Jcxx jcxx);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(String id);

}
