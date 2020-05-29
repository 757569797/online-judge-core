# online-judge-core
核心评测服务。

在线编程竞赛平台分为三个服务，一个是前台服务，一个是后台API服务，最后是后台核心评测服务。
- [前台服务](https://github.com/evanlaochen/online-judge-fe)
- [API服务](https://github.com/evanlaochen/online-judge)
- [核心评测服务](https://github.com/evanlaochen/online-judge-core)。

# docker部署
本应用使用docker-compose部署。

将项目根目录docker-compose.yml文件上传至服务器，并将项目jar包上传到宿主机目录中（见docker-compose.yml文件具体说明），然后docker-compose命令创建并启动容器即可访问。

启动服务：
```shell
docker-compose up -d
```
查看服务：
```shell
docker-compose ps
```
修改服务：
重新打包成jar包覆盖云服务器上的jar包，并执行：
```shell
docker-compose restart
```

# docker-compose.yml修改说明

#### kafka
- environment.KAFKA_ADVERTISED_HOST_NAME 修改为部署的kafka集群IP（这里只有一个broker，故只需要一个即可）
- environment.KAFKA_ADVERTISED_LISTENERS 修改为部署的kafka集群IP

#### judge-machine
- environment.SERVICE_URL 的IP修改为评测机所在服务器IP
- environment.BACKEND_URL 的IP修改为onlinejudge-core服务所在服务器IP

#### rsync-slave
- environment.RSYNC_USER 和master主机的名称一致
- environment.RSYNC_PASSWORD 和master主机的密码一致
- environment.RSYNC_MASTER_ADDR 修改为master主机服务器IP

# application.yml配置文件修改说明

#### mysql
mysql数据库链接修改为mysql所在服务器IP

#### redis
redis修改为redis所在服务器IP

#### kafka
kafka代理地址修改为部署的kafka集群IP

# todolist
-[ ] 提高竞赛实时排名数据的可用性（可能会出现服务器内存错误导致数据丢失）
-[ ] 添加爬虫主动心跳探测
-[ ] 添加爬虫和评测机的超时机制（超时异常判定）