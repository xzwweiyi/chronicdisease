package com.dchealth.cro.account.mapper;

import com.dchealth.cro.account.domain.SysUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户信息表Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface SysUserMapper 
{
    /**
     * 查询用户信息表
     * 
     * @param userId 用户信息表主键
     * @return 用户信息表
     */
    public SysUser selectSysUserByUserId(Long userId);

    /**
     * 查询用户信息表列表
     * 
     * @param sysUser 用户信息表
     * @return 用户信息表集合
     */
    public List<SysUser> selectSysUserList(SysUser sysUser);

    /**
     * 新增用户信息表
     * 
     * @param sysUser 用户信息表
     * @return 结果
     */
    public int insertSysUser(SysUser sysUser);

    /**
     * 修改用户信息表
     * 
     * @param sysUser 用户信息表
     * @return 结果
     */
    public int updateSysUser(SysUser sysUser);

    /**
     * 删除用户信息表
     * 
     * @param userId 用户信息表主键
     * @return 结果
     */
    public int deleteSysUserByUserId(Long userId);

    /**
     * 批量删除用户信息表
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserByUserIds(String[] userIds);
    
    /**
	 * 根据手机号查询
	 * @param mobile
	 * @return
	 */
    SysUser selectSysUserByMobile(@Param("mobile") String mobile);

    SysUser selectSysUserByMobile2(@Param("mobile") String mobile);
    SysUser selectSysUserByEmail(@Param("email") String email);
    
    SysUser selectSysUserByLoginName(@Param("loginName")String loginName);

    /**
     * 通过用户ID查询对应的部门ID
     * @param userId
     * @return
     */
    Long getDeptIdsByUserId(Long userId);
}
