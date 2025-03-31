package com.dchealth.medical.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dchealth.common.utils.StringUtils;
import com.dchealth.common.utils.poi.ExportExcelUtil;
import com.dchealth.compatibility.utils.HealthUtils;
import com.dchealth.medical.domain.MpPerson;
import com.dchealth.medical.domain.vo.PatientRecordVo;
import com.dchealth.medical.mapper.MpPersonMapper;
import com.dchealth.medical.service.IExportTableService;
import com.dchealth.template.domain.FMedicalExamItem;
import com.dchealth.template.domain.FMedicalExamSubject;
import com.dchealth.template.domain.FMedicalForm;
import com.dchealth.template.domain.FMedicalProperty;
import com.dchealth.template.mapper.FMedicalExamItemMapper;
import com.dchealth.template.mapper.FMedicalExamSubjectMapper;
import com.dchealth.template.mapper.FMedicalFormMapper;
import com.dchealth.template.mapper.FMedicalPropertyMapper;
import com.dchealth.template.service.impl.FMedicalFormServiceImpl;

@Service
public class IExportTableServiceImpl implements IExportTableService {

    @Autowired
    private FMedicalFormMapper fMedicalFormMapper;
    @Autowired
    private FMedicalPropertyMapper fMedicalPropertyMapper;
    @Autowired
    private FMedicalExamSubjectMapper fMedicalExamSubjectMapper;
    @Autowired
    private FMedicalFormServiceImpl fMedicalFormService;
    @Autowired
    private FMedicalExamItemMapper fMedicalExamItemMapper;
    @Autowired
    private MpPersonMapper mpPersonMapper;
    
    

    /**
     * 导出
     * @param workbook
     * @param sheetName
     * @param list
     * @param researchId
     */
    @Override
    public void exportTable(Workbook workbook, String sheetName, List<PatientRecordVo> list, Long researchId) {
        if(!CollectionUtils.isEmpty(list)){
            List<Long> followIds = list.stream().map(PatientRecordVo::getFollowId).distinct().collect(Collectors.toList());
            List<FMedicalForm> formList = fMedicalFormMapper.selectFMedicalFormByFollowIds(followIds);
            if(!CollectionUtils.isEmpty(formList)){
                for (int i = 0; i < formList.size(); i++) {
                    FMedicalForm fMedicalForm = formList.get(i);
                    packageProperty(fMedicalForm);
                    List<PatientRecordVo> vos = list.stream().filter(vo -> vo.getFollowId().intValue() == fMedicalForm.getFollowId().intValue()).collect(Collectors.toList());
                    getXGSheet(workbook, i, fMedicalForm, vos);
                }
            }
        }
    }

    /**
     * 组装property信息
     * @param fMedicalForm
     */
   public void packageProperty(FMedicalForm fMedicalForm){
       List<FMedicalProperty> fMedicalProperties = null;
       if("3".equals(fMedicalForm.getFormType())){
           //题干信息
           List<FMedicalExamSubject> subjectList = fMedicalExamSubjectMapper.selectFMedicalExamSubjectByFromId(fMedicalForm.getMedicalFormId());
           if(subjectList != null && !subjectList.isEmpty()){
               fMedicalProperties = new ArrayList<>();
               for (FMedicalExamSubject subject : subjectList) {
                   FMedicalProperty property = new FMedicalProperty();
                   BeanUtils.copyProperties(subject, property);
                   property.setFollowId(fMedicalForm.getFollowId());
                   property.setArticleId(fMedicalForm.getArticleId());
                   //选项信息
                   List<FMedicalExamItem> items = fMedicalExamItemMapper.selectFMedicalExamItemByProId(subject.getId());
                   property.setItemList(items);
                   fMedicalProperties.add(property);
               }
           }
       } else {
           fMedicalProperties = fMedicalPropertyMapper.selectFMedicalPropertyByMedicalFormId(fMedicalForm.getMedicalFormId());
       }
       List<FMedicalForm> fMedicalForms = fMedicalFormMapper.selectFMedicalFormByFollowIdAndParentId(fMedicalForm.getFollowId(), fMedicalForm.getMedicalFormId());
       fMedicalForm.setMedicalProperties(fMedicalProperties);
       fMedicalForm.setMedicalFormList(fMedicalForms);
    }

    public void getXGSheet(Workbook workbook, int index, FMedicalForm fMedicalForm, List<PatientRecordVo> vos){
        ExportExcelUtil<FMedicalProperty> excelUtil = new ExportExcelUtil<FMedicalProperty>(workbook,fMedicalForm.getMedicalName()) {
            @Override
            public void setUpSheet(List<FMedicalProperty> list) {
                createSheetNew(0, index);
                int colNum = 0;
                //第一行分类
                Row row0 = this.sheet.createRow(0);
                List<FMedicalForm> formList = fMedicalForm.getMedicalFormList();
                if(CollectionUtils.isEmpty(formList)){
                    //基本信息
                    colNum = setBaseInfo(this, colNum, fMedicalForm, vos);
                } else {
                    //第二行分类
                    Row row1 = this.sheet.createRow(1);
                    //基本信息
                    colNum = setBaseInfo1(this, colNum, fMedicalForm, vos);
                }
            }

            @Override
            public void appendCellHeard(Row row, int column) {

            }

            @Override
            public void appendCellValue(Row row, int column, FMedicalProperty vo) {

            }
        };
        excelUtil.setUpSheet(fMedicalForm.getMedicalProperties());
    }

    /**
     * 构建excel头，赋值
     * @param excelUtil
     * @param colNum
     * @param form
     * @param vos
     * @return
     */
    private int setBaseInfo(ExportExcelUtil excelUtil, int colNum, FMedicalForm form, List<PatientRecordVo> vos) {
        List<FMedicalProperty> properties = form.getMedicalProperties();
        if(CollectionUtils.isEmpty(properties)){
            return colNum;
        }
        int startColNum = colNum;
        Row row0 = excelUtil.sheet.getRow(0);
        colNum = patientInfo(excelUtil, colNum, row0);
        for (FMedicalProperty property : properties) {
            excelUtil.addCell(row0,property.getPropertyName(),colNum);
            colNum++;
        }
        int rowNum = 1;
        packageValues(excelUtil,startColNum,rowNum,vos,form);
        return colNum;
    }

    /**
     * 赋值（包含多行标题）
     * @param excelUtil
     * @param colNum
     * @param form
     * @param vos
     * @return
     */
    private int setBaseInfo1(ExportExcelUtil excelUtil, int colNum, FMedicalForm form, List<PatientRecordVo> vos) {
        List<FMedicalForm> medicalFormList = form.getMedicalFormList();
        List<FMedicalProperty> medicalProperties = form.getMedicalProperties();
        if(CollectionUtils.isEmpty(medicalProperties) && CollectionUtils.isEmpty(medicalFormList)){
            return colNum;
        }
        int startColNum = colNum;
        Row row0 = excelUtil.sheet.getRow(0);
        Row row1 = excelUtil.sheet.getRow(1);
        colNum = patientInfo(excelUtil, colNum, row0);
        for (int i = 0; i < colNum; i++) {
            CellRangeAddress region = new CellRangeAddress(0,1,i,i);
            excelUtil.sheet.addMergedRegion(region);
        }
        for (FMedicalForm fmf : medicalFormList) {
            int firstCol = colNum;
            packageProperty(fmf);
            List<FMedicalProperty> fMedicalProperties = fmf.getMedicalProperties();
            excelUtil.addCell(row0,fmf.getMedicalName(),colNum);
            if(!CollectionUtils.isEmpty(fMedicalProperties)){
                for (FMedicalProperty property : fMedicalProperties) {
                    excelUtil.addCell(row1,property.getPropertyName(),colNum);
                    colNum++;
                }
                if((colNum - firstCol) > 1){
                    CellRangeAddress regionRow = new CellRangeAddress(0,0,firstCol,colNum-1);
                    excelUtil.sheet.addMergedRegion(regionRow);
                }
            } else {
                colNum++;
            }
        }
        for (FMedicalProperty property : medicalProperties) {
            excelUtil.addCell(row0,property.getPropertyName(),colNum);
            CellRangeAddress ragion = new CellRangeAddress(0,1,colNum,colNum);
            excelUtil.sheet.addMergedRegion(ragion);
            colNum++;
        }
        int rowNum = 2;
        packageValues(excelUtil,startColNum,rowNum,vos,form);
        return colNum;
    }

    /**
     * 患者基础信息
     * @param excelUtil
     * @param colNum
     * @param row0
     * @return
     */
    public int patientInfo(ExportExcelUtil excelUtil, int colNum, Row row0){
        excelUtil.addCell(row0,"患者ID",colNum);
        colNum++;
        excelUtil.addCell(row0,"患者姓名",colNum);
        colNum++;
        excelUtil.addCell(row0,"患者性别",colNum);
        colNum++;
        excelUtil.addCell(row0,"出生日期",colNum);
        colNum++;
        excelUtil.addCell(row0,"民族",colNum);
        colNum++;
        excelUtil.addCell(row0,"阶段",colNum);
        colNum++;
        return colNum;
    }

    /**
     * 赋值
     * @param excelUtil
     * @param startColNum
     * @param rowNum
     * @param vos
     * @param form
     */
    public void packageValues(ExportExcelUtil excelUtil, int startColNum, int rowNum, List<PatientRecordVo> vos, FMedicalForm form){
        if(!CollectionUtils.isEmpty(vos)){
            for (PatientRecordVo record : vos) {
            	MpPerson info =  mpPersonMapper.selectMpPersonByPatientId(record.getPatientId());
                int sc = startColNum;
                Row row = excelUtil.sheet.getRow(rowNum++);
                if (row == null) {
                    row = excelUtil.sheet.createRow(rowNum - 1);
                }
                excelUtil.addCell(row, String.valueOf(record.getPatientId()), sc++);
                excelUtil.addCell(row, record.getPatientName(), sc++);
                if(StringUtils.isNotBlankAndNotEmpty(info.getCardId())) {
                	 excelUtil.addCell(row, HealthUtils.getSexName(info.getCardId()), sc++);
                }else {
                	 excelUtil.addCell(row, "未知", sc++);
                }
               
                excelUtil.addCell(row, HealthUtils.getBirthFromIdCard(info.getCardId()), sc++);
                excelUtil.addCell(row, "中国", sc++);
                excelUtil.addCell(row, record.getName(), sc++);
                List<FMedicalForm> medicalFormList = form.getMedicalFormList();
                if(!CollectionUtils.isEmpty(medicalFormList)){
                    for (FMedicalForm fmf : medicalFormList) {
                        List<FMedicalProperty> medicalProperties = fmf.getMedicalProperties();
                        if(!CollectionUtils.isEmpty(medicalProperties)){
                            for (FMedicalProperty property : medicalProperties) {
                                String propertyValue = fMedicalFormService.packageMedicalPropertyValues(medicalProperties, fmf, record, property.getId());
                                excelUtil.addCell(row, propertyValue != null ? propertyValue : "", sc++);
                            }
                        } else {
                            excelUtil.addCell(row, "", sc++);
                        }
                    }
                }
                List<FMedicalProperty> medicalProperties = form.getMedicalProperties();
                if(!CollectionUtils.isEmpty(medicalProperties)){
                    for (FMedicalProperty property : medicalProperties) {
                        String propertyValue = fMedicalFormService.packageMedicalPropertyValues(medicalProperties, form, record, property.getId());
                        excelUtil.addCell(row, propertyValue != null ? propertyValue : "", sc++);
                    }
                }
            }
        }
    }

    public String queryExamItemValue(Long propertyId, String value){
        List<String> values = Arrays.asList(value.split(","));
        List<FMedicalExamItem> item = fMedicalExamItemMapper.selectFMedicalExamItemByProIdAndValue(propertyId, values);
        return String.join(",",item.stream().map(FMedicalExamItem::getItemLabel).collect(Collectors.toList()));
    }

}
