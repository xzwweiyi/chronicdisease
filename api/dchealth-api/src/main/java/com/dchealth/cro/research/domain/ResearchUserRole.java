package com.dchealth.cro.research.domain;

import lombok.Data;

@Data
public class ResearchUserRole {
    private Long userId;//用户id
    private Long roleId;//角色id
    private Long researchId;//队列id
}
