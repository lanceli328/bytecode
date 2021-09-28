package com.lance.bytecode.stackframe;

import org.junit.Test;

/**
 * Created by lihua on 8/11/2017. email: lihua@qq.com
 */
public class FrameTest {
  private String str1;
  private String str2 = "lance";
  private int a = 0;

  private int add(int x,int y){
    a = x + y;
    return a;
  }

  private int calcatue(int m){

    if (m>0){
      int n = 1;
      m = n + add(m,n);
    } else if (m == 0){
      int t = this.a;
      m = add(m,t);
    }

    return m;
  }

  public static void main(String arg[]){
    int x = 9;
    FrameTest frameTest = new FrameTest();
    int q = frameTest.calcatue(x);
    System.out.println(q);
  }
}
