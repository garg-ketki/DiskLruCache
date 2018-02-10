package com.example.lruimp.lru;

/**
 * Created by ketkigarg on 15/01/18.
 */

public interface Cache<Key, Value> {
  void put(Key key, Value value);

  Value get(Key key);

  void removeEntry(Key key);

  void clear();
}
