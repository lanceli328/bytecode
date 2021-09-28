package com.lance.bytecode.asm;

import com.google.common.collect.Lists;
import java.util.List;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class PrintMessageMethodVisitor extends MethodVisitor {

  private String methodName;
  private String className;
  private boolean isAnnotationPresent = false;
  private List parameterIndexes = Lists.newArrayList();

  PrintMessageMethodVisitor(int access, MethodVisitor mv, String methodName, String className){
    super(access,mv);
    this.methodName = methodName;
    this.className = className;
  }

  public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
    if ("Lcom/lance/bytecode/ImportantLog;".equals(desc)) {
      isAnnotationPresent = true;
      return new AnnotationVisitor(Opcodes.ASM5,
          super.visitAnnotation(desc, visible)) {
        public AnnotationVisitor visitArray(String name, Object value) {
          if ("fields".equals(name)) {
            return new AnnotationVisitor(Opcodes.ASM5,
                super.visitArray(name)) {
              public void visit(String name, Object value) {
                parameterIndexes.add((String) value);
                super.visit(name, value);
              }
            };
          } else {
            return super.visitArray(name);
          }
        }
      };
    }
    return super.visitAnnotation(desc, visible);
  }

  public void visitCode() {
    if (isAnnotationPresent) {
      // create string builder
      mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System",
          "out", "Ljava/io/PrintStream;");
      mv.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
      mv.visitInsn(Opcodes.DUP);
      // add everything to the string builder
      mv.visitLdcInsn("A call was made to method \"");
      mv.visitMethodInsn(Opcodes.INVOKESPECIAL,
          "java/lang/StringBuilder", "",
          "(Ljava/lang/String;)V", false);
      mv.visitLdcInsn(methodName);
      mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
          "java/lang/StringBuilder", "append",
          "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
    }
  }

}
