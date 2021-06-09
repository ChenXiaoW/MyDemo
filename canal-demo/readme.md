# 单数据源的canal同步
# canal数据同步

## 使用
官方文档：https://github.com/alibaba/canal

1. 下载 canal.deployer-x.x.x.tar.gz包
2. 解压后修改 canal.deployer-1.1.5.tar\conf\example\instance.properties文件，配置master库
```properties
# 数据库地址
canal.instance.master.address=127.0.0.1:3306
# binlog日志名称
canal.instance.master.journal.name=mysql-bin.000224
# mysql主库链接时起始的binlog偏移量
canal.instance.master.position=7617
# 在MySQL服务器授权的账号密码
canal.instance.dbUsername=canal
canal.instance.dbPassword=canal
# table regex  .*\\..*表示监听所有表 也可以写具体的表名，用，隔开
canal.instance.filter.regex=.*\\..*  # live\\..* 监控live数据库下的所有表
# table black regex  mysql 数据解析表的黑名单，多个表用，隔开
canal.instance.filter.black.regex=mysql\\.slave_.*
```
3. 配置完成后，执行`bin`目录下的启动文件
4. 完成

