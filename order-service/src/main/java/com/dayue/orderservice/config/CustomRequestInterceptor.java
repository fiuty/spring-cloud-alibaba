package com.dayue.orderservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author zhengdayue
 * @time 2022/7/28 21:54
 */
@Configuration
public class CustomRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if(Objects.isNull(ra)){
            return;
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            // 复制请求头,把版本号带进去
            if (name.equals("version-id")) {
                String value = request.getHeader(name);
                requestTemplate.header("version-id", value);
            }
        }
    }
}
