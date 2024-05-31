package net.catfoolyou.fastutil.objects;

import java.util.Iterator;

public interface ObjectIterator<K> extends Iterator<K> {
   int skip(int var1);
}
