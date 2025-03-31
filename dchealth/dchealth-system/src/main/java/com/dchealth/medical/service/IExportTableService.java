package com.dchealth.medical.service;

import com.dchealth.medical.domain.vo.PatientRecordVo;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface IExportTableService {
    void exportTable(Workbook workbook, String sheetName, List<PatientRecordVo> list, Long researchId);
}
