package com.dchealth.web.controller.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.dchealth.base.domain.FastFile;
import com.dchealth.base.service.IFastFileService;
import com.dchealth.common.config.DchealthConfig;
import com.dchealth.common.constant.Constants;
import com.dchealth.common.core.domain.AjaxResult;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.file.CalcMD5;
import com.dchealth.common.utils.file.FileUploadUtils;
import com.dchealth.common.utils.file.FileUtils;
import com.dchealth.common.utils.http.HttpClientUtil;
import com.dchealth.framework.config.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用请求处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private IFastFileService fastFileService;

    private static final String FILE_DELIMETER = ",";
    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = DchealthConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadFile(MultipartFile file) throws Exception {
        log.info("----------------------通用上传请求开始----------------------------");
        try {
            // 上传文件路径
            String filePath = null;
            if("prod".equals(profiles)){
                filePath = DchealthConfig.getProdFastPath();
            }else{
                filePath = DchealthConfig.getTestFastPath();
            }
            String hashString = CalcMD5.calcMD5(file);
            AjaxResult ajax = AjaxResult.success();
            //返回文件名
            ajax.put("fileName", file.getOriginalFilename());
            if (StringUtils.isNotBlankAndNotEmpty(hashString)) {
                FastFile  fastFile = fastFileService.selectFastFileByFileMd5(hashString);
                if (fastFile!= null &&  StringUtils.isNotBlankAndNotEmpty(fastFile.getFilePath())) {
                    ajax.put("fileMd5", hashString);
                    ajax.put("url", fastFile.getFilePath());
                    ajax.put("fastFileId",fastFile.getId());
                    return ajax;
                }

            }
            // 上传并返回新文件名称
            // 上传文件流处理
            String url = null;
            String httpUrl = filePath + "ngupload/nonCompressFile";
            String result = HttpClientUtil.getInstance().sendHttpPost(httpUrl, file);
            if (StringUtils.isNotEmpty(result) && StringUtils.isNotBlank(result)) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject != null) {
                    int code = jsonObject.getInteger("code");
                    if (code == 1000) {
                        url = jsonObject.getString("data");
                    }
                }
            }

            if(StringUtils.isNotBlankAndNotEmpty(url)) {
                FastFile fastFile = new FastFile();
                fastFile.setChannel("2");
                fastFile.setCreateDate(DateUtils.getNowDate());
                fastFile.setFileMd5(hashString);
                fastFile.setFilePath(url);
                FastFile f = new FastFile();
                f.setFileMd5(hashString);
                List<FastFile> list = fastFileService.selectFastFileList(f);
                if(list.size()==0){
                    fastFileService.insertFastFile(fastFile);
                    ajax.put("fastFileId",fastFile.getId());
                }else{
                    ajax.put("fastFileId",list.get(0).getId());
                }
                log.info("----------------------通用上传请求插入t_fast_file结束----------------------------");
            }
            ajax.put("fileMd5", hashString);
            ajax.put("url", url);
            return ajax;
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }



    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = DchealthConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = DchealthConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
