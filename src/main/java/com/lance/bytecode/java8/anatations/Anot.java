package com.lance.bytecode.java8.anatations;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@seczone.cn
 */

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
/**
 * 重复注解机制本身必须用 @Repeatable 注解。
 */
@Repeatable(Anots.class)
@interface Anot {
  String value();
}
