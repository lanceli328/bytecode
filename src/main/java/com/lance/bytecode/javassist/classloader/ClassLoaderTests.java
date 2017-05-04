package com.lance.bytecode.javassist.classloader;

import static org.junit.Assert.assertEquals;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Loader;
import javassist.LoaderClassPath;
import javassist.Modifier;
import javassist.Translator;
import javassist.compiler.Javac.CtFieldWithInit;
import org.junit.Test;

/**
 * Created by lihua on 19/4/2017.
 * email: lihua@seczone.cn
 */
public class ClassLoaderTests {

  private static final String TEST_CLASS = "com.lance.bytecode.BankTransactions";

  /**
   * Update a class and load it from class loader
   */
  @Test
  public void testDefaultClassLoader() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    Loader cl = new Loader(pool);

    CtClass ct = pool.get(TEST_CLASS);
    CtField ctField = CtFieldWithInit.make("private String name;", ct);
    ct.addField(ctField);

    // load modified class
    Class c = cl.loadClass(TEST_CLASS);
    Object rect = c.newInstance();

    assertEquals("name", c.getDeclaredField("name").getName());
  }


  @Test
  public void testLoaderClassPath() throws Exception{
    ClassPool pool = ClassPool.getDefault();

    ClassLoader loader = this.getClass().getClassLoader();
    LoaderClassPath classPath = new LoaderClassPath(loader);
    pool.insertClassPath(classPath);

    CtClass ct = pool.get(TEST_CLASS);
    CtField ctField = CtFieldWithInit.make("private String name;", ct);
    ct.addField(ctField);

    Class c = ct.toClass();
    assertEquals("name", c.getDeclaredField("name").getName());
  }

  /**
   * Update a class before class load and then call it
   */
  @Test
  public void testClassTranslator() throws Throwable {
    Translator translator = new MyTranslator();
    ClassPool pool = ClassPool.getDefault();
    Loader cl = new Loader();
    cl.addTranslator(pool, translator);
    cl.run(TEST_CLASS, null);
  }

  /**
   * Update the class when load it and then call it
   */
  @Test
  public void testMyClassLoader() throws Throwable {
    MyClassLoader myClassLoader = new MyClassLoader();
    Class c = myClassLoader.loadClass("com.lance.bytecode.Pairs");
    c.getDeclaredMethod("main", new Class[]{String[].class})
        .invoke(null, new Object[]{null});
  }

  /**
   * The system classes like java.lang.String cannot be loaded by a class loader other than the
   * system class loader. Therefore, SampleLoader or javassist.Loader shown above cannot modify the
   * system classes at loading time.
   *
   * This program produces a file "./java/lang/String.class".
   *
   * To run your program MyApp with this modified String class, do as follows:
   *
   * % java -Xbootclasspath/p:. MyApp arg1 arg2...
   * Suppose that the definition of MyApp is as follows:
   *
   * public class MyApp {
   * public static void main(String[] args) throws Exception {
   * System.out.println(String.class.getField("hiddenValue").getName());
   * }
   * }
   *
   * If the modified String class is correctly loaded, MyApp prints hiddenValue.
   *
   * Note: Applications that use this technique for the purpose of overriding a system class in
   * rt.jar should not be deployed as doing so would contravene the Java 2 Runtime Environment
   * binary code license.
   */
  @Test
  public void testModifySystemClass() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get("java.lang.String");
    CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
    f.setModifiers(Modifier.PUBLIC);
    cc.addField(f);
    cc.writeFile("javassist");
  }

  /**
   * If the JVM is launched with the JPDA (Java Platform Debugger Architecture) enabled, a class is
   * dynamically reloadable. After the JVM loads a class, the old version of the class definition
   * can be unloaded and a new one can be reloaded again. That is, the definition of that class can
   * be dynamically modified during runtime. However, the new class definition must be somewhat
   * compatible to the old one. The JVM does not allow schema changes between the two versions. They
   * have the same set of methods and fields.
   *
   * Javassist provides a convenient class for reloading a class at runtime. For more information,
   * see the API documentation of javassist.tools.HotSwapper.
   */
  @Test
  public void testReloadClassAtRuntime() {

  }
}
