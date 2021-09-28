package com.lance.bytecode.java8.lambada;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by lihua on 28/5/2017.
 * email: lihua@qq.com
 */
public class People {

  private List<Person> persons = new ArrayList<Person>();

  public List<Person> getMaleList(Predicate<Person> predicate) {
    List<Person> res = new ArrayList<Person>();
    persons.forEach(
        person -> {
          if (predicate.test(person)) {//调用 Predicate 的抽象方法 test
            res.add(person);
          }
        });
    return res;
  }

}
