package com.lance.bytecode.java8.anatations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@seczone.cn
 */

@Retention(RetentionPolicy.RUNTIME)

@interface Anots {

  Anot[] value();
}
