package com.dchealth.user.service.impl;

import java.util.List;

import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.SecurityUtils;
import com.dchealth.common.utils.bean.BeanUtils;
import com.dchealth.user.domain.UserInfo;
import com.dchealth.user.domain.UserInfoLog;
import com.dchealth.user.mapper.UserInfoLogMapper;
import com.dchealth.user.mapper.UserInfoMapper;
import com.dchealth.user.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-13
 */
@Service
public class UserInfoServiceImpl implements IUserInfoService
{
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoLogMapper userInfoLogMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param userId 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public UserInfo selectUserInfoByUserId(Long userId)
    {
        return userInfoMapper.selectUserInfoByUserId(userId);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserInfo> selectUserInfoList(UserInfo userInfo)
    {
        return userInfoMapper.selectUserInfoList(userInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param userInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserInfo(UserInfo userInfo)
    {
        UserInfoLog userInfoLog = new UserInfoLog();
        Long userId = SecurityUtils.getUserId();
        UserInfo info = userInfoMapper.selectUserInfoByUserId(userId);
        if(null != info){
            throw new ServiceException("用户已存在，不允许新增");
        }
        userInfo.setUserId(userId);
        //记录日志信息
        BeanUtils.copyBeanProp(userInfoLog,userInfo);
        userInfoLogMapper.insertUserInfoLog(userInfoLog);
        return userInfoMapper.insertUserInfo(userInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param userInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserInfo(UserInfo userInfo)
    {
        Long userId = SecurityUtils.getUserId();
        userInfo.setUserId(userId.longValue());
        return userInfoMapper.updateUserInfo(userInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param userIds 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoByUserIds(Long[] userIds)
    {
        return userInfoMapper.deleteUserInfoByUserIds(userIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param userId 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserInfoByUserId(Long userId)
    {
        return userInfoMapper.deleteUserInfoByUserId(userId);
    }
}
