package com.dchealth.dynamic.service;

import com.dchealth.dynamic.exception.DynamicException;

public interface DynamicTableService {
    boolean createTable();

    /**
     * 根据传入后缀参数创建表
     * archive_data_1、archive_data_log_1、patient_files_1、
     * patient_files_log_1、patient_records_1、patient_records_log_1
     * @param suffix lg 1,2,3
     * @return 创建结果 成功/失败+原因
     */
    void createTableBySuffix(int suffix) throws DynamicException;
}
