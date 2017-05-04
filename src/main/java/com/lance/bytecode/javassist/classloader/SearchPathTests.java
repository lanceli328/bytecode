package com.lance.bytecode.javassist.classloader;

import static org.junit.Assert.assertEquals;

import com.lance.bytecode.BankTransactions;
import javassist.ByteArrayClassPath;
import javassist.ClassClassPath;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.URLClassPath;
import org.junit.Test;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@seczone.cn
 */
public class SearchPathTests {

  @Test
  public void testAddSearchPath() throws Exception {
    ClassPool pool = ClassPool.getDefault();

    ClassLoader classLoader = ClassLoader.getSystemClassLoader();

    //CtClass cc = pool.get("com.lance.bytecode.BankTransactions");
    byte[] b = pool.get("com.lance.bytecode.BankTransactions").toBytecode();
    String className = BankTransactions.class.getName();
    pool.insertClassPath(new ByteArrayClassPath(className, b));
    CtClass cc = pool.get(className);
    assertEquals(BankTransactions.class.getName(), cc.getName());

    //with class
    pool.insertClassPath(new ClassClassPath(this.getClass()));

    //with local path
    pool.insertClassPath("/usr/local/javalib");

    //with URL
    ClassPath cp = new URLClassPath("www.javassist.org", 80, "/java/", "org.javassist.");
    pool.insertClassPath(cp);
  }

  /**
   * Multiple ClassPool objects can be cascaded like java.lang.ClassLoader. For example,
   *
   * ClassPool parent = ClassPool.getDefault();
   * ClassPool child = new ClassPool(parent);
   * child.insertClassPath("./classes");
   *
   * If child.get() is called, the child ClassPool first
   * delegates to the parent ClassPool. If the parent ClassPool fails to find a class file, then the
   * child ClassPool attempts to find a class file under the ./classes directory.
   *
   * If child.childFirstLookup is true, the child ClassPool attempts to find a class file before
   * delegating to the parent ClassPool. For example,
   */
  @Test
  public void testCascadedClassPool() throws Exception {
    ClassPool parent = ClassPool.getDefault();
    ClassPool child = new ClassPool(parent);
    child.insertClassPath("./classes");

    // changes the behavior of the child.
    child.childFirstLookup = true;
  }


}
