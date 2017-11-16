package com.lance.bytecode.java8;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@seczone.cn
 */
public class StreamTests {

  @Test
  public void testSequential() {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < 1000000; i++) {
      double d = Math.random() * 1000;
      list.add(d + "");
    }

    long start = System.nanoTime();
    int count = (int) ((Stream) list.stream().sequential()).sorted().count();
    long end = System.nanoTime();
    long ms = TimeUnit.NANOSECONDS.toMillis(end - start);
    System.out.println(ms);
  }

  @Test
  public void testParallel() {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < 1000000; i++) {
      double d = Math.random() * 1000;
      list.add(d + "");
    }

    long start = System.nanoTime();
    int count = (int) ((Stream) list.stream().parallel()).sorted().count();
    long end = System.nanoTime();
    long ms = TimeUnit.NANOSECONDS.toMillis(end - start);
    System.out.println(ms);
  }

  /**
   * 中间操作
   * 该操作会保持 stream 处于中间状态，允许做进一步的操作。它返回的还是的 Stream，允许更多的链式操作。常见的中间操作有：
   * filter()：对元素进行过滤；
   * sorted()：对元素排序；
   * map()：元素的映射；
   * distinct()：去除重复元素；
   * subStream()：获取子 Stream 等。
   */
  @Test
  public void testMiddleOper() {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < 1000000; i++) {
      double d = Math.random() * 1000;
      if (d % 2 < 1) {
        list.add("s" + d);
      } else {
        list.add(d + "");
      }
    }
    list.stream()
        .filter((s) -> s.startsWith("s"))
        .forEach(System.out::println);
  }

  /**
   * 终止操作
   * 该操作必须是流的最后一个操作，一旦被调用，Stream 就到了一个终止状态，而且不能再使用了。常见的终止操作有：
   * forEach()：对每个元素做处理；
   * toArray()：把元素导出到数组；
   * findFirst()：返回第一个匹配的元素；
   * anyMatch()：是否有匹配的元素等。
   */
  @Test
  public void testAbortOper() {
    List<String> list = new ArrayList<>();
    for (int i = 0; i < 100000; i++) {
      double d = Math.random() * 1000;
      if (d % 2 < 1) {
        list.add("s" + d);
      } else {
        list.add(d + "");
      }
    }
    list.stream()
        .filter((s) -> s.startsWith("s"))
        .forEach(System.out::println);
  }

}


