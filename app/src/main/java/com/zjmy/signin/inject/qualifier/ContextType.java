package com.zjmy.signin.inject.qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * @Description:
 * @authors: utopia
 * @Create time: 17-1-9 上午9:31
 * @Update time: 17-1-9 上午9:31
*/
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface ContextType {
    String value() default "application";
}
