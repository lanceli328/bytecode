package com.lance.bytecode.javassist.writeReadByteCode;

import static org.junit.Assert.assertEquals;

import com.lance.bytecode.BankTransactions;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtField.Initializer;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import org.junit.Test;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class Tests {

  private static final String TEST_CLASS = "com.lance.bytecode.BankTransactions";

  /**
   * Read and write a class
   */
  @Test
  public void testReadWriteClass() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    //cc.setSuperclass(pool.get("test.Point"));
    cc.writeFile("javassist");
    byte[] bytes = cc.toBytecode();
    Class cclass = cc.toClass();
  }

  /**
   * To define a new class from scratch, makeClass() must be called on a ClassPool
   */
  @Test
  public void testCreateNewMethod() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.makeClass("com.lance.bytecode.Point");
    CtMethod ctMethod = CtNewMethod.make("public void test(){System.out.println(1);}", cc);
    cc.addMethod(ctMethod);
    cc.writeFile("javassist");
  }

  @Test
  public void testCreateNewInterface() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.makeInterface("com.lance.bytecode.PointInterface");
    CtClass returnType = pool.get("java.lang.String");
    CtClass[] parameters = {pool.get("java.lang.String")};
    CtMethod ctMethod = CtNewMethod.abstractMethod(returnType, "test", parameters, null, cc);
    cc.addMethod(ctMethod);
    cc.writeFile("javassist");
  }

  @Test
  public void testCreateNewClassFromInputStream() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    InputStream ins = new FileInputStream(
        "target/classes/com/lance/bytecode/BankTransactions.class");
    CtClass cc = pool.makeClass(ins);
    assertEquals(BankTransactions.class.getName(), cc.getName());
  }

  /**
   * This specification of ClassPool may cause huge memory consumption if the number of CtClass
   * objects becomes amazingly large (this rarely happens since Javassist tries to reduce memory
   * consumption in various ways). To avoid this problem, you can explicitly remove an unnecessary
   * CtClass object from the ClassPool. If you call detach() on a CtClass object, then that CtClass
   * object is removed from the ClassPool.
   *
   * You must not call any method on that CtClass
   * object after detach() is called. However, you can call get() on ClassPool to make a new
   * instance of CtClass representing the same class. If you call get(), the ClassPool reads a class
   * file again and newly creates a CtClass object, which is returned by get().
   */
  @Test
  public void testClassPool() throws Exception {
    ClassPool pool = new ClassPool();
    pool.appendSystemPath();
    CtClass cc = pool.get("com.lance.bytecode.BankTransactions");
    cc.writeFile();
    //removed from the ClassPool
    cc.detach();
    //assertEquals(null, cc.getName());
  }

  /**
   * This program first obtains the CtClass object for class Point. Then it calls setName() to give
   * a new name Pair to that CtClass object. After this call, all occurrences of the class name in
   * the class definition represented by that CtClass object are changed from Point to Pair. The
   * other part of the class definition does not change.
   */
  @Test
  public void testChangeClassName() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    // cc1 is identical to cc.
    CtClass cc1 = pool.get(TEST_CLASS);

    cc.setName("com.lance.bytecode.NewName");
    cc.writeFile("target/classes");

    // cc2 is identical to cc.
    CtClass cc2 = pool.get(TEST_CLASS);
    // cc3 is not identical to cc.
    CtClass cc3 = pool.get(TEST_CLASS);

    // use after writeFile was called
    CtClass cc4 = pool.getAndRename("com.lance.bytecode.NewName", "com.lance.bytecode.Point");
    cc4.writeFile("javassist");
  }

  /**
   * Note that the program above depends on the fact that the Hello class is never loaded before
   * toClass() is invoked. If not, the JVM would load the original Hello class before toClass()
   * requests to load the modified Hello class. Hence loading the modified Hello class would be
   * failed (LinkageError is thrown).
   *
   * If the program is running on some application server such as JBoss and Tomcat, the context
   * class loader used by toClass() might be inappropriate. In this case, you would see an
   * unexpected ClassCastException. To avoid this exception, you must explicitly give an appropriate
   * class loader to toClass(). For example, if bean is your session bean object, then the following
   * code:
   *
   * CtClass cc = ...;
   * Class c = cc.toClass(bean.getClass().getClassLoader());
   */
  @Test
  public void testChangeMethod() throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass cc = cp.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("login");
    m.insertBefore("{ System.out.println(\"Please enter username and password:\"); }");
    Class c = cc.toClass();
    BankTransactions h = (BankTransactions) c.newInstance();
    h.login("pass", "lance", "lance");
  }

  @Test
  public void testOthers() throws Exception {
    ClassPool cp = ClassPool.getDefault();
    CtClass ctClass = cp.makeClass("com.slovef.JavassistClass");

    StringBuffer body = null;
    //参数  1：属性类型  2：属性名称  3：所属类CtClass
    CtField ctField = new CtField(cp.get("java.lang.String"), "name", ctClass);
    ctField.setModifiers(Modifier.PRIVATE);
    //设置name属性的get set方法
    ctClass.addMethod(CtNewMethod.setter("setName", ctField));
    ctClass.addMethod(CtNewMethod.getter("getName", ctField));
    ctClass.addField(ctField, Initializer.constant("default"));

    //参数  1：参数类型   2：所属类CtClass
    CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
    body = new StringBuffer();
    body.append("{\n name=\"me\";\n}");
    ctConstructor.setBody(body.toString());
    ctClass.addConstructor(ctConstructor);

    //参数：  1：返回类型  2：方法名称  3：传入参数类型  4：所属类CtClass
    CtMethod ctMethod = new CtMethod(CtClass.voidType, "execute", new CtClass[]{}, ctClass);
    ctMethod.setModifiers(Modifier.PUBLIC);
    body = new StringBuffer();
    body.append("{\n System.out.println(name);");
    body.append("\n System.out.println(\"execute ok\");");
    body.append("\n return ;");
    body.append("\n}");
    ctMethod.setBody(body.toString());
    ctClass.addMethod(ctMethod);
    Class<?> c = ctClass.toClass();
    Object o = c.newInstance();
    Method method = o.getClass().getMethod("execute", new Class[]{});
    //调用字节码生成类的execute方法
    method.invoke(o, new Object[]{});
  }

}
