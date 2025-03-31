package com.dchealth.warn.mapper;

import java.util.List;
import com.dchealth.warn.domain.WarnGuideUser;

/**
 * 预警指导人员Mapper接口
 * 
 * @author lchbu
 * @date 2022-10-08
 */
public interface WarnGuideUserMapper 
{
    /**
     * 查询预警指导人员
     * 
     * @param warnGuideUserId 预警指导人员主键
     * @return 预警指导人员
     */
    public WarnGuideUser selectWarnGuideUserByWarnGuideUserId(Long warnGuideUserId);

    /**
     * 查询预警指导人员列表
     * 
     * @param warnGuideUser 预警指导人员
     * @return 预警指导人员集合
     */
    public List<WarnGuideUser> selectWarnGuideUserList(WarnGuideUser warnGuideUser);

    /**
     * 根据档案ID查询
     * @param recordId
     * @return
     */
    public List<WarnGuideUser> selectWarnGuideUserListByRecordId(Long recordId);

    /**
     * 新增预警指导人员
     * 
     * @param warnGuideUser 预警指导人员
     * @return 结果
     */
    public int insertWarnGuideUser(WarnGuideUser warnGuideUser);

    /**
     * 修改预警指导人员
     * 
     * @param warnGuideUser 预警指导人员
     * @return 结果
     */
    public int updateWarnGuideUser(WarnGuideUser warnGuideUser);

    /**
     * 删除预警指导人员
     * 
     * @param warnGuideUserId 预警指导人员主键
     * @return 结果
     */
    public int deleteWarnGuideUserByWarnGuideUserId(Long warnGuideUserId);

    /**
     * 批量删除预警指导人员
     * 
     * @param warnGuideUserIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnGuideUserByWarnGuideUserIds(Long[] warnGuideUserIds);
}
