package com.dchealth.warn.mapper;

import java.util.List;
import com.dchealth.warn.domain.WarnGuideMsg;

/**
 * 预警指导信息Mapper接口
 * 
 * @author lchbu
 * @date 2022-10-08
 */
public interface WarnGuideMsgMapper 
{
    /**
     * 查询预警指导信息
     * 
     * @param warnGuideMsgId 预警指导信息主键
     * @return 预警指导信息
     */
    public WarnGuideMsg selectWarnGuideMsgByWarnGuideMsgId(Long warnGuideMsgId);

    /**
     * 查询预警指导信息列表
     * 
     * @param warnGuideMsg 预警指导信息
     * @return 预警指导信息集合
     */
    public List<WarnGuideMsg> selectWarnGuideMsgList(WarnGuideMsg warnGuideMsg);

    /**
     * 新增预警指导信息
     * 
     * @param warnGuideMsg 预警指导信息
     * @return 结果
     */
    public int insertWarnGuideMsg(WarnGuideMsg warnGuideMsg);

    /**
     * 修改预警指导信息
     * 
     * @param warnGuideMsg 预警指导信息
     * @return 结果
     */
    public int updateWarnGuideMsg(WarnGuideMsg warnGuideMsg);

    /**
     * 删除预警指导信息
     * 
     * @param warnGuideMsgId 预警指导信息主键
     * @return 结果
     */
    public int deleteWarnGuideMsgByWarnGuideMsgId(Long warnGuideMsgId);

    /**
     * 批量删除预警指导信息
     * 
     * @param warnGuideMsgIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWarnGuideMsgByWarnGuideMsgIds(Long[] warnGuideMsgIds);

    /**
     * 根据档案ID查询
     * @param recordId
     * @return
     */
    public List<WarnGuideMsg> selectWarnGuideMsgListByRecordId(Long recordId);
}
