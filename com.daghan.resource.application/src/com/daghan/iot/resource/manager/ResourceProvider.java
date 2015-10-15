package com.daghan.iot.resource.manager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ResourceProvider {

	String url() default "/";
	Class<?> resourceInputInterface() default Object.class;
	Class<?> resourceOutputInterface() default Object.class;
}