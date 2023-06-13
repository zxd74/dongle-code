package spring.eureka;

import com.netflix.appinfo.InstanceInfo;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaRegistryAvailableEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaServerStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Dongle
 * @desc
 * @since 2022/1/29 13:36
 */
@Component
public class EurekaListener {

    @EventListener
    //服务下线监听
    public void listen(EurekaInstanceCanceledEvent event){
        System.err.println(event.getServerId()+"\t"+event.getAppName()+"服务下线");
    }

    @EventListener
    //服务上线监听
    public void listen(EurekaInstanceRegisteredEvent event){
        InstanceInfo instanceInfo = event.getInstanceInfo();
        System.err.println(instanceInfo.getAppName()+instanceInfo.getHostName()+instanceInfo.getIPAddr()+"进行注册");
    }

    @EventListener
    //注册中心启动
    public void listen(EurekaRegistryAvailableEvent event){
        System.err.println("注册中心 启动");
    }

    @EventListener
    //Eureka Server启动
    public void listen(EurekaServerStartedEvent event){
        System.err.println("Eureka Server 启动");
    }
}
