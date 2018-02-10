package com.example.lruimp.lru;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ketkigarg on 15/01/18.
 */

public class BitmapCachePolicy implements CachePolicy {

  public BitmapCachePolicy() {

  }

  @Override
  public boolean writeObject(File outputFile, Object value) {
    FileOutputStream out = null;
    try {
      out = new FileOutputStream(outputFile);
      ((Bitmap) value).compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
      // PNG is a lossless format, the compression factor (100) is ignored
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public Object readObject(File inputFile) {
    return BitmapFactory.decodeFile(inputFile.getName());
  }

  @Override
  public long size(Object value) {
    return ((Bitmap) value).getByteCount();
  }
}
