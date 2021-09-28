package com.lance.bytecode.asm.classtests;

import com.lance.bytecode.asm.classloader.MyClassLoader;
import com.lance.bytecode.asm.classtests.MyClassVisit;
import com.lance.bytecode.asm.classtests.MyClassWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

/**
 * Created by lihua on 16/11/2017. email: lihua@qq.com
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

  @Test
  public void testTraceClassVisitor() throws IOException {
    ClassWriter writer = new ClassWriter(Opcodes.ASM5);
    PrintWriter printWriter = new PrintWriter(System.out);
    TraceClassVisitor tracer = new TraceClassVisitor(writer,printWriter);

    ClassReader reader = new ClassReader("java.lang.String");
    reader.accept(tracer, ClassReader.EXPAND_FRAMES);
  }

  @Test
  public void testCheckClassVisitor() throws IOException {
    ClassWriter writer = new ClassWriter(Opcodes.ASM5);
    CheckClassAdapter checker = new CheckClassAdapter(writer);

    ClassReader reader = new ClassReader("java.lang.String");
    reader.accept(checker, ClassReader.EXPAND_FRAMES);
  }

}
