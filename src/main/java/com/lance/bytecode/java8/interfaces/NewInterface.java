package com.lance.bytecode.java8.interfaces;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@qq.com
 */
public interface NewInterface {

  static int count2(){
    return 2;
  }

  default int count(){
    return 1;
  }

  int add();
}
