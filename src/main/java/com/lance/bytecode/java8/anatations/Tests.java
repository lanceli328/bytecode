package com.lance.bytecode.java8.anatations;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@seczone.cn
 */
@Anot("a1")
@Anot("a2")
public class Tests {

  public static void main(String[] args) {
    Anots annots1 = Tests.class.getAnnotation(Anots.class);
    System.out.println(annots1.value()[0] + "," + annots1.value()[1]);
    // 输出: @Annot(value=a1),@Annot(value=a2)
    Anot[] annots2 = Tests.class.getAnnotationsByType(Anot.class);
    System.out.println(annots2[0] + "," + annots2[1]);
    // 输出: @Annot(value=a1),@Annot(value=a2)
  }

}
