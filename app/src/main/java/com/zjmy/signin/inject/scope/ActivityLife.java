package com.zjmy.signin.inject.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * @Description:
 * @authors: utopia
 * @Create time: 17-1-9 上午9:32
 * @Update time: 17-1-9 上午9:32
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)//1.SOURCE:在源文件中有效（即源文件保留）2.CLASS:在class文件中有效（即class保留）3.RUNTIME:在运行时有效（即运行时保留）
public @interface ActivityLife {
}
