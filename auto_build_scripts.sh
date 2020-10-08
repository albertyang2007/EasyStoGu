#!/bin/bash

cd /home/eyaweiw/github/EasyStoGu

echo 'maven build and package'
mvn8 clean package
mvn8 clean package -Pportal -o
mvn8 clean package -Pscheduled -o
mvn8 clean package -Peweb -o

echo 'portal docker build image and upload'
cd /home/eyaweiw/github/EasyStoGu/Docker/src/main/resources/springboot_portal
cp /home/eyaweiw/github/EasyStoGu/PortalApp/target/easystogu-portal-app-2.0.0-SNAPSHOT-exec.jar ./easystogu-portal-app.jar

docker build -t easystogu-portal:2.0.0-snapshot .
docker tag easystogu-portal:2.0.0-snapshot eyaweiw.cn:5000/easystogu-portal:2.0.0-snapshot
docker push eyaweiw.cn:5000/easystogu-portal:2.0.0-snapshot

echo 'scheduled docker build image and upload'
/home/eyaweiw/github/EasyStoGu/Docker/src/main/resources/springboot_scheduled
cp /home/eyaweiw/github/EasyStoGu/ScheduledApp/target/easystogu-scheduled-app-2.0.0-SNAPSHOT-exec.jar ./easystogu-scheduled-app.jar

docker build -t easystogu-scheduled:2.0.0-snapshot .
docker tag easystogu-scheduled:2.0.0-snapshot eyaweiw.cn:5000/easystogu-scheduled:2.0.0-snapshot
docker push eyaweiw.cn:5000/easystogu-scheduled:2.0.0-snapshot

echo 'eweb docker build image and upload'
cd /home/eyaweiw/github/EasyStoGu/Docker/src/main/resources/wildfly_eweb
cp /home/eyaweiw/github/EasyStoGu/EWeb/target/easystogu-eweb-2.0.0-SNAPSHOT.war ./eweb.war

docker build -t easystogu-eweb:2.0.0-snapshot .
docker tag easystogu-eweb:2.0.0-snapshot eyaweiw.cn:5000/easystogu-eweb:2.0.0-snapshot
docker push eyaweiw.cn:5000/easystogu-eweb:2.0.0-snapshot



