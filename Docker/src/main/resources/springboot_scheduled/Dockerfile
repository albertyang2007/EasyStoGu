FROM eyaweiw.cn:5000/openjdk:8-jdk-alpine
COPY easystogu-scheduled-app.jar easystogu-scheduled-app.jar
COPY logback.xml logback.xml
ENTRYPOINT ["java","-Dlogging.config=/logback.xml","-jar","/easystogu-scheduled-app.jar"]

