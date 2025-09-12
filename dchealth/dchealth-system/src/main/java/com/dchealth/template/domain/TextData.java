package com.dchealth.template.domain;

import lombok.Data;

@Data
public class TextData {
    private String propertyCode;
    private Long propertyId;
    private String propertyValue;
    private Long formId;
}
