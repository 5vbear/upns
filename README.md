UPNS(Unified Push Notification Serivce)
====
    Unified Push Notification Serivce for IOS/Android/Websocket Device
    为IOS,Android,Web提供的统一消息推送服务
    It's based on [netty](http://netty.io/) and [redis](http://redis.io/).
    
##The service contain
    1) tcp server for android device
    2) embedded web socket server for browser device
    3) apple push connector for ios device

##Requirement
    You must have jdk1.6 or higher and maven3.0 orhigher.
    Redis Server
    MongoDB Server

##Quick Start
    Required
    1) Redis run on 127.0.0.1:6379
    2) Mongo run on 127.0.0.1:27017

    Build Service
    1) open terminal
    2) enter project root directory and input "mvn clean compile install"
    3) enter ./upns-server/integration and input "mvn assembly:assembly"

    Start Service
    1) enter ./upns-server/integration/target and input "tar -xzvf upns-server.tar.gz"
    2) input chmod +x shell
    3) ./shell start dev
    4) open http://127.0.0.1:8888/console/index.xhtml
    5) the tcp client emulator is com.sirius.upns.test.unit.endpoint.TCPClientDaemon

    Customize Service
    1) enter ./upns-server/integration/target/profile/dev
    2) all settings in the file ctu.application.settings.properties

##Performance
    Hardware:kvm virtual machine as vcpu*4 memory=4g
    Software:CentOS 6.4 and SUN JDK 1.7.0_45-b18

    Test Case:30,000 long connection users, 1000 msg per second, last 4 hours
    Result:the avg msg ack small then 5 second



