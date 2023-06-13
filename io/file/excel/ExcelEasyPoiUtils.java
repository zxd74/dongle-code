package file.excel;

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
