package com.example.lruimp.lru;

import java.io.File;

/**
 * Created by ketkigarg on 15/01/18.
 */

public interface CachePolicy {
  boolean writeObject(File outputFile, Object value);

  Object readObject(File inputFile);

  long size(Object value);
}
