package com.dchealth.template.mapper;

import com.dchealth.template.domain.FMedicalForm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 单模板Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-08
 */
public interface FMedicalFormMapper
{
    /**
     * 查询单模板
     *
     * @param medicalFormId 单模板主键
     * @return 单模板
     */
    public FMedicalForm selectFMedicalFormByMedicalFormId(Long medicalFormId);

    /**
     * 查询单模板列表
     *
     * @param fMedicalForm 单模板
     * @return 单模板集合
     */
    public List<FMedicalForm> selectFMedicalFormList(FMedicalForm fMedicalForm);

    /**
     * 随访节点id查询
     * @param followId
     * @return
     */
    public List<FMedicalForm> selectFMedicalFormByFollowId(Long followId);

    /**
     * 新增单模板
     *
     * @param fMedicalForm 单模板
     * @return 结果
     */
    public int insertFMedicalForm(FMedicalForm fMedicalForm);

    /**
     * 修改单模板
     *
     * @param fMedicalForm 单模板
     * @return 结果
     */
    public int updateFMedicalForm(FMedicalForm fMedicalForm);

    public int updateFMedicalFormParentSortNum(FMedicalForm fMedicalForm);

    /**
     * 删除单模板
     *
     * @param medicalFormId 单模板主键
     * @return 结果
     */
    public int deleteFMedicalFormByMedicalFormId(Long medicalFormId);

    /**
     * 根据followId查询
     * @param followId
     * @return
     */
    public int deleteFMedicalFormByMedicalFollowId(Long followId);

    /**
     * 批量删除单模板
     *
     * @param medicalFormIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalFormByMedicalFormIds(Long[] medicalFormIds);

    List<FMedicalForm> selectFMedicalFormByArticleId(Long articleId);

    List<FMedicalForm> selectFMedicalFormByFollowIdAndParentId(@Param("followId") Long followId, @Param("parentId") Long parentId);

    public List<FMedicalForm> selectFMedicalFormJoinByMedicalFollowId(@Param("followId") Long followId, @Param("recordId") Long recordId);

    List<FMedicalForm> selectFMedicalFormByMedicalFollowId(Long followId);

    List<FMedicalForm> selectFMedicalFormByMedicalFollowId2(Long followId);

    List<FMedicalForm> selectFMedicalFormByMedicalFollowId3(Long followId);

    List<FMedicalForm> selectFMedicalFormByFollowIds(List<Long> followIds);

    List<FMedicalForm> queryFMedicalFormList(@Param("followIds") List<Long> followIds);

    List<FMedicalForm> selectFMedicalForm(@Param("followId") Long followId,@Param("medicalFormId") Long medicalFormId);


    public int updateScoreByMedicalFormId(@Param("medicalFormId") Long medicalFormId, @Param("scoresTotalVal") String scoresTotalVal);


    public Integer selectMinStatusByRecordIdAndMedicalFormId(@Param("recordId") Long recordId,@Param("followId") Long followId);

    /**
     * 新添加的健康管理的需求
     * @param followId
     * @return
     */
    List<FMedicalForm> selectHealthFMedicalFormByMedicalFollowId(@Param("followId") Long followId);

    /**
     * 健康管理随访表单列表
     * @param followIds
     * @return
     */
    List<FMedicalForm> queryFMedicalFormHealthList(@Param("followIds") List<Long> followIds);
}
