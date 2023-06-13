package spring.eureka;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Dongle
 * @desc
 * @since 2022/1/29 12:32
 */
@Component
public class EurekaDiscovery {

    private static final Logger LOGGER = LoggerFactory.getLogger("EurekaJob");

    @Autowired
    private EurekaClient eurekaClient;  // 注意是EurekaClient不是DiscoveryClient，该类型未被初始化

    @Scheduled(cron = "*/10 * * * * *")
    public void checkEureka(){
        // 发现所有服务列表 eurekaClient.getApplications();
        // 发现指定服务列表
        Applications applications = eurekaClient.getApplications("dongle-eureka-server");
        for (Application application : applications.getRegisteredApplications()){
            for (InstanceInfo info:application.getInstances()){
                LOGGER.info("ip:{},host:{},home:{}" ,info.getIPAddr() ,info.getHostName(),info.getHomePageUrl());
            }
        }
    }
}
