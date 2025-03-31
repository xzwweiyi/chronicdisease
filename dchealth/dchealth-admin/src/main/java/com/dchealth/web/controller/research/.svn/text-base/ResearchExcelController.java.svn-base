package com.dchealth.web.controller.research;

import com.dchealth.common.utils.StringUtils;
import com.dchealth.medical.domain.PatientInfo;
import com.dchealth.medical.domain.PatientRecord;
import com.dchealth.medical.domain.PatientRecordTemplate;
import com.dchealth.medical.domain.form.PatientRecordDetailForm;
import com.dchealth.medical.domain.vo.*;
import com.dchealth.medical.mapper.PatientInfoMapper;
import com.dchealth.medical.mapper.PatientRecordFilesMapper;
import com.dchealth.medical.mapper.PatientRecordMapper;
import com.dchealth.medical.mapper.PatientRecordTemplateMapper;
import com.dchealth.medical.service.IPatientRecordService;
import com.dchealth.research.domain.ResearchInfo;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/research/excel")
public class ResearchExcelController {

    @Autowired
    private PatientRecordMapper patientRecordMapper;

    @Autowired
    private PatientInfoMapper patientInfoMapper;

    @Autowired
    private PatientRecordTemplateMapper patientRecordTemplateMapper;

    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;

    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;

    @Autowired
    private PatientRecordFilesMapper patientRecordFilesMapper;

    //@Autowired
    //private Executor executor;

   @Autowired
   private IPatientRecordService iPatientRecordService;

    //@PreAuthorize("@ss.hasPermi('research:excel:export')")
    @PostMapping("/export")
    public void dynamicExcel(HttpServletResponse response, @RequestBody ResearchInfo researchInfo) {

        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = null;
        sheet = workBook.createSheet("周期信息导出表");

        HSSFRow row = sheet.createRow(0);
        HSSFRow row2 = sheet.createRow(1);
        HSSFRow row3 = sheet.createRow(2);

        Long researchId = researchInfo.getId();
        //根据队列查询所有患者
        PatientInfo patientInfo = new PatientInfo();
        patientInfo.setResearchId(researchId);
        List<PatientInfo> patientInfos = patientInfoMapper.selectPatientInfoList(patientInfo);
        List<List<String>> valueList = new ArrayList<>();
        if (patientInfos.size()>0){
            for (int p = 0; p < patientInfos.size(); p++) {
                Long patientId = patientInfos.get(p).getPatientId();
                List<PatientRecord> patientRecords = patientRecordMapper.selectPatientRecordsByResearchIdAndPatientId(researchId,patientId);
                if (patientRecords.size()>0){
                    List<PatientRecordDetailForm> patientRecordDetailForms = iPatientRecordService.patientRecordDetail(patientRecords);
                   if (patientRecordDetailForms.size()>0){
                       List<String> propertyValueList = new ArrayList<>();
                       for (PatientRecordDetailForm patientRecordDetailForm : patientRecordDetailForms) {
                           List<MedicalFormVO> medicalFormVOS = patientRecordDetailForm.getMedicalFormVOS();
                           if (medicalFormVOS != null && medicalFormVOS.size()>0){
                               for (MedicalFormVO medicalFormVO : medicalFormVOS) {
                                   List<MedicalPropertyVO> propertyVOS = medicalFormVO.getPropertyVOS();
                                   if (propertyVOS != null && propertyVOS.size()>0){
                                       for (MedicalPropertyVO propertyVO : propertyVOS) {
                                           String propertyType = propertyVO.getPropertyType();
                                           if ("Upload".equals(propertyType)){
                                               List<String> imageUrls = propertyVO.getImageUrls();
                                               if (imageUrls!= null && imageUrls.size()>0){
                                                   propertyValueList.add(StringUtils.strip(imageUrls.toString(),"[]"));
                                               }else {
                                                   propertyValueList.add("");
                                               }
                                           }else {
                                               String propertyValue = propertyVO.getPropertyValue();
                                               if (StringUtils.isNotNull(propertyValue)){
                                                   propertyValueList.add(propertyValue);
                                               }else {
                                                   propertyValueList.add("");
                                               }

                                           }

                                       }
                                   }
                               }
                           }

                       }
                       valueList.add(propertyValueList);
                   }
                }
            }
        }
        //根据队列id查询该队列的所有周期配置表
        List<PatientRecordTemplate> patientRecordTemplates = patientRecordTemplateMapper.selectPatientRecordTemplateByResearchId(researchId);
        if (patientRecordTemplates.size() > 0) {
            int num = 0;
            int flag = 0;
            for (int i = 0; i < patientRecordTemplates.size(); i++) {
                //节点id
                Long followId = patientRecordTemplates.get(i).getFollowId();
                //一级标题
                String followName = patientRecordTemplates.get(i).getFollowName();
                if (i == 0) {
                    row.createCell(i).setCellValue(followName);
                } else {
                    row.createCell(num).setCellValue(followName);
                    flag = num;
                }

                //根据随访节点id查询表单模板
                List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowId(followId);
                if (fMedicalForms.size() > 0) {
                    int flag2 = 0;
                    for (int j = 0; j < fMedicalForms.size(); j++) {
                        //表单id
                        Long medicalFormId = fMedicalForms.get(j).getMedicalFormId();
                        //二级标题
                        String medicalName = fMedicalForms.get(j).getMedicalName();
                        if (num == 0) {
                            row2.createCell(j).setCellValue(medicalName);
                        } else {
                            row2.createCell(num).setCellValue(medicalName);
                            flag2 = num;
                        }
                        //获取表单的所有属性
                        List<FMedicalProperty> fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormId(medicalFormId);
                        if (fMedicalProperties.size() > 0) {
                            for (int k = 0; k < fMedicalProperties.size(); k++) {
                                //属性id
                                Long id = fMedicalProperties.get(k).getId();
                                //三级标题
                                String propertyName = fMedicalProperties.get(k).getPropertyName();
                                //是否是大字段
                                String propertyType = fMedicalProperties.get(k).getPropertyType();
                                if (num == 0) {
                                    row3.createCell(k).setCellValue(propertyName);
                                } else {
                                    row3.createCell(num + k).setCellValue(propertyName);
                                }
                            }
                            num = num + fMedicalProperties.size();
                        } else {
                            num = num + 1;
                        }

                        //二级标题合并
                        if (num == 0) {
                            CellRangeAddress region = new CellRangeAddress(1, 1, 0, 0);
                            sheet.addMergedRegion(region);
                        } else {
                            if (flag2 != num - 1) {
                                CellRangeAddress region = new CellRangeAddress(1, 1, flag2, num - 1);
                                sheet.addMergedRegion(region);
                            }
                        }
                    }
                } else {
                    num = num + 1;
                }

                //一级标题合并
                if (num == 0) {
                    CellRangeAddress region = new CellRangeAddress(0, 0, 0, 0);
                    sheet.addMergedRegion(region);
                } else {
                    if (flag != num - 1) {
                        CellRangeAddress region = new CellRangeAddress(0, 0, flag, num - 1);
                        sheet.addMergedRegion(region);
                    }
                }
            }
        }
        if (valueList.size()>0){
            for (int i = 0; i < valueList.size(); i++) {
                List<String> stringList = valueList.get(i);
                if (stringList.size()>0){
                    boolean b = stringList.stream().allMatch(a -> a.equals(""));
                    if (!b){
                        HSSFRow rowX = sheet.createRow(3+i);
                        for (int j = 0; j < stringList.size(); j++) {
                            rowX.createCell(j).setCellValue(stringList.get(j));
                        }
                    }
                }
            }
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("D:\\demo.xls");
            workBook.write(fileOutputStream);
            fileOutputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
