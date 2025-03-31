package com.dchealth.cro.cms.controller;

import com.dchealth.cro.cms.utils.FileUtils;
import com.dchealth.cro.cms.utils.TdApiHttpClientUtil;
import com.dchealth.cro.common.base.ApiBaseController;
import com.dchealth.cro.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/transController")
public class TransController extends ApiBaseController {
//
//    @Value("${tdapi.baseUrl}")
//    private String baseUrl;
//
//    /**
//     * 本地分包, 并上解
//     * @param file
//     * @param token
//     * @return
//     */
//    @RequestMapping(value = "/upload", method = RequestMethod.POST)
//    @ResponseBody
//    public String trans(MultipartFile file,String token) {
////        String path = "C:/tmp/upload_temp/pkg";
//        String path = "/usr/local/server/mirco-server/upload_temp/pkg";
//
//        if (StringUtils.isEmpty(token)) {
//            return buildErrorParamMView();
//        }
//        File file1 = new File(path);
//        if (!file1.exists()) {
//            file1.mkdirs();
//        }
//        try {
//            File file2 = new File(path+ "/" + file.getOriginalFilename());
//            file.transferTo(file2);
//            FileUtils fileUtils = new FileUtils();
//            List<String> split = fileUtils.split(file2, path, 1024*950);
//            for (String s : split) {
//                // 生产 baseUrl="https://td.dchealthclinical.com/tdapi/"
////            TdApiHttpClientUtil.getInstance().sendFileDefaultHttpPost("http://localhost:7918/tdapi/transfer/sms/publish",new HashMap<String, String>(){{
////                put("token", token);}},"file",new File(s));
//                TdApiHttpClientUtil.getInstance().sendFileDefaultHttpPost(baseUrl+"transfer/sms/publish",new HashMap<String, String>(){{
//                    put("token", token);}},"file",new File(s));
//            }
//
//        } catch (IOException e) {
//            // 优化返回
//            throw new RuntimeException(e);
//        }
//        return buildSuccessMView(file.getOriginalFilename());
//    }
}

