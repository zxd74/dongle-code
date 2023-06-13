package file.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

/**
 * @author Dongle
 * @desc  Excel文件读取，适用于.xls和.xlsx文件类型
 * @since 2021/12/10 13:49
 */
public class ExcelPoiUtils {

    public static void readExcel(String path){
        FileInputStream fis = null;
        try {
            File file = new File(path);
            if (!file.exists()){
                System.out.println("Not Find File!" + path);
                return;
            }
            fis = new FileInputStream(file);
            Workbook workbook = null;
            if (path.endsWith("xls")){
                workbook = new HSSFWorkbook(fis);
            }else if(path.endsWith("xlsx")){
                workbook = new XSSFWorkbook(fis);
            }
            if (workbook == null){
                return;
            }
            // 循环工作表sheet
            for(int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                if (numSheet != 5){
                    continue;
                }
                Sheet sheet = workbook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                // 循环row，如果第一行是字段，则 numRow = 1
                for (int numRow = 1; numRow <= sheet.getLastRowNum(); numRow++) {
                    Row row = sheet.getRow(numRow);
                    if (row == null) {
                        continue;
                    }
                    // 循环cell
                    // 注意：row.getPhysicalNumberOfCells() 和 row.getLastCellNum() 对应真实cell可能存在误差
                    // 当单元格是未知空值是，可能会造成获取物理列及最后列数不匹配问题，也有可能会和实际列对不上
                    // cell.getCellType():
                    //                    CellType.NUMERIC: // 数字
                    //                    CellType.STRING: // 字符串
                    //                    CellType.BOOLEAN: // Boolean
                    //                    CellType.FORMULA: // 公式
                    //                    CellType.BLANK: // 空值
                    //                    CellType.ERROR: // 故障

                    for (int numCell = 0; numCell < row.getLastCellNum(); numCell++) {
                        Cell cell = row.getCell(numCell);
                        if (cell == null) {
                            continue;
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(){
        // 表格
        Workbook workbook = new HSSFWorkbook();
        // Sheet，可创建多个
        Sheet sheet = workbook.createSheet(appName);
        // 首行
        Row row = sheet.createRow(0);
        // 列
        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("描述");

        for (int i = 0; i < 10; i++) {
            Row valueRow = sheet.createRow(i + 1);
            valueRow.createCell(0).setCellValue(i);
            valueRow.createCell(1).setCellValue("desc_" + i);
        }
        // 输出
        OutputStream ops = new FileOutPutStream(new File(""));
        workbook.write(ops);
    }
}