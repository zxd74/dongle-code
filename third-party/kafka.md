# Prducer & Consumer
## Java原生
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-clients</artifactId>
	<version>3.7.1</version>
</dependency>
```
### Producer
```java
public static void main(String[] args) {
    Properties kafkaProps = new Properties();
    // 配置kafka地址
    kafkaProps.put("bootstrap.servers", "localhost:9092,remote1:9092,remote2:9092");
    // 绑定key，value序列化方法
    kafkaProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    kafkaProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    KafkaProducer<String, String> producer = new KafkaProducer<String, String>(kafkaProps);

    for (int i = 0; i < 10; i++) {
        ProducerRecord<String,String> record = new ProducerRecord<>("","","");
        producer.send(record);
    }
    producer.flush();
    producer.close();
}
```
### Consumer
```java
public static void main(String[] args) {
    Properties kafkaProps = new Properties();
    kafkaProps.put("bootstrap.servers", "localhost:9092,remote1:9092,remote2:9092");
    //kafkaProps.put("group.id", "test");
    kafkaProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    kafkaProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    KafkaConsumer<String,String> consumer = new KafkaConsumer<>(kafkaProps);
    while (true){
        ConsumerRecords<String,String> records = consumer.poll(100);
        for (ConsumerRecord<String,String> record:records){
            System.out.println("topic = " + record.topic() + " offset = " + record.offset() + " value = " + record.value());
        }
        consumer.commitAsync();
    }
}
```

## Spring Kafka
* 导入spring-kafka依赖
```xml
<dependency>
  <groupId>org.springframework.kafka</groupId>
  <artifactId>spring-kafka</artifactId>
  <version>2.2.6.RELEASE</version>
</dependency>
```
* 配置application.properties
```properties
# application.properties
spring.kafka.producer.bootstrap-servers=127.0.0.1:9092
```
* 注入KafkaTemplate：既可以是生产者又可以是消费者
  * `@KafkaListener`代表消费者监听
  * spring boot环境下，自动完成kafka自动装配:`org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration`
```java
@Component
public class KafkaProducer {
    @Autowired
    private KafkaTemplate<Object, Object> template;

    // 生产者
    public void sendMessage(String msg) {
        template.send("topic", msg);
    }

    // consumer @KafkaListener监听
    @KafkaListener(topics = "topic")
    public void receiveMessage(String msg) {
        System.out.println("receive message: " + msg);
    }
}
```


# Connect
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>connect-api</artifactId>
	<version>3.7.1</version>
</dependency>
```
## SourceConnector
* `META-INF/services/org.apache.kafka.connect.source.SourceConnector`:`com.example.FileStreamSourceConnector`
```java
public class FileStreamSourceConnector extends SourceConnector {
    private Map<String, String> props;
    @Override
    public void start(Map<String, String> map) { // 解析配置文件的配置数据
        // Initialization logic and setting up of resources can take place in this method.
        // This connector doesn't need to do any of that, but we do log a helpful message to the user.

        this.props = props;
        AbstractConfig config = new AbstractConfig(CONFIG_DEF, props);
        String filename = config.getString(FILE_CONFIG);
        filename = (filename == null || filename.isEmpty()) ? "standard input" : config.getString(FILE_CONFIG);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileStreamSourceTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int i) { 
        // Note that the task configs could contain configs additional to or different from the connector configs if needed. For instance,
        // if different tasks have different responsibilities, or if different tasks are meant to process different subsets of the source data stream).
        ArrayList<Map<String, String>> configs = new ArrayList<>();
        // Only one input stream makes sense.
        configs.add(props);
        return configs;
    }
    // ...
}
public class FileStreamSourceTask extends SourceTask {
    private String filename;
    private InputStream stream;
    private String topic;
    private int batchSize;

    @Override
    public void start(Map<String, String> props) { // 解析数据，源于sourceconnect.taskConfigs()
        filename = props.get(FileStreamSourceConnector.FILE_CONFIG);
        stream = openOrThrowError(filename);
        topic = props.get(FileStreamSourceConnector.TOPIC_CONFIG);
        batchSize = props.get(FileStreamSourceConnector.TASK_BATCH_SIZE_CONFIG);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        try {
            ArrayList<SourceRecord> records = new ArrayList<>();
            while (streamValid(stream) && records.isEmpty()) {
                LineAndOffset line = readToNextLine(stream);
                if (line != null) {
                    Map<String, Object> sourcePartition = Collections.singletonMap("filename", filename);
                    Map<String, Object> sourceOffset = Collections.singletonMap("position", streamOffset);
                    records.add(new SourceRecord(sourcePartition, sourceOffset, topic, Schema.STRING_SCHEMA, line));
                    if (records.size() >= batchSize) {
                        return records;
                    }
                } else {
                    Thread.sleep(1);
                }
            }
            return records;
        } catch (IOException e) {
            // Underlying stream was killed, probably as a result of calling stop. Allow to return
            // null, and driving thread will handle any shutdown if necessary.
        }
        return null;
    }
    // ...
}
```
## SinkConnector
* `META-INF/services/org.apache.kafka.connect.source.SinkConnector`:`com.example.FileStreamSinkConnector`
```java
public class FileStreamSinkConnector extends SinkConnector{
    private Map<String, String> props;
    @Override
    public void start(Map<String, String> props) {
        // 接收属性，转换目标源属性
        this.props = props;
    }

    @Override
    public Class<? extends Task> taskClass() {
        return FileStreamSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        // 将目标源属性根据key/value配置
        return null;
    }

    // ...
}

public class FileStreamSinkTask extends SinkTask{
    private  TargetWriter writer; // 目标写入器
    @Override
    public void start(Map<String, String> props) {
        this.writer = new TargetWriter(props);  // 目标写入器配置
    }

    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        if(sinkRecords.isEmpty()){
            return;
        }
        try {
            writer.write(sinkRecords); // 目标写入数据
        } catch (IOException e) {
            throw new TargetConntorException("数据写入有问题");
        }
    }
    // ...
}
```
## 打包和配置
* 将connector打包成jar，并添加其到kafka响应的java环境中
* `connect-properties`: 配置kafka connect源和目标相关配置
* `connect-target.peroperties`:配置目标所需配置，会在`Connector.start()`方法中解析
## 启动启动
* 测试可使用单机模式，生产建议分布式
  * connect 命令启动
```shell
> ./connect-standalone.sh   ../config/connect-standalone.properties  ../config/connect-target-sink|source.properties
```
  * connect rest接口启动
```shell
curl -X POST /connectors HTTP/1.1
Host: kafka.test.nd1
Content-Type: application/json
Accept: application/json
{
    "name": "local-dw-sink",
    "config": {
    "connector.class":"com.trcloud.hamal.sink.jdbc.JdbcSinkConnector",
    "tasks.max":"1",
    "topics":"sql-log" ,
    "url":"jdbc:mysql://node4:3306/DW",
    "driven":"com.mysql.jdbc.Driver",
    "userName":"root",
    "passwd":"1234"
    }

}
```

# Stream
```xml
<dependency>
	<groupId>org.apache.kafka</groupId>
	<artifactId>kafka-streams</artifactId>
	<version>3.7.1</version>
</dependency>
```
