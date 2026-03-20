package com.dchealth.template.mapper;

import java.util.List;
import java.util.Map;

import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.domain.vo.FMedicalValueVo;
import org.apache.ibatis.annotations.Param;

/**
 * 属性Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-08
 */
public interface FMedicalPropertyMapper
{
    /**
     * 查询属性
     *
     * @param id 属性主键
     * @return 属性
     */
    public FMedicalProperty selectFMedicalPropertyById(Long id);

    /**
     * 查询属性列表
     *
     * @param fMedicalProperty 属性
     * @return 属性集合
     */
    public List<FMedicalProperty> selectFMedicalPropertyList(FMedicalProperty fMedicalProperty);


    public List<FMedicalProperty> selectPropertyNumsList(FMedicalProperty fMedicalProperty);

    /**
     * 随访节点id查询
     * @param followId
     * @return
     */
    public List<FMedicalProperty> selectFMedicalPropertyByFollowId(Long followId);

    /**
     * 表单id查询
     * @param medicalFormId
     * @return
     */
    public List<FMedicalProperty> selectFMedicalPropertyByMedicalFormId(Long medicalFormId);

    /**
     * 新增属性
     *
     * @param fMedicalProperty 属性
     * @return 结果
     */
    public int insertFMedicalProperty(FMedicalProperty fMedicalProperty);

    /**
     * 批量新增
     * @param list
     * @return
     */
    public int batchInsertFMedicalProperty(List<FMedicalProperty> list);

    /**
     * 修改属性
     *
     * @param fMedicalProperty 属性
     * @return 结果
     */
    public int updateFMedicalProperty(FMedicalProperty fMedicalProperty);

    /**
     * 删除属性
     *
     * @param id 属性主键
     * @return 结果
     */
    public int deleteFMedicalPropertyById(Long id);

    public int deleteFMedicalPropertyByMedicalFormId(Long medicalFormId);

    /**
     * 根据followId删除
     * @param followId
     * @return
     */
    public int deleteFMedicalPropertyByMedicalFollowId(Long followId);

    /**
     * 批量删除属性
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFMedicalPropertyByIds(Long[] ids);

    public int deleteFMedicalPropertyByMedicalFormIds(List<Long> fromIds);

    /**
     * 根据模板ids查询属性信息
     * @return
     */
    List<FMedicalProperty> selectFMedicalPropertyByMedicalFormIds(List<Long> fromIds);

    List<FMedicalValueVo> selectFMedicalValues(@Param("sql") String sql);

    List<FMedicalProperty> selectFMedicalPropertyByArticleId(Long articleId);

    void runInsertSql(@Param("sql") String sql);

    Map runSelectSql(@Param("selectSql")String selectSql);

    void runDelSql(@Param("delSql")String delSql);

    void runInsertLogSql(@Param("sqlLog")String sqlLog);

    List<FMedicalProperty> selectFMedicalProperty(@Param("followId") Long followId,@Param("medicalFormId") Long medicalFormId);

}
