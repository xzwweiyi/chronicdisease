package com.dchealth.user.service;

import java.util.List;
import com.dchealth.user.domain.UserInfo;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2022-06-13
 */
public interface IUserInfoService
{
    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserInfo selectUserInfoByUserId(Long userId);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userInfo 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    /**
     * 新增【请填写功能名称】
     *
     * @param userInfo 【请填写功能名称】
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改【请填写功能名称】
     *
     * @param userInfo 【请填写功能名称】
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteUserInfoByUserIds(Long[] userIds);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserInfoByUserId(Long userId);
}
