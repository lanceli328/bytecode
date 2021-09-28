package com.lance.bytecode.javassist.classloader;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.Translator;

/**
 * Created by lihua on 19/4/2017.
 * email: lihua@qq.com
 */
public class MyTranslator implements Translator {

  /**
   * The method start() is called when this event listener is added to a javassist.Loader object by
   * addTranslator() in javassist.Loader.
   */
  @Override
  public void start(ClassPool pool)
      throws NotFoundException, CannotCompileException {

  }

  /**
   * The method onLoad() is called before javassist.Loader loads a class. onLoad() can modify the
   * definition of the loaded class.
   *
   * Note that onLoad() does not have to call toBytecode() or writeFile() since javassist.Loader
   * calls these methods to obtain a class file.
   */
  @Override
  public void onLoad(ClassPool pool, String classname)
      throws NotFoundException, CannotCompileException {
    CtClass cc = pool.get(classname);
    cc.setModifiers(Modifier.PUBLIC);
    CtMethod m = cc.getDeclaredMethod("main");
    m.insertBefore("{ System.out.println(\"I come from MyTranslator!\"); }");
  }

}
