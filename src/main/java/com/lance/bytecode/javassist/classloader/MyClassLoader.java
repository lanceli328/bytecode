package com.lance.bytecode.javassist.classloader;

import java.io.IOException;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;

/**
 * Created by lihua on 19/4/2017.
 * email: lihua@qq.com
 */
public class MyClassLoader extends ClassLoader {

  private ClassPool pool;

  public MyClassLoader() throws NotFoundException {
    pool = new ClassPool();
    pool.insertClassPath("javassist");
  }

  /* Finds a specified class.
   * The bytecode for that class can be modified.
   */
  protected Class findClass(String name) throws ClassNotFoundException {
    try {
      CtClass cc = pool.get(name);
      cc.setModifiers(Modifier.PUBLIC);
      CtField f = new CtField(CtClass.intType, "hiddenValue", cc);
      f.setModifiers(Modifier.PUBLIC);
      cc.addField(f);
      byte[] b = cc.toBytecode();
      return defineClass(name, b, 0, b.length);
    } catch (NotFoundException e) {
      throw new ClassNotFoundException();
    } catch (IOException e) {
      throw new ClassNotFoundException();
    } catch (CannotCompileException e) {
      throw new ClassNotFoundException();
    }
  }


}
