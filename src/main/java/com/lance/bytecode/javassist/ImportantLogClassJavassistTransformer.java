package com.lance.bytecode.javassist;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.MethodInfo;
import javassist.ByteArrayClassPath;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * Created by lihua on 18/4/2017.
 * email: lihua@qq.com
 */
public class ImportantLogClassJavassistTransformer implements ClassFileTransformer {

  private static final String METHOD_ANNOTATION =
      "com.lance.javassist.javassist.ImportantLog";
  private static final String ANNOTATION_ARRAY = "fields";
  private ClassPool pool;

  public ImportantLogClassJavassistTransformer() {
    pool = ClassPool.getDefault();
  }

  public byte[] transform(ClassLoader loader, String className,
      Class classBeingRedefined, ProtectionDomain protectionDomain,
      byte[] classfileBuffer) throws IllegalClassFormatException {
    try {
      pool.insertClassPath(new ByteArrayClassPath(className,
          classfileBuffer));
      CtClass cclass = pool.get(className.replaceAll("/", "."));
      if (!cclass.isFrozen()) {
        for (CtMethod currentMethod : cclass.getDeclaredMethods()) {
          Annotation annotation = getAnnotation(currentMethod);
          if (annotation != null) {
            List parameterIndexes = getParamIndexes(annotation);
            currentMethod.insertBefore(createJavaString(
                currentMethod, className, parameterIndexes));
          }
        }
        return cclass.toBytecode();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private Annotation getAnnotation(CtMethod method) {
    MethodInfo mInfo = method.getMethodInfo();
    // the attribute we are looking for is a runtime invisible attribute
    // use Retention(RetentionPolicy.RUNTIME) on the annotation to make it
    // visible at runtime
    AnnotationsAttribute attInfo = (AnnotationsAttribute) mInfo
        .getAttribute(AnnotationsAttribute.invisibleTag);
    if (attInfo != null) {
      // this is the type name meaning use dots instead of slashes
      return attInfo.getAnnotation(METHOD_ANNOTATION);
    }
    return null;
  }

  private List getParamIndexes(Annotation annotation) {
    ArrayMemberValue fields = (ArrayMemberValue) annotation
        .getMemberValue(ANNOTATION_ARRAY);
    if (fields != null) {
      MemberValue[] values = (MemberValue[]) fields.getValue();
      List parameterIndexes = new ArrayList();
      for (MemberValue val : values) {
        parameterIndexes.add(((StringMemberValue) val).getValue());
      }
      return parameterIndexes;
    }
    return Collections.emptyList();
  }

  private String createJavaString(CtMethod currentMethod, String className,
      List indexParameters) {
    StringBuilder sb = new StringBuilder();
    sb.append("{StringBuilder sb = new StringBuilder");
    sb.append("(\"A call was made to method '\");");
    sb.append("sb.append(\"");
    sb.append(currentMethod.getName());
    sb.append("\");sb.append(\"' on class '\");");
    sb.append("sb.append(\"");
    sb.append(className);
    sb.append("\");sb.append(\"'.\");");
    sb.append("sb.append(\"\\n    Important params:\");");
    for (Object index : indexParameters) {
      try {
        // add one because 0 is "this" for instance variable
        // if were a static method 0 would not be anything
        int localVar = Integer.parseInt(index.toString()) + 1;
        sb.append("sb.append(\"\\n        Index \");");
        sb.append("sb.append(\"");
        sb.append(index);
        sb.append("\");sb.append(\" value: \");");
        sb.append("sb.append($" + localVar + ");");
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }
    }
    sb.append("System.out.println(sb.toString());}");
    return sb.toString();
  }
}
