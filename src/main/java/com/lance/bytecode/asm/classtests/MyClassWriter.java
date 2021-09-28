package com.lance.bytecode.asm.classtests;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Created by lihua on 16/11/2017. email: lihua@qq.com
 */
public class MyClassWriter {

  public byte[] createClass() {
    ClassWriter cw = new ClassWriter(0);
    cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
        "com/lance/bytecode/asm/Comparable", null, "java/lang/Object",
        new String[]{});
    cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "LESS", "I",
        null, new Integer(-1)).visitEnd();
    cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "EQUAL", "I",
        null, new Integer(0)).visitEnd();
    cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC, "GREATER", "I",
        null, new Integer(1)).visitEnd();
    cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_ABSTRACT, "compareTo",
        "(Ljava/lang/Object;)I", null, null).visitEnd();
    cw.visitEnd();
    return cw.toByteArray();
  }

}
