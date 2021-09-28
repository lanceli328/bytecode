package com.lance.bytecode.javassist.bytecode;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import javassist.CtClass;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Mnemonic;
import org.junit.Test;

/**
 * Created by lihua on 21/4/2017.
 * email: lihua@qq.com
 */
public class ClassFileTests {

  private static final String ACCOUNT_CLASS = "com.lance.bytecode.BankAccount";

  @Test
  public void testCreateNewClass() throws Exception {
    ClassFile cf = new ClassFile(false, "com.lance.bytecode.Foo", null);
    cf.setInterfaces(new String[]{"java.lang.Cloneable"});

    FieldInfo f = new FieldInfo(cf.getConstPool(), "width", "I");
    f.setAccessFlags(AccessFlag.PUBLIC);
    cf.addField(f);

    cf.write(new DataOutputStream(new FileOutputStream("javassist/com/lance/bytecode/Foo.class")));
  }

  /**
   * To remove a field or a method from a ClassFile object, you must first obtain a java.util.List
   * object containing all the fields of the class. getFields() and getMethods() return the lists. A
   * field or a method can be removed by calling remove() on the List object. An attribute can be
   * removed in a similar way. Call getAttributes() in FieldInfo or MethodInfo to obtain the list of
   * attributes, and remove one from the list.
   */
  @Test
  public void testRemoveMember() throws Exception {
    BufferedInputStream fin
        = new BufferedInputStream(new FileInputStream(ACCOUNT_CLASS.replace(".", "/") + ".class"));
    ClassFile classFile = new ClassFile(new DataInputStream(fin));

    List<MethodInfo> methods = classFile.getMethods();
    for (MethodInfo method : methods) {
      if (method.getName().equals("getName")) {
        methods.remove(method);
      }
    }

    List<FieldInfo> fields = classFile.getFields();
    for (FieldInfo field : fields) {
      if (field.getName().equals("name")) {
        fields.remove(field);
        break;
      }
    }

    classFile.write(new DataOutputStream(
        new FileOutputStream("javassist/com/lance/bytecode/BankAccount.class")));
  }

  /**
   * A CodeIterator object allows you to visit every bytecode instruction one by one from the
   * beginning to the end. The following methods are part of the methods declared in CodeIterator:
   *
   * void begin() Move to the first instruction.
   * void move(int index) Move to the instruction specified by the given index.
   * boolean hasNext() Returns true if there is more instructions.
   * int next() Returns the index of the next instruction. Note that it does not return the opcode of the next instruction.
   * int byteAt(int index) Returns the unsigned 8bit value at the index.
   * int u16bitAt(int index) Returns the unsigned 16bit value at the index.
   * int write(byte[] code, int index) Writes a byte array at the index.
   * void insert(int index, byte[] code) Inserts a byte array at the index. Branch offsets etc. are automatically adjusted.
   */
  @Test
  public void TraversingMethodBody() throws Exception {
    BufferedInputStream fin
        = new BufferedInputStream(new FileInputStream(ACCOUNT_CLASS.replace(".", "/") + ".class"));
    ClassFile classFile = new ClassFile(new DataInputStream(fin));
    MethodInfo minfo = classFile.getMethod("setName");
    CodeAttribute ca = minfo.getCodeAttribute();
    CodeIterator ci = ca.iterator();
    while (ci.hasNext()) {
      int index = ci.next();
      int op = ci.byteAt(index);
      System.out.println(Mnemonic.OPCODE[op]);
    }
  }

  @Test
  public void testBytecode() throws Exception {
    BufferedInputStream fin
        = new BufferedInputStream(new FileInputStream(ACCOUNT_CLASS.replace(".", "/") + ".class"));
    ClassFile cf = new ClassFile(new DataInputStream(fin));

    Bytecode code = new Bytecode(cf.getConstPool());
    code.addAload(0);
    code.addInvokespecial("java/lang/Object", "toString", "()java/lang/Object");
    code.addReturn(null);
    code.setMaxLocals(1);

    MethodInfo minfo = new MethodInfo(cf.getConstPool(), "replaceName", "()Ljava/lang/String");
    minfo.setCodeAttribute(code.toCodeAttribute());
    cf.addMethod(minfo);

    cf.write(new DataOutputStream(
        new FileOutputStream("javassist/com/lance/bytecode/BankAccount.class")));
  }

}
