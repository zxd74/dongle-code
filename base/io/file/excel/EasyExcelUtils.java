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