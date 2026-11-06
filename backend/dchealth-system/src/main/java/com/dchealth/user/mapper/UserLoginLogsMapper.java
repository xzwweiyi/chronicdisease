package com.dchealth.user.mapper;

import com.dchealth.user.domain.UserLoginLogs;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-15
 */
public interface UserLoginLogsMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserLoginLogs selectUserLoginLogsById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userLoginLogs 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserLoginLogs> selectUserLoginLogsList(UserLoginLogs userLoginLogs);

    /**
     * 新增【请填写功能名称】
     *
     * @param userLoginLogs 【请填写功能名称】
     * @return 结果
     */
    public int insertUserLoginLogs(UserLoginLogs userLoginLogs);

    /**
     * 修改【请填写功能名称】
     *
     * @param userLoginLogs 【请填写功能名称】
     * @return 结果
     */
    public int updateUserLoginLogs(UserLoginLogs userLoginLogs);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserLoginLogsById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserLoginLogsByIds(Long[] ids);
}
