package com.dchealth.cro.account.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dchealth.cro.account.domain.UserImages;

/**
 * 医生头像Mapper接口
 * 
 * @author genemax1107@aliyun.com
 * @date 2022-06-14
 */
@Repository
public interface UserImagesMapper 
{
    /**
     * 查询医生头像
     * 
     * @param id 医生头像主键
     * @return 医生头像
     */
    public UserImages selectUserImagesById(Long id);

    /**
     * 查询医生头像列表
     * 
     * @param userImages 医生头像
     * @return 医生头像集合
     */
    public List<UserImages> selectUserImagesList(UserImages userImages);

    /**
     * 新增医生头像
     * 
     * @param userImages 医生头像
     * @return 结果
     */
    public int insertUserImages(UserImages userImages);

    /**
     * 修改医生头像
     * 
     * @param userImages 医生头像
     * @return 结果
     */
    public int updateUserImages(UserImages userImages);

    /**
     * 删除医生头像
     * 
     * @param id 医生头像主键
     * @return 结果
     */
    public int deleteUserImagesById(Long id);

    /**
     * 批量删除医生头像
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserImagesByIds(String[] ids);
}
