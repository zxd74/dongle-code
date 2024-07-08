# 依赖
```xml
    <!-- https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-hdfs -->
    <dependency>
      <groupId>org.apache.hadoop</groupId>
      <artifactId>hadoop-hdfs</artifactId>
      <version>2.6.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-client -->
    <dependency>
      <groupId>org.apache.hadoop</groupId>
      <artifactId>hadoop-client</artifactId>
      <version>2.6.0</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-common -->
    <dependency>
      <groupId>org.apache.hadoop</groupId>
      <artifactId>hadoop-common</artifactId>
      <version>2.6.0</version>
    </dependency>
```

# MapReduce
## Mapper
```java
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author Dongle
 * @desc
 * @since 2024/7/1 10:46
 */
public class MyMapper extends Mapper<Text, LongWritable,Text,LongWritable> {
    private LongWritable one = new LongWritable(1);
    @Override
    protected void map(Text key, LongWritable value, Mapper<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        String[] wds = value.toString().split(" "); // 每行按空格分割单词
        for (String word : wds) { // 遍历所有单词
            Text wd = new Text(word); // 转换为key的类型
            context.write(wd,one); // 每个读取一个word，都要记录一次，并就交由 reducer 进行合并
        }
    }
}
```
## Reducer
```java
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author Dongle
 * @desc
 * @since 2024/7/1 10:46
 */
public class MyReduce extends Reducer<Text, LongWritable,Text,LongWritable> {
    private LongWritable res = new LongWritable(); // 记录结果

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
        long ressum = 0;
        for (LongWritable one : values) {
            ressum+=one.get();
        }
        res.set(ressum);
        context.write(key,res); // 继续将合并结果交给下一个reducer进行多次合并，得到最终每个key的最终结果
    }
}
```

## Job
```java
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public static void main(String[] args) throws Exception {
    Configuration config = new Configuration();
    Job job = Job.getInstance(config,"job-demo");
    job.setMapperClass(MyMapper.class);
    job.setReducerClass(MyReduce.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Long.class);
    FileInputFormat.addInputPath(job,new Path(args[0]));  // 注意不要和org.apache.hadoop.mapred.FileInputFormat搞混，如果使用，可以通过Job.getConfiguration()作为参数
    FileOutputFormat.setOutputPath(job,new Path(args[1])); // org.apache.hadoop.mapred.FileInputFormat(job.getConfiguration,new Path("ddd"))，同理
    job.waitForCompletion(true);
}
```
```shell
bin/hadoop jar wc.jar WordCount /user/joe/wordcount/input /user/joe/wordcount/output
```