package com.dchealth.template.domain.vo;

import lombok.Data;

import java.util.List;


@Data
public class FMedicalFormSortReq {
    private List<FMedicalFormSortVo> formSortVos;
}
