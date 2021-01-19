# 消息队列 Demo
***
## 简介
&ensp;&ensp;&ensp;&ensp;实现消息队列 Demo 程序

## 程序运行说明
&ensp;&ensp;&ensp;&ensp;直接运行Example工程下的ExampleApplication即可

## 设计思路

## 程序说明
- core ：消息队列功能API实现
- example ： 使用消息队列API，实现生产和消费

### Core工程
实现内存消息队列
实现Producer API
实现Consumer API

### Example工程
简单的测试用例，看Core是否能进行使用

## 迭代说明
### 第一版：内存 Queue
- [x] 1、基于内存Queue实现生产和消费API（已经完成）
  - [x] 1）创建内存Queue，作为底层消息存储
  - [x] 2）定义Topic，支持多个Topic
  - [x] 3）定义Producer，支持Send消息
  - [x] 4）定义Consumer，支持Poll消
  
&ensp;&ensp;&ensp;&ensp;目前使用HTTP作为通信方式

#### v1.0 HTTP
使用队列： ConcurrentLinkedQueue
性能测试：

start producer test
Producer 1000 messages spend time : 15324 ms
 
Start consumer test
Consumer 1000 messages spend time : 19 ms

jdk.internal.misc.Unsafe.park[native] 占用94%的运行时间
看着像是网络通信的
感觉HTTP没有长连接，每次都是新的

#### v1.1 websocket
测试下面有并发问题，后面需要排除改进
producer use websocket
consumer use http

```text
start producer test
Producer 1000 messages spend time : 86 ms 
Start consumer test
Consumer 1000 messages spend time : 177 ms
```

```text
start producer test
start producer test
Producer 100000 messages spend time : 408 ms 
Start consumer test
Consumer 100000 messages spend time : 167 ms
```

### 第二个版本：自定义 Queue
- [ ] 2、去掉内存Queue，设计自定义Queue，实现消息确认和消费offset
    - [ ] 1）自定义内存Message数组模拟Queue。
    - [ ] 2）使用指针记录当前消息写入位置。
    - [ ] 3）对于每个命名消费者，用指针记录消费位置。

### 第三个版本：基于 SpringMVC 实现 MQServer
- [ ] 3、拆分broker和client(包括producer和consumer)
    - [ ] 1）将Queue保存到web server端
    - [ ] 2）设计消息读写API接口，确认接口，提交offset接口
    - [ ] 3）producer和consumer通过httpclient访问Queue
    - [ ] 4）实现消息确认，offset提交
    - [ ] 5）实现consumer从offset增量拉取

### 第四个版本：功能完善 MQ
- [ ] 4、增加多种策略（各条之间没有关系，可以任意选择实现）
    - [ ] 1）考虑实现消息过期，消息重试，消息定时投递等策略
    - [ ] 2）考虑批量操作，包括读写，可以打包和压缩
    - [ ] 2）考虑消息清理策略，包括定时清理，按容量清理等
    - [ ] 3）考虑消息持久化，存入数据库，或WAL日志文件，或BookKeeper
    - [ ] 4）考虑将spring mvc替换成netty下的tcp传输协议

### 第五个版本：体系完善 MQ
- [ ] 5、对接各种技术（各条之间没有关系，可以任意选择实现）
    - [ ] 1）考虑封装 JMS 1.1 接口规范
    - [ ] 2）考虑实现 STOMP 消息规范
    - [ ] 3）考虑实现消息事务机制与事务管理器
    - [ ] 4）对接Spring
    - [ ] 5）对接Camel或Spring Integration
    - [ ] 6）优化内存和磁盘的使用
    
    
## 参考链接
- [SpringBoot+WebSocket实现服务端、客户端](https://my.oschina.net/u/4504531/blog/4557921)