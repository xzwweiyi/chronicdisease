package com.dchealth.question.service.impl;

import com.dchealth.common.utils.DateUtils;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordEdcStatus;
import com.dchealth.medical.mapper.PatientRecordEdcStatusMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.question.domain.QuestionQuick;
import com.dchealth.question.domain.QuestionRecord;
import com.dchealth.question.domain.QuestionRecordForm;
import com.dchealth.question.domain.form.QuestionForm;
import com.dchealth.question.domain.form.QuestionQuickForm;
import com.dchealth.question.domain.vo.QuestionListVO;
import com.dchealth.question.domain.vo.QuestionQuickListVO;
import com.dchealth.question.mapper.QuestionQuickMapper;
import com.dchealth.question.mapper.QuestionRecordFormMapper;
import com.dchealth.question.mapper.QuestionRecordMapper;
import com.dchealth.question.service.IQuestionService;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.mapper.FMedicalFormMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QuestionService implements IQuestionService {

    @Autowired
    private QuestionQuickMapper questionQuickMapper;

    @Autowired
    private QuestionRecordMapper questionRecordMapper;

    @Autowired
    private QuestionRecordFormMapper questionRecordFormMapper;

    @Autowired
    private PatientRecordMapper patientRecordMapper;


    @Autowired
    private PatientRecordEdcStatusMapper patientRecordEdcStatusMapper;
    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;

    @Override
    public List<QuestionQuickListVO> quickList(Long userId) {
        return Optional.ofNullable(questionQuickMapper.selectQuestionQuickListByUserId(userId)).map(x->
                x.stream().map(y->new QuestionQuickListVO(y)).collect(Collectors.toList())).orElseGet(()->new ArrayList<QuestionQuickListVO>());
    }

    @Override
    public int quickSave(QuestionQuickForm questionQuickForm) {
        QuestionQuick questionQuick = new QuestionQuick();
        questionQuick.setCreateUserId(questionQuickForm.getUserId());
        questionQuick.setQuickContent(questionQuickForm.getQuickContent());
        questionQuick.setDeptId(questionQuickForm.getDeptId());
        return questionQuickMapper.insertQuestionQuick(questionQuick);
    }

    @Override
    public int quickDelete(Long quickId) {
        return questionQuickMapper.deleteQuestionQuickByQuestionQuickId(quickId);
    }

    //随访质疑
    @Override
    public List<QuestionListVO> questionList(Long recordId) {
        return Optional.ofNullable(questionRecordMapper.selectQuestionRecordByRecordId(recordId))
                .map(m->m.stream().map(QuestionListVO :: new).collect(Collectors.toList()))
                .orElseGet(()->new ArrayList<QuestionListVO>());
    }

    @Override
    public int sendQuestion(QuestionForm questionForm) {
        PatientRecord patientRecord = new PatientRecord();
        patientRecord.setRecordId(questionForm.getRecordId());
        patientRecord.setFollowStatus("2");
        patientRecord.setQuestionStatus("1");
        return questionRecordMapper.insertQuestionRecord(new QuestionRecord(questionForm));
    }

    @Override
    public int saveQuestionRecord(QuestionRecord questionRecord) {
        //新发起的质疑建立关系  回复不需要
        PatientRecordEdcStatus pres = patientRecordEdcStatusMapper.selectByTwoId(questionRecord.getRecordId(),questionRecord.getMedicalFormId());
        if (questionRecord.getQuestionId() != null){
            QuestionRecord qr = questionRecordMapper.selectQuestionRecordByQuestionId(questionRecord.getQuestionId());
            if(qr != null){
                qr.setReplyUserId(questionRecord.getReplyUserId());
                qr.setReplyContent(questionRecord.getReplyContent());
                qr.setReplyTime(DateUtils.getNowDate());
                questionRecordMapper.updateQuestionRecord(qr);
                //质疑回复状态

                if(pres==null){
                    PatientRecordEdcStatus patientRecordEdcStatus = new PatientRecordEdcStatus(questionRecord.getRecordId(),questionRecord.getMedicalFormId(),"2");
                    patientRecordEdcStatus.setCreateTime(DateUtils.getNowDate());
                    patientRecordEdcStatusMapper.insertPatientRecordEdcStatus(patientRecordEdcStatus);
                }else{
                    pres.setEdcStatus("2");
                    patientRecordEdcStatusMapper.updatePatientRecordEdcStatus(pres);
                }
                FMedicalForm fMedicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(questionRecord.getMedicalFormId());
                Integer edcStatus = fMedicalFormMapper.selectMinStatusByRecordIdAndMedicalFormId(questionRecord.getRecordId(),fMedicalForm.getFollowId());
                patientRecordMapper.updateEdcStatus(questionRecord.getRecordId(), String.valueOf(edcStatus));
            }
        }else {
            questionRecord.setQuestionStatus("1");
            questionRecord.setFromTime(DateUtils.getNowDate());
            // edc 质疑类型
            questionRecord.setType("2");
            int i = questionRecordMapper.insertQuestionRecord(questionRecord);
            QuestionRecordForm  questionRecordForm = new QuestionRecordForm();
            questionRecordForm.setQuestionId(questionRecord.getQuestionId());
            questionRecordForm.setMedicalFormId(questionRecord.getMedicalFormId());
            questionRecordForm.setRecordId(questionRecord.getRecordId());
            questionRecordFormMapper.insertQuestionRecordForm(questionRecordForm);
            //发起质疑后进入【质疑待回复】状态   1=待提交 2=待质检 3=质疑待回复 4=锁库
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setRecordId(questionRecord.getRecordId());
            patientRecord.setEdcStatus("3");
            patientRecord.setQuestionStatus("1");
            patientRecordMapper.updatePatientRecord(patientRecord);

            if(pres==null){
                PatientRecordEdcStatus patientRecordEdcStatus = new PatientRecordEdcStatus(questionRecord.getRecordId(),questionRecord.getMedicalFormId(),"3");
                patientRecordEdcStatus.setCreateTime(DateUtils.getNowDate());
                patientRecordEdcStatusMapper.insertPatientRecordEdcStatus(patientRecordEdcStatus);
            }else{
                pres.setEdcStatus("3");
                patientRecordEdcStatusMapper.updatePatientRecordEdcStatus(pres);
            }
        }
        return 1;
    }

    @Override
    public List<QuestionRecord> listQuestionRecord(QuestionRecord questionRecord) {
        Long recordId = questionRecord.getRecordId();
        Long medicalFormId = questionRecord.getMedicalFormId();
        List<QuestionRecord> questionRecords = questionRecordMapper.selectQuestionRecords(recordId,medicalFormId);

        if(questionRecords.size()>0){
            QuestionRecord qr = new QuestionRecord();
            qr.setType("2");
            qr.setRecordId(recordId);
            qr.setMedicalFormId(medicalFormId);
            qr.setEdcStatus("3");
            List<QuestionRecord> statusList = questionRecordMapper.selectEdcStatisList(qr);
            if(statusList.size()>0){
                Map<Long,QuestionRecord> questionRecordMap = statusList.stream().collect(Collectors.toMap(QuestionRecord::getQuestionId, Function.identity(), (key1, key2) -> key2));
                for(QuestionRecord q:questionRecords){
                    if(questionRecordMap.containsKey(q.getQuestionId())){
                        q.setCanReplay(true);
                    }
                }
            }
        }
        return questionRecords;
    }

    @Override
    public int editPatientRecordEdcStatus(Long recordId,Long medicalFormId) {
        if(medicalFormId==null){
            PatientRecord patientRecord = new PatientRecord();
            patientRecord.setRecordId(recordId);
            patientRecord.setEdcStatus("4");
            patientRecordMapper.updatePatientRecord(patientRecord);
        }else{
            PatientRecordEdcStatus pres = patientRecordEdcStatusMapper.selectByTwoId(recordId,medicalFormId);
            if(pres==null){
                PatientRecordEdcStatus patientRecordEdcStatus = new PatientRecordEdcStatus(recordId,medicalFormId,"4");
                patientRecordEdcStatus.setCreateTime(DateUtils.getNowDate());
                patientRecordEdcStatusMapper.insertPatientRecordEdcStatus(patientRecordEdcStatus);
            }else{
                pres.setEdcStatus("4");
                patientRecordEdcStatusMapper.updatePatientRecordEdcStatus(pres);
            }
            FMedicalForm fMedicalForm = fMedicalFormMapper.selectFMedicalFormByMedicalFormId(medicalFormId);
            Integer edcStatus = fMedicalFormMapper.selectMinStatusByRecordIdAndMedicalFormId(recordId,fMedicalForm.getFollowId());
            patientRecordMapper.updateEdcStatus(recordId, String.valueOf(edcStatus));
        }
        return 1;
    }


}
