## 启动 ES
```shell
docker run --name es01 --net elastic -p 9200:9200 -it -m 1GB docker.elastic.co/elasticsearch/elasticsearch:8.13.1
```
> 记住命令行内显示的用户和密码