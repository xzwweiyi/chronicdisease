package com.dchealth.dynamic.templete;

import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_BIGINT;
import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_TIME;
import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_VARCHAR256;
import static com.dchealth.dynamic.DynamicConstant.COLUMN_TYPE_VARCHAR64;

import org.springframework.stereotype.Service;

import com.dchealth.common.utils.StringUtils;

@Service
public class DynamicPatient extends DynamicBase {
    String TABLE_NAME_PRE = "patient_";


    @Override
    public void buildDynamicTableName() {
        tableNamePre = TABLE_NAME_PRE;
        String order = dynamicTableMapper.queryTableOrder(TABLE_NAME_PRE);
        if (StringUtils.isEmpty(order)) {
            createTimes = 1;
        } else {
            createTimes = Integer.parseInt(order) + 1;
        }
        tableName = TABLE_NAME_PRE + createTimes;
    }

    @Override
    public void buildDynamicColumn() {
        columnList.add(buildColumnPrimaryKey("id", COLUMN_TYPE_BIGINT, true));
        columnList.add(buildColumnDefaultNull("archive_id", COLUMN_TYPE_BIGINT, "档案Id", true));
        columnList.add(buildColumnDefaultNull("research_id", COLUMN_TYPE_BIGINT, "队列Id", true));
        columnList.add(buildColumnDefaultNull("hospital_id", COLUMN_TYPE_BIGINT, "中心Id", true));
        columnList.add(buildColumnDefaultNull("property_id", COLUMN_TYPE_BIGINT, "属性Id"));
        columnList.add(buildColumnDefaultNull("property_value", COLUMN_TYPE_VARCHAR256, "属性值"));
        columnList.add(buildColumnWithDefaultValue("create_by", COLUMN_TYPE_VARCHAR64, " ", "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
        columnList.add(buildColumnWithDefaultValue("update_by", COLUMN_TYPE_VARCHAR64, " ", "更新者"));
        columnList.add(buildColumnDefaultNull("update_time", COLUMN_TYPE_TIME, "更新时间"));
    }
}
