package com.dchealth.dynamic.templete;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.dynamic.DynamicConstant.*;
import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_TIME;

@Service
public class DynamicPatientRecordLog extends DynamicBase {
    String TABLE_NAME_PRE = "patient_records_log_";


    @Override
    public void buildDynamicTableName() {
        tableNamePre = TABLE_NAME_PRE;
        List<String> tableNames = dynamicTableMapper.querySamePreTable(TABLE_NAME_PRE);
        samePreTables.addAll(tableNames.stream().filter(s -> isNum(s.replace(TABLE_NAME_PRE, "")))
                .collect(Collectors.toList()));
    }

    @Override
    public void buildDynamicColumn() {
        if (CollectionUtils.isNotEmpty(samePreTables)) {
            return;
        }
        columnList.add(buildColumnPrimaryKey("log_id", COLUMN_TYPE_BIGINT, true));
        columnList.add(buildColumnDefaultNull("archive_id", COLUMN_TYPE_BIGINT_20, "档案Id", true));
        columnList.add(buildColumnDefaultNull("patient_id", COLUMN_TYPE_BIGINT_20, "患者ID", true));
        columnList.add(buildColumnDefaultNull("research_id", COLUMN_TYPE_BIGINT_20, "研究项目ID", true));
        columnList.add(buildColumnDefaultNull("records_name", COLUMN_TYPE_VARCHAR64, "周期名称", true));
        columnList.add(buildColumnDefaultNull("dept_id", COLUMN_TYPE_BIGINT_20, "创建人所在科室ID", true));
        columnList.add(buildColumnDefaultNull("record_num", COLUMN_TYPE_INT_11, "周期数", true));
        columnList.add(buildColumnDefaultNull("stage_num", COLUMN_TYPE_INT_11, "第几次随访", true));
        columnList.add(buildColumnDefaultNull("progress_rate", COLUMN_TYPE_TEXT, "档案进度"));
        columnList.add(buildColumnDefaultNull("month_stat", COLUMN_TYPE_INT_11, "创建月份"));
        columnList.add(buildColumnDefaultNull("property_value", COLUMN_TYPE_INT_11, "year_stat"));
        columnList.add(buildColumnWithDefaultValue("create_by", COLUMN_TYPE_VARCHAR64, " ", "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
        columnList.add(buildColumnWithDefaultValue("update_by", COLUMN_TYPE_VARCHAR64, " ", "更新者"));
        columnList.add(buildColumnDefaultNull("update_time", COLUMN_TYPE_TIME, "更新时间"));
    }
}
