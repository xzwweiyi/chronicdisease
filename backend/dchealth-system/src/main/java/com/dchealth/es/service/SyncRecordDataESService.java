package com.dchealth.es.service;

import java.io.IOException;

/**
 * @author zjl
 * @since 2023/4/18
 */
public interface SyncRecordDataESService<T> {



    int syncMpDiagnosisInfos(String id, String startDate, String endDate) throws Exception;


}
