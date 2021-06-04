package com.panda.example.common.utils;

import lombok.extern.slf4j.Slf4j;



import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;


import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author yk
 * @version 1.0
 * @describe panda-example
 * @date 2020-06-04 17:53
 */
@Slf4j
public class ExcelUtils {


    /**
     * 获取并解析excel文件，返回一个二维集合
     * @param file 上传的文件
     * @return 二维集合（第一重集合为行，第二重集合为列，每一行包含该行的列集合，列集合包含该行的全部单元格的值）
     */
    public static List<List<String>> analysis(MultipartFile file) {
        if (file == null){
            log.error("analysis--传入的文件对象为空");
            return null;
        }
        List<List<String>> row = new ArrayList<>();
        //获取文件名称
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)){
            return null;
        }
        //获取输入流
        InputStream in = null;
        //判断excel版本
        Workbook workbook = null;
        try {
            //获取输入流
            in = file.getInputStream();
            if (judgeExcelEdition(fileName)) {
                workbook = new XSSFWorkbook(in);
            } else {
                workbook = new HSSFWorkbook(in);
            }

            //获取第一张工作表
            Sheet sheet = workbook.getSheetAt(0);
            //从第二行开始获取
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                //循环获取工作表的每一行
                Row sheetRow = sheet.getRow(i);
                //循环获取每一列
                ArrayList<String> cell = new ArrayList<>();
                for (int j = 0; j < sheetRow.getPhysicalNumberOfCells(); j++) {
                    //将每一个单元格的值装入列集合
                    cell.add(sheetRow.getCell(j).getStringCellValue());
                }
                //将装有每一列的集合装入大集合
                row.add(cell);
            }
            return row;
        } catch (FileNotFoundException e) {
            log.error("analysis--not found file！",e);
            return null;
        } catch (IOException e) {
            log.error("analysis--流处理异常！",e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (workbook != null) {
                    workbook.close();
                }
            }catch (IOException e){
                log.error("analysis--关闭流异常！",e);
            }
        }

    }
    /**
     * 判断上传的excel文件版本（xls为2003，xlsx为2017）
     * @param fileName 文件路径
     * @return excel2007及以上版本返回true，excel2007以下版本返回false
     */
    private static boolean judgeExcelEdition(String fileName){
        if (fileName.matches("^.+\\.(?i)(xls)$")){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 私有化构造方法
     */
    private ExcelUtils() {
    }

    /**
     * 读取 Excel(多个 sheet)
     */
    public static <T> List<T> readExcel(ExcelReader reader, Class<T> rowModel, int sheetCount)  {
        if (reader == null) {
            return new ArrayList<>();
        }
        try {
            List<ReadSheet> readSheetList = new ArrayList<>();
            ExcelListener<T> excelListener = new ExcelListener<>();
            ReadSheet readSheet = EasyExcel.readSheet(sheetCount)
                    .head(rowModel)
                    .registerReadListener(excelListener)
                    .build();
            readSheetList.add(readSheet);
            reader.read(readSheetList);
            return getExtendsBeanList(excelListener.getDataList(), rowModel);
        }finally {
            if (reader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                reader.finish();
            }
        }
    }



    /**
     * 读取 Excel(单个 sheet)
     * 将多sheet合并成一个list数据集，通过自定义ExcelReader继承AnalysisEventListener
     * 重写invoke doAfterAllAnalysed方法
     * getExtendsBeanList 主要是做Bean的属性拷贝 ，可以通过ExcelReader中添加的数据集直接获取
     */
    public static <T> List<T> readFirstSheetExcel(InputStream excel, Class<T> rowType) {
        ExcelReader reader = EasyExcel.read(excel).build();
        if (reader == null) {
            return new ArrayList<>();
        }
        return readExcel(reader, rowType, 0);
    }




    /**
     * 利用BeanCopy转换list
     */
    public static <T> List<T> getExtendsBeanList(List<?> list, Class<T> typeClazz) {
        List<T> targets = new ArrayList();
        if (CollectionUtils.isEmpty(list)) {
            return targets;
        }
        for (Object obj : list) {
            try {
                T target = typeClazz.newInstance();
                BeanUtils.copyProperties(obj, target);
                targets.add(target);
            } catch (Exception e) {
                log.error("ExcelUtils.getExtendsBeanList()..error", e);
            }
        }
        return targets;
    }

    private static class ExcelListener<T> extends AnalysisEventListener<T> {

        private final List<Object> dataList = new ArrayList<>();

        /**
         * 通过 AnalysisContext 对象还可以获取当前 sheet，当前行等数据
         */
        @Override
        public void invoke(T object, AnalysisContext context) {
            if (!checkObjAllFieldsIsNull(object)) {
                dataList.add(object);
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }

        private static final String SERIAL_VERSION_UID = "serialVersionUID";

        /**
         * 判断对象中属性值是否全为空
         */
        private static boolean checkObjAllFieldsIsNull(Object object) {
            if (null == object) {
                return true;
            }
            try {
                for (Field f : object.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    //只校验带ExcelProperty注解的属性
                    ExcelProperty property = f.getAnnotation(ExcelProperty.class);
                    if (property == null || SERIAL_VERSION_UID.equals(f.getName())) {
                        continue;
                    }
                    if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
                        return false;
                    }
                }
            } catch (Exception e) {
                log.error("ExcelListener.checkObjAllFieldsIsNull()..error", e);
            }
            return true;
        }

        public List<?> getDataList() {
            return dataList;
        }
    }

    /**
     * 根据类获取excel需要的FieldNames
     *
     * @param exportFieldNames
     * @param <T>
     * @return
     */
    public static <T> List<String> getMethodNames(Field[] exportFieldNames) {
        List<String> methodList = new ArrayList<>();
        for (Field field :
                exportFieldNames) {
            methodList.add(field.getName());
        }
        return methodList;
    }

    public static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell, String value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }


    public static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell) {
        cell.setCellStyle(style);
        return cell;
    }

    public static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell, RichTextString value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    public static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell, Date value) {
        cell.setCellStyle(style);
        if(value != null){
            cell.setCellValue(format(value, "yyyy-MM-dd HH:mm:ss"));
        }else{
            cell.setCellValue("");
        }
        return cell;
    }

    public static Cell setCellStyleWithStyleAndValue(CellStyle style, Cell cell, BigDecimal value) {
        cell.setCellStyle(style);
        if(value != null){
            cell.setCellValue(value.toString());
        }else{
            cell.setCellValue("");
        }
        return cell;
    }

    public static Cell setCellStyleWithValue(CellStyle style,Cell cell, Object value) {
        cell.setCellStyle(style);
        cell.setCellValue(value == null ? "" : value.toString());
        return cell;
    }

    public static Cell setCellStyleWithValue(CellStyle style,Cell cell, int value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    public static Cell setCellStyleWithValue(CellStyle style,Cell cell, double value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }

    public static Cell setCellStyleWithValue(CellStyle style,Cell cell, Date value) {
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return cell;
    }


    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     *
     * @param date    日期
     * @param pattern 格式，如：DateUtils.DATE_TIME_PATTERN
     * @return 返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
}
