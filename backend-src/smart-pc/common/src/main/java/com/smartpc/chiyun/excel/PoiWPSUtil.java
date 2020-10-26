package com.smartpc.chiyun.excel;


import com.smartpc.chiyun.utils.FileUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PoiWPSUtil {

//该文件工具类不全  需要抽时间补全   全局搜合格证  把合格证用的工具类移到该文件下补全  需要重新测试合格证打印

    /**
     * @param excelName
     * @param excelData
     * @param rowIndex  数据列从第几行开始 0 1 2 3
     * @return
     * @throws Exception
     */
    public static XWPFDocument writeWPS(String excelName, String[][] excelData, Map<String, Object> params, int rowIndex) throws Exception {

        // 根据 File 文件类创建 Workbook 工作簿类
        FileInputStream fis = new FileInputStream(excelName);
        XWPFDocument xdoc = new XWPFDocument(fis);

        //替换段落中的占位符信息
        replaceInPara(xdoc, params);

        XWPFTable table = xdoc.getTables().get(0);
        //向table中添加数据信息
        insertTable(table, excelData, rowIndex);

        return xdoc;
    }


    /**
     * 为表格插入数据，行数不够添加新行
     *
     * @param table     需要插入数据的表格
     * @param tableList 插入数据集合
     * @param rowIndex  表格数据列从第几行开始 0 1 2 3
     */
    public static void insertTable(XWPFTable table, String[][] tableList, int rowIndex) {

        for (int i = 0; i < tableList.length; i++) {//遍历要添加的数据的list
            XWPFTableRow newRow = table.insertNewTableRow(i + rowIndex);//为表格添加行
            String[] strings = tableList[i];//获取list中的字符串数组
            for (int j = 0; j < strings.length; j++) {//遍历list中的字符串数组
                String strings1 = strings[j];
                newRow.createCell();//在新增的行上面创建cell

                XWPFTableCell cell = newRow.getCell(j);
                /** 设置水平居中 */
                CTTc cttc = cell.getCTTc();
                CTTcPr ctPr = cttc.addNewTcPr();
                ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);

                cell.setText(strings1);//给每个cell赋值。

            }
        }
    }


    /**
     * 替换段落里面的变量
     *
     * @param doc    要替换的文档
     * @param params 参数
     */
    private static void replaceInPara(XWPFDocument doc, Map<String, Object> params) {
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInPara(para, params);
        }
    }

    /**
     * 替换段落里面的变量
     *
     * @param para   要替换的段落
     * @param params 参数
     */
    private static void replaceInPara(XWPFParagraph para, Map<String, Object> params) {
        List<XWPFRun> runs;
        Matcher matcher;
        if (matcher(para.getParagraphText()).find()) {
            runs = para.getRuns();
            for (int i = 0; i < runs.size(); i++) {
                XWPFRun run = runs.get(i);
                String runText = run.toString();
                matcher = matcher(runText);
                ///System.out.println(runText);
                if (matcher.find()) {
                    ///System.out.println("进1");
                    while ((matcher = matcher(runText)).find()) {
                        runText = matcher.replaceFirst(String.valueOf(params.get(matcher.group(1))));
                    }
                    //直接调用XWPFRun的setText()方法设置文本时，在底层会重新创建一个XWPFRun，把文本附加在当前文本后面，
                    //所以我们不能直接设值，需要先删除当前run,然后再自己手动插入一个新的run。
                    para.removeRun(i);
                    para.insertNewRun(i).setText(runText);
                }
            }
        }
    }

    /**
     * 正则匹配字符串
     *
     * @param str
     * @return
     */
    private static Matcher matcher(String str) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(str);
    }



    public static void exportWps(XWPFDocument workBook, String fileName){
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        OutputStream os = null;
        try {
            FileUtil.fileResponse(response, fileName);
            os = response.getOutputStream();
            workBook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
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


}
