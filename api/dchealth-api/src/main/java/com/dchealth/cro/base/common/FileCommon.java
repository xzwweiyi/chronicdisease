package com.dchealth.cro.base.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.cro.common.utils.StringUtils;
import com.dchealth.cro.common.utils.http.HttpClientUtil;
import com.dchealth.cro.core.config.CroCoreConfig;
import com.dchealth.cro.medical.form.PatientRecordFilesForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件通用
 */
@Component
public class FileCommon {

    private static final Logger L = LoggerFactory.getLogger(FileCommon.class);

    @Autowired
    private CroCoreConfig croCoreConfig;
    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 处理上传文件
     * @param request
     * @return
     * @throws IOException
     */
    public List<PatientRecordFilesForm> uploadFiles(List<PatientRecordFilesForm> recordFilesForms, MultipartHttpServletRequest request)
            throws IOException {
        if (recordFilesForms == null || recordFilesForms.isEmpty()){
            return null;
        }
        for (PatientRecordFilesForm r : recordFilesForms){
            String key = r.getPropertyId() == null ? r.getMedicalFormId().toString() : r.getPropertyId().toString();
            List<MultipartFile> imagesFiles = request.getFiles(key);
            // 上传文件流处理
            if (imagesFiles != null && imagesFiles.size() > 0) {
                String fastPath = null;
                if("prod".equals(active)){
                    fastPath = croCoreConfig.getProdFastPath();
                }else {
                    fastPath = croCoreConfig.getTestFastPath();
                }
                String httpUrl = fastPath + "ngupload/singleFiles";
                String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, imagesFiles);
                if (StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject != null) {
                        int code = jsonObject.getInteger("code");
                        if (code == 1000) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            r.setImageUrl(r.getImageUrl() == null ? new ArrayList<>() : r.getImageUrl());
                            for (Object url : jsonArray) {
                                r.getImageUrl().add(String.valueOf(url));
                            }
                        }
                    }
                }
            }
        }
/*
        for (PatientRecordFilesForm r : recordFilesForms){
            String key = r.getMedicalFormId() == null ? r.getPropertyId().toString() : r.getMedicalFormId().toString();
            List<MultipartFile> imagesFiles = request.getFiles(key);
            // 上传文件流处理
            if (imagesFiles != null && imagesFiles.size() > 0) {
                String httpUrl = croCoreConfig.getFastPath() + "ngupload/singleFiles";
                String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, imagesFiles);
                if (StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    if (jsonObject != null) {
                        int code = jsonObject.getInteger("code");
                        if (code == 1000) {
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            r.setImageUrl(r.getImageUrl() == null ? new ArrayList<>() : r.getImageUrl());
                            for (Object url : jsonArray) {
                                r.getImageUrl().add(String.valueOf(url));
                            }
                        }
                    }
                }
            }
        }
*/
        return recordFilesForms;
    }
    /**
     * 处理上传文件
     * @param request
     * @return
     * @throws IOException
     */
    public List<PatientRecordFilesForm> uploadExamFiles(List<PatientRecordFilesForm> recordFilesForms,
                                                        MultipartHttpServletRequest request) throws IOException {
        if (recordFilesForms == null || recordFilesForms.isEmpty()) {
            return null;
        }
        for (PatientRecordFilesForm r : recordFilesForms) {
            Long key = r.getId();
            if (Objects.isNull(key)) {
                L.error("key 为空:{}", r);
            } else {
                List<MultipartFile> imagesFiles = request.getFiles(key.toString());
                // 上传文件流处理
                if (imagesFiles != null && imagesFiles.size() > 0) {
                    String fastPath = null;
                    if("prod".equals(active)){
                        fastPath = croCoreConfig.getProdFastPath();
                    }else {
                        fastPath = croCoreConfig.getTestFastPath();
                    }
                    String httpUrl = fastPath + "ngupload/singleFiles";
                    String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, imagesFiles);
                    if (StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)) {
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject != null) {
                            int code = jsonObject.getInteger("code");
                            if (code == 1000) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                r.setImageUrl(r.getImageUrl() == null ? new ArrayList<>() : r.getImageUrl());
                                for (Object url : jsonArray) {
                                    r.getImageUrl().add(String.valueOf(url));
                                }
                            }
                        }
                    }
                }
            }
        }


        return recordFilesForms;
    }
}
