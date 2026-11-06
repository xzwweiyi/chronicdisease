package com.dchealth.research.domain.vo;

import com.dchealth.research.domain.ResearchOutbound;
import lombok.Data;

import java.util.List;

@Data
public class ResearchOutboundVo{

    List<ResearchOutbound> researchOutboundList;

    private Long userId;

    private String updateBy;
}
