package com.dchealth.cro.research.mapper;

import com.dchealth.cro.research.domain.SysPopNotice;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 消息弹框Mapper接口
 * 
 * @author lchbu
 * @date 2024-01-23
 */
@Repository
public interface SysPopNoticeMapper 
{
    /**
     * 查询消息弹框
     * 
     * @param id 消息弹框主键
     * @return 消息弹框
     */
    public SysPopNotice selectSysPopNoticeById(Long id);

    /**
     * 查询消息弹框列表
     * 
     * @param sysPopNotice 消息弹框
     * @return 消息弹框集合
     */
    public List<SysPopNotice> selectSysPopNoticeList(SysPopNotice sysPopNotice);

    /**
     * 新增消息弹框
     * 
     * @param sysPopNotice 消息弹框
     * @return 结果
     */
    public int insertSysPopNotice(SysPopNotice sysPopNotice);

    /**
     * 修改消息弹框
     * 
     * @param sysPopNotice 消息弹框
     * @return 结果
     */
    public int updateSysPopNotice(SysPopNotice sysPopNotice);

    /**
     * 删除消息弹框
     * 
     * @param id 消息弹框主键
     * @return 结果
     */
    public int deleteSysPopNoticeById(Long id);

    /**
     * 批量删除消息弹框
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysPopNoticeByIds(Long[] ids);
}
