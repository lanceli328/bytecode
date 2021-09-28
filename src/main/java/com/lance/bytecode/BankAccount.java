package com.lance.bytecode;

/**
 * Created by lihua on 20/4/2017.
 * email: lihua@qq.com
 */
public class BankAccount {
  private String name = "test";

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  private String password = "pass";

  public String getName(){
    return this.name;
  }

  public void setName(String name){
    this.name = name;
  }

}
