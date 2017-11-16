package com.lance.bytecode.java8.interfaces;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@seczone.cn
 */
public class NewClass implements NewInterface {

  @Override
  public int add() {
    int a = this.count();
    int b = NewInterface.count2();
    int c = a + b;
    return c;
  }
}
