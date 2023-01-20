FROM tomcat:9.0.70-jdk17
ENV TZ Europe/Moscow

# Добавить требуемый сертификат в хранилище
#COPY lib/inlinegroup-c-ru.pem inlinegroup-c-ru.pem
#RUN keytool -noprompt -import -alias inlinegroup-c.ru -keystore $JAVA_HOME/jre/lib/security/cacerts -file inlinegroup-c-ru.pem -storepass changeit

# Наименование war сделать переменной
ADD build/libs/ROOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]