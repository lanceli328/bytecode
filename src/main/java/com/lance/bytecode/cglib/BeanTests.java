package com.lance.bytecode.cglib;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.beans.BulkBean;
import net.sf.cglib.beans.ImmutableBean;
import org.junit.Test;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@seczone.cn
 */
public class BeanTests {
  /**
   * ImmutableBean
   */
  @Test(expected = IllegalStateException.class)
  public void testImmutableBean() throws Exception {
    SampleBean bean = new SampleBean();
    bean.setValue("Hello world!");
    SampleBean immutableBean = (SampleBean) ImmutableBean.create(bean);
    assertEquals("Hello world!", immutableBean.getValue());
    bean.setValue("Hello world, again!");
    assertEquals("Hello world, again!", immutableBean.getValue());
    immutableBean.setValue("Hello cglib!"); // Causes exception.
  }

  /**
   * BeanGenerator
   */
  @Test
  public void testBeanGenerator() throws Exception {
    BeanGenerator beanGenerator = new BeanGenerator();
    beanGenerator.addProperty("value", String.class);
    Object myBean = beanGenerator.create();

    Method setter = myBean.getClass().getMethod("setValue", String.class);
    setter.invoke(myBean, "Hello cglib!");
    Method getter = myBean.getClass().getMethod("getValue");
    assertEquals("Hello cglib!", getter.invoke(myBean));
  }

  /**
   * Bean copier
   */
  @Test
  public void testBeanCopier() throws Exception {
    BeanCopier copier = BeanCopier.create(SampleBean.class, SampleBean2.class, false);
    SampleBean bean = new SampleBean();
    bean.setValue("Hello cglib!");
    SampleBean2 otherBean = new SampleBean2();
    copier.copy(bean, otherBean, null);
    assertEquals("Hello cglib!", otherBean.getValue());
  }

  /**
   * Bulk Bean The BulkBean takes an array of getter names, an array of setter names and an array of
   * property types as its constructor arguments. The resulting instrumented class can then
   * extracted as an array by BulkBean#getPropertyBalues(Object). Similarly, a bean's properties can
   * be set by BulkBean#setPropertyBalues(Object, Object[]).
   */
  @Test
  public void testBulkBean() throws Exception {
    BulkBean bulkBean = BulkBean.create(SampleBean.class,
        new String[]{"getValue"},
        new String[]{"setValue"},
        new Class[]{String.class});
    SampleBean bean = new SampleBean();
    bean.setValue("Hello world!");
    assertEquals(1, bulkBean.getPropertyValues(bean).length);
    assertEquals("Hello world!", bulkBean.getPropertyValues(bean)[0]);
    bulkBean.setPropertyValues(bean, new Object[]{"Hello cglib!"});
    assertEquals("Hello cglib!", bean.getValue());
  }

  /**
   * Bean Map
   * @throws Exception
   */
  @Test
  public void testBeanMap() throws Exception {
    SampleBean bean = new SampleBean();
    BeanMap map = BeanMap.create(bean);
    bean.setValue("Hello cglib!");
    assertEquals("Hello cglib", map.get("value"));
  }

}
