package com.dchealth.cro.transfer.service;

import com.dchealth.cro.common.base.DchealthCloudResponseEntity;
import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.medical.domain.PatientRecordOutbound;
import com.dchealth.cro.medical.mapper.PatientRecordOutboundMapper;
import com.dchealth.cro.research.domain.ResearchOutbound;
import com.dchealth.cro.research.mapper.ResearchOutboundMapper;
import com.dchealth.cro.transfer.vo.*;
import lombok.Cleanup;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class TransferRecordOutbounService {
    private static final Logger L = LoggerFactory.getLogger(TransferRecordOutbounService.class);

    @Autowired
    private ResearchOutboundMapper researchOutboundMapper;
    @Autowired
    private PatientRecordOutboundMapper patientRecordOutboundMapper;

    @Autowired
    private CroCoreConfig croCoreConfig;


    public DchealthCloudResponseEntity<RecordOutboundVO> getRecordOutboundVO(RecordOutbounForm recordOutbounForm) {
        DchealthCloudResponseEntity<RecordOutboundVO> result = new DchealthCloudResponseEntity<RecordOutboundVO>();
        RecordOutboundVO recordOutboundVO = new RecordOutboundVO();
        List<RecordOutboundHttpVO> resultList = new ArrayList<>();

        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setResearchId(recordOutbounForm.getResearchId());
        patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
        patientRecordOutbound.setStatus("1");
        List<PatientRecordOutbound> recordOutboundList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        if(recordOutboundList.size()>0){
            Map<Long,List<PatientRecordOutbound>> recordOutboundMap = recordOutboundList.stream().
                    filter(x -> x.getOutboundId()!=null).collect(Collectors.groupingBy(PatientRecordOutbound::getOutboundId));
            for(Long outboundId:recordOutboundMap.keySet()){
                List<PatientRecordOutbound> outboundList = recordOutboundMap.get(outboundId);
                ResearchOutbound researchOutbound = researchOutboundMapper.selectResearchOutboundById(outboundId);
                RecordOutboundHttpVO recordOutboundHttpVO = new RecordOutboundHttpVO();
                recordOutboundHttpVO.setRecordOutboundList(outboundList);
                recordOutboundHttpVO.setResearchOutbound(researchOutbound);
                resultList.add(recordOutboundHttpVO);
            }
        }
        recordOutboundVO.setRecordOutboundHttpVO(resultList);
        result.setData(recordOutboundVO);
        return result;
    }

    public DchealthCloudResponseEntity<Boolean> recordOutboundUpdate(RecordOutbounUpdateForm recordOutbounUpdateForm) {
        DchealthCloudResponseEntity<Boolean> result = new DchealthCloudResponseEntity<Boolean>();
        List<PatientRecordOutbound> patientRecordOutboundList = recordOutbounUpdateForm.getPatientRecordOutboundList();
        for(PatientRecordOutbound p:patientRecordOutboundList){
            patientRecordOutboundMapper.updateBatch(p);
        }
        result.setData(true);
        return result;
    }


    public DchealthCloudResponseEntity<RecordOutboundHttpVO> getResultOutboundList(RecordOutbounForm recordOutbounForm) {
        DchealthCloudResponseEntity<RecordOutboundHttpVO> result = new DchealthCloudResponseEntity<RecordOutboundHttpVO>();
        RecordOutboundHttpVO recordOutboundHttpVO = new RecordOutboundHttpVO();
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setResearchId(recordOutbounForm.getResearchId());
        patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
        patientRecordOutbound.setStatus("2");
        List<PatientRecordOutbound> recordOutboundList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        recordOutboundHttpVO.setRecordOutboundList(recordOutboundList);
        result.setData(recordOutboundHttpVO);
        return result;
    }


    public DchealthCloudResponseEntity<RecordOutboundHttpVO> getSendSmsList(RecordOutbounForm recordOutbounForm) {
        DchealthCloudResponseEntity<RecordOutboundHttpVO> result = new DchealthCloudResponseEntity<RecordOutboundHttpVO>();
        RecordOutboundHttpVO recordOutboundHttpVO = new RecordOutboundHttpVO();
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setResearchId(recordOutbounForm.getResearchId());
        patientRecordOutbound.setOutboundType("2");//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
        patientRecordOutbound.setStatus("1");
        List<PatientRecordOutbound> recordOutboundList = patientRecordOutboundMapper.selectSmsOutboundList(patientRecordOutbound);
        recordOutboundHttpVO.setRecordOutboundList(recordOutboundList);
        result.setData(recordOutboundHttpVO);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(DateUtils.dateNowTime());
    }

    public DchealthCloudResponseEntity<RecordOutboundHttpVO> getHistoryDounloadList(RecordOutbounForm recordOutbounForm) {
        DchealthCloudResponseEntity<RecordOutboundHttpVO> result = new DchealthCloudResponseEntity<RecordOutboundHttpVO>();
        RecordOutboundHttpVO recordOutboundHttpVO = new RecordOutboundHttpVO();
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setResearchId(recordOutbounForm.getResearchId());
        patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setIsDownloadSound("0");
        patientRecordOutbound.setIsDownloadSoundCode("0");
        List<PatientRecordOutbound> recordOutboundList = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        recordOutboundHttpVO.setRecordOutboundList(recordOutboundList);
        result.setData(recordOutboundHttpVO);
        return result;
    }
    public DchealthCloudResponseEntity<RecordOutboundHttpVO> getDounloadList(RecordOutbounForm recordOutbounForm) {
        DchealthCloudResponseEntity<RecordOutboundHttpVO> result = new DchealthCloudResponseEntity<RecordOutboundHttpVO>();
        RecordOutboundHttpVO recordOutboundHttpVO = new RecordOutboundHttpVO();
        List<PatientRecordOutbound> recordOutboundList = new ArrayList<>();

        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setResearchId(recordOutbounForm.getResearchId());
        patientRecordOutbound.setOutboundType("1");//外呼类型0无状态1智能外呼2短信提醒3app站内信
        patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
        patientRecordOutbound.setIsDownloadSound("0");
        patientRecordOutbound.setIsDownloadSoundCode("0");
        List<PatientRecordOutbound> list1 = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        if(list1.size()>0){
            recordOutboundList.addAll(list1);
        }

        Date yesterday = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(yesterday);
        cal.add(Calendar.DATE,-1);
        patientRecordOutbound.setOutboundCreateTime(DateUtils.parseDateToStrShort(cal.getTime()));
        List<PatientRecordOutbound> list2 = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        if(list2.size()>0){
            recordOutboundList.addAll(list2);
        }


        recordOutboundHttpVO.setRecordOutboundList(recordOutboundList);
        result.setData(recordOutboundHttpVO);
        return result;
    }

    public DchealthCloudResponseEntity<Boolean> uploadToLocalWavVo(UploadToLocalWavVo uploadToLocalWavVo, MultipartFile file) {
        DchealthCloudResponseEntity<Boolean> result = new DchealthCloudResponseEntity<Boolean>();
        String fileName = getUuidFileName("wav");
        String localUrl = null;
        try {
            @Cleanup
            InputStream is = file.getInputStream();
            localUrl = uploadImFile(is,fileName);
            PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
            patientRecordOutbound.setId(uploadToLocalWavVo.getId());
            patientRecordOutbound.setIsDownloadSound("1");
            patientRecordOutbound.setLocalRecordUrl(localUrl);
            patientRecordOutboundMapper.updatePatientRecordOutbound(patientRecordOutbound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        result.setData(true);
        return result;
    }

    private String uploadImFile(InputStream is,String fileName) throws IOException {
        try{
            String httpUrl = croCoreConfig.getFastPath() + "ngupload/single";
            String filesResult = HttpClientUtil.getInstance().sendHttpPostWav(httpUrl, is,fileName);
            if (StringUtils.isNotEmpty(filesResult)) {
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSON.parseObject(filesResult);
                if (jsonObject != null) {
                    int  code = jsonObject.getInteger("code");
                    if(code ==1000) {
                        return jsonObject.getString("data");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (is != null){
                is.close();
            }
        }
        return "";
    }


    public static String getUuidFileName(String imageFormat){
        String fileName = "";
        if (!StringUtils.isNotEmpty(fileName)){
            String uuid = UUID.randomUUID().toString();
            fileName = uuid.replace("-", "");
        }
        fileName += "."+ imageFormat;
        return fileName;
    }


}
