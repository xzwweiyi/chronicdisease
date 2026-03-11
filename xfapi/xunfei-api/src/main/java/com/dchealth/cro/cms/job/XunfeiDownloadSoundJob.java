package com.dchealth.cro.cms.job;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.dchealth.cro.cms.domain.PatientRecordOutbound;
import com.dchealth.cro.cms.domain.RecordOutboundHttpVO;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @Auther: zdd
 */
@Component
public class XunfeiDownloadSoundJob {

    private static Logger logger = LoggerFactory.getLogger(XunfeiDownloadSoundJob.class);

    @Value("${tdapi.baseUrl}")
    private String baseUrl ;


    @Value("${spring.servlet.multipart.location}")
    private String location;


    /**
     *  处理讯飞语音文件下载到api的服务器上  * * 23 * * ?
     */

    //启动延期5秒
    //每分钟执行一次
//     @Scheduled(cron = "0 0/1 * * * ?")
//     @Scheduled(cron = "0 50 17 * * ?")
    @Scheduled(cron = "* * 23 * * ?")
    public void xunfeiGetResultJobHandler() throws Exception {
        logger.info("讯飞api呼获取需要下载本地语音文件任务日期{}开始", DateUtils.getTime());
        Map<String, String> maps = new HashMap<String, String>();
//        maps.put("researchId", "2172");
        String httpResult = TdApiHttpClientUtil.getInstance().sendSzDefaultHttpPost(baseUrl+"transfer/recordOutbound/getDounloadList", maps);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(httpResult);
        String code = jsonObject != null ? jsonObject.getString("code") : null;
         SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        if(httpResult!=null && "1000".equals(code)){
            RecordOutboundHttpVO recordOutboundHttpVO = JSONObject.parseObject(jsonObject.getString("data"),RecordOutboundHttpVO.class);
            List<PatientRecordOutbound> list = recordOutboundHttpVO.getRecordOutboundList();

            if(list.size()>0){
                String dict = location+"/"+sdf.format(new Date());
                for(PatientRecordOutbound p:list){
                    String fileName = dict +"/"+ getUuidFileName("wav");

                    File file0 =new File(dict);
                    if (!file0.exists()) {
                        file0.mkdirs();
                    }
                    if(p.getXunfeiRecordUrl()!=null){
                        try {
                            URL url = new URL(p.getXunfeiRecordUrl());
                            URLConnection con = url.openConnection();
                            InputStream is = con.getInputStream();

                            FileOutputStream outputStream = new FileOutputStream(fileName);
                            int bytesRead;
                            byte[] buffer = new byte[4096];
                            while ((bytesRead = is.read(buffer))!=-1){
                                outputStream.write(buffer,0,bytesRead);
                            }
                            outputStream.close();
                            is.close();
                            Map<String, String> mapOutbound = new HashMap<String, String>();
                            mapOutbound.put("id", String.valueOf(p.getId()));
                            File file = new File(fileName);
                            String uploadResult = TdApiHttpClientUtil.getInstance().sendFileDefaultHttpPost(baseUrl+
                                    "transfer/recordOutbound/uploadToLocalWavVo", mapOutbound,"file",file);
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                File sourceFile = new File(dict);
                delFile(sourceFile);

            }
        }else{
            logger.info("讯飞api呼获取需要下载本地语音文件任务失败");
        }
         logger.info("讯飞api呼获取需要下载本地语音文件任务结束");
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

    /**
     * 删除文件
     */
    public void delFile(File file) {
        File[] files = file.listFiles();//将file子目录及子文件放进文件数组
        if (files!=null){//如果包含文件进行删除操作
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()){//删除子文件
                    files[i].delete();
                }else if(files[i].isDirectory()){//通过递归方法删除子目录的文件
                    delFile(files[i]);
                }
                files[i].delete();//删除子目录
            }
        }
        file.delete();
    }


}
