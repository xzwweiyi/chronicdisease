package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Jcmxx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 查明细项目(Jcmxx)表服务接口
 *
 * @author makejava
 * @since 2023-03-22 17:39:51
 */
public interface JcmxxService {

    /**
     * 通过ID查询单条数据
     *
     * @param
     * @return 实例对象
     */
    Jcmxx queryById( );

    /**
     * 分页查询
     *
     * @param jcmxx 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<Jcmxx> queryByPage(Jcmxx jcmxx, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param jcmxx 实例对象
     * @return 实例对象
     */
    Jcmxx insert(Jcmxx jcmxx);


    /**
     * 通过主键删除数据
     *
     * @param  主键
     * @return 是否成功
     */
    boolean deleteById( );

}
