package io.github.wimdeblauwe.htmx.spring.boot.mvc;

import org.springframework.core.annotation.AliasFor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@HxRequest
@RequestMapping(
        method = {RequestMethod.GET}
)
public @interface HxGetMapping {
    @AliasFor(annotation = RequestMapping.class, attribute = "value")
    String value();
}
