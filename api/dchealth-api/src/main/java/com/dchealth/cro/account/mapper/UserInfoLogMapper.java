package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.UserInfoLog;

/**
 * 医生信息日志表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface UserInfoLogMapper 
{
    /**
     * 查询医生信息日志表
     * 
     * @param logId 医生信息日志表主键
     * @return 医生信息日志表
     */
    public UserInfoLog selectUserInfoLogByLogId(Long logId);

    /**
     * 查询医生信息日志表列表
     * 
     * @param userInfoLog 医生信息日志表
     * @return 医生信息日志表集合
     */
    public List<UserInfoLog> selectUserInfoLogList(UserInfoLog userInfoLog);

    /**
     * 新增医生信息日志表
     * 
     * @param userInfoLog 医生信息日志表
     * @return 结果
     */
    public int insertUserInfoLog(UserInfoLog userInfoLog);

    /**
     * 修改医生信息日志表
     * 
     * @param userInfoLog 医生信息日志表
     * @return 结果
     */
    public int updateUserInfoLog(UserInfoLog userInfoLog);

    /**
     * 删除医生信息日志表
     * 
     * @param logId 医生信息日志表主键
     * @return 结果
     */
    public int deleteUserInfoLogByLogId(Long logId);

    /**
     * 批量删除医生信息日志表
     * 
     * @param logIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserInfoLogByLogIds(String[] logIds);
}
