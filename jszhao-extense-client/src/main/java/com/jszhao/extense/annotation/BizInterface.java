package com.jszhao.extense.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BizInterface {
    /**
     * 接口名
     *
     * @return
     */
    Class interfaceName();
}
