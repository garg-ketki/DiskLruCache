package com.example.lruimp.lru;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ketkigarg on 15/01/18.
 */

public class DiskLruCache<Key, Value> implements Cache {

  private static final String TAG = DiskLruCache.class.getSimpleName();
  private LinkedHashMap linkedHashMap;
  private long maxSize;
  private long expiryTime;
  private String cacheDirectory;
  private long currentSize;
  private CachePolicy cachePolicy;

  public DiskLruCache(String cacheDirectory, long maxSize, long expiryTime) {
    linkedHashMap = new LinkedHashMap(16, 0.7f, true);
    this.maxSize = maxSize;
    this.expiryTime = expiryTime;
    this.cacheDirectory = cacheDirectory;
    this.currentSize = 0;
    this.cachePolicy = new BitmapCachePolicy();
    initializeCache();
  }

  private void initializeCache() {
    printOperation("initializeCache");
    File file = new File(cacheDirectory);
    if (!file.exists()) {
      file.mkdir();
    } else {
      File[] files = file.listFiles();
      ArrayList<File> filesList = new ArrayList<>(Arrays.asList(files));
      Collections.sort(filesList, new Comparator<File>() {
        @Override
        public int compare(File file1, File file2) {
          if ((file1.lastModified() > file2.lastModified())) {
            return 1;
          }
          return 0;
        }
      });
      for (File f : filesList) {
        if (isValidSize(f.length())) {
          String hashCode = f.getName().hashCode() + "";
          CacheEntry cacheEntry = new CacheEntry(f.lastModified(), hashCode, f,
              f.getName(), f.length());
          if (notExpired(cacheEntry.getLastModified())) {
            insertValueInHashMap(cacheEntry);
          } else {
            printOperation("Expiry time reached");
          }
        }
      }
    }
    printLogs();
  }

  private boolean notExpired(long time) {
    return (System.currentTimeMillis() - time) < expiryTime;
  }

  private void printOperation(String op) {
    Log.v(TAG, op);
  }

  private void printLogs() {
    Log.v(TAG, "....................");
    Log.v(TAG, "currentSize: " + currentSize + " listOfItems: " +
        "" + linkedHashMap.size());
    for (Object entry : linkedHashMap.entrySet()) {
      CacheEntry cacheEntry = (CacheEntry) ((Map.Entry) entry).getValue();
      Log.v(TAG, "entry: " + cacheEntry.getFileName());
    }
    Log.v(TAG, "....................");
  }

  private void insertValueInHashMap(CacheEntry cacheEntry) {
    linkedHashMap.put(cacheEntry.getHashCode(), cacheEntry);
    currentSize = currentSize + cacheEntry.getSize();
  }

  private void removeValueFromHashMap(CacheEntry cacheEntry) {
    printOperation("old size: " + currentSize + " cacheEntry.getSize(): " + cacheEntry.getSize());
    currentSize = currentSize - cacheEntry.getSize();
    printOperation("after removal new size: " + currentSize);
    cacheEntry.getFile().delete();
    linkedHashMap.remove(cacheEntry.getHashCode());
  }

  private boolean isValidSize(long size) {
    if (size > maxSize) {
      printOperation("Could not insert due to large size");
      return false;
    }
    while ((currentSize + size) > maxSize && linkedHashMap.size() > 0) {
      CacheEntry entry = (CacheEntry) linkedHashMap.get(linkedHashMap.keySet().iterator().next());
      removeEntry(entry.getFileName());
    }
    return true;
  }

  @Override
  public void put(Object key, Object value) {
    printOperation("put" + key);
    String hashCode = key.hashCode() + "";
    if (isValidSize(cachePolicy.size(value))) {
      File file = new File(cacheDirectory, key + "");
      cachePolicy.writeObject(file, value);
      insertValueInHashMap(new CacheEntry(System.currentTimeMillis(), hashCode, file,
          file.getName(), cachePolicy.size(value)));
    }
    printLogs();
  }

  @Override
  public Object get(Object fileName) {
    printOperation("get");
    String hashCode = fileName.hashCode() + "";
    if (linkedHashMap.containsKey(hashCode)) {
      CacheEntry entry = (CacheEntry) linkedHashMap.get(hashCode);
      if (notExpired(entry.getLastModified())) {
        printOperation("successfully returned: " + entry.getFileName());
        printLogs();
        return cachePolicy.readObject(entry.getFile());
      } else {
        removeEntry(entry.getFileName());
        printOperation("Expired");
        return null;
      }
    }
    return null;
  }

  @Override
  public void removeEntry(Object key) {
    String hashCode = key.hashCode() + "";
    if (linkedHashMap.containsKey(hashCode)) {
      CacheEntry entry = (CacheEntry) linkedHashMap.get(hashCode);
      removeValueFromHashMap(entry);
    }
  }

  @Override
  public void clear() {
    Iterator it = linkedHashMap.entrySet().iterator();
    while (it.hasNext()) {
      removeEntry(it.next());
    }
    linkedHashMap.clear();
    currentSize = 0;
  }
}
