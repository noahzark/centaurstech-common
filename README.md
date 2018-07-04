# centaurstech-common
Centaurs Technologies Common Libraries

[![](https://jitpack.io/v/noahzark/centaurstech-common.svg)](https://jitpack.io/#noahzark/centaurstech-common)

# domain包

## EngineQuery

引擎提交表单 Model 的父类，提供了处理 chat_key 以及计算请求处理时间的方法

## QueryResult

返回给引擎的 solution 结构，提供了指定 solution, process, status 的构造方法

# utils包

## ChatApi

用于和接口层 robot-service 交互的工具

## GetNetworkAddress

用于获取本机Mac地址的工具

## Md5

用于计算 Md5 值的工具

## TimeUtil

用于时间计算的工具，提供了 beginOfDay, endOfDay, getTimeString 等方法
