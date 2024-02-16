package com.example.raj.testbitgo;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestBitGoApplication {

  public static void main(String[] args) {
    SpringApplication.run(TestBitGoApplication.class, args);

    boolean found = true;
    int offset = 25;
    while (found) {
      found = HttpUtils.getTransactions(offset);
      offset += 25;
    }

    // O(m * n) + O(m) : m: No of acutal txn ids , n : no of parents + Heapify log(n)
    // Space O( n * m) : m: No of acutal txn ids , n : no of parents 
    PriorityQueue<TransactionCount> queue = new PriorityQueue<>(new CustomComparator());  // Heapify
    Iterator<Entry<String, Set<String>>> mapIt = HttpUtils.parentMap.entrySet().iterator();
    while (mapIt.hasNext()) {

      int parentCount = 0;

      Entry<String, Set<String>> entry = mapIt.next();
      Set<String> parents = entry.getValue();
      List<String> s = new CopyOnWriteArrayList<>(parents);
      s.addAll(parents);

      Iterator<String> it = s.iterator();

      while (it.hasNext()) {
        String parent = it.next();
        if (HttpUtils.parentMap.containsKey(parent)) {
          parentCount++;
          parents.addAll(HttpUtils.parentMap.get(parent));
          parents.remove(parent);
        }
      }
      TransactionCount transactionCount = new TransactionCount();
      transactionCount.setName(entry.getKey());
      transactionCount.setCount(parentCount);
      queue.add(transactionCount);
    }

    while (!queue.isEmpty()) {
      System.out.println(queue.poll());
    }

    System.out.println("############ END ############");
  }

}
