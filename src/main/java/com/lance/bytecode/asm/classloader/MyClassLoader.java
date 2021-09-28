package com.lance.bytecode.asm.classloader;

/**
 * Created by lihua on 16/11/2017. email: lihua@qq.com
 */
public class MyClassLoader extends ClassLoader {

  public Class defineClass(String name, byte[] b) {
    return defineClass(name, b, 0, b.length);
  }

}
