package file.excel;

import java.io.*;

/**
 * @author Dongle
 * @desc java csv
 * @since 2021/12/10 13:49
 */
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