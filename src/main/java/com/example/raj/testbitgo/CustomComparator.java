package com.example.raj.testbitgo;

import java.util.Comparator;

public class CustomComparator implements Comparator<TransactionCount> {

  @Override
  public int compare(TransactionCount t1, TransactionCount t2) {

    return t2.getCount().compareTo(t1.getCount());
  }
}
