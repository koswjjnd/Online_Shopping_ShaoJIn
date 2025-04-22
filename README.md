# Getting Started



## RocketMQ

Enter CMD go to Folder \rocketmq-all-4.9.3-bin-release\rocketmq-4.9.3\bin
Type
./mqnamesrv`

Launch a new terminal with:
注意先看echo $JAVA_HOME, 是不是/Users/jin/Downloads/Java8HOME
如果不是
要 export JAVA_HOME= /Users/jin/Downloads/Java8HOME
`./mqbroker -n localhost:9876 autoCreateTopic=true`
Delete Message:
`./mqadmin deleteTopic -c DefaultCluster -n localhost:9876 -t {topicName}`
### Check Port:
netstat -ano | findstr "9876"
### ShutDown RocketMQ
mqshutdown broker
mqshutdown namesrv
如果不是第一次运行rocketmq，则将C:\Users\Administrator\store文件夹下的文件全部删除，则可以正常启动
### Delete Message:
`./mqadmin deleteTopic -c DefaultCluster -n localhost:9876 -t {topicName}`
### Environment setup
Key: ROCKETMQ_HOME
Val:\rocketmq-all-4.9.3-bin-release\rocketmq-4.9.3
### Elasticsearch
Enter CMD go to Folder \elasticsearch-7.4.2\bin
type
`elasticsearch`

Enter CMD goto folder \kibana-7.4.2-windows-x86_64\kibana-7.4.2-windows-x86_64\bin
type
`kibana.bat`
visit http://127.0.0.1:5601/


### Install Jar
https://maven.apache.org/download.cgi
Update POM with pacakage Jar and plugins for maven
In Plugins do compile and then jar
Copy from target to root path, then type:
```
mvn install:install-file -Dfile=target/OnlineShopping_02-1.0.jar -DgroupId=com.qiuzhitech -DartifactId=OnlineShopping -Dversion=1.1 -Dpackaging=jar
```

因为是java8的原因，所以在pom.xml里加上这些
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
