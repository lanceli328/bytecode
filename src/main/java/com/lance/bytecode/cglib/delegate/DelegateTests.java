package com.lance.bytecode.cglib.delegate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.lance.bytecode.cglib.SampleBean;
import net.sf.cglib.reflect.ConstructorDelegate;
import net.sf.cglib.reflect.MethodDelegate;
import net.sf.cglib.reflect.MulticastDelegate;
import org.junit.Test;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@seczone.cn
 */
public class DelegateTests {

  /**
   * There are however some things to note:
   *
   * A9. The factory method MethodDelegate#create takes exactly one method name as its second
   * argument. This is the method the MethodDelegate will proxy for you. B). There must be a method
   * without arguments defined for the object which is given to the factory method as its first
   * argument. Thus, the MethodDelegate is not as strong as it could be. C). The third argument must
   * be an interface with exactly one argument. The MethodDelegate implements this interface and can
   * be cast to it. When the method is invoked, it will call the proxied method on the object that
   * is the first argument.
   *
   * Furthermore, consider these drawbacks: A). cglib creates a new class for each proxy.
   * Eventually, this will litter up your permanent generation heap space B). You cannot proxy
   * methods that take arguments. C). If your interface takes arguments, the method delegation will
   * simply not work without an exception thrown (the return value will always be null).If your
   * interface requires another return type (even if that is more general), you will get a
   * IllegalArgumentException.
   */
  @Test
  public void testMethodDelegate() throws Exception {
    SampleBean bean = new SampleBean();
    bean.setValue("Hello cglib!");
    BeanDelegate delegate = (BeanDelegate) MethodDelegate.create(
        bean, "getValue", BeanDelegate.class);
    assertEquals("Hello cglib!", delegate.getValueFromDelegate());
  }

  /**
   * Based on this interface-backed bean we can create a MulticastDelegate that dispatches all calls
   * to setValue(String) to several classes that implement the DelegationProvider interface.
   *
   * Again, there are some drawbacks:
   * A). The objects need to implement a single-method interface.
   * This sucks for third-party libraries and is awkward when you use CGlib to do some magic where
   * this magic gets exposed to the normal code. Also, you could implement your own delegate easily
   * (without byte code though but I doubt that you win so much over manual delegation).
   * B). When
   * your delegates return a value, you will receive only that of the last delegate you added. All
   * other return values are lost (but retrieved at some point by the multicast delegate).
   */
  @Test
  public void testMulticastDelegate() throws Exception {
    MulticastDelegate multicastDelegate = MulticastDelegate.create(
        DelegatationProvider.class);
    SimpleMulticastBean first = new SimpleMulticastBean();
    SimpleMulticastBean second = new SimpleMulticastBean();
    multicastDelegate = multicastDelegate.add(first);
    multicastDelegate = multicastDelegate.add(second);

    DelegatationProvider provider = (DelegatationProvider) multicastDelegate;
    provider.setValue("Hello world!");

    assertEquals("Hello world!", first.getValue());
    assertEquals("Hello world!", second.getValue());
  }

  /**
   * A ConstructorDelegate allows to create a byte-instrumented factory method. For that, that we
   * first require an interface with a single method newInstance which returns an Object and takes
   * any amount of parameters to be used for a constructor call of the specified class. For example,
   * in order to create a ConstructorDelegate for the SampleBean, we require the following to call
   * SampleBean's default (no-argument) constructor
   */
  @Test
  public void testConstructorDelegate() throws Exception {
    SampleBeanConstructorDelegate constructorDelegate = (SampleBeanConstructorDelegate) ConstructorDelegate
        .create(
            SampleBean.class, SampleBeanConstructorDelegate.class);
    SampleBean bean = (SampleBean) constructorDelegate.newInstance();
    bean.setValue("Hello world!");
    assertTrue(SampleBean.class.isAssignableFrom(bean.getClass()));
    assertEquals("Hello world!", bean.getValue());
  }


}
