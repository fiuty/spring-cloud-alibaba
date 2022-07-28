package com.dayue.orderservice.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.*;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhengdayue
 * @time 2022/7/25 0:15
 */
public class GrayConfiguration implements ReactorServiceInstanceLoadBalancer {
    private static final Log log = LogFactory.getLog(GrayConfiguration.class);
    final AtomicInteger position;
    final String serviceId;
    ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;

    public GrayConfiguration(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId) {
        this(serviceInstanceListSupplierProvider, serviceId, (new Random()).nextInt(1000));
    }

    public GrayConfiguration(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, int seedPosition) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(seedPosition);
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        RequestDataContext context = (RequestDataContext)request.getContext();
        RequestData clientRequest = context.getClientRequest();
        HttpHeaders headers = clientRequest.getHeaders();
        List<String> versionIds = headers.get("version-id");
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
            // 过滤出version-id的版本出来
            if (versionIds != null && versionIds.size() != 0) {
                instances = instances.stream().filter(item -> {
                    Map<String, String> metadata = item.getMetadata();
                    String value = metadata.get("version-id");
                    return Objects.equals(value, versionIds.get(0));
                }).collect(Collectors.toList());
            }
            int pos = Math.abs(this.position.incrementAndGet());
            ServiceInstance instance = instances.get(pos % instances.size());
            return new DefaultResponse(instance);
        }
    }

}
