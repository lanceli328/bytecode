package com.lance.bytecode.javassist.modifyclass;

import static org.junit.Assert.assertEquals;

import com.lance.bytecode.BankTransactions;
import com.lance.bytecode.BankAccount;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.expr.ConstructorCall;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;
import org.junit.Test;

/**
 * Created by lihua on 20/4/2017.
 * email: lihua@qq.com
 */
public class MethodAnalyserTests {

  private static final String TEST_CLASS = "com.lance.bytecode.BankTransactions";
  private static final String ACCOUNT_CLASS = "com.lance.bytecode.BankAccount";

  /**
   * javassist.expr.MethodCall
   *
   * A MethodCall object represents a method call. The method replace() in MethodCall substitutes a
   * statement or a block for the method call. It receives source text representing the substitued
   * statement or block, in which the identifiers starting with $ have special meaning as in the
   * source text passed to insertBefore().
   *
   * $0	The target object of the method call. This is not equivalent to this, which represents the
   * caller-side this object. $0 is null if the method is static.
   *
   *
   * $1, $2, ...    	The parameters of the method call.
   * $_	The resulting value of the method call.
   * $r	The result type of the method call.
   * $class    	A java.lang.Class object representing the class declaring the method.
   * $sig    	An array of java.lang.Class objects representing the formal parameter types.
   * $type    	A java.lang.Class object representing the formal result type.
   * $proceed    	The name of the method originally called in the expression. Here the method call
   * means the one represented by the MethodCall object.
   *
   * The other identifiers such as $w, $args and $$ are also available.
   *
   * Unless the result type of the method call is void, a value must be assigned to $_ in the source
   * text and the type of $_ is the result type. If the result type is void, the type of $_ is
   * Object and the value assigned to $_ is ignored.
   *
   * $proceed is not a String value but special syntax. It must be followed by an argument list
   * surrounded by parentheses ( ).
   *
   * Check all method calls in method and then do something:
   */
  @Test
  public void testMethodCall() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.instrument(
        new ExprEditor() {
          public void edit(MethodCall m)
              throws CannotCompileException {
            StringBuilder builder = new StringBuilder("");
            builder.append(m.getClassName() + "\n");
            builder.append(m.getFileName() + "\n");
            builder.append(m.getLineNumber() + "\n");
            builder.append(m.getMethodName() + "\n");
            builder.append(m.getSignature() + "\n");
            builder.append(m.getEnclosingClass() + "\n");
            System.out.println(builder.toString());
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
   * javassist.expr.ConstructorCall
   *
   * A ConstructorCall object represents a constructor call such as this() and super included in a
   * constructor body. The method replace() in ConstructorCall substitutes a statement or a block
   * for the constructor call. It receives source text representing the substituted statement or
   * block, in which the identifiers starting with $ have special meaning as in the source text
   * passed to insertBefore().
   *
   * $0	The target object of the constructor call. This is equivalent to this. $1, $2, ...    	The
   * parameters of the constructor call. $class    	A java.lang.Class object representing the class
   * declaring the constructor. $sig    	An array of java.lang.Class objects representing the formal
   * parameter types. $proceed    	The name of the constructor originally called in the expression.
   *
   * Here the constructor call means the one represented by the ConstructorCall object.
   *
   * The other identifiers such as $w, $args and $$ are also available.
   *
   * Since any constructor must call either a constructor of the super class or another constructor
   * of the same class, the substituted statement must include a constructor call, normally a call
   * to $proceed().
   *
   * $proceed is not a String value but special syntax. It must be followed by an argument list
   * surrounded by parentheses ( ).
   */
  @Test
  public void testConstructorCall() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass accountClass = pool.get(ACCOUNT_CLASS);
    accountClass.instrument(
        new ExprEditor() {
          public void edit(ConstructorCall m)
              throws CannotCompileException {
            StringBuilder builder = new StringBuilder("");
            builder.append(m.isSuper() + "\n");
            builder.append(m.getClassName() + "\n");
            builder.append(m.getFileName() + "\n");
            builder.append(m.getLineNumber() + "\n");
            builder.append(m.getMethodName() + "\n");
            builder.append(m.getSignature() + "\n");
            String src = "super($$); System.out.println(\"testConstructorCall\"); this.name=\"testConstructorCall\";";

            try {
              m.replace(src);
            } catch (Exception e) {
              e.printStackTrace();
            }

            System.out.println(builder.toString());
          }
        }
    );
    accountClass.writeFile("javassist");

    //add to class loader
    pool.toClass(accountClass);

    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.instrument(
        new ExprEditor() {
          public void edit(MethodCall m)
              throws CannotCompileException {
            if (m.getClassName().equals(BankAccount.class.getName())
                && m.getMethodName().equals("setName")) {
              m.replace("{ }");
            }
          }
        });
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("testConstructorCall", result);
  }

  /**
   * javassist.expr.FieldAccess
   *
   * A FieldAccess object represents field access. The method edit() in ExprEditor receives this
   * object if field access is found. The method replace() in FieldAccess receives source text
   * representing the substitued statement or block for the field access.
   *
   * In the source text, the identifiers starting with $ have special meaning:
   *
   * $0	The object containing the field accessed by the expression. This is not equivalent to this.
   * this represents the object that the method including the expression is invoked on. $0 is null
   * if the field is static.
   *
   *
   * $1	The value that would be stored in the field if the expression is write access. Otherwise, $1
   * is not available.
   *
   * $_	The resulting value of the field access if the expression is read access. Otherwise, the
   * value stored in $_ is discarded.
   *
   * $r	The type of the field if the expression is read access. Otherwise, $r is void.
   *
   * $class A java.lang.Class object representing the class declaring the field. $type	A
   * java.lang.Class object representing the field type. $proceed  The name of a virtual method
   * executing the original field access. . The other identifiers such as $w, $args and $$ are also
   * available.
   *
   * If the expression is read access, a value must be assigned to $_ in the source text. The type
   * of $_ is the type of the field.
   */
  @Test
  public void testFieldAccess() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass accountClass = pool.get(ACCOUNT_CLASS);
    accountClass.instrument(
        new ExprEditor() {
          public void edit(FieldAccess m)
              throws CannotCompileException {
            StringBuilder builder = new StringBuilder("");
            builder.append(m.getClassName() + "\n");
            builder.append(m.getFileName() + "\n");
            builder.append(m.getLineNumber() + "\n");
            builder.append(m.getFieldName() + "\n");
            builder.append(m.getSignature() + "\n");
            String src = "$_=\"testFieldAccess\";";

            try {
              m.replace(src);
            } catch (Exception e) {
              e.printStackTrace();
            }

            System.out.println(builder.toString());
          }
        }
    );
    accountClass.writeFile("javassist");

    //add to class loader
    pool.toClass(accountClass);

    CtClass cc = pool.get(TEST_CLASS);
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.instrument(
        new ExprEditor() {
          public void edit(MethodCall m)
              throws CannotCompileException {
            if (m.getClassName().equals(BankAccount.class.getName())
                && m.getMethodName().equals("setName")) {
              m.replace("{ }");
            }
          }
        });
    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("testFieldAccess", result);
  }

  /**
   * javassist.expr.NewExpr
   *
   * A NewExpr object represents object creation with the new operator (not including array
   * creation). The method edit() in ExprEditor receives this object if object creation is found.
   * The method replace() in NewExpr receives source text representing the substitued statement or
   * block for the object creation.
   *
   * In the source text, the identifiers starting with $ have special meaning:
   *
   * $0	null. $1, $2, ...    	The parameters to the constructor. $_	  The resulting value of the
   * object creation. A newly created object must be stored in this variable.
   *
   * $r	The type of the created object. $sig    	An array of java.lang.Class objects representing
   * the formal parameter types. $type    	A java.lang.Class object representing the class of the
   * created object. $proceed    	The name of a virtual method executing the original objec
   * creation.
   *
   * The other identifiers such as $w, $args and $$ are also available.
   */
  @Test
  public void testNewExpr() throws Exception {
    ClassPool pool = ClassPool.getDefault();
    CtClass cc = pool.get(TEST_CLASS);
    pool.importPackage("com.lance.bytecode");
    CtMethod cm = cc.getDeclaredMethod("login");
    cm.instrument(
        new ExprEditor() {
          public void edit(NewExpr m)
              throws CannotCompileException {
            StringBuilder builder = new StringBuilder("");
            builder.append(m.getClassName() + "\n");
            builder.append(m.getFileName() + "\n");
            builder.append(m.getLineNumber() + "\n");
            builder.append(m.getSignature() + "\n");
            builder.append(m.getEnclosingClass() + "\n");
            System.out.println(builder.toString());
            if (m.getClassName().equals(BankAccount.class.getName())) {
              m.replace(
                  "{ BankAccount _tmp_account = new BankAccount(); _tmp_account.setName(\"testNewExpr\"); $_ = _tmp_account; }");
            }
          }

          public void edit(MethodCall m)
              throws CannotCompileException {
            if (m.getClassName().equals(BankAccount.class.getName())
                && m.getMethodName().equals("setName")) {
              m.replace("{ }");
            }
          }
        });

    BankTransactions bankTransactions = (BankTransactions) cc.toClass().newInstance();
    String result = bankTransactions.login("pass", "lance", "test");
    System.out.println(result);
    assertEquals("testNewExpr", result);
  }

  /**
   * javassist.expr.NewArray
   *
   * A NewArray object represents array creation with the new operator. The method edit() in
   * ExprEditor receives this object if array creation is found. The method replace() in NewArray
   * receives source text representing the substitued statement or block for the array creation.
   *
   * In the source text, the identifiers starting with $ have special meaning:
   *
   * $0	null. $1, $2, ...    	The size of each dimension. $_	The resulting value of the array
   * creation. A newly created array must be stored in this variable.
   *
   * $r	The type of the created array. $type    	A java.lang.Class object representing the class of
   * the created array. $proceed    	The name of a virtual method executing the original array
   * creation.
   *
   * The other identifiers such as $w, $args and $$ are also available.
   *
   * For example, if the array creation is the following expression,
   *
   * String[][] s = new String[3][4]; then the value of $1 and $2 are 3 and 4, respectively. $3 is
   * not available. If the array creation is the following expression,
   *
   * String[][] s = new String[3][]; then the value of $1 is 3 but $2 is not available.
   */
  @Test
  public void testNewArray() {

  }

  /**
   * javassist.expr.Instanceof
   *
   * A Instanceof object represents an instanceof expression. The method edit() in ExprEditor
   * receives this object if an instanceof expression is found. The method replace() in Instanceof
   * receives source text representing the substitued statement or block for the expression.
   *
   * In the source text, the identifiers starting with $ have special meaning:
   *
   * $0	null. $1	The value on the left hand side of the original instanceof operator. $_	The
   * resulting value of the expression. The type of $_ is boolean. $r	The type on the right hand
   * side of the instanceof operator. $type	A java.lang.Class object representing the type on the
   * right hand side of the instanceof operator. $proceed    	The name of a virtual method executing
   * the original instanceof expression. It takes one parameter (the type is java.lang.Object) and
   * returns true if the parameter value is an instance of the type on the right hand side of the
   * original instanceof operator. Otherwise, it returns false.
   *
   * The other identifiers such as $w, $args and $$ are also available.
   */
  @Test
  public void testInstanceof() {

  }

  /**
   * A Cast object represents an expression for explicit type casting. The method edit() in
   * ExprEditor receives this object if explicit type casting is found. The method replace() in Cast
   * receives source text representing the substitued statement or block for the expression.
   *
   * In the source text, the identifiers starting with $ have special meaning:
   *
   * $0	null. $1	The value the type of which is explicitly cast. $_	The resulting value of the
   * expression. The type of $_ is the same as the type after the explicit casting, that is, the
   * type surrounded by ( ).
   *
   * $r	the type after the explicit casting, or the type surrounded by ( ). $type	A java.lang.Class
   * object representing the same type as $r. $proceed    	The name of a virtual method executing
   * the original type casting. It takes one parameter of the type java.lang.Object and returns it
   * after the explicit type casting specified by the original expression.
   *
   * The other identifiers such as $w, $args and $$ are also available.
   */
  @Test
  public void testCast() {

  }

  /**
   * javassist.expr.Handler
   *
   * A Handler object represents a catch clause of try-catch statement. The method edit() in
   * ExprEditor receives this object if a catch is found. The method insertBefore() in Handler
   * compiles the received source text and inserts it at the beginning of the catch clause.
   *
   * In the source text, the identifiers starting with $ have meaning:
   *
   * $1	The exception object caught by the catch clause.
   * $r	the type of the exception caught by the
   * catch clause. It is used in a cast expression.
   * $w	The wrapper type. It is used in a cast expression.
   * $type    	A java.lang.Class object representing the type of the exception caught by
   * the catch clause.
   *
   * If a new exception object is assigned to $1, it is passed to the original catch clause as the
   * caught exception.
   */
  @Test
  public void testHandler() {

  }

}
