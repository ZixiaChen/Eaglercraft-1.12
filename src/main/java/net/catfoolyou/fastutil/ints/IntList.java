package net.catfoolyou.fastutil.ints;

import java.util.List;

public interface IntList extends List<Integer>, Comparable<List<? extends Integer>>, IntCollection {
   IntListIterator iterator();

   @Deprecated
   IntListIterator intListIterator();

   @Deprecated
   IntListIterator intListIterator(int var1);

   IntListIterator listIterator();

   IntListIterator listIterator(int var1);

   @Deprecated
   IntList intSubList(int var1, int var2);

   IntList subList(int var1, int var2);

   void size(int var1);

   void getElements(int var1, int[] var2, int var3, int var4);

   void removeElements(int var1, int var2);

   void addElements(int var1, int[] var2);

   void addElements(int var1, int[] var2, int var3, int var4);

   @Override
   boolean add(int var1);

   void add(int var1, int var2);

   boolean addAll(int var1, IntCollection var2);

   boolean addAll(int var1, IntList var2);

   boolean addAll(IntList var1);

   int getInt(int var1);

   int indexOf(int var1);

   int lastIndexOf(int var1);

   int removeInt(int var1);

   int set(int var1, int var2);
}
