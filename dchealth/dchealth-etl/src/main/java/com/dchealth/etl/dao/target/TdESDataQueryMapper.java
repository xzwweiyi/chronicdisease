package com.dchealth.etl.dao.target;

import com.dchealth.etl.domain.TdHealthIndex;
import com.dchealth.etl.domain.target.TdDiagnosis;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zjl
 * @since 2023/4/26
 * ES 同步数据查询
 */
@Component
public interface TdESDataQueryMapper {
    /**
     * 诊断记录
     * @param params 查询条件
     * @return 对象列表
     */
    public Integer queryDiagnosisInfosCount(Map params);
    public List<TdHealthIndex> queryDiagnosisInfos(Map params);
    /**
     * 一般检查
     * @param params 查询条件
     * @return 对象列表
     */
    public Integer queryGeneralInspectionsCount(Map params);
    public List<TdHealthIndex> queryGeneralInspectionInfos(Map params);
    /**
     * 辅助检查
     * @param params 查询条件
     * @return 对象列表
     */
    public Integer queryExamInfoCount(Map params);
    public List<TdHealthIndex> queryExamInfoInfos(Map params);
    /**
     * 检验信息
     * @param params 查询条件
     * @return 对象列表
     */
    public Integer queryTestInfoCount(Map params);
    public List<TdHealthIndex> queryTestInfoInfos(Map params);
    /**
     * 医嘱信息
     * @param params 查询条件
     * @return 对象列表
     */
    public Integer queryOrdersInfoCount(Map params);
    public List<TdHealthIndex> queryOrdersInfos(Map params);
    /**
     * 药品处方信息
     * @param params 查询条件
     * @return 对象列表
     */
    public Integer queryDrugPrescInfoCount(Map params);
    public List<TdHealthIndex> queryDrugPrescInfos(Map params);
}
