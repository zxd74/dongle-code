# Pom.xml配置
## Build
主要配置通过`pom.xml`的`build`标签
```xml
<build></build>
```
### 插件
```xml
<plugins>
    <plugin>
        <groupId></groupId>
        <artifactId></artifactId>
        <!-- 其它配置 -->
    </plugin>
</plugins>
```
* 编译
```xml
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
            <source>8</source>
            <target>8</target>
        </configuration>
    </plugin>
```
* 组装插件：可将依赖的jar包打包到一起
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>2.4.1</version>
    <configuration>
        <!--打包时，包含所有依赖的jar包-->
        <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
</plugin>
```
* **执行jar包**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>2.4</version>
    <configuration>
        <archive>
            <manifest>
                <addClasspath>true</addClasspath>
                <classpathPrefix>/data/lib</classpathPrefix>
                <mainClass>com.zhang.spring.App</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```
* Javadoc文档
```xml
<!--生成javadoc文件-->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.1.0</version>
    <configuration>
        <encoding>UTF-8</encoding>
        <charset>UTF-8</charset>
        <docencoding>UTF-8</docencoding>
        <doclint>none</doclint>
    </configuration>
    <executions>
        <execution>
            <id>attach-javadocs</id>
            <goals>
                <goal>jar</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
* Java Source源码
```xml
<!--生成source文件-->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-source-plugin</artifactId>
    <version>2.4</version>
    <executions>
        <execution>
            <id>attach-sources</id>
            <goals>
                <goal>jar</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
### 非标准Java项目打包
需要声明source和resources目录，否则打包时不会包含这些目录
```xml
    <sourceDirectory>.</sourceDirectory> <!-- 默认src/main/java -->
    <resources>     <!-- 默认src/main/resources -->
        <resource>
            <directory>.</directory>      <!-- 资源目录也是项目根目录 -->
            <includes>
                <include>**/*.properties</include>  <!-- 包含你需要的资源文件 -->
                <include>**/*.xml</include>
            </includes>
            <excludes>
                <exclude>target/**</exclude>      <!-- 排除target目录 -->
                <exclude>pom.xml</exclude>         <!-- 排除pom文件 -->
                <exclude>*.md</exclude>            <!-- 排除其他不需要的文件 -->
            </excludes>
        </resource>
    </resources>
```
