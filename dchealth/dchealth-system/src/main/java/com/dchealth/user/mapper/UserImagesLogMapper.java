package com.dchealth.user.mapper;

import java.util.List;
import com.dchealth.user.domain.UserImagesLog;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-13
 */
public interface UserImagesLogMapper
{
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public UserImagesLog selectUserImagesLogById(Long id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userImagesLog 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<UserImagesLog> selectUserImagesLogList(UserImagesLog userImagesLog);

    /**
     * 新增【请填写功能名称】
     *
     * @param userImagesLog 【请填写功能名称】
     * @return 结果
     */
    public int insertUserImagesLog(UserImagesLog userImagesLog);

    /**
     * 修改【请填写功能名称】
     *
     * @param userImagesLog 【请填写功能名称】
     * @return 结果
     */
    public int updateUserImagesLog(UserImagesLog userImagesLog);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteUserImagesLogById(Long id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserImagesLogByIds(Long[] ids);
}
