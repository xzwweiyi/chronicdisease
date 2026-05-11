package com.dchealth.research.mapper;

import java.util.List;

import com.dchealth.medical.domain.form.TreeForm;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.research.domain.vo.ResearchInfoVo;
import org.apache.ibatis.annotations.Param;

/**
 * 队列基础信息Mapper接口
 * 
 * @author lchbu
 * @date 2022-08-22
 */
public interface ResearchInfoMapper 
{
    /**
     * 查询队列基础信息
     * 
     * @param id 队列基础信息主键
     * @return 队列基础信息
     */
    public ResearchInfo selectResearchInfoById(Long id);


    /**
     * 根据状态查询队列
     * @param researchStatus
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByStatus(String researchStatus);

    /**
     * 随访工作站使用需完善
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByAuth(TreeForm treeForm);

    public List<ResearchInfo> selectResearchInfoByUserId(TreeForm treeForm);

    /**
     * 根据ids查询
     * @param ids
     * @return
     */
    public List<ResearchInfo> selectResearchInfoByIds(Long[] ids);

    /**
     * 查询队列基础信息列表
     * 
     * @param researchInfo 队列基础信息
     * @return 队列基础信息集合
     */
    public List<ResearchInfo> selectResearchInfoList(ResearchInfo researchInfo);

    public List<ResearchInfo> selectResearchInfoListLess(ResearchInfo researchInfo);

    /**
     * 根据病种查询队列
     * @param diseaseId
     * @return
     */
    public List<ResearchInfo> selectResearchInfoListByDiseaseId(Long diseaseId);

    /**
     * 根据兵种id查询信息
     * @param diseaseId 兵种id
     * @return 信息列表
     */
    public List<ResearchInfo> selectListByDiseaseTypeId(Long diseaseId);

    public List<ResearchInfo> selectResearchListByDiseaseId(Long diseaseId);

    /**
     * 新增队列基础信息
     * 
     * @param researchInfo 队列基础信息
     * @return 结果
     */
    public int insertResearchInfo(ResearchInfo researchInfo);

    /**
     * 修改队列基础信息
     * 
     * @param researchInfo 队列基础信息
     * @return 结果
     */
    public int updateResearchInfo(ResearchInfo researchInfo);

    /**
     * 删除队列基础信息
     * 
     * @param id 队列基础信息主键
     * @return 结果
     */
    public int deleteResearchInfoById(Long id);

    /**
     * 批量删除队列基础信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchInfoByIds(Long[] ids);

    /**
     * 查询队列详细信息
     * @param vo
     * @return
     */
    List<ResearchInfoVo> selectResearchInfoDetailList(ResearchInfoVo vo);

    /**
     * 根据id查询详情
     * @param id
     * @return
     */
    ResearchInfoVo selectResearchInfoDetailById(Long id);

    /**
     * 更新删除状态
     * @param id
     * @return
     */
    int updateInfoDelFlagById(Long id);
    /**
     * 关闭随访率状态
     * @param id
     * @return
     */
    int closeFollowRate(Long id);

    /**
     * 更新阶段信息
     * @param id
     * @return
     */
    int updateResearchInfoStage(@Param("id") Long id, @Param("stage") String stage);

    /**
     * 更新队列状态
     * @param id
     * @param status
     * @return
     */
    int updateResearchInfoStatus(@Param("id") Long id, @Param("status") String status);

    List<ResearchInfo> selectResearchInfoByStatus2();


}
