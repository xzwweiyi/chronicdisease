package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.SysUserLog;

/**
 * 用户信息表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface SysUserLogMapper 
{
    /**
     * 查询用户信息表
     * 
     * @param logsId 用户信息表主键
     * @return 用户信息表
     */
    public SysUserLog selectSysUserLogByLogsId(Long logsId);

    /**
     * 查询用户信息表列表
     * 
     * @param sysUserLog 用户信息表
     * @return 用户信息表集合
     */
    public List<SysUserLog> selectSysUserLogList(SysUserLog sysUserLog);

    /**
     * 新增用户信息表
     * 
     * @param sysUserLog 用户信息表
     * @return 结果
     */
    public int insertSysUserLog(SysUserLog sysUserLog);

    /**
     * 修改用户信息表
     * 
     * @param sysUserLog 用户信息表
     * @return 结果
     */
    public int updateSysUserLog(SysUserLog sysUserLog);

    /**
     * 删除用户信息表
     * 
     * @param logsId 用户信息表主键
     * @return 结果
     */
    public int deleteSysUserLogByLogsId(Long logsId);

    /**
     * 批量删除用户信息表
     * 
     * @param logsIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserLogByLogsIds(String[] logsIds);
}
