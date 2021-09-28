package com.lance.bytecode.cglib.delegate;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class SimpleMulticastBean implements DelegatationProvider {

  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
