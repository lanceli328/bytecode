package com.lance.bytecode.asm.methodtests;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * Created by lihua on 19/11/2017. email: lihua@qq.com
 */
public class TestClassVisitor extends ClassVisitor{

  public TestClassVisitor(int api, ClassWriter cw) {
    super(api,cw);
  }

  public void visit(int version, int access, String name, String signature,
      String superName, String[] interfaces) {
    System.out.println(name + " extends " + superName + " {");

    if ("a/a/a/k".equals(name)){
      name = "cn/test/globalstorage/fa";
    } else if("a/a/a/j".equals(name)){
      name = "cn/test/http/report/es";
    } else if("com/develorium/metracer/i".equals(name)){
      name = "cn/test/http/oi";
    } else if("com/alibaba/fastjson/serializer/ASMSerializerFactory".equals(name)){
      name = "cn/test/instrument/gd";
    } else if("com/develorium/metracer/a".equals(name)){
      name = "cn/test/plugins/security/sensitive/eo";
    }

    super.visit(version, access, name, signature, superName, interfaces);
  }

  public void visitEnd(){
    super.visitEnd();
  }
}
