package com.dchealth.web.job;

import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.service.impl.PatientInfoServiceImpl;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FollowPatientInfoListJob {


    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private PatientRecordMapper patientRecordMapper;


    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;

    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;


    public void followPatientInfoList() {

        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(new PatientInfo());

        patientInfos.forEach(item -> {
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setResearchId(item.getResearchId());
            patientRecord.setFillStatus("1");
            List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordList(patientRecord);

            // 找到对应的模板信息
            patientRecords.forEach(p -> {
                FMedicalForm fMedicalForm = new FMedicalForm();
                fMedicalForm.setFollowId(p.getFollowId());
                fMedicalForm.setParentId(0L);
                List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormList(fMedicalForm);

                fMedicalForms.forEach(t -> {
                    List<FMedicalForm> fMedicalForms1 = fMedicalFormMapper.selectFMedicalFormByFollowIdAndParentId(p.getFollowId(), t.getMedicalFormId());
                });

            });

        });



    }
}
