package io.github.wimdeblauwe.htmx.spring.boot.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.webmvc.autoconfigure.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@AutoConfiguration
@ConditionalOnWebApplication
public class HtmxMvcAutoConfiguration implements WebMvcRegistrations, WebMvcConfigurer {

    private final ObjectMapper objectMapper;
    private final HtmxHandlerMethodHandler handlerMethodHandler;

    HtmxMvcAutoConfiguration() {
        this.objectMapper = JsonMapper.builder().build();
        this.handlerMethodHandler = new HtmxHandlerMethodHandler(this.objectMapper);
    }

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new HtmxRequestMappingHandlerMapping();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HtmxHandlerInterceptor(handlerMethodHandler));
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HtmxHandlerMethodArgumentResolver());
        resolvers.add(new HtmxResponseHandlerMethodArgumentResolver());
    }

    @Override
    public ExceptionHandlerExceptionResolver getExceptionHandlerExceptionResolver() {
        return new HtmxExceptionHandlerExceptionResolver(handlerMethodHandler);
    }

    @Bean
    @ConditionalOnBean(View.class)
    @ConditionalOnMissingBean
    HtmxViewResolver htmxViewResolver() {
        HtmxViewResolver resolver = new HtmxViewResolver();
        resolver.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return resolver;
    }

}
