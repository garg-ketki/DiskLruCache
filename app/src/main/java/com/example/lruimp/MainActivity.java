package com.example.lruimp;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lruimp.lru.DiskLruCache;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }

  @Override
  protected void onStart() {
    super.onStart();
    initializeCache();
  }

  private void initializeCache() {
    DiskLruCache cache = new DiskLruCache(getExternalCacheDir().getPath(), 8000000, 100000);
    cache.put("i1", BitmapFactory.decodeResource(this.getResources(), R.drawable.i1));
    cache.put("i2", BitmapFactory.decodeResource(this.getResources(), R.drawable.i2));
    cache.put("i3", BitmapFactory.decodeResource(this.getResources(), R.drawable.i3));
    cache.put("i4", BitmapFactory.decodeResource(this.getResources(), R.drawable.i4));
    cache.put("i5", BitmapFactory.decodeResource(this.getResources(), R.drawable.i5));
    cache.put("i6", BitmapFactory.decodeResource(this.getResources(), R.drawable.i6));
    cache.get("i5");
  }
}
