# 注意设置 sshkey 
# cat ~/.ssh/id_rsa.pub | ssh root@47.96.170.246 "cat - >> ~/.ssh/authorized_keys"

#声明重要的变量,根据部署的实际情况修改

# server.config.js 路径
export config_js_path=$WORKSPACE/service-config/server_hzby.config.js
# 数据库 json 文件
export json_path=$WORKSPACE/service-config/org-db-hzby.json
# 目标服务器 ip 地址
export target_host=root@47.96.170.246
# 目标服务器存放路径
export target_path=/home/chiyun/docker/APP-DEPLOY-ORG

# ------- 此处正常情况不需要修改 -----
# mvn 路径
export mvn_home=/Users/mac/dev-install/apache-maven-3.5.4/bin
# 前端 dist 路径
export f_path=$WORKSPACE/frontend-src/dist/
# 手机端 dist 路径
export m_path=$WORKSPACE/mobile-src/dist/
# war 包路径
export war_path=$WORKSPACE/backend-src/chiyun/system/target/chiyun-0.0.1-SNAPSHOT.war
# json db 文件路径
export target_json_path=$WORKSPACE/backend-src/chiyun/system/src/main/resources/org-db.json
# ------- end -----


echo "build web >>>>>>>"

cd $WORKSPACE/frontend-src

cp $config_js_path ./server.config.js
npm i --unsafe-perm=true --allow-root
npm run build

# 复制前端构建好的文件到docker构建目录
scp -r $f_path $target_host:$target_path/cy

echo "build mobile >>>>>>>"

cd $WORKSPACE/mobile-src

cp $config_js_path  ./server.config.js
npm i --unsafe-perm=true --allow-root
npm run build

scp -r $m_path $target_host:$target_path/cym

echo "static web build success >>>>>>>"

echo "build war >>>>>>>"
cd $WORKSPACE/backend-src/chiyun

cp $json_path $target_json_path

$mvn_home/mvn clean install

scp $war_path $target_host:$target_path/chiyun.war

echo "------------> build docker images and restart container <-------------"
ssh -t -p 22 $target_host "/home/chiyun/docker/APP-DEPLOY-ORG/docker-run.sh"
exit #退出ssh