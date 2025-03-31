package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.UserLoginLogs;

/**
 * 医生登录日志Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface UserLoginLogsMapper 
{
    /**
     * 查询医生登录日志
     * 
     * @param id 医生登录日志主键
     * @return 医生登录日志
     */
    public UserLoginLogs selectUserLoginLogsById(Long id);

    /**
     * 查询医生登录日志列表
     * 
     * @param userLoginLogs 医生登录日志
     * @return 医生登录日志集合
     */
    public List<UserLoginLogs> selectUserLoginLogsList(UserLoginLogs userLoginLogs);

    /**
     * 新增医生登录日志
     * 
     * @param userLoginLogs 医生登录日志
     * @return 结果
     */
    public int insertUserLoginLogs(UserLoginLogs userLoginLogs);

    /**
     * 修改医生登录日志
     * 
     * @param userLoginLogs 医生登录日志
     * @return 结果
     */
    public int updateUserLoginLogs(UserLoginLogs userLoginLogs);

    /**
     * 删除医生登录日志
     * 
     * @param id 医生登录日志主键
     * @return 结果
     */
    public int deleteUserLoginLogsById(Long id);

    /**
     * 批量删除医生登录日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserLoginLogsByIds(String[] ids);
}
