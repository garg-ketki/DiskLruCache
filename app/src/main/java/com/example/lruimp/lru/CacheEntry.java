package com.example.lruimp.lru;

import java.io.File;

/**
 * Created by ketkigarg on 15/01/18.
 */

public class CacheEntry {
  private long lastModified;
  private String hashCode;
  private File file;
  private String fileName;
  private long size;

  public CacheEntry(long lastModified, String hashCode, File file, String fileName, long size) {
    this.lastModified = lastModified;
    this.hashCode = hashCode;
    this.file = file;
    this.fileName = fileName;
    this.size = size;
  }

  public long getLastModified() {
    return lastModified;
  }

  public void setLastModified(long lastModified) {
    this.lastModified = lastModified;
  }

  public String getHashCode() {
    return hashCode;
  }

  public void setHashCode(String hashCode) {
    this.hashCode = hashCode;
  }

  public File getFile() {
    return file;
  }

  public void setFile(File file) {
    this.file = file;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }
}
