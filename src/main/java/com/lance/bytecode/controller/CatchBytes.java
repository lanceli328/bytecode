package com.lance.bytecode.controller;

import org.junit.Test;

/**
 * Created by lihua on 9/11/2017. email: lihua@qq.com
 */
public class CatchBytes {

  @AnnotationBytes
  public void tryCatchCatchFinally(int i) {
    try {
      i = 2;
    } catch (RuntimeException e) {
      i = 3;
    } finally {
      i = 4;
    }
  }

  @Test
  public void test(){
    System.out.println("just compile");
  }
}
