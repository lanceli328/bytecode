package com.lance.bytecode.cglib.Mixin;

import static org.junit.Assert.assertEquals;

import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.Mixin;
import org.junit.Test;
import org.objectweb.asm.Type;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@seczone.cn
 */
public class Tests {
  @Test
  public void testMixin() throws Exception {
    Mixin mixin = Mixin.create(new Class[]{Interface1.class, Interface2.class,
        MixinInterface.class}, new Object[]{new Class1(), new Class2()});
    MixinInterface mixinDelegate = (MixinInterface) mixin;
    assertEquals("first", mixinDelegate.first());
    assertEquals("second", mixinDelegate.second());
  }

  /**
   * InterfaceMaker dynamically creates a new interface. Other than any other class of cglib's
   * public API, the interface maker relies on ASM types. The creation of an interface in a running
   * application will hardly make sense since an interface only represents a type which can be used
   * by a compiler to check types. It can however make sense when you are generating code that is to
   * be used in later development
   */
  @Test
  public void testInterfaceMaker() throws Exception {
    Signature signature = new Signature("foo", Type.DOUBLE_TYPE, new Type[]{Type.INT_TYPE});
    InterfaceMaker interfaceMaker = new InterfaceMaker();
    interfaceMaker.add(signature, new Type[0]);
    Class iface = interfaceMaker.create();
    assertEquals(1, iface.getMethods().length);
    assertEquals("foo", iface.getMethods()[0].getName());
    assertEquals(double.class, iface.getMethods()[0].getReturnType());
  }
}
