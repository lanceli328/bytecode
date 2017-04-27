package com.lance.bytecode.javassist;

import java.lang.instrument.Instrumentation;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@seczone.cn
 */
public class JavassistAgent {
  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("Starting the agent");
    inst.addTransformer(new ImportantLogClassJavassistTransformer());
  }
}
