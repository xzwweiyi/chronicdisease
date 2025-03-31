package com.dchealth.cro.research.mapper;

import com.dchealth.cro.research.domain.ResearchInfoArticlePatient;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * 患者完成患教量关系Mapper接口
 *
 * @author lchbu
 * @date 2022-09-15
 */
@Repository
public interface ResearchInfoArticlePatientMapper
{
    /**
     * 查询患者完成患教量关系
     *
     * @param accountId 患者完成患教量关系主键
     * @return 患者完成患教量关系
     */
    public ResearchInfoArticlePatient selectResearchInfoArticlePatientByAccountId(Long accountId);

    /**
     * 查询患者完成患教量关系列表
     *
     * @param researchInfoArticlePatient 患者完成患教量关系
     * @return 患者完成患教量关系集合
     */
    public List<ResearchInfoArticlePatient> selectResearchInfoArticlePatientList(ResearchInfoArticlePatient researchInfoArticlePatient);

    public List<ResearchInfoArticlePatient> selectResearchInfoArticlePatientByArticleIdAndAccountId(ResearchInfoArticlePatient researchInfoArticlePatient);

    /**
     * 新增患者完成患教量关系
     *
     * @param researchInfoArticlePatient 患者完成患教量关系
     * @return 结果
     */
    public int insertResearchInfoArticlePatient(ResearchInfoArticlePatient researchInfoArticlePatient);

    /**
     * 修改患者完成患教量关系
     *
     * @param researchInfoArticlePatient 患者完成患教量关系
     * @return 结果
     */
    public int updateResearchInfoArticlePatient(ResearchInfoArticlePatient researchInfoArticlePatient);
    public int updateResearchInfoArticlePatientByExamRecordId(ResearchInfoArticlePatient researchInfoArticlePatient);

    /**
     * 删除患者完成患教量关系
     *
     * @param accountId 患者完成患教量关系主键
     * @return 结果
     */
    public int deleteResearchInfoArticlePatientByAccountId(Long accountId);

    /**
     * 批量删除患者完成患教量关系
     *
     * @param accountIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteResearchInfoArticlePatientByAccountIds(Long[] accountIds);

    /**
     * 根据账号、问卷id、队列id查询问卷状态
     *
     * @param articlePatient
     * @return
     */
    ResearchInfoArticlePatient selectResearchInfoArticlePatient(ResearchInfoArticlePatient articlePatient);

    /**
     * 根据账号、问卷id、队列id删除患教关系量表
     *
     * @param articlePatient
     */
    void deleteResearchInfoArticlePatient(ResearchInfoArticlePatient articlePatient);

    /**
     * 根据账号、问卷id、队列id更新问卷状态
     *
     * @param articlePatient
     */
    void updateResearchInfoArticlePatientStatus(ResearchInfoArticlePatient articlePatient);
}
