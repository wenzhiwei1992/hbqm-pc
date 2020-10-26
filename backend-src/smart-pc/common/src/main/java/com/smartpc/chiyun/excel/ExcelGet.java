package com.smartpc.chiyun.excel;

import com.alibaba.fastjson.JSON;
import com.smartpc.chiyun.utils.FileUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ExcelGet {
    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式


        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }


    /**
     * @param excelName
     * @param excelData
     * @param rowIndex  数据列从第几行开始 0 1 2 3
     * @return
     * @throws Exception
     */
    public static Workbook writeExcel(String excelName, String[][] excelData, int rowIndex) throws Exception {
        // 根据 File 文件类创建 Workbook 工作簿类
        FileInputStream fis = new FileInputStream(excelName);
        Workbook workbook = new XSSFWorkbook(fis);
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.getSheetAt(0);
        //格式基准行  后续格子的样式全部拷贝这行的样式  完了再把这行删除 （工具行）
        Row row1 = sheet.getRow(rowIndex);
        //从第几行插入进去
        int insertStartPointer = rowIndex + 1;
        /* 开始逐行遍历 */
        for (int rowNum = insertStartPointer, i = 0; i < excelData.length; rowNum++, i++) {
            // 获取新的没有数据的一列
            //Cell cell1 = row1.getCell(i);
            Row row = ExcelUtil.createRow(sheet, insertStartPointer + i);
            // 拿到当前行数据
            String[] rowData = excelData[i];
            //System.out.println("rowData.length = " + rowData.length);
            /* 开始逐列遍历 */
            for (int columnNum = 0, j = 0; j < rowData.length; columnNum++, j++) {
                Cell cell1 = row1.getCell(j);
                // 单元格存储数据
                Cell cell = row.createCell(columnNum);
                CellStyle cellStyle = cell.getRow().getSheet().getWorkbook().createCellStyle();
                cellStyle.cloneStyleFrom(cell1.getCellStyle());
                cell.setCellStyle(cellStyle);
                cell.setCellValue(rowData[j]);
                System.out.println(1);
            }
        }
        //删除指定工具行的数据
        ExcelUtil.removeRow(sheet, rowIndex);
        return workbook;
    }

    public static Workbook writeExcels(String excelName, String[][] excelData, int rowIndex) throws Exception {
        // 根据 File 文件类创建 Workbook 工作簿类
        FileInputStream fis = new FileInputStream(excelName);
        Workbook workbook = new XSSFWorkbook(fis);
        // 根据 Workbook 获取当前 Sheet 页
        Sheet sheet = workbook.getSheetAt(0);
        //格式基准行  后续格子的样式全部拷贝这行的样式  完了再把这行删除 （工具行）
        Row row1 = sheet.getRow(rowIndex);
        //从第几行插入进去
        int insertStartPointer = rowIndex + 1;
        /* 开始逐行遍历 */
        for (int rowNum = insertStartPointer, i = 0; i < excelData.length; rowNum++, i++) {
            // 获取新的没有数据的一列
            Row row = ExcelUtil.createRow(sheet, insertStartPointer + i);
            sheet.addMergedRegion(new CellRangeAddress(insertStartPointer + i, insertStartPointer + i, 4, 5));//第11行第5个单元格至第6个单元格合并
            sheet.addMergedRegion(new CellRangeAddress(insertStartPointer + i, insertStartPointer + i, 6, 7));//7-8
            sheet.addMergedRegion(new CellRangeAddress(insertStartPointer + i, insertStartPointer + i, 8, 9));//9-10
            // 拿到当前行数据
            String[] rowData = excelData[i];
            //System.out.println("rowData.length = " + rowData.length);
            /* 开始逐列遍历 */
            for (int columnNum = 0, j = 0; j < rowData.length; columnNum++, j++) {
                Cell cell1 = row1.getCell(j);
                // 单元格存储数据
                Cell cell = row.createCell(columnNum);
                CellStyle cellStyle = cell.getRow().getSheet().getWorkbook().createCellStyle();
                cellStyle.cloneStyleFrom(cell1.getCellStyle());
                cell.setCellStyle(cellStyle);
                cell.setCellValue(rowData[j]);
            }
        }
        //删除指定工具行的数据
        ExcelUtil.removeRow(sheet, rowIndex);
        return workbook;
    }

    public static void exportExcel(Workbook workBook, String fileName) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        OutputStream os = null;
        try {
            FileUtil.fileResponse(response, fileName);
            os = response.getOutputStream();
            workBook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void exportExcel(String sheetName, String[] title, String[][] values, HSSFWorkbook wb, String fileName) {
        HSSFWorkbook hssfWorkbook = getHSSFWorkbook(sheetName, title, values, wb);
        exportExcel(hssfWorkbook, fileName);
    }


}
