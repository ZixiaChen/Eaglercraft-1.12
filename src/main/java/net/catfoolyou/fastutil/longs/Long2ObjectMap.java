package net.catfoolyou.fastutil.longs;

import net.catfoolyou.fastutil.objects.ObjectCollection;
import net.catfoolyou.fastutil.objects.ObjectIterator;
import net.catfoolyou.fastutil.objects.ObjectSet;
import java.util.Map;

public interface Long2ObjectMap<V> extends Long2ObjectFunction<V>, Map<Long, V> {
   ObjectSet<java.util.Map.Entry<Long, V>> entrySet();

   ObjectSet<Long2ObjectMap.Entry<V>> long2ObjectEntrySet();

   LongSet keySet();

   ObjectCollection<V> values();

   public interface Entry<V> extends java.util.Map.Entry<Long, V> {
      @Deprecated
      Long getKey();

      long getLongKey();
   }

   public interface FastEntrySet<V> extends ObjectSet<Long2ObjectMap.Entry<V>> {
      ObjectIterator<Long2ObjectMap.Entry<V>> fastIterator();
   }
}
