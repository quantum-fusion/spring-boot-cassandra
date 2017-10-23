# spring-boot-cassandra

#Step1: get Apache Cassandra running

cd apache-cassandra*

./run

#Step2: compile Persistence classes for Cassandra

cd ./Persistence

mvn clean install

cd ..

./build.script


#Step3: Compile rest service

mvn install



