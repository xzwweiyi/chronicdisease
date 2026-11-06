package com.dchealth.dynamic.templete;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.dynamic.DynamicConstant.*;
import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_TIME;

@Service
public class DynamicArchive extends DynamicBase {
    String TABLE_NAME_PRE = "archive_data_";


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
        columnList.add(buildColumnPrimaryKey("id", COLUMN_TYPE_BIGINT, true));
        columnList.add(buildColumnDefaultNull("archive_id", COLUMN_TYPE_BIGINT, "档案Id", true));
        columnList.add(buildColumnDefaultNull("research_id", COLUMN_TYPE_BIGINT, "队列Id", true));
        columnList.add(buildColumnDefaultNull("hospital_id", COLUMN_TYPE_BIGINT, "中心Id", true));
        columnList.add(buildColumnDefaultNull("dept_id", COLUMN_TYPE_BIGINT, "创建人所在科室ID", true));
        columnList.add(buildColumnDefaultNull("form_id", COLUMN_TYPE_BIGINT, "表单ID", true));
        columnList.add(buildColumnDefaultNull("property_id", COLUMN_TYPE_BIGINT, "属性Id", true));
        columnList.add(buildColumnDefaultNull("property_value", COLUMN_TYPE_VARCHAR256, "属性值"));
        columnList.add(buildColumnWithDefaultValue("create_by", COLUMN_TYPE_VARCHAR64, " ", "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
        columnList.add(buildColumnWithDefaultValue("update_by", COLUMN_TYPE_VARCHAR64, " ", "更新者"));
        columnList.add(buildColumnDefaultNull("update_time", COLUMN_TYPE_TIME, "更新时间"));
    }
}
