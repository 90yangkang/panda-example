package com.panda.example.common.utils;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;

import java.io.*;

/**
 * @author yk
 * @version 1.0
 * @describe panda-example
 * @date 2016-06-07 18:46
 */
public class ExcelDemo {

    public void createExcel() {
        HSSFWorkbook wb = new HSSFWorkbook();//创建工作薄
        //头样式
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setFontName("宋体");
        font.setColor(HSSFColor.BLACK.index);

        HSSFCellStyle style = wb.createCellStyle();
        style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        style.setFont(font);

        HSSFSheet sheet = wb.createSheet("订单");//创建工作表，名称为test
        int iRow = 0;//行号
        HSSFRow row = sheet.createRow(iRow);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(new HSSFRichTextString("客户订单号："));
        cell.setCellStyle(style);
        //内容样式
        HSSFFont contentFont = wb.createFont();
        contentFont.setFontHeightInPoints((short) 9);
        contentFont.setFontName("Calibri");
        HSSFCellStyle contentstyle = wb.createCellStyle();
        contentstyle.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        contentstyle.setFont(contentFont);

        //第一行内容
        int iRow2 = 2;//行号
        HSSFRow row2 = sheet.createRow(iRow2);
        HSSFCell cell2 = row2.createCell((short) 0);
        cell2.setCellValue(new HSSFRichTextString("客户卡号/名称："));
        cell2.setCellStyle(contentstyle);

        HSSFCell cell22 = row2.createCell((short) 1);
        cell22.setCellValue(new HSSFRichTextString("1002030"));
        cell22.setCellStyle(contentstyle);

        HSSFCell cell27 = row2.createCell((short) 7);
        cell27.setCellValue(new HSSFRichTextString("预订单号："));
        cell27.setCellStyle(contentstyle);

        HSSFCell cell28 = row2.createCell((short) 8);
        cell28.setCellValue(new HSSFRichTextString("NO:11223.3"));
        cell28.setCellStyle(contentstyle);

        //第二行内容
        int iRow3 = 3;//行号
        HSSFRow row3 = sheet.createRow(iRow3);
        HSSFCell cell3 = row3.createCell((short) 0);
        cell3.setCellValue(new HSSFRichTextString("持卡人："));
        cell3.setCellStyle(contentstyle);

        HSSFCell cell32 = row3.createCell((short) 1);
        cell32.setCellValue(new HSSFRichTextString("Candy"));
        cell32.setCellStyle(contentstyle);

        HSSFCell cell37 = row3.createCell((short) 7);
        cell37.setCellValue(new HSSFRichTextString("送货日期："));
        cell37.setCellStyle(contentstyle);

        HSSFCell cell38 = row3.createCell((short) 8);
        cell38.setCellValue(new HSSFRichTextString("2015-06-12"));
        cell38.setCellStyle(contentstyle);

        //第三行内容
        int iRow4 = 4;//行号
        HSSFRow row4 = sheet.createRow(iRow4);
        HSSFCell cell4 = row4.createCell((short) 0);
        cell4.setCellValue(new HSSFRichTextString("收货地址："));
        cell4.setCellStyle(contentstyle);

        HSSFCell cell42 = row4.createCell((short) 1);
        cell42.setCellValue(new HSSFRichTextString("city shanghai"));
        cell42.setCellStyle(contentstyle);

        HSSFCell cell47 = row4.createCell((short) 7);
        cell47.setCellValue(new HSSFRichTextString("支付方式：："));
        cell47.setCellStyle(contentstyle);

        HSSFCell cell48 = row4.createCell((short) 8);
        //货到付款刷卡
        cell48.setCellValue(new HSSFRichTextString("货到付款刷卡"));
        cell48.setCellStyle(contentstyle);

        //第四行内容
        int iRow5 = 5;//行号
        HSSFRow row5 = sheet.createRow(iRow5);
        HSSFCell cell5 = row5.createCell((short) 0);
        cell5.setCellValue(new HSSFRichTextString("订单备注："));
        cell5.setCellStyle(contentstyle);

        HSSFCell cell52 = row5.createCell((short) 0);
        cell52.setCellValue(new HSSFRichTextString("orderRemark"));
        cell52.setCellStyle(contentstyle);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try {
            wb.write(os);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //String urlString = qiniuUtils.uploadFileBybyte(os.toByteArray(), "文件名", false);

        byte[] xls = os.toByteArray();
        File file = new File("D:\\demo.xlsx");
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            try {
                out.write(xls);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
