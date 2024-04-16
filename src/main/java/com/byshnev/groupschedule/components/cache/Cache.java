package com.byshnev.groupschedule.components.cache;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import lombok.Getter;

/**
 * Cache
 * */
@Getter
public class Cache<K, T> {

  private static final int CAPACITY = 50;

  private final HashMap<K, T> storage;

  //to save the order of adding elements
  private final LinkedList<K> linkedList;

  public Cache() {
    storage = new HashMap<>();
    linkedList = new LinkedList<>();
  }

  public Optional<T> get(final K key) {
    Optional<T> result = Optional.empty();
    if (contains(key)) {
      final T node = storage.get(key);
      result = Optional.of(node);
      linkedList.remove(key);
      linkedList.addFirst(key);
    }
    return result;
  }

  public int size() {
    return linkedList.size();
  }

  public void put(final K key, final T value) {
    if (contains(key)) {
      linkedList.remove(key);
    } else {
      ensureSize();
    }
    storage.put(key, value);
    linkedList.addFirst(key);
  }

  public boolean contains(final K key) {
    return storage.containsKey(key);
  }

  public void remove(final K key) {
    if (contains(key)) {
      linkedList.remove(key);
      storage.remove(key);
    }
  }

  //if the amount of cache elements is more than capacity
  //then remove the last saved element
  private void ensureSize() {
    if (size() >= CAPACITY) {
      K key = linkedList.removeLast();
      storage.remove(key);
    }
  }
}