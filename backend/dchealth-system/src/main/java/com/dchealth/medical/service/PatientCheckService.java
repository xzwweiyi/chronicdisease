package com.dchealth.medical.service;

import com.alibaba.fastjson2.JSONObject;
import com.dchealth.medical.dto.PatientCheckItemDto;
import com.dchealth.medical.dto.PersonCheckItemDto;
import com.dchealth.medical.dto.PatientCheckRecordDto;
import com.dchealth.medical.dto.PersonCheckItemDto;

import java.util.List;

/**
 * @Author lzx
 * @Date 2023/4/7
 * @Description:
 */
public interface PatientCheckService {
    /**
     * 查询体检报告检验项目列表
     *
     * @param dto
     * @return
     */
    List<JSONObject> queryCheckTestItemList(PatientCheckRecordDto dto);

    /**
     * 查询体检报告检查项目列表
     *
     * @param dto
     * @return
     */
    List<JSONObject> checkExamItemList(PatientCheckRecordDto dto);

    /**
     * 查询体检报告检验项目详情
     *
     * @param dto
     * @return
     */
    List<JSONObject> queryCheckTestItems(PatientCheckItemDto dto);

    /**
     * 查询体检报告检查项目详情
     *
     * @param dto
     * @return
     */
    Object queryCheckExamItems(PersonCheckItemDto dto);
}
