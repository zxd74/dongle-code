# CsvUtils
```java
public class CsvUtils {

    private static void readCsv(String path){
        try{
			if(!path.endsWith(".csv")){
				System.out.println("Not .csv File!" + path);
				return;
			}
            File file = new File(path);
            if (!file.exists()){
                System.out.println("Not Find File!" + path);
                return;
            }
			// 推荐，有编码格式处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			// BufferedReader reader = new BufferedReader(new FileReader(path)); // new BufferedReader(new FileReader(file));

            // 只有一个sheet工作表，不支持多个
            String line = null;
            int num = 0;

            while((line= reader.readLine())!=null){
                num ++;
                //CSV格式文件为逗号分隔符文件，这里根据逗号切分
                String[] item = line.split(",");
				// TODO 处理数据
                System.out.println("row:" + num + ",cell:"+ item.length + ",content:" + line);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
```
# EasyExcel
```java
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;

public class DataHandler {

    public static List<Data> exportByEasyExcel(String path){
        List<Data> dataList = new ArrayList<>();
        File file = new File(path);
        if (!file.exists()){
            System.out.println("WARN:File is not found!");
            return dataList;
        }
		// file 指定读取文件，必需，可合并head,registerReadListener配置
		// head代表转换类型，必需
		// sheet代表读取指定工作表
		// registerReadListener 实现ReeadListeener接口，读取单行数据，必需
        EasyExcel.read(file).head(Data.class).sheet(0).registerReadListener(new EasyExcelReadListener(dataList)).doRead();
        //EasyExcel.read(file,Data.class,new EasyExcelReadListener(dataList)).sheet(0).doRead();
        return dataList;
    }
	private static class EasyExcelReadListener implements ReadListener<Data> {
        List<Data> dataList;

        public EasyExcelReadListener(List<Data> dataList) {
            this.dataList = dataList;
        }

		// 读取单行数据
        @Override
        public void invoke(Data data, AnalysisContext context) {
            dataList.add(data);
        }
		
		// 读取结束操作
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
        }
    }
	
	public class Data {
		
		// 通过ExcelProperty 绑定列信息，可通过index列索引，或者name列名等绑定，推荐使用同一种方式
		@ExcelProperty("app_name")
		private String appName;
		
		public String getAppName() {
			return appName;
		}
	}
}
```
# Poi
```java
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
```
# EasyPoi
```java
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.List;

/**
 * @author Dongle
 * @desc
 * @since 2021/12/10 13:49
 */
public class ExcelReadDemo {

    public static void readExcel(String path){
        try {
            File file = new File(path);
            if (!file.exists()){
                System.out.println("not find file!" + path);
                return;
            }
            InputStream is = new FileInputStream(file);
            ImportParams importParams = new ImportParams();
            //设置有一行行头 列名只能在行头搜索
            importParams.setHeadRows(1);
            List<Data> dataList = ExcelImportUtil.importExcel(is, Data.class, importParams);
            if (CollectionUtils.isEmpty(dataList)){
                System.out.println("file not find data!" + path);
                return;
            }
            System.out.println("file find data size:" + dataList.size());
            dataList.forEach(data -> {
                System.out.println(JSON.toJSONString(data));
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

	public static class Data {

		@Excel(name = "APP",isImportField = "true")
		private String appName;
		@Excel(name ="位置",isImportField = "true")
		private String posName;
		@Excel(name ="平台",isImportField = "true")
		private String platform;
		@Excel(name ="代码位名称",isImportField = "true")
		private String platformPosName;
		@Excel(name ="广告",isImportField = "true")
		private String ad;
		@Excel(name ="SDKID",isImportField = "true")
		private String sdkId;
		@Excel(name ="代码位ID",isImportField = "true")
		private String platformPosKey;
		@Excel(name ="AppKey ID",isImportField = "true")
		private String platformAppKey;
		@Excel(name ="TOPON appkey",isImportField = "true")
		private String appKey;
		@Excel(name ="TOPON appid",isImportField = "true")
		private String appId;
		@Excel(name ="TOPON 广告位ID",isImportField = "true")
		private String posId;

		public String getAppName() {
			return appName;
		}

		public void setAppName(String appName) {
			this.appName = appName;
		}

		public String getPosName() {
			return posName;
		}

		public void setPosName(String posName) {
			this.posName = posName;
		}

		public String getPlatform() {
			return platform;
		}

		public void setPlatform(String platform) {
			this.platform = platform;
		}

		public String getPlatformPosName() {
			return platformPosName;
		}

		public void setPlatformPosName(String platformPosName) {
			this.platformPosName = platformPosName;
		}

		public String getAd() {
			return ad;
		}

		public void setAd(String ad) {
			this.ad = ad;
		}

		public String getSdkId() {
			return sdkId;
		}

		public void setSdkId(String sdkId) {
			this.sdkId = sdkId;
		}

		public String getPlatformPosKey() {
			return platformPosKey;
		}

		public void setPlatformPosKey(String platformPosKey) {
			this.platformPosKey = platformPosKey;
		}

		public String getPlatformAppKey() {
			return platformAppKey;
		}

		public void setPlatformAppKey(String platformAppKey) {
			this.platformAppKey = platformAppKey;
		}

		public String getAppKey() {
			return appKey;
		}

		public void setAppKey(String appKey) {
			this.appKey = appKey;
		}

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getPosId() {
			return posId;
		}

		public void setPosId(String posId) {
			this.posId = posId;
		}
	}
}
```