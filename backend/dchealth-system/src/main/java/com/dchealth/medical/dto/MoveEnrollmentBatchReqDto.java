package com.dchealth.medical.dto;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.vo.MoveEnrollmentVo;
import lombok.Data;

import java.util.List;

@Data
/**
 * 批量移动入组请求
 */
public class MoveEnrollmentBatchReqDto {

    /**
     * 选中的患者列表
     */
    private List<MoveEnrollmentVo> selectedList;
}