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
     * 选中的患者列表（部分选择时传）
     */
    private List<MoveEnrollmentVo> selectedList;

    /**
     * 是否全选当前列表（true=全选，false=部分）
     */
    private Boolean selectAll = false;

    /**
     * 全选时的查询条件（和列表接口保持一致）
     */
    private PatientInfo queryParam;
}