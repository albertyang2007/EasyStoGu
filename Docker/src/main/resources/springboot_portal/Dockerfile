FROM eyaweiw.cn:5000/openjdk:8-jdk-alpine
COPY easystogu-portal-app.jar easystogu-portal-app.jar
COPY logback.xml logback.xml
ENTRYPOINT ["java","-Dlogging.config=/logback.xml","-jar","/easystogu-portal-app.jar"]

