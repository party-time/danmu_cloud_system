package cn.partytime.util;

import cn.partytime.common.util.ListUtils;
import cn.partytime.model.DanmuLogicModel;
import cn.partytime.model.danmu.DanmuPool;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExcelUtil {


    /**
     *
     * @param fileName 文件名
     * @param sheetName sheet名
     * @param headList 头标题
     * @param dataArray 数据内容(二维数组)
     */
    public static void createFile(String fileName,String sheetName,List<String> headList, String[][] dataArray){
        // 第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        HSSFRow row = sheet.createRow((int) 0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        HSSFCell cell = null;
        for(int i=0; i<headList.size(); i++){
            cell = row.createCell((short) i);
            cell.setCellValue(headList.get(i));
            cell.setCellStyle(style);
        }
        FileOutputStream fileOutputStream = null;
        try {
            for(int i = 0; i < dataArray.length; i++){
                row = sheet.createRow((int) i + 1);
                for(int j = 0; j < dataArray[i].length; j++){
                    row.createCell((short) 1).setCellValue(dataArray[i][j]);
                }
            }
            fileOutputStream = new FileOutputStream(fileName);
            wb.write(fileOutputStream);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fileOutputStream!=null){
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
