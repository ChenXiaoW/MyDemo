package ink.cwblog.poiexport.util;

import ink.cwblog.poiexport.enums.ExcelTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author chenw
 * @date 2021/7/23 13:46
 *
 * excel工具类
 */
@Slf4j
public class ExcelUtil {


    /**
     * 创建excel
     *
     * @param data
     * @param savePath
     * @param fileName
     * @return
     */
    public static String createExcel(@Valid @NotEmpty(message = "excel数据不能为空") List data,
                                     @NotBlank(message = "excel存储路径不能为空") String savePath,
                                     @NotBlank(message = "excel文件名不能为空") String fileName, @NotBlank ExcelTypeEnums excelTypeEnums){
        try {
            Workbook workbook = (Workbook)Class.forName(excelTypeEnums.getClassName()).newInstance();
            //创建表格
            Sheet sheet = workbook.createSheet();
            Row headRow = sheet.createRow(0);
            String[] fieldsNames = getFieldsName(data.get(0).getClass());
            //设置表头
            for (int i = 0;i< fieldsNames.length;i++){
                headRow.createCell(i).setCellValue(fieldsNames[i]);
            }
            //填充数据
            Method [] methods = new Method[fieldsNames.length];
            for (int i = 0; i<data.size();i++){
                Row row = sheet.createRow(i+1);
                Object obj = data.get(i);
                for (int j = 0; j < fieldsNames.length; j++) {
                    //加载第一行数据时，初始化所有属性的getter方法
                    if(i == 0){
                        String fieldName = fieldsNames[j];
                        //处理布尔值命名 "isXxx" -> "setXxx"
                        if (fieldName.contains("is")) {
                            fieldName = fieldName.split("is")[1];
                        }
                        methods[j] = obj.getClass().getMethod("get" +
                                fieldName.substring(0,1).toUpperCase() +
                                fieldName.substring(1));
                    }
                    Cell cell = row.createCell(j);
                    Object value = methods[j].invoke(obj);
                    //注意判断 value 值是否为空
                    if(value == null){
                        value = "无";
                    }
                    cell.setCellValue(value.toString());
                }
            }
            String fileSavePath = savePath+File.separator+fileName+"."+excelTypeEnums.getSuffix();
            log.info("文件保存路径:{}",fileSavePath);
            File file = new File(fileSavePath);
            if(!file.exists()){
                if (file.createNewFile()){
                    log.info("创建文件成功");
                }else {
                    log.error("创建文件失败");
                }
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return fileSavePath;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean createSheet(Sheet sheet,@Valid @NotEmpty(message = "excel数据不能为空") List data){
        try {
            //创建表格

            Row headRow = sheet.createRow(0);
            String[] fieldsNames = getFieldsName(data.get(0).getClass());
            //设置表头
            for (int i = 0;i< fieldsNames.length;i++){
                headRow.createCell(i).setCellValue(fieldsNames[i]);
            }
            //填充数据
            Method [] methods = new Method[fieldsNames.length];
            for (int i = 0; i<data.size();i++){
                Row row = sheet.createRow(i+1);
                Object obj = data.get(i);
                for (int j = 0; j < fieldsNames.length; j++) {
                    //加载第一行数据时，初始化所有属性的getter方法
                    if(i == 0){
                        String fieldName = fieldsNames[j];
                        //处理布尔值命名 "isXxx" -> "setXxx"
                        if (fieldName.contains("is")) {
                            fieldName = fieldName.split("is")[1];
                        }
                        methods[j] = obj.getClass().getMethod("get" +
                                fieldName.substring(0,1).toUpperCase() +
                                fieldName.substring(1));
                    }
                    Cell cell = row.createCell(j);
                    Object value = methods[j].invoke(obj);
                    //注意判断 value 值是否为空
                    if(value == null){
                        value = "无";
                    }
                    cell.setCellValue(value.toString());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 根据对象获取其属性名
     * @param clazz
     * @return
     */
    private static String[] getFieldsName(Class clazz){
        Field[] declaredFields = clazz.getDeclaredFields();
        String [] fileNames = new String[declaredFields.length];
        for (int i = 0;i<declaredFields.length;i++){
            fileNames[i] = declaredFields[i].getName();
        }
        return fileNames;
    }

}
