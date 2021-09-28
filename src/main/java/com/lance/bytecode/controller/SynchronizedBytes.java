package com.lance.bytecode.controller;

import org.junit.Test;

/**
 * Created by lihua on 9/11/2017. email: lihua@qq.com
 */
public class SynchronizedBytes {

  public synchronized void sayGoodbye() {
    System.out.println("say good bye");
  }

  public synchronized static void sayHi() {
    System.out.println("say hi");
  }

  public void sayHello() {
    synchronized (SynchronizedBytes.class) {
      System.out.println("say hello");
    }
  }

  public void sayHelloThis() {
    synchronized (this) {
      System.out.println("say hello");
    }
  }

  @Test
  public void test(){
    Thread.currentThread().getContextClassLoader();
    System.out.println("just compile");
  }
}
