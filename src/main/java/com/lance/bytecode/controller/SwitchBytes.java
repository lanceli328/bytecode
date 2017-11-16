package com.lance.bytecode.controller;

import org.junit.Test;

/**
 * Created by lihua on 9/11/2017. email: lihua@seczone.cn
 */
public class SwitchBytes {

  private int tableSwitch(int x){
    int y = -1;
    switch (x){
      case 0:
        y = 0;
        break;
      case 1:
        return 1;
      case 2:
        return 2;
      default:
        return -1;
    }
    return y;
  }

  private int lookupSwitch(int x){
    int y = -1;
    switch (x){
      case 0:
        y = 0;
        break;
      case 100:
        return 1;
      case 200:
        return 2;
      default:
        return -1;
    }
    return y;
  }

  private int stringSwitch(String x){
    String y = "-1";
    switch (x){
      case "ab":
        y = "y0";
        break;
      case "bcsdfsfafadfddafdsdfadsfdafdfdfdfdfsdafdadfrhdfggffdfdfdsafsdfdafdfdafafdaddfdfadfdfdadfssdf":
        return 1;
      case "c":
        return 2;
      default:
        return -1;
    }
    return Integer.valueOf(y);
  }

  /**
   *
   * True --> 1
   * False --> 0
   *
   * @param x
   * @return
   */
  private int duplicateHashSwitch(String x){
    String y = "-1";
    switch (x){
      case "FB":
        y = "y0";
        break;
      case "Ea":
        return 1;
      default:
        return -1;
    }
    return Integer.valueOf(y);
  }

  @Test
  public void test(){
    System.out.println("just compile");
  }
}
