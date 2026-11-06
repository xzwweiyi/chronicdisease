package com.dchealth.dynamic.service.impl;

import com.dchealth.dynamic.domain.TbDynamicRecord;
import com.dchealth.dynamic.exception.DynamicException;
import com.dchealth.dynamic.mapper.DynamicRecordMapper;
import com.dchealth.dynamic.service.DynamicTableService;
import com.dchealth.dynamic.templete.*;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLSyntaxErrorException;
import java.util.Date;

@Service
public class DynamicTableServiceImpl implements DynamicTableService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicTableServiceImpl.class);
    @Autowired
    private DynamicData dataService;

    @Autowired
    private DynamicPatient patientService;

    @Autowired
    private DynamicArchive archiveService;

    @Autowired
    private DynamicArchiveLog archiveLogService;

    @Autowired
    private DynamicPatientFile patientFileService;

    @Autowired
    private DynamicPatientFileLog patientFileLogService;

    @Autowired
    private DynamicPatientRecord patientRecordService;

    @Autowired
    private DynamicPatientRecordLog patientRecordLogService;

    @Autowired
    private DynamicRecordMapper dynamicRecordMapper;

    @Autowired
    public SqlSessionFactory sqlSessionFactory;

    @Override
    public boolean createTable() {
        DynamicBase table1 = dataService.init().createTable();
        saveTbDynamicRecord(table1);

        DynamicBase table2 = patientService.init().createTable();
        saveTbDynamicRecord(table2);
        return true;
    }

    private void saveTbDynamicRecord(DynamicBase table) {
        TbDynamicRecord dynamicRecord = new TbDynamicRecord();
        dynamicRecord.setCreateTime(new Date());
        dynamicRecord.setTbFullName(table.getTableName());
        dynamicRecord.setTbPre(table.getTableNamePre());
        dynamicRecord.setTbOrder(table.getCreateTimes());
        dynamicRecordMapper.save(dynamicRecord);
    }

    @Override
    public void createTableBySuffix(int suffix) throws DynamicException {
        try {
            archiveService.init().createTableBySuffix(suffix);
            archiveLogService.init().createTableBySuffix(suffix);
            patientFileService.init().createTableBySuffix(suffix);
            patientFileLogService.init().createTableBySuffix(suffix);
            patientRecordService.init().createTableBySuffix(suffix);
            patientRecordLogService.init().createTableBySuffix(suffix);
        } catch (Exception e) {
            LOGGER.error("DynamicTableServiceImpl createTableBySuffix Exception:{}", e.getMessage());
            // 手动删表，mybatis 缓存手动清理
            if (e instanceof SQLSyntaxErrorException) {
                sqlSessionFactory.openSession().clearCache();
                createTableBySuffix(suffix);
            } else {
                throw e;
            }
        }
    }
}
