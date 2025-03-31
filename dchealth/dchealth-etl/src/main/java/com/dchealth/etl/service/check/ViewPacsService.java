package com.dchealth.etl.service.check;

import com.dchealth.etl.domain.check.ViewJcb;
import com.dchealth.etl.domain.check.ViewPacs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 影像类检查(ViewPacs)表服务接口
 *
 * @author makejava
 * @since 2023-06-06 11:34:38
 */
public interface ViewPacsService {


    /**
     * 分页查询
     *
     * @param viewPacs 筛选条件
     * @param pageRequest      分页对象
     * @return 查询结果
     */
    Page<ViewPacs> queryByPage(ViewPacs viewPacs, PageRequest pageRequest);

    long querySyncInfoCount(ViewPacs pacs);

    /**
     * 分页查询
     *
     * @param pacs 筛选条件
     * @param pageRequest      分页对象
     * @param total
     * @return 查询结果
     */
    Page<ViewPacs> querySyncInfoByPage(ViewPacs pacs, PageRequest pageRequest,long total);

    /**
     * 新增数据
     *
     * @param viewPacs 实例对象
     * @return 实例对象
     */
    ViewPacs insert(ViewPacs viewPacs);



}
