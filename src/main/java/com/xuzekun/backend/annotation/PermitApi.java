package com.xuzekun.backend.annotation;

import java.lang.annotation.*;

/**
 * Spring Security 放开接口请求权限注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermitApi {

}
