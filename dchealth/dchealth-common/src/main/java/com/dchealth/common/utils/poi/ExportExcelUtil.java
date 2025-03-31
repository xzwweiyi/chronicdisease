package com.dchealth.common.utils.poi;

import com.dchealth.common.annotation.Excel;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;

public abstract class ExportExcelUtil<T> extends ExcelUtil<T>{
    private static final Logger log = LoggerFactory.getLogger(ExportExcelUtil.class);

    public ExportExcelUtil(Workbook workbook, String sheetName){
        this.wb = workbook;
        this.sheetName = sheetName;
    }

    public ExportExcelUtil(Class<T> clazz, Workbook wb){
        this.clazz = clazz;
        this.wb = wb;
    }

    public abstract void setUpSheet(List<T> list);

    public void setUpSheet(List<T> list,String sheetName,Integer index){
        this.initExportMoreSheet(list,sheetName);
        this.sheetIndex = index;
        setUpSheet();
    }

    public void setUpSheet(List<T> list,String sheetName,Integer index,boolean isAppend){
        this.initExportMoreSheet(list,sheetName);
        this.sheetIndex = index;
        setUpSheet(isAppend);
    }

    /**
     * 追加列标头
     */
    public abstract void appendCellHeard(Row row,int column);

    /**
     * 追加值
     * @param row 当前行
     * @param vo 当前数据对象
     */
    public abstract void appendCellValue(Row row,int column,T vo);

    /**
     * 设置表格
     */
    public void setUpSheet(){
        setUpSheet(false);
    }


    /**
     * 可追加表格
     * @param isAppend
     */
    public void setUpSheet(boolean isAppend)
    {
        try
        {
            // 取出一共有多少个sheet.
            createSheet(0, sheetIndex);

            // 产生一行
            Row row = sheet.createRow(0);
            int column = 0;
            // 写入各个字段的列头名称
            for (Object[] os : fields)
            {
                Excel excel = (Excel) os[1];
                this.createCell(excel, row, column++);
            }
            if (isAppend){
                appendCellHeard(row,column);
            }
            if (Excel.Type.EXPORT.equals(type))
            {
                fillExcelData(0, row,isAppend);
            }
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
        }
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     */
    public void fillExcelData(int index, Row row,boolean isAppend)
    {
        if (row == null)
            row = sheet.getRow(0);
        int startNo = index * SHEET_SIZE;
        int endNo = Math.min(startNo + SHEET_SIZE, list.size());
        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            T vo = (T) list.get(i);
            int column = 0;
            for (Object[] os : fields)
            {
                Field field = (Field) os[0];
                Excel excel = (Excel) os[1];
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                this.addCell(excel, row, vo, field, column++);
            }
            if (isAppend){
                appendCellValue(row,column,vo);
            }
        }
    }
}
