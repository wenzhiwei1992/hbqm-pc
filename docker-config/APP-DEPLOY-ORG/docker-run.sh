#!/bin/sh
cd /home/chiyun/docker/APP-DEPLOY-ORG
# 删除旧版本
rm -rf ./nginx/cy
rm -rf ./nginx/cym
rm -rf ./tomcat/chiyun.war
# 拷贝新版本
cp -r cy ./nginx/cy
cp -r cym ./nginx/cym
cp chiyun.war ./tomcat/chiyun.war
# 删除多余新版本
rm -rf cy/
rm -rf cym/
rm -rf chiyun.war

echo "===docker build ==="
cd ./nginx
docker build -t cynginx-org .
cd ../tomcat
docker build -t chiyun-org .

echo "===docker container stop ==="
docker container stop chiyun-org
docker container stop cynginx-org

echo "===docker delete stoped containers==="
docker container prune -f

docker run -d \
-v /home/chiyun/docker/app/files2:/home/files \
--name chiyun-org \
--link mysql-5.6:orgdb \
chiyun-org

echo "===docker  chiyun-org  started==="

docker run -d -p 80:80 \
--name cynginx-org \
--link chiyun-org:chiyun \
cynginx-org

echo "===docker  cynginx-test  started==="
