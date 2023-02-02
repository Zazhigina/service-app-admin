FROM gitlab.inlinegroup-c.ru/mirror/dependency_proxy/containers/gradle:7.6.0-jdk17-alpine as build
ENV TZ Europe/Moscow
WORKDIR /application
ADD app.pub /application/src/main/resources/app.pub
COPY . .
ENV GRADLE_USER_HOME=/application/gradle
RUN gradle build -Dspring.profiles.active="test"
RUN java -Djarmode=layertools -jar /application/build/libs/root.jar extract
#
FROM gitlab.inlinegroup-c.ru/mirror/dependency_proxy/containers/eclipse-temurin:17-jre-ubi9-minimal
ENV TZ Europe/Moscow
ADD root.ca.pem /tmp/root.ca.pem
RUN keytool -noprompt -import -trustcacerts -cacerts -storepass changeit -alias mirror.pem -file /tmp/root.ca.pem
WORKDIR application
COPY --from=build /application/dependencies/ ./
COPY --from=build /application/spring-boot-loader/ ./
COPY --from=build /application/snapshot-dependencies/ ./
COPY --from=build /application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]