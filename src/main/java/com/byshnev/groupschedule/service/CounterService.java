package com.byshnev.groupschedule.service;


import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@NoArgsConstructor
public class CounterService {

  private static final AtomicInteger count = new AtomicInteger(0);

  public static synchronized int increment() {
    return count.incrementAndGet();
  }

}