package com.lance.bytecode.asm.classtests;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;

/**
 * Created by lihua on 16/11/2017. email: lihua@qq.com
 */
public class MyClassVisit extends ClassVisitor {

  public MyClassVisit(int api) {
    super(api);
  }

  public MyClassVisit(int api, ClassVisitor cv){
    super(api,cv);
  }

  @Override
  public void visit(int version, int access, String name, String signature,
      String superName, String[] interfaces) {
    System.out.println(
        String
            .format("visit: version=%s access=%s name=%s signature=%s superName=%s interfaces=%s", version,
                access, name, signature, superName, interfaces));
    super.visit(version, access, name, signature, superName, interfaces);
  }

  @Override
  public void visitSource(String source, String debug){
    System.out.println(
        String
            .format("visitSource: source=%s debug=%s", source,debug));
    super.visitSource(source, debug);
  }

  @Override
  public void visitOuterClass(String owner, String name, String desc) {
    System.out.println(
        String
            .format("visitOuterClass: owner=%s name=%s desc=%s", owner,name,desc));
    super.visitOuterClass(owner, name, desc);
  }

  @Override
  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    System.out.println(
        String
            .format("visitAnnotation: desc=%s visible=%s", desc,visible));
    return visitAnnotation(desc,visible);
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(int typeRef,
      TypePath typePath, String desc, boolean visible) {
    if (api < Opcodes.ASM5) {
      throw new RuntimeException();
    }
    System.out.println(
        String
            .format("visitTypeAnnotation: typeRef=%s typePath=%s desc=%s visible=%s", typeRef,typePath,desc,visible));
    return super.visitTypeAnnotation(typeRef,typePath,desc,visible);
  }

  @Override
  public void visitAttribute(Attribute attr) {
    System.out.println(
        String
            .format("visitAttribute: visitAttribute=%s", new String(attr.type.getBytes())));
    super.visitAttribute(attr);
  }

  @Override
  public void visitInnerClass(String name, String outerName,
      String innerName, int access) {
    System.out.println(
        String
            .format("visitInnerClass: name=%s outerName=%s innerName=%s access=%d", name,outerName,innerName,access));
    super.visitInnerClass(name, outerName, innerName, access);
  }

  @Override
  public FieldVisitor visitField(int access, String name, String desc,
      String signature, Object value) {
    System.out.println(
        String
            .format("visitField: access=%s name=%s desc=%s signature=%s Object=%s", access,name,desc,signature,value));
    //return null; 不要委托至下一个访问器 -> 这样将移除该方法
    return super.visitField(access,name,desc,signature,value);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc,
      String signature, String[] exceptions) {
    System.out.println(
        String
            .format("visitMethod: access=%s name=%s desc=%s signature=%s exceptions=%s", access,name,desc,signature,exceptions));
    //return null; 不要委托至下一个访问器 -> 这样将移除该方法
    return super.visitMethod(access,name,desc,signature,exceptions);
  }

  @Override
  public void visitEnd() {
    System.out.println(
        String
            .format("visitEnd"));
    super.visitEnd();
  }
}
