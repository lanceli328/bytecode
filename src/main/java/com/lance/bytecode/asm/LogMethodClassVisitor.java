package com.lance.bytecode.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class LogMethodClassVisitor extends ClassVisitor {
  private String className;

  public LogMethodClassVisitor(ClassVisitor cv, String pClassName) {
    super(Opcodes.ASM5, cv);
    className = pClassName;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc,
      String signature, String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, desc, signature,
        exceptions);
    return new PrintMessageMethodVisitor(access, mv, name, className);
  }


}

