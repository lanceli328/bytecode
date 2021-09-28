package com.lance.bytecode.java8.interfaces;

import org.junit.Test;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@qq.com
 */
public class Tests {

  @Test
  public void test(){
    NewClass obj = new NewClass();
    int res = obj.add();
    System.out.println(res);
  }

}
