

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.2.RELEASE)


# spring-boot-cassandra Project

#Step1: get Apache Cassandra running

cd apache-cassandra*

./run

cd ..

#Step2: Build project

mvn clean install

./build.script


#Step3: Run service

mvn install

java -jar ./target/rest-server-1.0.0.jar

