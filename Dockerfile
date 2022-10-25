FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./admin/target/xxx.jar xxx.jar
ENTRYPOINT ["java","-jar","/xxx.jar","&"]