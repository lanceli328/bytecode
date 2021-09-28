package com.lance.bytecode.asm;

import com.lance.bytecode.javassist.ImportantLogClassJavassistTransformer;
import java.lang.instrument.Instrumentation;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class AsmAgent {
  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("Starting the agent");
    inst.addTransformer(new ImportantLogClassJavassistTransformer());
  }
}
