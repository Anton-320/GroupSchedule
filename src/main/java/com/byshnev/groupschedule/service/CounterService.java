package com.byshnev.groupschedule.service;


import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CounterService {

  private static final AtomicInteger count = new AtomicInteger(0);

  private CounterService() {
  }

  public static synchronized int increment() {
    return count.incrementAndGet();
  }

}