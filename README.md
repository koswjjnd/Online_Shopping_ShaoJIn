# Getting Started

## 🚀 RocketMQ

Enter CMD and go to the folder `\rocketmq-all-4.9.3-bin-release\rocketmq-4.9.3\bin`, then type `./mqnamesrv`.

Launch a new terminal. First, check `echo $JAVA_HOME` and make sure it shows `/Users/jin/Downloads/Java8HOME`. If not, set it using `export JAVA_HOME=/Users/jin/Downloads/Java8HOME`, then run `./mqbroker -n localhost:9876 autoCreateTopic=true`.

To delete a topic, type `./mqadmin deleteTopic -c DefaultCluster -n localhost:9876 -t {topicName}`.

### Check Port  
Use `netstat -ano | findstr "9876"` to check if the port is in use.

### Shut Down RocketMQ  
Use `mqshutdown broker` and `mqshutdown namesrv` to shut down the services.

If this is not your first time running RocketMQ, delete all files under `C:\Users\Administrator\store` to ensure a clean startup.

### Environment Setup  
Set the environment variable:  
Key: `ROCKETMQ_HOME`  
Value: `\rocketmq-all-4.9.3-bin-release\rocketmq-4.9.3`

---

## 🔍 Elasticsearch

Enter CMD, go to the folder `\elasticsearch-7.4.2\bin`, and type `elasticsearch`.

---

## 📊 Kibana

Enter CMD, go to the folder `\kibana-7.4.2-windows-x86_64\kibana-7.4.2-windows-x86_64\bin`, and type `kibana.bat`.

Visit [http://127.0.0.1:5601/](http://127.0.0.1:5601/) in your browser.

---

## ☕ Install JAR (Maven)

Download Maven from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).

Update the `pom.xml` with your JAR and plugins. Use the plugin tab to `compile` and then `package` as a `.jar`.

Copy the JAR from the `target/` folder to your root path, then type:  
`mvn install:install-file -Dfile=target/OnlineShopping_02-1.0.jar -DgroupId=com.qiuzhitech -DartifactId=OnlineShopping -Dversion=1.1 -Dpackaging=jar`

Since you're using Java 8, add the following to your `pom.xml`:

```xml
<plugins>
  <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
      <excludes>
        <exclude>
          <groupId>org.projectlombok</groupId>
          <artifactId>lombok</artifactId>
        </exclude>
      </excludes>
    </configuration>
  </plugin>
  <plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M5</version>
    <configuration>
      <argLine>--add-opens java.base/java.lang=ALL-UNNAMED</argLine>
    </configuration>
  </plugin>
</plugins>
