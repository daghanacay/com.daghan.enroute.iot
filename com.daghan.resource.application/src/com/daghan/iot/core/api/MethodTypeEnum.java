package com.daghan.iot.core.api;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Associates a method type to an annotation. All these methods can be requested
 * from a device. Implementing device should annotate the method to be called by
 * the annotation defined in this enumeration.
 * 
 * @author daghan
 *
 */
public enum MethodTypeEnum {
	// Call the GET method on the device.
	GET(GetMethod.class),
	// Call the POST method on the device.
	POST(PostMethod.class),
	// Call the DELETE method on the service
	DELETE(DeleteMethod.class);
	Class<? extends Annotation> annotation;

	MethodTypeEnum(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	public Class<? extends Annotation> getAnnotation() {
		return annotation;
	}

	/**
	 * Provides the get method type. The method will be called when the GET
	 * request is done on the request
	 * 
	 * @author daghan
	 *
	 */

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface GetMethod {
	}

	/**
	 * Provides the get method type. The method will be called when the GET
	 * request is done on the request
	 * 
	 * @author daghan
	 *
	 */

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface PostMethod {
	}

	/**
	 * Provides the get method type. The method will be called when the GET
	 * request is done on the request
	 * 
	 * @author daghan
	 *
	 */

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface DeleteMethod {
	}
}
