package com.example.raj.testbitgo;

public class TransactionCount {

  String name;
  Integer count;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public String toString() {
    return name + " : " + count;
  }
}
