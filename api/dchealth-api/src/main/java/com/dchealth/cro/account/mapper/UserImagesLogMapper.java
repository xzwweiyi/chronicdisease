package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.UserImagesLog;

/**
 * 医生图片日志Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface UserImagesLogMapper 
{
    /**
     * 查询医生图片日志
     * 
     * @param id 医生图片日志主键
     * @return 医生图片日志
     */
    public UserImagesLog selectUserImagesLogById(Long id);

    /**
     * 查询医生图片日志列表
     * 
     * @param userImagesLog 医生图片日志
     * @return 医生图片日志集合
     */
    public List<UserImagesLog> selectUserImagesLogList(UserImagesLog userImagesLog);

    /**
     * 新增医生图片日志
     * 
     * @param userImagesLog 医生图片日志
     * @return 结果
     */
    public int insertUserImagesLog(UserImagesLog userImagesLog);

    /**
     * 修改医生图片日志
     * 
     * @param userImagesLog 医生图片日志
     * @return 结果
     */
    public int updateUserImagesLog(UserImagesLog userImagesLog);

    /**
     * 删除医生图片日志
     * 
     * @param id 医生图片日志主键
     * @return 结果
     */
    public int deleteUserImagesLogById(Long id);

    /**
     * 批量删除医生图片日志
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserImagesLogByIds(String[] ids);
}
