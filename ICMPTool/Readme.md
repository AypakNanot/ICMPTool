# ICMP报文发送，接收工具
# 实现批量ping命令。 获取 时延、丢包率、带宽。

## 版本：V0.0.1

## How run?
### 安装WinPcap_*.exe工具。由第三方提供对底层网络的支持
### 把jar里面res取出来放在jar所在目录。
		host.txt 存储需要ping的目的地址。可通过配置文件，读取数据库里面地址也可以手动填写。
		resource.properties 存储配置信息。
### 把jar包里面的lib目录提取到根目录，包含lib。
### 根据系统中jdk版本（64位或者32位），从jar里面lib目录中对应的jpcap.dll文件到根目录。
### 打开res目录下resource.properties文件，修改ndt.dst.host.database：是否为数据库源。修改ndt.record.unreachable：是否需要记录不可到达的地址信息。具体可查看该文件
### 执行run.bat

## 工程中目录样式至少有以下文件 
---ICMPTool
	|---------lib
			  |------icmp-****.jar
			  |------jpcap.jar
			  |------mysql-connector-java-***.jar
	|---------res
			  |------host.txt
			  |------resource.properties
	|---------results-->该目录下所有文件由系统生成.
	|---------Jpcap.dll
	|---------run.bat
	|---------WinPcap_*.exe-->该文件为安装文件.