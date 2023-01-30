FROM registry.inlinegroup-c.ru/tools/gradle:7.5.1-jdk17-focal as build
WORKDIR /app
COPY . .
ENV GRADLE_USER_HOME=/app/gradle
RUN gradle --build-cache assemble -Dspring.profiles.active="test"
#
FROM tomcat:9.0.70-jdk17
ENV TZ Europe/Moscow
ADD app.pub /tmp/app.pub
# Добавить требуемый сертификат в хранилище
ADD root.ca.pem /tmp/root.ca.pem
RUN keytool -noprompt -import -trustcacerts -cacerts -storepass changeit -alias mirror.pem -file /tmp/root.ca.pem
# Наименование war сделать переменной
COPY --from=build /app/build/libs/ROOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]