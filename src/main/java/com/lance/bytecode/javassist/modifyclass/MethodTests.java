package com.lance.bytecode.javassist.modifyclass;

import static org.junit.Assert.assertEquals;

import com.lance.bytecode.BankTransactions;
import com.lance.bytecode.BankAccount;
import com.lance.bytecode.ImportantLog;
import com.sun.tools.corba.se.idl.StringGen;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.junit.Test;

/**
 * Created by lihua on 20/4/2017.
 * email: lihua@seczone.cn
 */
public class MethodTests {

  private static final String TEST_CLASS = "com.lance.bytecode.BankTransactions";

  @Test
  public void testAddNewMethod() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);

    CtMethod newMethod = CtNewMethod.make(
        "public String newLogin(String username, String password) { System.out.println(\"testAddNewMethod\"); return \"testAddNewMethod\";}",
        cc);
    cc.addMethod(newMethod);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.setBody("{ return newLogin($1,$2); }");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();

    // method call it in class
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("testAddNewMethod", result);

    // call it out of class
    Method method = bankTransactions.getClass()
        .getDeclaredMethod("newLogin", String.class, String.class);
    result = (String) method.invoke(bankTransactions, "lance", "test");
    System.out.println(result);
    assertEquals("testAddNewMethod", result);
  }

  @Test
  public void testAddNewField() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);

    CtField f = CtField.make("public static String version = \"1.0.0\";", cc);
    cc.addField(f);
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    Field version = bankTransactions.getClass().getDeclaredField("version");
    String result = (String) version.get(bankTransactions);
    System.out.println(result);
    assertEquals("1.0.0", result);
  }

  /**
   * Change the method body and set it to empty
   */
  @Test
  public void testEmptyMethod() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.setBody("{ return null; }");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals(null, result);
  }

  /**
   * CtMethod and CtConstructor provide methods insertBefore(), insertAfter(), and addCatch(). They
   * are used for inserting a code fragment into the body of an existing method. The users can
   * specify those code fragments with source text written in Java. Javassist includes a simple Java
   * compiler for processing source text. It receives source text written in Java and compiles it
   * into Java bytecode, which will be inlined into a method body.
   *
   * The methods insertBefore(), insertAfter(), addCatch(), and insertAt() receive a String object
   * representing a statement or a block. A statement is a single control structure like if and
   * while or an expression ending with a semi colon (;). A block is a set of statements surrounded
   * with braces {}. Hence each of the following lines is an example of valid statement or block:
   *
   * System.out.println("Hello");
   * { System.out.println("Hello"); }
   * if (i < 0) { i = -i; }
   *
   * The String object passed to the methods insertBefore(), insertAfter(), addCatch(), and
   * insertAt() are compiled by the compiler included in Javassist. Since the compiler supports
   * language extensions, several identifiers starting with $ have special meaning:
   *
   * $0, $1, $2, ...    	this and actual parameters
   * $args	An array of parameters. The type of $args is Object[].
   * $$	All actual parameters. For example, m($$) is equivalent to m($1,$2,...)
   * $cflow(...)	cflow variable
   * $r	The result type. It is used in a cast expression.
   * $w	The wrapper type. It is used in a cast expression.
   * $_	The resulting value
   * $sig	An array of java.lang.Class objects representing the formal parameter types.
   * $type	A java.lang.Class object representing the formal result type.
   * $class	A java.lang.Class object representing the class currently edited.
   *
   * Details: http://jboss-javassist.github.io/javassist/tutorial/tutorial2.html
   */
  @Test
  public void testInsertBefore() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("login");
    m.insertBefore("{ System.out.println($1); System.out.println($2); }");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    bankTransactions.login("pass", "lance", "test");
  }

  /**
   * The variable $_ represents the resulting value of the method. The type of that variable is the
   * type of the result type (the return type) of the method. If the result type is void, then the
   * type of $_ is Object and the value of $_ is null.
   *
   * Although the compiled code inserted by insertAfter() is executed just before the control
   * normally returns from the method, it can be also executed when an exception is thrown from the
   * method. To execute it when an exception is thrown, the second parameter asFinally to
   * insertAfter() must be true.
   *
   * If an exception is thrown, the compiled code inserted by insertAfter() is executed as a finally
   * clause. The value of $_ is 0 or null in the compiled code. After the execution of the compiled
   * code terminates, the exception originally thrown is re-thrown to the caller. Note that the
   * value of $_ is never thrown to the caller; it is rather discarded.
   */
  @Test
  public void testInsertAfter() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("login");
    m.insertAfter("$_ = \"After\";", true);
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("After", result);
  }

  @Test
  public void testInsertAt() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("login");
    m.insertAt(26, "System.out.println(25);");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("After", result);
  }

  /**
   * addCatch() inserts a code fragment into a method body so that the code fragment is executed
   * when the method body throws an exception and the control returns to the caller. In the source
   * text representing the inserted code fragment, the exception value is referred to with the
   * special variable $e.
   *
   * translates the method body represented by m into something like this:
   *
   * try {
   * the original method body
   * }
   * catch (java.io.IOException e) {
   * System.out.println(e);
   * throw e;
   * }
   *
   * Note that the inserted code fragment must end with a throw or return statement.
   */
  @Test
  public void testInsertAddCatch() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("login");
    CtClass exception = ClassPool.getDefault().get("java.io.IOException");
    m.addCatch("{ System.out.println($e); throw $e; }", exception);
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("lance", result);
  }

  /**
   * In the source text given to setBody(), the identifiers starting with $ have special meaning
   *
   * $0, $1, $2, ...    	this and actual parameters
   * $args	An array of parameters. The type of $args is Object[].
   * $$	All actual parameters.
   * $cflow(...)	cflow variable
   * $r	The result type. It is used in a cast expression.
   * $w	The wrapper type. It is used in a cast expression.
   * $sig	An array of java.lang.Class objects representing the formal parameter types.
   * $type	A java.lang.Class object representing the formal result type.
   * $class	A java.lang.Class object representing the class that declares the method
   * currently edited (the type of $0).
   *
   * Note that $_ is not available.
   */
  @Test
  public void testAlertWholeMethodBody() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.setBody("{System.out.println(\"Before method call\"); return \"WHOLE_METHOD\";}");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("WHOLE_METHOD", result);
  }

  /**
   * Javassist allows modifying only an expression included in a method body.
   * javassist.expr.ExprEditor is a class for replacing an expression in a method body. The users
   * can define a subclass of ExprEditor to specify how an expression is modified.
   *
   * searches the method body represented by cm and replaces all calls to move() in class Point with
   * a block
   */
  @Test
  public void testAlertMethodCall() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.instrument(
        new ExprEditor() {
          public void edit(MethodCall m)
              throws CannotCompileException {
            if (m.getClassName().equals(BankAccount.class.getName())
                && m.getMethodName().equals("setName")) {
              m.replace("{ $1 = \"testAlertMethodCall\"; $_ = $proceed($$); }");
            }
          }
        });
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("testAlertMethodCall", result);
  }

  /**
   * Calling replace() on the parameter to edit() substitutes the given statement or block for the
   * expression. If the given block is an empty block, that is, if replace("{}") is executed, then
   * the expression is removed from the method body. If you want to insert a statement (or a block)
   * before/after the expression, a block like the following should be passed to replace():
   *
   * {
   * before-statements;
   * $_ = $proceed($$);
   * after-statements;
   * }
   */
  @Test
  public void testWrapperMethodBody() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.instrument(
        new ExprEditor() {
          public void edit(MethodCall m)
              throws CannotCompileException {
            if (m.getClassName().equals("Point")
                && m.getMethodName().equals("move")) {
              m.replace(
                  "{ System.out.println(\"Before method call\"); $_ = $proceed($$); System.out.println(\"After method call\");}");
            }
          }
        });
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("test", result);
  }

  /**
   * $sig
   * The value of $sig is an array of java.lang.Class objects that represent the formal parameter
   * types in declaration order.
   *
   * $type
   * The value of $type is an java.lang.Class object representing the formal type of the result
   * value. This variable refers to Void.class if this is a constructor.
   *
   * $class
   * The value of $class is an java.lang.Class object representing the class in which the edited
   * method is declared. This represents the type of $0.
   */
  @Test
  public void testOtherParameters() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("login");
    m.insertBefore(
        "System.out.println(\"\"+$sig);System.out.println(\"\"+$type);System.out.println(\"\"+$class);");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("lance", result);
  }

  /**
   * $cflow means "control flow". This read-only variable returns the depth of the recursive calls
   * to a specific method.
   *
   * Then, $cflow(fact) represents the depth of the recursive calls to the method specified by cm.
   * The value of $cflow(fact) is 0 (zero) when the method is first called whereas it is 1 when the
   * method is recursively called within the method.
   *
   * The value of $cflow is the number of stack frames associated with the specified method cm under
   * the current topmost stack frame for the current thread. $cflow is also accessible within a
   * method different from the specified method cm.
   */
  @Test
  public void testCFlow() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod m = cc.getDeclaredMethod("main");
    m.useCflow("main_track");
    m.insertBefore("System.out.println(\"login flow: \" + $cflow(main_track));");
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    bankTransactions.main(null);
  }

  /**
   * CtClass, CtMethod, CtField and CtConstructor provides a convenient method getAnnotations() for
   * reading annotations. It returns an annotation-type object.
   *
   * To use getAnnotations(), annotation types such as Author must be included in the current class
   * path. They must be also accessible from a ClassPool object. If the class file of an annotation
   * type is not found, Javassist cannot obtain the default values of the members of that annotation
   * type.
   */
  @Test
  public void testAnnotations() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);

    CtMethod m = cc.getDeclaredMethod("login");
    Object[] all = m.getAnnotations();
    ImportantLog a = (ImportantLog) all[0];

    String result = "";
    for (String field : a.fields()) {
      System.out.println("first: " + field);
      result += field;
    }

    assertEquals("12", result);
  }

  @Test
  public void testInsertStringBefore() throws Exception {
    ClassPool pool = ClassPool.getDefault();

    CtClass cc = pool.get("java.lang.String");
    CtMethod m = cc.getDeclaredMethod("toString");
    m.insertBefore("{ System.out.println(\"I am tester\");}");

    cc.writeFile();
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    classLoader.loadClass("java.lang.String");
    ProtectionDomain domain = String.class.getProtectionDomain();

    String o = "anything";
    System.out.println(o.toString());

  }
}
