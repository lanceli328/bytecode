package com.lance.bytecode.asm.methodtests;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * Created by lihua on 29/11/2017. email: lihua@qq.com
 */
public class Main {

  @Test
  public void testVisitClass() throws Exception {
    File classDir = new File("/Users/lihua/Desktop/classes/");
    File targetDir = new File("/Users/lihua/Desktop/classes/new/");
    targetDir.mkdir();
    for (File file : classDir.listFiles()) {
      convertClass(targetDir, file.getAbsolutePath());
    }
  }

  private void convertClass(File targetDir,String className) throws IOException{
    if (!className.endsWith(".class")){
      return;
    }
    Path path = Paths.get(className);
    byte[] classBytes = Files.readAllBytes(path);
    ClassReader reader = new ClassReader(classBytes);
    ClassWriter writer = new ClassWriter(reader,
        ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    TestClassVisitor visit = new TestClassVisitor(Opcodes.ASM5, writer);
    reader.accept(visit, ClassReader.EXPAND_FRAMES);
    byte[] bytes1 = writer.toByteArray();
    path = Paths.get(targetDir.getAbsolutePath() + "/" + path.getFileName());
    File file = new File(path.toString());
    file.createNewFile();

    Files.write(path, bytes1, StandardOpenOption.WRITE);
  }

}
