# 部署注意事项
## 数据库
对于每个厂区，需要手动建好数据库，org-db.json里建立好厂区数据库名的对应关系

## server config
- 设置好环境变量chiyun.server.config，引用server-config.properties的位置
- 建立附件和图片的保存根目录，在server-config.properties配置好，如果用docker，将该目录需做数据卷持久化
- 配置好企业微信，用于jssdk的调用

## 域名验证
企业微信的域名验证文件按照规定放在域名可访问的位置