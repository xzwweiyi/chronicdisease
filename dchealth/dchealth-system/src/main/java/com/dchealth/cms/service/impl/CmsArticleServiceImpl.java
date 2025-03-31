package com.dchealth.cms.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dchealth.cms.domain.CmsArticleData;
import com.dchealth.cms.domain.CmsArticleMoveParam;
import com.dchealth.cms.domain.CmsArticleRecord;
import com.dchealth.cms.domain.CmsCategory;
import com.dchealth.cms.domain.CmsDeptArticle;
import com.dchealth.cms.domain.CmsDeptArticleMoveDo;
import com.dchealth.cms.domain.FMedicalExamRes;
import com.dchealth.cms.domain.ResearchInfoArticlePatient;
import com.dchealth.cms.domain.vo.CmsArticleCateGoryVo;
import com.dchealth.cms.mapper.CmsArticleDataMapper;
import com.dchealth.cms.mapper.CmsCategoryMapper;
import com.dchealth.cms.mapper.CmsDeptArticleMapper;
import com.dchealth.cms.service.ICmsCategoryService;
import com.dchealth.common.DynamicSql;
import com.dchealth.common.exception.ServiceException;
import com.dchealth.common.utils.DateUtils;
import com.dchealth.common.utils.uuid.UUID;
import com.dchealth.dynamic.mapper.DynamicCommonMapper;
import com.dchealth.dynamic.templete.DynamicArticle;
import com.dchealth.dynamic.templete.DynamicArticleLog;
import com.dchealth.medical.contants.MedicalContants;
import com.dchealth.medical.domain.PatientExamRecord;
import com.dchealth.medical.domain.PatientRecordFiles;
import com.dchealth.medical.domain.form.DynamicCommonProperty;
import com.dchealth.medical.domain.form.DynamicForm;
import com.dchealth.medical.mapper.PatientExamRecordMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.research.domain.ResearchInfoArticle;
import com.dchealth.research.domain.constant.PropertyType;
import com.dchealth.research.mapper.ResearchInfoArticleMapper;
import com.dchealth.system.mapper.SysRoleDeptMapper;
import com.dchealth.template.domain.FMedicalExamForm;
import com.dchealth.template.domain.FMedicalExamItem;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.mapper.FMedicalExamItemMapper;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.dchealth.cms.mapper.CmsArticleMapper;
import com.dchealth.cms.domain.CmsArticle;
import com.dchealth.cms.service.ICmsArticleService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * 资讯管理Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-03-16
 */
@Service
@Slf4j
public class CmsArticleServiceImpl implements ICmsArticleService
{

    private static final Logger L = LoggerFactory.getLogger(CmsArticleServiceImpl.class);

    @Autowired
    private CmsArticleMapper cmsArticleMapper;
    @Autowired
    private DynamicArticle dynamicArticle;
    @Autowired
    private DynamicArticleLog dynamicArticleLog;
    @Autowired
    private CmsArticleDataMapper cmsArticleDataMapper;
    @Autowired
    private SysRoleDeptMapper sysRoleDeptMapper;

    @Autowired
    private CmsDeptArticleMapper cmsDeptArticleMapper;

    @Autowired
    private CmsCategoryMapper cmsCategoryMapper;

    @Autowired
    private ResearchInfoArticleMapper researchInfoArticleMapper;

    @Autowired
    private PatientExamRecordMapper patientExamRecordMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;

    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;

    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;

    @Autowired
    private DynamicCommonMapper dynamicCommonMapper;


    @Autowired
    private DynamicSql dynamicSql;

    @Autowired
    private ICmsCategoryService cmsCategoryService;

    /**
     * 查询资讯管理
     * 
     * @param id 资讯管理主键
     * @return 资讯管理
     */
    @Override
    public CmsArticle selectCmsArticleById(Long id)
    {
        return cmsArticleMapper.selectCmsArticleById(id);
    }

    /**
     * 查询资讯管理列表
     * 
     * @param cmsArticle 资讯管理
     * @return 资讯管理
     */
    @Override
    public List<CmsArticle> selectCmsArticleList(CmsArticle cmsArticle)
    {
        return cmsArticleMapper.selectCmsArticleList(cmsArticle);
    }

    @Override
    public void edcExportData(HttpServletResponse response, List<Long> examRecordIds) {
        Workbook workbook = new SXSSFWorkbook(1000);
        ServletOutputStream sos = null;
        ZipOutputStream zos = null;
        try {
            log.info("导出功能{}", JSON.toJSONString(examRecordIds));
            // 创建sheet，填充数据
            Map<String, List<String>> zip = handleData(workbook,examRecordIds);
            // response处理
//            buildFileResponse(response);
//            workbook.write(response.getOutputStream()); // todo 替换成zip包导出

            // 从response获取输出流
             sos = response.getOutputStream();
//            // Zip输出流
             zos = new ZipOutputStream(sos);


            ZipEntry zipEntry = new ZipEntry("问卷" + System.currentTimeMillis() + ".xlsx");
//
//                // 把这个zip实体添加到压缩流中
            zos.putNextEntry(zipEntry);
            workbook.write(zos);
            zos.closeEntry();

            for (String s : zip.keySet()) {
                for (String string : zip.get(s)) {

                // 从 URL 下载图片
                    InputStream is = null;
                    try {
                        log.info("图片url={}",string);
                        String imageUrl = string.replace("https://td.dchealthclinical.com", "http://172.100.45.33")
                                .replace("https:","http:")
                                .replace("td.dchealthclinical.com","172.100.45.33");
                        log.info("图片转换后url={}",imageUrl);
                        is = new URL(imageUrl).openStream();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] buffer = new byte[1024];
                    int length;

                    // 创建 ZipEntry 对象
                    ZipEntry zipEntry2 = new ZipEntry(s+"/"+ UUID.fastUUID()+string.substring(string.lastIndexOf(".")));
                    zos.putNextEntry(zipEntry2);
                    // 将图片数据写入 ZIP 输出流
                    while ((length = is.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    // 关闭当前 ZipEntry
                    zos.closeEntry();
                }
            }


            zos.flush();
//
//            String filePath = "large_excel_file.xlsx";
//            FileOutputStream fileOut = new FileOutputStream(new File(filePath));
//            workbook.write(fileOut);
//            File file = new File(filePath);
//            if (file.exists()) {
//                System.out.println("file = " + file);
//            }
//
////            return downloadFiles(response,file);
//            byte[] bytes = downloadCode();
//            buildZipFileResponse(response,bytes);
        } catch (Exception e) {
            log.error("Download error:{}", e);
            throw new ServiceException("Download template fail");
        } finally {
            IOUtils.closeQuietly(workbook);
            try {
                assert zos != null;
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                sos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public byte[] downloadCode()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
//        for (String tableName : tableNames)
//        {
//            generatorCode(tableName, zip);
//        }
        org.apache.commons.io.IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    public ResponseEntity<byte[]> downloadFiles(HttpServletResponse response, File file) throws IOException {
        // 创建一个临时文件来存储 ZIP 文件
        File tempZipFile = File.createTempFile("temp", ".zip");

        try (FileOutputStream fos = new FileOutputStream(tempZipFile);
             ZipArchiveOutputStream zipOut = new ZipArchiveOutputStream(fos)) {

            // 添加 Excel 文件
//            addFileToZip(zipOut, "classpath:example.xlsx", "example.xlsx");
//
//            // 添加图片文件夹
//            addDirectoryToZip(zipOut, "classpath:images", "images");
//            ClassPathResource resource = new ClassPathResource(resourcePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            try (InputStream is = fileInputStream) {
                ZipArchiveEntry zipEntry = new ZipArchiveEntry(file.getName());
                zipOut.putArchiveEntry(zipEntry);
                org.apache.commons.compress.utils.IOUtils.copy(is, zipOut);
                zipOut.closeArchiveEntry();
            }

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=download.zip");

            // 返回 ZIP 文件
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(org.apache.commons.compress.utils.IOUtils.toByteArray(new FileInputStream(tempZipFile)));
        } finally {
            // 删除临时文件
            tempZipFile.delete();
        }
    }

    private void addFileToZip(ZipArchiveOutputStream zipOut, String resourcePath, String zipEntryName) throws IOException {
        ClassPathResource resource = new ClassPathResource(resourcePath);
        try (InputStream is = resource.getInputStream()) {
            ZipArchiveEntry zipEntry = new ZipArchiveEntry(zipEntryName);
            zipOut.putArchiveEntry(zipEntry);
            org.apache.commons.compress.utils.IOUtils.copy(is, zipOut);
            zipOut.closeArchiveEntry();
        }
    }

    private void addDirectoryToZip(ZipArchiveOutputStream zipOut, String resourcePath, String zipEntryPrefix) throws IOException {
        ClassPathResource resourceDir = new ClassPathResource(resourcePath);
        File dir = resourceDir.getFile();
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    String zipEntryName = zipEntryPrefix + "/" + file.getName();
                    try (InputStream is = new FileInputStream(file)) {
                        ZipArchiveEntry zipEntry = new ZipArchiveEntry(zipEntryName);
                        zipOut.putArchiveEntry(zipEntry);
                        org.apache.commons.compress.utils.IOUtils.copy(is, zipOut);
                        zipOut.closeArchiveEntry();
                    }
                }
            }
        }
    }

    private Map<String, List<String>> handleData(Workbook workbook, List<Long> examRecordIds) {
        // 生产execl todo
        Map<String, List<String>> stringListMap = makeExeclByActicle(workbook, examRecordIds);
        // 生成图片 todo
//        Map<String, List<String>> pictureByActicles = getPictureByActicles(examRecordIds);
        // 压缩图片和execl todo

        return stringListMap;
    }

    private Map<String,List<String>/*urls*/> getPictureByActicles(List<Long> examRecordIds) {
        List<CmsArticleRecord> cmsArticleRecords = new ArrayList<>();
        if (CollectionUtils.isEmpty(examRecordIds)) {
            CmsArticleRecord cmsArticleRecord = new CmsArticleRecord();
            cmsArticleRecords = cmsArticleMapper.selectCmsArticleRecord(cmsArticleRecord);
        }else {
            for (Long examRecordId : examRecordIds) {
                CmsArticleRecord query = new CmsArticleRecord();
                query.setExamRecordId(examRecordId);
                List<CmsArticleRecord> records = cmsArticleMapper.selectCmsArticleRecord(query);
                cmsArticleRecords.addAll(records);
            }
        }
        if (CollectionUtils.isEmpty(cmsArticleRecords)) {
            return new HashMap<>();
        }

        Map<String, List<CmsArticleRecord>> groupbyPersonIdMAP = cmsArticleRecords.stream()
                .filter(item->Objects.nonNull(item.getPersonId()))
                .collect(Collectors.groupingBy(CmsArticleRecord::getPersonId));

        List<FMedicalExamForm> fMedicalExamForms = new ArrayList<>();
        for (CmsArticleRecord articleRecord : cmsArticleRecords) {
            FMedicalExamRes fMedicalExamRes = new FMedicalExamRes();
            fMedicalExamRes.setArticleId(articleRecord.getArticleId());
            fMedicalExamRes.setStatus("2");
            fMedicalExamRes.setExamRecordId(articleRecord.getExamRecordId());
            fMedicalExamRes.setAccountId(articleRecord.getAccountId());
            fMedicalExamRes.setResearchId(articleRecord.getResearchId());
            FMedicalExamForm fMedicalExamForm = selectExamFromDetailInfo(fMedicalExamRes);
            fMedicalExamForm.setExamRecordId(articleRecord.getExamRecordId());
            fMedicalExamForms.add(fMedicalExamForm);
        }
        Map<Long, List<FMedicalExamForm>> collected = fMedicalExamForms.stream().collect(Collectors.groupingBy(FMedicalExamForm::getExamRecordId));

        Map<String, List<String>> file = new HashMap<>();
        List<String/*url*/> urls = new ArrayList<>();
        groupbyPersonIdMAP.forEach((k,v)->{
            String fileName = k + "_" + v.get(0).getSubmitor();
            for (CmsArticleRecord cmsArticleRecord : v) {
                Long examRecordId = cmsArticleRecord.getExamRecordId();
                List<FMedicalExamForm> fMedicalExamForms1 = collected.get(examRecordId);
                if (CollectionUtils.isEmpty(fMedicalExamForms1)) {
                    continue;
                }
                for (FMedicalExamForm fMedicalExamForm : fMedicalExamForms1) {
                    List<FMedicalExamSubject> subjectList = fMedicalExamForm.getSubjectList();
                    if (CollectionUtils.isEmpty(subjectList)) {
                        continue;
                    }
                    for (FMedicalExamSubject fMedicalExamSubject : subjectList) {
                        List<String> imageUrls = fMedicalExamSubject.getImageUrls();
                        if (!CollectionUtils.isEmpty(imageUrls)) {
                            urls.addAll(imageUrls);
                        }
                    }
                }
            }
            file.put(fileName, urls);
        });
        return file;
    }

    private Map<String,List<String>> makeExeclByActicle(Workbook workbook, List<Long> examRecordIds) {
        // 查询文章
        List<CmsArticleRecord> cmsArticleRecords = new ArrayList<>();
        if (CollectionUtils.isEmpty(examRecordIds)) {
            CmsArticleRecord cmsArticleRecord = new CmsArticleRecord();
            cmsArticleRecords = cmsArticleMapper.selectCmsArticleRecord(cmsArticleRecord);
        }else {
            for (Long examRecordId : examRecordIds) {
                CmsArticleRecord query = new CmsArticleRecord();
                query.setExamRecordId(examRecordId);
                List<CmsArticleRecord> records = cmsArticleMapper.selectCmsArticleRecord(query);
                cmsArticleRecords.addAll(records);
            }
        }
        if (CollectionUtils.isEmpty(cmsArticleRecords)) {
            return new HashMap<>();
        }

        Map<Long, List<CmsArticleRecord>> groupedByArticleId = cmsArticleRecords.stream()
                .filter(item->Objects.nonNull(item.getArticleId()))
                .collect(Collectors.groupingBy(CmsArticleRecord::getArticleId));

        List<FMedicalExamForm> fMedicalExamForms = new ArrayList<>();
        for (CmsArticleRecord articleRecord : cmsArticleRecords) {
            FMedicalExamRes fMedicalExamRes = new FMedicalExamRes();
            fMedicalExamRes.setArticleId(articleRecord.getArticleId());
            fMedicalExamRes.setStatus("2");
            fMedicalExamRes.setExamRecordId(articleRecord.getExamRecordId());
            fMedicalExamRes.setAccountId(articleRecord.getAccountId());
            fMedicalExamRes.setResearchId(articleRecord.getResearchId());
            FMedicalExamForm fMedicalExamForm = selectExamFromDetailInfo(fMedicalExamRes);
            fMedicalExamForm.setExamRecordId(articleRecord.getExamRecordId());
            fMedicalExamForms.add(fMedicalExamForm);
        }
        Map<Long, List<FMedicalExamForm>> collected = fMedicalExamForms.stream().collect(Collectors.groupingBy(FMedicalExamForm::getExamRecordId));

        groupedByArticleId.forEach((articleId,cmsArticleRecordList)->{
            Sheet sheet = workbook.createSheet(cmsArticleRecordList.get(0).getTitle());
//            List<FMedicalExamSubject> subjectList = fMedicalExamForm.getSubjectList();
            // 创建第一行表头
            handleHeaderFeild(workbook,sheet);
            int count = 1;
            for (CmsArticleRecord articleRecord : cmsArticleRecordList) {
                Row row = sheet.createRow(count);

                // 填充数据
                String title = articleRecord.getTitle();
                Cell cell = row.createCell(0);
                cell.setCellValue(title);
                String submitor = articleRecord.getSubmitor();
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(submitor);
                String diseaseName = articleRecord.getDiseaseName();
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(diseaseName);
                String projectName = articleRecord.getProjectName();
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(projectName);
                Date submitTime = articleRecord.getSubmitTime();
                // 定义日期格式
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // 格式化日期
                String formattedDate = formatter.format(submitTime);
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(formattedDate);


                Long examRecordId = articleRecord.getExamRecordId();
                List<FMedicalExamForm> fMedicalExamForms1 = collected.get(examRecordId);
                FMedicalExamForm fMedicalExamForm = fMedicalExamForms1.get(0);
                int count2 = 0;
                Row row1 = sheet.getRow(0);
                for (FMedicalExamSubject fMedicalExamSubject : fMedicalExamForm.getSubjectList()) {

                    Cell cell7 = row1.getCell(7 + count2);
                    if (Objects.isNull(cell7)) {
                         cell7 = row1.createCell(7 + count2);
                        cell7.setCellValue(fMedicalExamSubject.getPropertyName());
                        // 创建一个新的 CellStyle
                        CellStyle cellStyle = workbook.createCellStyle();

                        // 配置 CellStyle
//                        cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
                        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
                        cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 设置背景颜色为黄色
                        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 设置填充模式

                        cell7.setCellStyle(cellStyle);
                    }

                    Cell cell8 = row.createCell(7+count2);
                    String propertyValue = fMedicalExamSubject.getPropertyValue();
                    cell8.setCellValue(propertyValue);
                    if ("Caption".equalsIgnoreCase(fMedicalExamSubject.getPropertyType())) {
                        String attribute = fMedicalExamSubject.getAttribute();
                        JSONObject jsonObject = JSON.parseObject(attribute);
                        String string = (String) jsonObject.get("description");
                        //清理string中的标签
                        string = string.replaceAll("<[^>]*>", "");
                        cell8.setCellValue(string);
                    }else if ("checkbox".equalsIgnoreCase(fMedicalExamSubject.getPropertyType())){
                        List<FMedicalExamItem> itemList = fMedicalExamSubject.getItemList();
                        if (!CollectionUtils.isEmpty(itemList)) {
                            StringBuffer sb = new StringBuffer();
                            for (FMedicalExamItem fMedicalExamItem : itemList) {
                                String itemLabel = fMedicalExamItem.getItemLabel();
                                String itemValue = fMedicalExamItem.getItemValue();
                                if (StringUtils.isEmpty(propertyValue)) {
                                    continue;
                                }
                                String[] split = propertyValue.split(",");
                                for (String s : split) {
                                    if (com.dchealth.common.utils.StringUtils.equals(itemValue, s)) {
                                        sb.append(itemLabel);
                                        sb.append(",");
                                    }
                                }
                            }
                            if (StringUtils.isNotBlank(sb.toString())) {
                                if (sb.toString().endsWith(",")) {
                                    cell8.setCellValue(sb.toString().substring(0,sb.toString().length()-1));
                                }
                            }
                        }
                    }else {
                        List<FMedicalExamItem> itemList = fMedicalExamSubject.getItemList();
                        if (!CollectionUtils.isEmpty(itemList)) {
                            for (FMedicalExamItem fMedicalExamItem : itemList) {
                                String itemLabel = fMedicalExamItem.getItemLabel();
                                if (com.dchealth.common.utils.StringUtils.equals(fMedicalExamItem.getItemValue(), propertyValue)) {
                                    cell8.setCellValue(itemLabel);
                                }
                            }
                        }
                    }
                    count2++;
                }
                // 评分
                Long scores = fMedicalExamForm.getScores();
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(scores);
                String evaluation = fMedicalExamForm.getEvaluation();
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(evaluation);

                count++;
            }
        });

        Map<String, List<CmsArticleRecord>> groupbyPersonIdMAP = cmsArticleRecords.stream()
                .filter(item->Objects.nonNull(item.getPersonId()))
                .collect(Collectors.groupingBy(CmsArticleRecord::getPersonId));

        Map<String, List<String>> file = new HashMap<>();
        groupbyPersonIdMAP.forEach((k/*personid*/,v/*record*/)->{
            List<String/*url*/> urls = new ArrayList<>();
            String fileName = k + "_" + v.get(0).getSubmitor();
            for (CmsArticleRecord cmsArticleRecord : v) {
                Long examRecordId = cmsArticleRecord.getExamRecordId();
                List<FMedicalExamForm> fMedicalExamForms1 = collected.get(examRecordId);
                if (CollectionUtils.isEmpty(fMedicalExamForms1)) {
                    continue;
                }
                for (FMedicalExamForm fMedicalExamForm : fMedicalExamForms1) {
                    List<FMedicalExamSubject> subjectList = fMedicalExamForm.getSubjectList();
                    if (CollectionUtils.isEmpty(subjectList)) {
                        continue;
                    }
                    for (FMedicalExamSubject fMedicalExamSubject : subjectList) {
                        List<String> imageUrls = fMedicalExamSubject.getImageUrls();
                        if (!CollectionUtils.isEmpty(imageUrls)) {
                            urls.addAll(imageUrls);
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(urls)) {
                file.put(fileName, urls);
            }
        });
        return file;
    }

    private void handleHeaderFeild(Workbook workbook,Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        List<String> list = Arrays.asList("问卷名称", "提交人", "问卷病种", "问卷队列", "提交时间", "得分","评价");
        // todo 解析动态表单, 并直接创建对应的值?
        for (int i = 0; i < list.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(list.get(i));

            // 创建一个新的 CellStyle
            CellStyle cellStyle = workbook.createCellStyle();

            // 配置 CellStyle
//            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 水平居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER); // 垂直居中
            cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex()); // 设置背景颜色为黄色
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND); // 设置填充模式

            cell.setCellStyle(cellStyle);
        }
    }


    private void buildFileResponse(HttpServletResponse response) {

        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    /**
     * 生成zip文件
     */
    private void buildZipFileResponse(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        org.apache.commons.io.IOUtils.write(data, response.getOutputStream());
    }
    @Override
    public List<CmsArticle> selectCmsInformationList(CmsArticle cmsArticle)
    {
        return cmsArticleMapper.selectCmsInformationList(cmsArticle);
    }


    /**
     * 查询科室资讯及子文件夹列表
     *
     * @param cmsArticle 资讯管理
     * @return 资讯管理
     */
    @Override
    public List<CmsArticle> selectCmsDeptArticleList(CmsArticle cmsArticle)
    {
        List<CmsArticle> list = cmsArticleMapper.selectCmsDeptArticleList(cmsArticle);
        return list;

    }

    /**
     * 查询科室分类资讯列表
     *
     * @param cmsArticle 资讯管理
     * @return 资讯管理
     */
    @Override
    public List<CmsArticleCateGoryVo> selectCmsDeptCategoryArticleList(CmsArticle cmsArticle)
    {
        return cmsArticleMapper.selectCmsDeptCategoryArticleList(cmsArticle);

    }

    @Override
    public List<CmsArticleRecord> selectCmsArticleRecord(CmsArticleRecord cmsArticleRecord) {
        return cmsArticleMapper.selectCmsArticleRecord(cmsArticleRecord);
    }



    public FMedicalExamForm selectExamFromDetailInfo(FMedicalExamRes fMedicalExamRes) {
        Long articleId = fMedicalExamRes.getArticleId();
        String status = fMedicalExamRes.getStatus();
        Long examRecordId = fMedicalExamRes.getExamRecordId();
        //查询问卷模板信息
        Optional<FMedicalExamForm> examForm =
                Optional.ofNullable(patientExamRecordMapper.selectFMedicalExamFormByArticleId(articleId));
        if (examForm.isPresent()) {
            FMedicalExamForm fMedicalExamForm = examForm.get();
            Long medicalFormId = fMedicalExamForm.getMedicalFormId();
            //查询问卷题干信息
            Optional<List<FMedicalExamSubject>> fMedicalExamSubjects =
                    Optional.ofNullable(fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(medicalFormId));
            if (fMedicalExamSubjects.isPresent()) {
                List<FMedicalExamSubject> examSubjects = fMedicalExamSubjects.get();
                examSubjects.forEach(subject -> {
                    //查询问卷选项信息
                    List<FMedicalExamItem> items =
                            fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                    subject.setItemList(items);
                    //查询问卷选项值信息
                    if (MedicalContants.EXAM_STATUS_1.equals(status) || MedicalContants.EXAM_STATUS_2.equals(status)) {
                        //题目类型是图片、文件
                        if (PropertyType.eq(PropertyType.UPLOAD, subject.getPropertyType())) {
                            List<PatientRecordFiles> recordFiles =
                                    patientRecordFilesMapper.selectPatientRecordFiles(subject.getId(),
                                            fMedicalExamRes.getExamRecordId());
                            List<String> imageUrls =
                                    recordFiles.stream().map(PatientRecordFiles::getImageUrl).collect(Collectors.toList());
                            subject.setImageUrls(imageUrls);
                        } else {
                            //题目类型是单选、多选、文本等
                            PatientExamRecord patientExamRecord =
                                    patientExamRecordMapper.selectPatientExamRecordById(examRecordId);
                            String sql = dynamicSql.buildSelectArticleByTableName(patientExamRecord.getExamTable());
                            DynamicForm dynamicForm = new DynamicForm();
                            dynamicForm.setArticleId(articleId);
                            dynamicForm.setExamRecordId(examRecordId);
                            dynamicForm.setSql(sql);
                            List<DynamicCommonProperty> properties = dynamicCommonMapper.selectBySql(dynamicForm);
                            if ("form".equalsIgnoreCase(subject.getPropertyType())) {
                                String attribute = subject.getAttribute();
                                JSONObject jsonObject1 = JSON.parseObject(attribute);
                                List<JSONObject> children = (List<JSONObject>) jsonObject1.get("children");
                                for (JSONObject jsonObject : children) {
                                    //
                                    for (DynamicCommonProperty property : properties) {
                                        if (jsonObject.get("propertyCode").equals(property.getPropertyCode())) {
                                            jsonObject.put("propertyValue", property.getPropertyValue());
                                            jsonObject.put("other", property.getOther());
                                        }
                                    }
                                }
                                subject.setAttribute(JSON.toJSONString(jsonObject1));
                            }else{
                                if (properties != null && !properties.isEmpty()) {
//                                Map<Long, List<DynamicCommonProperty>> collect = properties.stream().collect
//                                (Collectors.groupingBy(DynamicCommonProperty::getPropertyId));
                                    try {
                                        Map<Long, DynamicCommonProperty> collect =
                                                properties.stream().filter(x->!Objects.isNull(x.getPropertyId())).collect(Collectors.toMap(DynamicCommonProperty::getPropertyId, item -> item));
                                        if (collect != null && !collect.isEmpty() && collect.containsKey(subject.getId())) {
                                            DynamicCommonProperty commonProperties = collect.get(subject.getId());
                                            subject.setPropertyValue(commonProperties.getPropertyValue());
                                            subject.setOther(commonProperties.getOther());
                                        }
                                    } catch (Exception e) {
                                        for (DynamicCommonProperty property : properties) {
                                            if (subject.getPropertyCode().equals(property.getPropertyCode())) {
                                                subject.setPropertyValue(property.getPropertyValue());
                                                subject.setOther(property.getOther());
                                            }
                                        }
                                    }
                                }
                            }

                        }
                    }
                });
                //分数统计
                if (MedicalContants.EXAM_STATUS_2.equals(status)) {
                    ResearchInfoArticlePatient articlePatient = new ResearchInfoArticlePatient();
                    articlePatient.setExamRecordId(examRecordId);
                    articlePatient.setArticleId(articleId);
                    ResearchInfoArticlePatient riap =
                            researchInfoArticleMapper.selectResearchInfoArticlePatient(articlePatient);
                    if (riap != null && StringUtils.isNotBlank(riap.getScores())) {
                        fMedicalExamForm.setScores(Long.valueOf(riap.getScores()));
                        fMedicalExamForm.setEvaluation(riap.getEvaluationResult());
                    }
                    //题干分数
//                    long subjectScores = examSubjects.stream()
//                            .filter(subject -> subject.getSubjectScores() != null && "1".equals(subject.getIfScore
//                            ()) && (subject.getPropertyValue() != null || subject.getImageUrls() != null))
//                            .mapToLong(FMedicalExamSubject::getSubjectScores).sum();
//                    //选项分数
//                    //单选、多选、下拉框、级联分数
//                    long radioScores = 0;
//                   // AtomicLong radioScores = new AtomicLong();
//                    List<FMedicalExamSubject> subjects = examSubjects.stream().filter(subject -> (PropertyType
//                    .RADIO.equals(subject.getPropertyType())
//                            || PropertyType.CHECKBOX.equals(subject.getPropertyType())
//                            || PropertyType.SELECT.equals(subject.getPropertyType())
//                            || PropertyType.CASCADER.equals(subject.getPropertyType()))
//                            && "1".equals(subject.getIfScore()) && subject.getPropertyValue() != null).collect
//                            (Collectors.toList());
//                    for (FMedicalExamSubject subject : subjects) {
//                        //选项信息
//                        List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject
//                        .getId());
//                        //题所选答案
//                        List<String> propertyValues = Arrays.stream(subject.getPropertyValue().split(",")).collect
//                        (Collectors.toList());
//                        long scores = items.stream().filter(item -> propertyValues.contains(item.getItemValue()))
//                        .mapToLong(FMedicalExamItem::getItemScores).sum();
//                        radioScores += scores;
//                    }
//                    fMedicalExamForm.setScores(subjectScores + radioScores);
//
//
//                    CmsArticleSetEvaluation cmsArticleSetEvaluation = cmsArticleSetEvaluationMapper
//                    .selectCmsArticleSetEvaluationByCmsArticleId(articleId,fMedicalExamForm.getScores());
//                      if(cmsArticleSetEvaluation != null){
//                          fMedicalExamForm.setEvaluation(cmsArticleSetEvaluation.getEvaluationResult());
//                      }
                }
                for (FMedicalExamSubject examSubject : examSubjects) {
                    if ("form".equalsIgnoreCase(examSubject.getPropertyType())) {
                        String attribute = examSubject.getAttribute();
                        JSONObject jsonObject = JSON.parseObject(attribute);
                        List<JSONObject> children = (List<JSONObject>) jsonObject.get("children");
                        for (JSONObject child : children) {
                            Object o = child.get("propertyValue");
                            if (Objects.isNull(o)) {
                                child.put("propertyValue", "");
                            }
                        }
                        jsonObject.put("children", children);
                        examSubject.setAttribute(JSON.toJSONString(jsonObject));
                    }else{
                        if(StringUtils.isEmpty(examSubject.getPropertyValue())) examSubject.setPropertyValue("");
                    }
                }
                fMedicalExamForm.setSubjectList(examSubjects);
                //"再来一次"按钮是否显示处理
                handleIsShowAgain(articleId, fMedicalExamRes.getAccountId(), fMedicalExamForm);
            }

            return fMedicalExamForm;
        }
        return null;
    }


    private void handleIsShowAgain(Long articleId, Long accountId, FMedicalExamForm fMedicalExamForm) {
        // 该患者在哪几个队列中能看到该问卷，只要有一个队列能看到该问卷，“再来一次”按钮就显示
        // 至于当后台有队列删该问卷，App上就应该把删除该问卷的队列对应的问卷不展示，这个 别的逻辑 会处理。
        if(!Objects.isNull(accountId)){
            List<ResearchInfoArticle> researchInfoArticleList =
                    researchInfoArticleMapper.queryByArticleIdAndAccountId(articleId, accountId);
            if (!CollectionUtils.isEmpty(researchInfoArticleList) && researchInfoArticleList.size() > 0) {
                fMedicalExamForm.setIsShowAgain(true);
            } else {
                fMedicalExamForm.setIsShowAgain(false);
            }
        }

    }













    /**
     * 新增资讯管理
     * 
     * @param cmsArticle 资讯管理
     * @return 结果
     */
    @Override
    public int insertCmsArticle(CmsArticle cmsArticle)
    {
        cmsArticle.setCreateTime(DateUtils.getNowDate());
        cmsArticle.setUpdateTime(DateUtils.getNowDate());
        CmsArticleData cmsArticleData = new CmsArticleData();
        cmsArticleData.setContent(cmsArticle.getContent());
        cmsArticleData.setCopyFrom(cmsArticle.getCopyFrom());
        cmsArticleData.setRelation(cmsArticle.getRelation());
        cmsArticleData.setAllowComment(cmsArticle.getAllowComment());
//        String deptIds = sysRoleDeptMapper.getDeptIdsByUserId(cmsArticle.getCreateId());
//        cmsArticle.setDeptIds(deptIds);
        cmsArticleMapper.insertCmsArticle(cmsArticle);
        cmsArticleData.setId(cmsArticle.getId());
        cmsArticleDataMapper.insertCmsArticleData(cmsArticleData);
        return 1;
    }

    /**
     * 修改资讯管理
     * 
     * @param cmsArticle 资讯管理
     * @return 结果
     */
    @Override
    public int updateCmsArticle(CmsArticle cmsArticle)
    {
        cmsArticle.setUpdateTime(DateUtils.getNowDate());
        CmsArticleData cmsArticleData = new CmsArticleData();
        cmsArticleData.setAllowComment(cmsArticle.getAllowComment());
        cmsArticleData.setContent(cmsArticle.getContent());
        cmsArticleData.setCopyFrom(cmsArticle.getCopyFrom());
        cmsArticleData.setId(cmsArticle.getId());
        if(cmsArticleData.getContent()!=null){
            cmsArticleDataMapper.updateCmsArticleData(cmsArticleData);
        }
        return cmsArticleMapper.updateCmsArticle(cmsArticle);
    }

    /**
     * 批量删除资讯管理
     * 
     * @param ids 需要删除的资讯管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsArticleByIds(Long[] ids)
    {
        return cmsArticleMapper.deleteCmsArticleByIds(ids);
    }

    /**
     * 删除资讯管理信息
     * 
     * @param id 资讯管理主键
     * @return 结果
     */
    @Override
    public int deleteCmsArticleById(Long id)
    {
        return cmsArticleMapper.deleteCmsArticleById(id);
    }

    @Override
    public int deleteCmsArticleByDiseaseId(Long diseaseId) {
        return cmsArticleMapper.updateCmsArticleByDiseaseId(diseaseId);
    }

    /**
     * 逻辑删除
     * @param id
     * @return
     */
    @Override
    @Transactional
    public int updateDelById(Long id) {
        researchInfoArticleMapper.deleteResearchInfoArticleByArticleId(id);
        return cmsArticleMapper.updateDelById(id);
    }

    /**
     * 查询咨询嘻嘻
     * @param articleIds
     * @return
     */
    @Override
    public List<CmsArticle> selectCmsArticleByIds(List<Long> articleIds) {
        return cmsArticleMapper.selectCmsArticleByIds(articleIds);
    }

    /**
     * 问卷发布(问卷表创建)
     * @param id
     * @return
     */
    @Transactional
    @Override
    public int updateCmsArticleStatusById(Long id) {
        //创建问卷表(每10张问卷分一张表存值)
        //问卷表后缀名
        CmsArticle cmsArticle = cmsArticleMapper.selectCmsArticleById(id);
        int i = Math.floorDiv(cmsArticle.getQuesNum(), 10);
        //创建问卷表
        dynamicArticle.init().createArticleTableBySuffix(i);
        //创建问卷log表
        dynamicArticleLog.init().createArticleTableBySuffix(i);
        return cmsArticleMapper.updateCmsArticleStatusById(id);
    }


    @Override
    public Integer fileMove(CmsArticleMoveParam articleMoveParam) {
        List<CmsArticle> cmsArticles;
        CmsArticle cmsArticle = new CmsArticle();
        if (Objects.nonNull(articleMoveParam.getCategoryId())){
            Long categoryId = articleMoveParam.getCategoryId();
            List<CmsCategory> cmsCategoryList = cmsCategoryService.selectAllChildCategoryById(categoryId);
            List<Long> categoryIdList = cmsCategoryList.stream().map(CmsCategory::getId).collect(Collectors.toList());
            cmsArticle.setCategoryIdList(categoryIdList);
            cmsArticles = cmsArticleMapper.selectCmsInformationList(cmsArticle);
        }else {
            cmsArticle.setArticleId(articleMoveParam.getArticleId());
            cmsArticles = cmsArticleMapper.selectCmsInformationList(cmsArticle);
        }

        Long newCategoryId = articleMoveParam.getNewCategoryId();

        List<CmsArticle> cmsArticleList = new ArrayList<>();
        for (CmsArticle cmsArticleVo: cmsArticles) {
            CmsArticle cmsArticle1 = new CmsArticle();
            cmsArticle1.setArticleId(cmsArticleVo.getId());
            cmsArticle1.setCategoryId(newCategoryId);
            cmsArticleList.add(cmsArticle1);
        }
        Integer flagNum = 0;
        if (!CollectionUtils.isEmpty(cmsArticleList)){
            flagNum = cmsArticleMapper.batchUpdateCmsArticleCategory(cmsArticleList);
        }
        return flagNum;

    }


    @Override
    public Integer deptfileMove(CmsArticleMoveParam articleMoveParam) {
        Long categoryId = articleMoveParam.getCategoryId();
        List<CmsArticle> cmsArticles;
        CmsArticle cmsArticle = new CmsArticle();
        if (Objects.nonNull(categoryId)){
            List<CmsCategory> cmsCategoryList = cmsCategoryService.selectAllChildCategoryById(categoryId);
            List<Long> categoryIdList = cmsCategoryList.stream().map(CmsCategory::getId).collect(Collectors.toList());
            cmsArticle.setDeptCategoryIdList(categoryIdList);
            cmsArticles = cmsArticleMapper.selectCmsInformationList(cmsArticle);
        }else {
            cmsArticle.setArticleId(articleMoveParam.getArticleId());
            cmsArticles = cmsArticleMapper.selectCmsInformationList(cmsArticle);
        }
        Long newCategoryId = articleMoveParam.getNewCategoryId();
        CmsCategory cmsCategory = cmsCategoryMapper.selectCmsCategoryById(newCategoryId);
        Long newDeptId = cmsCategory.getDeptId();

        List<CmsDeptArticleMoveDo> cmsDeptArticleMoveDos = new ArrayList<>();
        for (CmsArticle cmsArticleVo: cmsArticles) {
            CmsDeptArticleMoveDo deptArticleMoveDo = new CmsDeptArticleMoveDo();
            deptArticleMoveDo.setDeptId(articleMoveParam.getDeptId());
            deptArticleMoveDo.setNewDeptId(newDeptId);
            deptArticleMoveDo.setArticleId(cmsArticleVo.getId());
            deptArticleMoveDo.setNewCategoryId(newCategoryId);
            cmsDeptArticleMoveDos.add(deptArticleMoveDo);
        }
        Integer flagNum = 0;
        if (!CollectionUtils.isEmpty(cmsDeptArticleMoveDos)){
            flagNum = cmsDeptArticleMapper.updateCmsDeptArticleCategoryAndDeptId(cmsDeptArticleMoveDos);
        }
        return flagNum;
    }

    @Override
    public List<CmsDeptArticle> selectCmsDeptArticleByArticleIdList(List<Long> articleIdList) {
        return cmsDeptArticleMapper.selectCmsDeptArticleByArticleIdList(articleIdList);
    }
}
