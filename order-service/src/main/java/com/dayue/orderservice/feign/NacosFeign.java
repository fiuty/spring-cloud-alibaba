package com.dayue.orderservice.feign;

import com.dayue.orderservice.vo.nacos.NacosTokenVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * nacos openapi
 *
 * @author zhengdayue
 * @time 2022/8/10 0:53
 */
@FeignClient(value = "nacosFeign",url = "http://192.168.217.1:8848/")
public interface NacosFeign {

    // 获取token
    @PostMapping("/nacos/v1/auth/login")
    NacosTokenVo getTokenInfo(@RequestParam("username") String username, @RequestParam("password") String password);

    // 获取配置信息
    // http://192.168.217.1:8848/nacos/v1/cs/configs?dataId=order-service.yaml&group=DEFAULT_GROUP&tenant=14f98fae-ffb6-4f45-9cbf-ec2163d0d60a&accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTY2MDA3OTEyOH0.1sAuiBPteTD87MhwPdIGI_CECDcjZB83FWcSUWtwgg0

    // 修改实例metadata
    // http://192.168.217.1:8848/nacos/v1/ns/instance?serviceName=order-service&groupName=DEFAULT_GROUP&ip=192.168.217.1&port=8080&namespaceId=14f98fae-ffb6-4f45-9cbf-ec2163d0d60a&metadata=%7B%22preserved.register.source%22:%22SPRING_CLOUD%22,%22version-id%22:%22v1%22%7D&accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTY2MDA3OTEyOH0.1sAuiBPteTD87MhwPdIGI_CECDcjZB83FWcSUWtwgg0

    // 修改nacos配置内容
    // http://192.168.217.1:8848/nacos/v1/cs/configs?dataId=order-service.yaml&group=DEFAULT_GROUP&tenant=14f98fae-ffb6-4f45-9cbf-ec2163d0d60a&accessToken=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYWNvcyIsImV4cCI6MTY2MDA3OTEyOH0.1sAuiBPteTD87MhwPdIGI_CECDcjZB83FWcSUWtwgg0&content=useLocalCache:%20true%0Aname:%20name&type=yaml

    // 获取服务列表
    // http://192.168.217.1:8848/nacos/v1/ns/instance/list?serviceName=order-service&groupName=DEFAULT_GROUP&namespaceId=14f98fae-ffb6-4f45-9cbf-ec2163d0d60a

}
