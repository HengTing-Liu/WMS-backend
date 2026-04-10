package com.abtk.product.common.utils.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 动态Excel导出工具
 * 支持基于Map数据和动态表头配置导出，无需实体类注解
 *
 * @author system
 */
public class DynamicExcelUtil {

    private static final Logger log = LoggerFactory.getLogger(DynamicExcelUtil.class);

    /** Excel sheet最大行数 */
    public static final int SHEET_SIZE = 65536;

    /** 默认日期格式 */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 导出配置项
     */
    public static class ExportField {
        /** 字段名（对应Map的key） */
        private String field;
        /** 表头名称 */
        private String title;
        /** 数据类型: string/int/bigint/decimal/date/datetime/boolean */
        private String dataType;
        /** 列宽（可选，默认120） */
        private Integer width;

        public ExportField() {}

        public ExportField(String field, String title) {
            this.field = field;
            this.title = title;
            this.dataType = "string";
            this.width = 120;
        }

        public ExportField(String field, String title, String dataType) {
            this.field = field;
            this.title = title;
            this.dataType = dataType != null ? dataType : "string";
            this.width = 120;
        }

        public ExportField(String field, String title, String dataType, Integer width) {
            this.field = field;
            this.title = title;
            this.dataType = dataType != null ? dataType : "string";
            this.width = width != null ? width : 120;
        }

        public String getField() { return field; }
        public void setField(String field) { this.field = field; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDataType() { return dataType; }
        public void setDataType(String dataType) { this.dataType = dataType; }
        public Integer getWidth() { return width; }
        public void setWidth(Integer width) { this.width = width; }
    }

    /**
     * 导出动态Excel（使用默认表头，从数据中推断字段）
     *
     * @param response HTTP响应
     * @param dataList 数据列表（List<Map>）
     * @param sheetName 工作表名称
     */
    public static void exportExcel(HttpServletResponse response, List<Map<String, Object>> dataList, String sheetName) {
        exportExcel(response, dataList, sheetName, null);
    }

    /**
     * 导出动态Excel
     *
     * @param response HTTP响应
     * @param dataList 数据列表
     * @param sheetName 工作表名称
     * @param fields 字段配置列表（为空则从第一条数据推断）
     */
    public static void exportExcel(HttpServletResponse response, List<Map<String, Object>> dataList,
                                    String sheetName, List<ExportField> fields) {
        if (dataList == null || dataList.isEmpty()) {
            dataList = new ArrayList<>();
        }

        // 如果没有指定字段，从第一条数据推断
        if (fields == null || fields.isEmpty()) {
            fields = inferFields(dataList.get(0));
        }

        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");

        SXSSFWorkbook wb = null;
        try {
            wb = new SXSSFWorkbook(500);
            Sheet sheet = wb.createSheet(sheetName);
            CellStyle headerStyle = createHeaderStyle(wb);
            CellStyle dataStyle = createDataStyle(wb);

            // 写入表头
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < fields.size(); i++) {
                ExportField field = fields.get(i);
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(field.getTitle());
                cell.setCellStyle(headerStyle);
                // 设置列宽
                sheet.setColumnWidth(i, (field.getWidth() != null ? field.getWidth() : 120) * 256);
            }

            // 写入数据
            int rowNum = 1;
            int sheetIndex = 0;
            Sheet currentSheet = sheet;
            for (Map<String, Object> rowData : dataList) {
                if (rowNum > 0 && rowNum % SHEET_SIZE == 0) {
                    // 超过单sheet限制，创建新sheet
                    sheetIndex++;
                    currentSheet = wb.createSheet(sheetName + "_" + sheetIndex);
                    // 写入表头到新sheet
                    Row newHeaderRow = currentSheet.createRow(0);
                    for (int i = 0; i < fields.size(); i++) {
                        ExportField field = fields.get(i);
                        Cell cell = newHeaderRow.createCell(i);
                        cell.setCellValue(field.getTitle());
                        cell.setCellStyle(headerStyle);
                        currentSheet.setColumnWidth(i, (field.getWidth() != null ? field.getWidth() : 120) * 256);
                    }
                    rowNum = 1;
                }

                Row row = currentSheet.createRow(rowNum);
                for (int i = 0; i < fields.size(); i++) {
                    ExportField field = fields.get(i);
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(dataStyle);
                    Object value = rowData.get(field.getField());
                    setCellValue(cell, value, field.getDataType());
                }
                rowNum++;
            }

            // 写入响应
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
        } catch (Exception e) {
            log.error("导出Excel异常: {}", e.getMessage());
        } finally {
            if (wb != null) {
                try {
                    wb.dispose();
                    wb.close();
                } catch (Exception e) {
                    log.error("关闭Workbook异常: {}", e.getMessage());
                }
            }
        }
    }

    /**
     * 导出动态Excel，支持多语言表头
     *
     * @param response HTTP响应
     * @param dataList 数据列表
     * @param sheetName 工作表名称
     * @param fieldTitleMap 字段名到表头的映射（支持多语言key到值的映射）
     * @param fieldDataTypeMap 字段名到数据类型的映射
     */
    public static void exportExcel(HttpServletResponse response, List<Map<String, Object>> dataList,
                                    String sheetName, Map<String, String> fieldTitleMap,
                                    Map<String, String> fieldDataTypeMap) {
        if (dataList == null || dataList.isEmpty()) {
            dataList = new ArrayList<>();
        }

        // 构建字段配置
        List<ExportField> fields = new ArrayList<>();
        for (Map.Entry<String, String> entry : fieldTitleMap.entrySet()) {
            ExportField field = new ExportField();
            field.setField(entry.getKey());
            field.setTitle(entry.getValue());
            field.setDataType(fieldDataTypeMap != null ? fieldDataTypeMap.get(entry.getKey()) : "string");
            fields.add(field);
        }

        exportExcel(response, dataList, sheetName, fields);
    }

    /**
     * 从第一条数据推断字段列表
     */
    private static List<ExportField> inferFields(Map<String, Object> sample) {
        List<ExportField> fields = new ArrayList<>();
        if (sample == null) return fields;

        for (Map.Entry<String, Object> entry : sample.entrySet()) {
            ExportField field = new ExportField();
            field.setField(entry.getKey());
            field.setTitle(entry.getKey());
            field.setDataType(inferDataType(entry.getValue()));
            fields.add(field);
        }
        return fields;
    }

    /**
     * 推断数据类型
     */
    private static String inferDataType(Object value) {
        if (value == null) return "string";
        if (value instanceof Integer || value instanceof Long) return "bigint";
        if (value instanceof BigDecimal) return "decimal";
        if (value instanceof Date) return "datetime";
        if (value instanceof Boolean) return "boolean";
        return "string";
    }

    /**
     * 设置单元格值
     */
    private static void setCellValue(Cell cell, Object value, String dataType) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            if ("decimal".equals(dataType)) {
                cell.setCellValue(((Number) value).doubleValue());
            } else {
                cell.setCellValue(((Number) value).longValue());
            }
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value ? "是" : "否");
        } else if (value instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "date".equals(dataType) ? DEFAULT_DATE_FORMAT : DEFAULT_DATETIME_FORMAT);
            cell.setCellValue(sdf.format((Date) value));
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * 创建表头样式
     */
    private static CellStyle createHeaderStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        Font font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        style.setFont(font);

        return style;
    }

    /**
     * 创建数据样式
     */
    private static CellStyle createDataStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        Font font = wb.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }
}
