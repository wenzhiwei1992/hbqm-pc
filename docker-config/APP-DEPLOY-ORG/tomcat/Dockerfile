FROM tomcat:8

# 拷贝后台应用的数据库连接配置文件
COPY server-config.properties /opt/chiyun/server-config.properties
# 设置运行时参数
ENV JAVA_OPTS -Dchiyun.server.config=/opt/chiyun/server-config.properties -Xmx3072m -XX:MaxPermSize=256m -Djava.security.egd=file:/dev/./urandom -Ddatabase=pcmaster_org
# 更改时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 将tomcat的配置文件替换
# COPY server.xml /usr/local/tomcat/conf/server.xml

# 运行tomcat 脚本
COPY run.sh /opt/chiyun/run.sh

# 添加执行权限
RUN chmod +x /opt/chiyun/run.sh

# 拷贝war包
COPY chiyun.war /usr/local/tomcat/webapps/

# 启动tomcat
ENTRYPOINT ["/opt/chiyun/run.sh"]
CMD [""]
