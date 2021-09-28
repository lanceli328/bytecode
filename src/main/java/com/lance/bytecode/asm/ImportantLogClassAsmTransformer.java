package com.lance.bytecode.asm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class ImportantLogClassAsmTransformer implements ClassFileTransformer {

  private static final String METHOD_ANNOTATION =
      "com.lance.javassist.javassist.ImportantLog";
  private static final String ANNOTATION_ARRAY = "fields";

  public byte[] transform(ClassLoader loader, String className,
      Class classBeingRedefined, ProtectionDomain protectionDomain,
      byte[] classfileBuffer) throws IllegalClassFormatException {
    ClassReader cr = new ClassReader(classfileBuffer);
    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
    ClassVisitor cv = new LogMethodClassVisitor(cw, className);
    cr.accept(cv, 0);
    return cw.toByteArray();
  }
}
