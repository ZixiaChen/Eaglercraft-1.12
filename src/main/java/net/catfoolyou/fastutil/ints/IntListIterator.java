package net.catfoolyou.fastutil.ints;

import java.util.ListIterator;

public interface IntListIterator extends ListIterator<Integer>, IntBidirectionalIterator {
   void set(int var1);

   void add(int var1);
}
