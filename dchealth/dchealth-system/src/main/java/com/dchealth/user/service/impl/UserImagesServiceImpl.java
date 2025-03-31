package com.dchealth.user.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.SecurityUtils;
import com.dchealth.common.utils.bean.BeanUtils;
import com.dchealth.user.domain.UserImages;
import com.dchealth.user.domain.UserImagesLog;
import com.dchealth.user.mapper.UserImagesLogMapper;
import com.dchealth.user.mapper.UserImagesMapper;
import com.dchealth.user.service.IUserImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-13
 */
@Service
public class UserImagesServiceImpl implements IUserImagesService
{
    @Autowired
    private UserImagesMapper userImagesMapper;

    @Autowired
    private UserImagesLogMapper userImagesLogMapper;


    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public UserImages selectUserImagesById(Long id)
    {
        return userImagesMapper.selectUserImagesById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param userImages 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<UserImages> selectUserImagesList(UserImages userImages)
    {
        return userImagesMapper.selectUserImagesList(userImages);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param userImages 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertUserImages(UserImages userImages)
    {
        userImages.setCreateTime(DateUtils.getNowDate());
        UserImagesLog userImagesLog = new UserImagesLog();
        BeanUtils.copyBeanProp(userImagesLog,userImages);
        userImagesLog.setCreateBy(SecurityUtils.getUsername());
        userImagesLogMapper.insertUserImagesLog(userImagesLog);
        return userImagesMapper.insertUserImages(userImages);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param userImages 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateUserImages(UserImages userImages)
    {
        return userImagesMapper.updateUserImages(userImages);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserImagesByIds(Long[] ids)
    {
        return userImagesMapper.deleteUserImagesByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteUserImagesById(Long id)
    {
        return userImagesMapper.deleteUserImagesById(id);
    }
}
