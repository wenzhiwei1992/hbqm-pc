package com.smartpc.chiyun.excel;

import com.smartpc.chiyun.utils.ArithUtil;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {
    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";
    public static final String EMPTY = "";
    public static final String POINT = ".";
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 获得path的后缀名
     *
     * @param path
     * @return
     */
    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());
        }
        return EMPTY;
    }

    /**
     * 单元格格式
     *
     * @param hssfCell
     * @return
     */
    @SuppressWarnings({"static-access", "deprecation"})
    public static String getHValue(HSSFCell hssfCell) {
        if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(hssfCell.getBooleanCellValue());
        } else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            if (HSSFDateUtil.isCellDateFormatted(hssfCell)) {
                Date date = HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue());
                cellValue = sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("#.##");
                cellValue = df.format(hssfCell.getNumericCellValue());
                String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
                if (strArr.equals("00")) {
                    cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                }
            }
            return cellValue;
        } else {
            return String.valueOf(hssfCell.getStringCellValue());
        }
    }

    /**
     * 单元格格式
     *
     * @param xssfCell
     * @return
     */
    public static String getXValue(XSSFCell xssfCell) {
        if (xssfCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        } else if (xssfCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            String cellValue = "";
            //if (XSSFDateUtil.isCellDateFormatted(xssfCell)) {
            //   Date date = XSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());
            //   cellValue = sdf.format(date);
            // } else {);
            DecimalFormat df = new DecimalFormat("##.#####");
            cellValue = df.format(xssfCell.getNumericCellValue());
            String strArr = cellValue.substring(cellValue.lastIndexOf(POINT) + 1, cellValue.length());
            if (strArr.equals("00")) {
                cellValue = cellValue.substring(0, cellValue.lastIndexOf(POINT));
                //  }
            }
            return cellValue;
        } else {
            return String.valueOf(xssfCell.getStringCellValue());
        }

    }

    /**
     * @param cell
     * @return String
     * 获取单元格中的值(poi解析excel为数字时科学技术法问题)
     */
    public static String getCellVal(Cell cell) {
        String val = null;
        //处理读取表格为null 直接默认赋值""字符串
        if (null == cell) {
            val = "";
        }
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); //日期格式yyyy-mm-dd
        //DecimalFormat df = new DecimalFormat("0");
        // DecimalFormat dft = new DecimalFormat("0.000");
        BigDecimal sum = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP); //数字格式，防止长数字成为科学计数法形式，或者int变为double形式
        List<ArrayList<String>> strLists = new ArrayList<ArrayList<String>>();  //存放Excel中的数据
        if (null != cell) {
            switch (cell.getCellType()) {
                case XSSFCell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        //日期型
                        val = fmt.format(cell.getDateCellValue());
                    } else {
                        // val = df.format(cell.getNumericCellValue()); //数字型
                        //数字型
                        val = String.valueOf(ArithUtil.roundDouble(cell.getNumericCellValue(), 3));
                    }
                    break;
                //文本类型
                case XSSFCell.CELL_TYPE_STRING:
                    //.replaceAll("[\\s\\u00A0]+","").trim() 去除字段空格
                    val = cell.getStringCellValue().replaceAll("[\\s\\u00A0]+", "").trim();
                    ;
                    break;
                //布尔型
                case XSSFCell.CELL_TYPE_BOOLEAN:
                    val = String.valueOf(cell.getBooleanCellValue());
                    break;
                //空白
                case XSSFCell.CELL_TYPE_BLANK:
                    val = cell.getStringCellValue();
                    break;
                //错误
                case XSSFCell.CELL_TYPE_ERROR:
                    val = "错误";
                    break;
                //公式
                case XSSFCell.CELL_TYPE_FORMULA:
                    try {
                        val = String.valueOf(cell.getStringCellValue());
                    } catch (IllegalStateException e) {
                        val = String.valueOf(ArithUtil.roundDouble(cell.getNumericCellValue(), 3));
                    }
                    break;
                default:
                    val = cell.getRichStringCellValue() == null ? null : cell.getRichStringCellValue().toString();
            }
        }
        return val;
    }

    /**
     * 找到需要插入的行数，并新建一个POI的row对象
     * Row  XSSFRow 的区别
     *
     * @param sheet
     * @param rowIndex
     * @return
     */
    public static Row createRow(Sheet sheet, Integer rowIndex) {
        Row row = null;
        if (sheet.getRow(rowIndex) != null) {
            int lastRowNo = sheet.getLastRowNum();
            sheet.shiftRows(rowIndex, lastRowNo, 1);
        }
        row = sheet.createRow(rowIndex);
        return row;
    }



    /**
     * 删除指定行
     *
     * @param sheet
     * @param rowIndex
     */
    public static void removeRow(Sheet sheet, int rowIndex) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowIndex >= 0 && rowIndex < lastRowNum)
            // 将行号为rowIndex+1一直到行号为lastRowNum的单元格全部上移一行，以便删除rowIndex行
            sheet.shiftRows(rowIndex + 1, lastRowNum, -1, true, false);
            Row removing = sheet.getRow(rowIndex);
        if (removing != null)
            sheet.removeRow(removing);
        if (rowIndex == lastRowNum) {
            Row removingRow = sheet.getRow(rowIndex);
            if (removingRow != null)
                sheet.removeRow(removingRow);
        }
    }


}
