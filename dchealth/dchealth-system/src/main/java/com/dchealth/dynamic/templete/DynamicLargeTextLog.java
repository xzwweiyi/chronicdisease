package com.dchealth.dynamic.templete;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dchealth.dynamic.DynamicConstant.*;

/**
 * 大文本
 */
@Service
public class DynamicLargeTextLog extends DynamicBase{

    String TABLE_NAME_PRE = "res_large_text_log_";

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
        columnList.add(buildColumnDefaultNull("record_id", COLUMN_TYPE_BIGINT, "档案Id", true));
        columnList.add(buildColumnDefaultNull("form_id", COLUMN_TYPE_BIGINT, "表单ID", true));
        columnList.add(buildColumnDefaultNull("type", COLUMN_TYPE_CHAR, "来源(1-患者，2-EDC)"));
        columnList.add(buildColumnDefaultNull("property_id", COLUMN_TYPE_BIGINT, "属性Id"));
        columnList.add(buildColumnDefaultNull("property_code", COLUMN_TYPE_VARCHAR64, "属性code"));
        columnList.add(buildColumnDefaultNull("property_value", COLUMN_TYPE_TEXT, "属性值"));
        columnList.add(buildColumnWithDefaultValue("create_by", COLUMN_TYPE_VARCHAR64, " ", "创建者"));
        columnList.add(buildColumnDefaultNull("create_time", COLUMN_TYPE_TIME, "创建时间"));
        columnList.add(buildColumnWithDefaultValue("update_by", COLUMN_TYPE_VARCHAR64, " ", "更新者"));
        columnList.add(buildColumnDefaultNull("update_time", COLUMN_TYPE_TIME, "更新时间"));
    }

    /**
     * 查询大文本信息
     * @param researchId
     * @param fromId
     * @return
     */
    public String buildSelectSql(Long researchId, Long fromId, Long recordId, Long propertyId, String propertyCode){
        StringBuilder builder = new StringBuilder();
        builder.append(" select log_id id, record_id recordId, form_id formId,property_id propertyId, property_code propertyCode, property_value propertyValue, create_by createBy, create_time createTime, update_by updateBy, update_time updateTime  from ");
        builder.append( TABLE_NAME_PRE + researchId);
        builder.append(" where ");
        builder.append(" form_id = " + fromId);
        builder.append(" and record_id = " + recordId);
        builder.append(" and property_id = " + propertyId);
        builder.append(" and property_code = '" + propertyCode + "'");
        return builder.toString();
    }


}
