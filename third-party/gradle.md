# 基础实践
1. 创建一个项目目录
2. 在项目目录下创建`settings.gradle`文件,并绑定项目名称
```gradle
rootProject.name = "dongle-gradle" // 本项目名称
include("dongle-gradle-module") // 关联子module，默认目录名与module名一致
project(':my-lib').projectDir = file('my-lib-module-dir')  // 绑定到物理目录，目录名与module名不一致
```
3. 在项目目录下创建`build.gradle`文件,用于项目的各种配置
```gradle
plugins { // 插件
    // id("java") // gradle官方java插件 == 
    java  // == id("java") gradle内嵌插件
    id("jar") // 构建jar包
    id("org.springframework.boot") version "3.1.0" // 第三方插件
}

group = "com.dongle.demo" // == maven groupId
version = "1.0-SNAPSHOT" // == maven version

repositories { // 仓库地址，支持maven仓库和本地仓库
    mavenCentral()
}

dependencies { // 各种依赖
    val springVersion = "2.6.15"
    implementation("org.springframework.boot:spring-boot-starter:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web:${springVersion}")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test { // 任务
    useJUnitPlatform()
}
```
4. 项目管理：项目单独关联独立`gradle`时，使用`./gradlew`命令
   - `gradle clean`：清理项目
   - `gradle build`：构建项目
   - `gradle run`：运行项目
   - `gradle test`：运行测试
   - `gradle dependencies`：查看依赖树
   - `gradle tasks`：查看所有任务
5. 项目构建路径：`build`目录,`jar`包(**结果文件**)生成在`build/libs`目录下

**注意**：
* 子module除不需要创建`settings.gradle`文件外，其他文件与父module类似
* 不同配置文件类型有不同语法
  * 如`build.gradle`和`build.gradle.kts`语法是有些区别的，`kts`适用于`kotlin`语法

## 插件管理
* `plugins`：插件管理
* `id`：插件id
  * 部分内嵌插件支持直接使用插件名，如`java`，`war`，`application`等
* `version`：插件版本
* 允许对插件进行配置(若需要)
```java
plugins { // 插件
    // id("java") // gradle官方java插件 == 
    java  // == id("java") gradle内嵌插件
    id("jar") // 构建jar包
    id("org.springframework.boot") version "3.1.0" // 第三方插件
}
// java插件配置
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // 设置 JDK 17
    }
    withSourcesJar() // 生成源码 JAR
    withJavadocJar() // 生成文档 JAR
}
// war插件配置
war {
    manifest {
        attributes 'Main-Class': 'com.example.Main'  # 主类（通常不用于 WAR）
    }
}
```

## 依赖管理
* `repositories` ：仓库地址，支持maven仓库和本地仓库
* `dependencies`：各种依赖
  * `implementation`：普通依赖
  * `files`：本地依赖
  * `testImplementation`：测试依赖
```gradle
// build.gradle
repositories { // 仓库地址，支持maven仓库和本地仓库
    mavenCentral()
}

dependencies { // 各种依赖
    val springVersion = "2.6.15"
    // 普通依赖
    implementation("org.springframework.boot:spring-boot-starter:${springVersion}")
    implementation("org.springframework.boot:spring-boot-starter-web:${springVersion}")

    files("local-dependency.jar") // 本地依赖

    // test 相关依赖
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

```
## 自定义任务
* 官方任务：`build,clean,jar,buildDependents,init,...`
* `tasks.register('task-name')`：自定义任务注册
* `doFirst`：任务执行前
* `doLast`：任务执行后
* `run`：任务执行
```gradle
// build.gradle
tasks.register('xxx'){ // 自定义任务tasks.xxx，非内部任务，需要通过register注册
    doFirst { // 任务执行前
        println "hello task begin"
    }
    run { 
        // 任务执行
    }
    doLast { // 任务执行后
        println "生成环境配置文件：${project.environment}"
    }
}
```
