package net.catfoolyou.fastutil.objects;

import java.util.Collection;

public interface ObjectCollection<K> extends Collection<K>, ObjectIterable<K> {
   @Override
   ObjectIterator<K> iterator();

   @Deprecated
   ObjectIterator<K> objectIterator();

   @Override
   <T> T[] toArray(T[] var1);
}
