# online-judge-core
judge core of online judge

# docker部署
本应用使用docker-compose部署。

将项目根目录docker-compose.yml文件上传至服务器，然后docker-compose命令创建并启动容器即可访问。

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

# todolist
-[ ] 提高竞赛实时排名数据的可用性（可能会出现服务器内存错误导致数据丢失）