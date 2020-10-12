# centaurstech-common
Centaurs Technologies Common Libraries

使用方法 [![](https://jitpack.io/v/noahzark/centaurstech-common.svg)](https://jitpack.io/#noahzark/centaurstech-common)

# Algorithm

## DFAFilter

敏感词过滤，通过敏感词库构造敏感词过滤器。提供替换敏感词、判断文字是否包含敏感词，计算句中敏感词长度及个数、获取句中敏感词等函数。



# domain包

## Cache子包

### TimeBasedCache

抽象出时间内容对应的类，提供了指定与不指定expireIn的构造方法，存入key，content，expireIn的put方法，通过key获取未超时内容的方法。



## Eventtrack子包

### EventTrack

数据上报(埋点)所用类

### EventTrackItem

数据上报所用具体参数类

### EventTrackProxy

数据上报代理，包含上各种数据，添加线程、缓存处理。



## BasicAuthData

抽象出用户名和密码的类，提供了通过编码后字符串的构造方法、通过用户名密码、字符串的填充方法



## ChatApp

将会话所需参数抽象出的类，提供了获取有效验证字符串的方法。



## EngineKeywordsQueryResult

继承了返回给引擎的回复的内容结构，并新增了keywords成员变量。



## EngineQuery

引擎提交表单 Model 的父类，提供了处理 chat_key 以及计算请求处理时间的方法



## EngineQueryProxy

继承了EngineQuery类，提供了通过指定chat_key 、requestParams， requestParams+keepRequestParams的构造方法



## EngineQueryResult

返回给引擎的 solution 结构，提供了指定 solution, process, status 的构造方法



## GPSLocation

GPS定位信息抽象出的类



## QueryResult

返回给引擎的 solution 结构，提供了指定 solution, process, status 的构造方法



# utils包

## email子包

### EmailClient

抽象出发送邮件所需信息的类，提供了指定端口和消息From地址、指定用户名和密码的构造方法，并提供了发送邮件的方法

### EmailClientAuthenticator

抽象出用户名密码的类



## encode子包

### AESCipher

提供了AES加密解密相关方法

### Base64Util

提供了Base64编码解码以及十进制转化为16进制的方法

### HttpUtil

提供了将Map对象转化成http查询字符串形式的方法，同时提供了将查询字符串转为Map对象的方法。

### Md5

提供了Md5加密方法

### RSA

提供了从字符串加载公钥，公钥解密、RSA验签名检查、编码并解码http查询字符串的方法。

### XXTEA

提供了XXTEA加密解密的方法，并综合了Base64编码解码。

## http子包

### BaseHttpClient

抽象出基础HTTP传输所需信息的类

### RequestBodyUtil

构造并重写了接受客户端传来数据的RequestBody类，对写入流进行缓存写入操作。

### SimpleHttpClient

继承了BaseHttpClient类，提供了构造请求接口所需信息的方法、并提供了不同发送请求(get/post)，与接受返回的消息进行String、JSON转化的方法。

## location子包

### GPSConverter

GPS定位信息转化类，提供与WGS84、GCJ02、BD09坐标系之间的互相转化



## time子包

### DatePeriod

提供获取一天最开始的时间和一天终结的时间、判断某时间是否在当前日、周、月的方法。



### TimeCalculator

提供获取毫秒级时间戳、秒级时间戳、通过日期和时区获取日期字符串的方法



### TimePeriod

提供了判断某日期是否在某天的方法、将文字表述转化为时间期的方法



## ChatApi

用于和接口层 robot-service 交互的工具



## DistanceCalculator

提供了计算GPS定位点之间的距离，并可以转为不同单位的方法。



## GetNetworkAddress

用于获取本机Mac地址的工具



## Md5

用于计算 Md5 值的工具



## QueryHelper

提供了使用url传参时，提供了将参数进行DES加密解密的方法



## StringExtractor

提供了解析GUEST_AUTH、BASIC_AUTH、BEARER_TOKEN、FACEBOOK_AUTH模式的字符串的方法，解析出用户名和密码



## TimeCalculator

提供供获取毫秒级时间戳、秒级时间戳的方法、获取一天最开始的时间和一天终结的时间的方法、通过日期和时区获取日期字符串的方法。



## TxtFileReader

提供了读txt文件转为Set集合、Map集合的方法



## WeightConverter 

提供了重量单位之间的转化



## TimeUtil

用于时间计算的工具，提供了 beginOfDay, endOfDay, getTimeString 等方法
