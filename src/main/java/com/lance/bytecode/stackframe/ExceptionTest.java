package com.lance.bytecode.stackframe;

import org.junit.Test;

/**
 * Created by lihua on 8/11/2017. email: lihua@seczone.cn
 */
public class ExceptionTest {

  private int add(int x, int y) throws Exception{
    try {
      try{
        if (x<0) {
          throw new Exception("x is less than 0");
        } else {
          return x + y;
        }
      } finally {
        System.err.println("finally method");
        return 0;
      }
    } catch (Exception e){
      System.err.println("catch exception method");
      return 1;
    }
  }

  @Test
  public void test() throws Exception{
    int z = add(3,3);
    System.out.println(z);
    int m = add(-1, 3);
    System.out.println(m);
  }

}
