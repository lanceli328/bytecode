package com.lance.bytecode.asm;

import com.lance.bytecode.asm.classloader.MyClassLoader;
import java.io.IOException;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Created by lihua on 16/11/2017. email: lihua@seczone.cn
 */
public class TestsLauncher {

  @Test
  public void testVisitClass() throws IOException {
    ClassReader reader = new ClassReader("java.lang.String");
    ClassWriter writer = new ClassWriter(reader,
        ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    MyClassVisit visit = new MyClassVisit(Opcodes.ASM5, writer);
    reader.accept(visit, ClassReader.EXPAND_FRAMES);
    for (byte b : writer.toByteArray()){
      System.out.print(b);
    }
  }

  @Test
  public void testCreateClass() throws IOException {
    MyClassWriter writer = new MyClassWriter();
    byte[] bytes = writer.createClass();

    MyClassLoader myClassLoader = new MyClassLoader();
    Class c = myClassLoader.defineClass("com.lance.bytecode.asm.Comparable", bytes);
    System.out.println(c.getName());
  }

}
