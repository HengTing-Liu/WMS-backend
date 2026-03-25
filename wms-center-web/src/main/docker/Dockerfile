FROM harbor.lotus.com/lotus/jre:1.8
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

#COPY demo-spring-web.jar /demo-spring-web.jar
COPY @project.build.finalName@.jar @project.build.finalName@.jar

ENTRYPOINT ["/entrypoint.sh"]