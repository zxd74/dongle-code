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
* 新版
```java
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
* 旧版
```java
public class WordCountMapper extends MapReduceBase 
    implements Mapper<Object, Text, Text, IntWritable> {

    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();

    @Override
    public void map(Object key, Text value, 
                    OutputCollector<Text, IntWritable> output, 
                    Reporter reporter) throws IOException {
        // 你的逻辑
    }
}
```
## Reducer
* 新版
```java
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
* 旧版
```java
public class WordCountReducer extends MapReduceBase 
    implements Reducer<Text, IntWritable, Text, IntWritable> {

    @Override
    public void reduce(Text key, Iterator<IntWritable> values,
                       OutputCollector<Text, IntWritable> output,
                       Reporter reporter) throws IOException {
        // 你的逻辑
    }
}
```

## Job
* **新版API** `Job.getInstance`
```java
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
public static void main(String[] args) throws Exception {
    Configuration config = new Configuration();
    Job job = Job.getInstance(config,"job-demo");

    job.setMapperClass(MyMapper.class);
    job.setReducerClass(MyReduce.class);

    // 设置输出 KV 类型
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Long.class);

    // 自动识别IO Format

    // 设置输入输出路径
    FileInputFormat.addInputPath(job,new Path(args[0]));  
    FileOutputFormat.setOutputPath(job,new Path(args[1])); 

    job.waitForCompletion(true); // 提交运行
    // JobClient.runJob(conf); // 旧版方式提交Job
}
```
* **旧版API(兼容)**：`new JobClient() + JobConf`(**废弃**)
```java
public static void main(String[] args) throws Exception {
    // 创建 JobConf（旧版配置 + 任务对象二合一）
    JobConf conf = new JobConf(OldJobCreator.class);
    conf.setJobName("job-demo");

    // 设置 Mapper / Reducer
    conf.setMapperClass(MyMapper.class);
    conf.setReducerClass(MyReduce.class);

    // 设置输出 KV 类型
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Long.class);

    // 需要手动绑定IO Format
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    // 设置输入输出路径
    FileInputFormat.addInputPath(job,new Path(args[0])); 
    FileOutputFormat.setOutputPath(job,new Path(args[1])); 

    JobClient.runJob(conf); // 提交运行（旧版写法）
}
```
* 执行
```shell
bin/hadoop jar wc.jar WordCount /user/joe/wordcount/input /user/joe/wordcount/output
```