package com.dayue.gateway.config;

import com.dayue.gateway.constants.GrayConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhengdayue
 * @time 2022/9/12 21:40
 */
public class GrayLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private static final Log log = LogFactory.getLog(GrayLoadBalancer.class);
    final AtomicInteger position;
    final String serviceId;
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider, serviceId, (new Random()).nextInt(1000));
    }

    public GrayLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        RequestDataContext context = (RequestDataContext)request.getContext();
        RequestData clientRequest = context.getClientRequest();
        HttpHeaders headers = clientRequest.getHeaders();
        List<String> versionIds = headers.get(GrayConstants.VERSION_ID);
        ServiceInstanceListSupplier supplier = this.serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map((serviceInstances) -> this.processInstanceResponse(supplier, serviceInstances, versionIds));
    }

    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier, List<ServiceInstance> serviceInstances,
                                                              List<String> versionIds) {
        Response<ServiceInstance> serviceInstanceResponse = this.getInstanceResponse(serviceInstances, versionIds);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }


    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, List<String> versionIds) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + this.serviceId);
            }
            return new EmptyResponse();
        } else {
            List<ServiceInstance> newInstances = new ArrayList<>(0);
            // 过滤出version-id的版本出来
            if (versionIds != null && versionIds.size() != 0) {
                newInstances = instances.stream().filter(item -> {
                    Map<String, String> metadata = item.getMetadata();
                    String value = metadata.get(GrayConstants.VERSION_ID);
                    return Objects.equals(value, versionIds.get(0));
                }).collect(Collectors.toList());
            }
            // 防止过滤完无实例可负载均衡，如为空，则用原先的实例列表进行负载均衡
            if (newInstances.isEmpty()) {
                newInstances = instances;
            }
            int pos = Math.abs(this.position.incrementAndGet());
            ServiceInstance instance = newInstances.get(pos % instances.size());
            return new DefaultResponse(instance);
        }
    }

}
