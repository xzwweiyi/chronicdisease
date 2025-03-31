package com.dchealth.research.service.impl;

import java.util.List;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.research.domain.SysPopNotice;
import com.dchealth.research.mapper.SysPopNoticeMapper;
import com.dchealth.research.service.ISysPopNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息弹框Service业务层处理
 * 
 * @author lchbu
 * @date 2024-01-23
 */
@Service
public class SysPopNoticeServiceImpl implements ISysPopNoticeService
{
    @Autowired
    private SysPopNoticeMapper sysPopNoticeMapper;

    /**
     * 查询消息弹框
     * 
     * @param id 消息弹框主键
     * @return 消息弹框
     */
    @Override
    public SysPopNotice selectSysPopNoticeById(Long id)
    {
        return sysPopNoticeMapper.selectSysPopNoticeById(id);
    }

    /**
     * 查询消息弹框列表
     * 
     * @param sysPopNotice 消息弹框
     * @return 消息弹框
     */
    @Override
    public List<SysPopNotice> selectSysPopNoticeList(SysPopNotice sysPopNotice)
    {
        return sysPopNoticeMapper.selectSysPopNoticeList(sysPopNotice);
    }

    /**
     * 新增消息弹框
     * 
     * @param sysPopNotice 消息弹框
     * @return 结果
     */
    @Override
    public int insertSysPopNotice(SysPopNotice sysPopNotice)
    {
        sysPopNotice.setCreateTime(DateUtils.getNowDate());
        return sysPopNoticeMapper.insertSysPopNotice(sysPopNotice);
    }

    /**
     * 修改消息弹框
     * 
     * @param sysPopNotice 消息弹框
     * @return 结果
     */
    @Override
    public int updateSysPopNotice(SysPopNotice sysPopNotice)
    {
        sysPopNotice.setUpdateTime(DateUtils.getNowDate());
        return sysPopNoticeMapper.updateSysPopNotice(sysPopNotice);
    }

    /**
     * 批量删除消息弹框
     * 
     * @param ids 需要删除的消息弹框主键
     * @return 结果
     */
    @Override
    public int deleteSysPopNoticeByIds(Long[] ids)
    {
        return sysPopNoticeMapper.deleteSysPopNoticeByIds(ids);
    }

    /**
     * 删除消息弹框信息
     * 
     * @param id 消息弹框主键
     * @return 结果
     */
    @Override
    public int deleteSysPopNoticeById(Long id)
    {
        return sysPopNoticeMapper.deleteSysPopNoticeById(id);
    }
}
