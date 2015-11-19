/**
 * 
 */
package com.sushant.learning;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Sushant
 *
 */
@Target(value=ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONField {
	String columnName() default "";
	String columnFamily() default "";
}

