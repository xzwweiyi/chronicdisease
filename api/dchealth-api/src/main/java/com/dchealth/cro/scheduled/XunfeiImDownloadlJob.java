package com.dchealth.cro.scheduled;

import com.dchealth.cro.common.utils.DateUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.medical.domain.PatientRecordOutbound;
import com.dchealth.cro.medical.mapper.PatientRecordOutboundMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @Auther: zdd
 * @Date: 2023/10/25
 */
@Component
public class XunfeiImDownloadlJob {

    private static Logger log = LoggerFactory.getLogger(XunfeiImDownloadlJob.class);

    @Autowired
    private PatientRecordOutboundMapper patientRecordOutboundMapper;

    @Autowired
    private CroCoreConfig croCoreConfig;
    @Value("${spring.profiles.active}")
    private String active;

    /**
     * -----------------------------------------废弃-----------------------------------------------------
     */
     //每小时一次
//     @Scheduled(cron = "0 */1 * * * ?")
     //启动延期5秒
//     @Scheduled(fixedDelay =5000)
     //每分钟执行一次
//     @Scheduled(cron = "0 0/1 * * * ?")
     //具体几点
//     @Scheduled(cron = "0 47 14 * * ?")
    public void downloadLocalSoundJobHandler() throws Exception {
        PatientRecordOutbound patientRecordOutbound = new PatientRecordOutbound();
        patientRecordOutbound.setIsDownloadSound("0");
        Date yesterday = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(yesterday);
        cal.add(Calendar.DATE,-1);
        patientRecordOutbound.setOutboundCreateTime(DateUtils.parseDateToStrShort(cal.getTime()));
        List<PatientRecordOutbound> list = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
        System.out.println(list.size()+"------------------");
//        if(list.size()>0){
//            for(PatientRecordOutbound p:list){
//                downloadWav(p);
//            }
//        }
        patientRecordOutbound.setOutboundCreateTime(DateUtils.dateNowTime());
        List<PatientRecordOutbound> list2 = patientRecordOutboundMapper.selectPatientRecordOutboundList(patientRecordOutbound);
         System.out.println(list2.size()+"====================");
//        if(list2.size()>0){
//            for(PatientRecordOutbound p:list2){
//                downloadWav(p);
//            }
//        }
        log.info("批量查询语音文件下载本地任务执行结束");
    }

    public void downloadWav(PatientRecordOutbound patientRecordOutbound) {
        String fileName = getUuidFileName("wav");
        try {
            String localUrl = uploadImFile(patientRecordOutbound.getXunfeiRecordUrl(),fileName);
            patientRecordOutbound.setIsDownloadSound("1");
            patientRecordOutbound.setLocalRecordUrl(localUrl);
            patientRecordOutboundMapper.updatePatientRecordOutbound(patientRecordOutbound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private String uploadImFile(String fileUrl,String fileName) throws IOException {
        InputStream is = null;
        try{
            URL url = new URL(fileUrl);
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            if (is == null){
                return "";
            }
            log.info("uploadSoundFile------------------"+fileName);
            String fastPath = null;
            if("prod".equals(active)){
                fastPath = croCoreConfig.getProdFastPath();
            }else {
                fastPath = croCoreConfig.getTestFastPath();
            }
            String httpUrl = fastPath+ "ngupload/single";
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
}
