package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.UserInfo;

/**
 * 医生信息表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface UserInfoMapper 
{
    /**
     * 查询医生信息表
     * 
     * @param userId 医生信息表主键
     * @return 医生信息表
     */
    public UserInfo selectUserInfoByUserId(Long userId);

    /**
     * 查询医生信息表列表
     * 
     * @param userInfo 医生信息表
     * @return 医生信息表集合
     */
    public List<UserInfo> selectUserInfoList(UserInfo userInfo);

    /**
     * 新增医生信息表
     * 
     * @param userInfo 医生信息表
     * @return 结果
     */
    public int insertUserInfo(UserInfo userInfo);

    /**
     * 修改医生信息表
     * 
     * @param userInfo 医生信息表
     * @return 结果
     */
    public int updateUserInfo(UserInfo userInfo);

    /**
     * 删除医生信息表
     * 
     * @param userId 医生信息表主键
     * @return 结果
     */
    public int deleteUserInfoByUserId(Long userId);

    /**
     * 批量删除医生信息表
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserInfoByUserIds(String[] userIds);
}
