package com.dchealth.dynamic.templete;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.dynamic.DynamicConstant.*;

@Service
public class DynamicPatientFileLog extends DynamicBase{
    String TABLE_NAME_PRE = "patient_files_log_";

    @Override
    public void buildDynamicTableName() {
        tableNamePre = TABLE_NAME_PRE;
        List<String> tableNames = dynamicTableMapper.querySamePreTable(TABLE_NAME_PRE);
        samePreTables.addAll(tableNames.stream().filter(s -> isNum(s.replace(TABLE_NAME_PRE, "")))
                .collect(Collectors.toList()));
    }

    @Override
    public void buildDynamicColumn() {
        if (CollectionUtils.isNotEmpty(samePreTables)){
            return;
        }
        columnList.add(buildColumnPrimaryKey("log_id", COLUMN_TYPE_BIGINT, true));
        columnList.add(buildColumnDefaultNull("image_url", COLUMN_TYPE_VARCHAR64, "图片地址"));
        columnList.add(buildColumnDefaultNull("patient_id", COLUMN_TYPE_BIGINT, "患者id", true));
        columnList.add(buildColumnDefaultNull("archive_id", COLUMN_TYPE_BIGINT, "档案id", true));
        columnList.add(buildColumnDefaultNull("cate_type", COLUMN_TYPE_VARCHAR64, "图片一级类型"));
        columnList.add(buildColumnDefaultNull("image_type", COLUMN_TYPE_BIGINT, "二级分类"));
        columnList.add(buildColumnDefaultNull("create_by", COLUMN_TYPE_VARCHAR64,  "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
    }
}
