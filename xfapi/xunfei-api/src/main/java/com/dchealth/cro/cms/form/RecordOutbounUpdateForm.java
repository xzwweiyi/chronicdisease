package com.dchealth.cro.cms.form;

import com.dchealth.cro.cms.domain.PatientRecordOutbound;
import lombok.Data;

import java.util.List;


@Data
public class RecordOutbounUpdateForm {

	private List<PatientRecordOutbound> patientRecordOutboundList;


}
