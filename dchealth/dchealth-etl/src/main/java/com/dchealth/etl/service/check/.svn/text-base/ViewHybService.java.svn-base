package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.Hyb;
import com.dchealth.etl.domain.check.ViewHyb;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 检验结果(ViewHyb)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 11:34:37
 */
public interface ViewHybService {

    /**
     * 通过ID查询单条数据
     *
     * @param studyid 主键
     * @return 实例对象
     */
    ViewHyb queryById(String studyid);

    /**
     * 分页查询
     *
     * @param viewHyb 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ViewHyb> queryByPage(ViewHyb viewHyb, PageRequest pageRequest);


    long querySyncInfoCount(ViewHyb hyb);

    /**
     * 分页查询
     *
     * @param hyb 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<ViewHyb> querySyncInfoByPage(ViewHyb hyb, PageRequest pageRequest,long total);
    /**
     * 新增数据
     *
     * @param viewHyb 实例对象
     * @return 实例对象
     */
    ViewHyb insert(ViewHyb viewHyb);

    /**
     * 修改数据
     *
     * @param viewHyb 实例对象
     * @return 实例对象
     */
    ViewHyb update(ViewHyb viewHyb);

    /**
     * 通过主键删除数据
     *
     * @param studyid 主键
     * @return 是否成功
     */
    boolean deleteById(String studyid);

}
