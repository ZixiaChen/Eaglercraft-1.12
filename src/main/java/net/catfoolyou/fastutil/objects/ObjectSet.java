package net.catfoolyou.fastutil.objects;

import java.util.Set;

public interface ObjectSet<K> extends ObjectCollection<K>, Set<K> {
   @Override
   ObjectIterator<K> iterator();

   @Override
   boolean remove(Object var1);
}
