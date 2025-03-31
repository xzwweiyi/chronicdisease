package com.dchealth.cro.cms.domain;

import lombok.Data;

import java.util.List;

@Data
public class RecordOutboundHttpVO {

  private List<PatientRecordOutbound> recordOutboundList;

  private ResearchOutbound researchOutbound;

}
