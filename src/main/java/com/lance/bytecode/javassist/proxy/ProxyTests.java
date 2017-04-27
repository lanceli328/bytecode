package com.lance.bytecode.javassist.proxy;

import com.lance.bytecode.BankTransactions;
import java.lang.reflect.Method;
import javassist.util.proxy.MethodFilter;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;
import org.junit.Test;

/**
 * Created by lihua on 21/4/2017.
 * email: lihua@seczone.cn
 */
public class ProxyTests {

  private static final String TEST_CLASS = "com.lance.bytecode.BankTransactions";

  /**
   * java.lang.Object invoke(java.lang.Object self,
   *                         java.lang.reflect.Method thisMethod,
   *                         java.lang.reflect.Method proceed,
   *                         java.lang.Object[] args) throws java.lang.Throwable
   * Is called when a method is invoked on a proxy instance associated with this handler. This method must
   * process that method invocation.
   *
   * Parameters:
   * self - the proxy instance.
   * thisMethod - the overridden method declared in the super class or interface.
   * proceed - the forwarder method for invoking the overridden method. It is null if the overridden method is abstract or declared in the interface.
   * args - an array of objects containing the values of the arguments passed in the method invocation on the proxy instance.
   *
   * If a parameter type is a primitive type, the type of
   * the array element is a wrapper class.
   */
  @Test
  public void testProxyFactory() throws Exception{
    ProxyFactory f = new ProxyFactory();
    f.setSuperclass(BankTransactions.class);

    // proxy login method
    f.setFilter(new MethodFilter() {
      public boolean isHandled(Method m) {
        return m.getName().equals("login");
      }
    });

    Class c = f.createClass();
    MethodHandler mi = new MethodHandler() {
      public Object invoke(Object self, Method m, Method proceed,
          Object[] args) throws Throwable {
        System.out.println("method name: " + m.getName());
        // execute the original method.
        return proceed.invoke(self, args) + "_Proxy";
      }
    };

    BankTransactions foo = (BankTransactions) c.newInstance();
    ((ProxyObject)foo).setHandler(mi);
    String result = foo.login("test","lance","junit");
    assert(result.endsWith("_Proxy"));
  }
  
}
