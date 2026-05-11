package com.dchealth.research.service;

import java.util.List;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.vo.ResearchInfoVo;

/**
 * 队列基础信息Service接口
 * 
 * @author lchbu
 * @date 2022-08-22
 */
public interface IResearchInfoService 
{
    /**
     * 查询队列基础信息
     * 
     * @param id 队列基础信息主键
     * @return 队列基础信息
     */
    public ResearchInfo selectResearchInfoById(Long id);

    /**
     * 查询队列基础信息列表
     * 
     * @param researchInfo 队列基础信息
     * @return 队列基础信息集合
     */
    public List<ResearchInfo> selectResearchInfoList(ResearchInfo researchInfo);

    /**
     * 新增队列基础信息
     * 
     * @param vo 队列基础信息
     * @return 结果
     */
    public Long insertResearchInfo(ResearchInfoVo vo);

    /**
     * 修改队列基础信息
     * 
     * @param vo 队列基础信息
     * @return 结果
     */
    public int updateResearchInfo(ResearchInfoVo vo);

    /**
     * 批量删除队列基础信息
     * 
     * @param ids 需要删除的队列基础信息主键集合
     * @return 结果
     */
    public int deleteResearchInfoByIds(Long[] ids);

    /**
     * 删除队列基础信息信息
     * 
     * @param id 队列基础信息主键
     * @return 结果
     */
    public int deleteResearchInfoById(Long id);

    /**
     * 查询队列详细信息
     * @param vo
     * @return
     */
    List<ResearchInfoVo> selectResearchInfoDetailList(ResearchInfoVo vo);

    /**
     * 根据id查询详情信息
     * @param id
     * @return
     */
    ResearchInfoVo selectResearchInfoDetailById(Long id);

    /**
     * 更新删除状态
     * @param id
     * @return
     */
    public int updateInfoDelFlagById(Long id);

    /**
     * 更新队列阶段
     * @param id
     * @return
     */
    int updateResearchInfoStage(Long id, String stage);

    /**
     * 更新队列状态
     * @param id
     * @param status
     * @return
     */
    int updateResearchInfoStatus(Long id, String status);

    int delete(Long id);

    /**
     * 创建表： 针对为发布的队列 在添加随访用户时创建需要的表
     * @param id 队列id
     */
    void createTableInAddUser(Long id);

    int closeFollowRate(Long id);
}
