package net.catfoolyou.fastutil.ints;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class IntArrayList extends AbstractIntList implements RandomAccess, Cloneable, Serializable {
   private static final long serialVersionUID = -7046029254386353130L;
   public static final int DEFAULT_INITIAL_CAPACITY = 16;
   protected transient int[] a;
   protected int size;
   private static final boolean ASSERTS = false;

   protected IntArrayList(int[] a, boolean dummy) {
      this.a = a;
   }

   public IntArrayList(int capacity) {
      if (capacity < 0) {
         throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
      } else {
         this.a = new int[capacity];
      }
   }

   public IntArrayList() {
      this(16);
   }

   public IntArrayList(Collection<? extends Integer> c) {
      this(c.size());
      this.size = IntIterators.unwrap(IntIterators.asIntIterator(c.iterator()), this.a);
   }

   public IntArrayList(IntCollection c) {
      this(c.size());
      this.size = IntIterators.unwrap(c.iterator(), this.a);
   }

   public IntArrayList(IntList l) {
      this(l.size());
      l.getElements(0, this.a, 0, this.size = l.size());
   }

   public IntArrayList(int[] a) {
      this(a, 0, a.length);
   }

   public IntArrayList(int[] a, int offset, int length) {
      this(length);
      System.arraycopy(a, offset, this.a, 0, length);
      this.size = length;
   }

   public IntArrayList(Iterator<? extends Integer> i) {
      this();

      while (i.hasNext()) {
         this.add(i.next());
      }
   }

   public IntArrayList(IntIterator i) {
      this();

      while (i.hasNext()) {
         this.add(i.nextInt());
      }
   }

   public int[] elements() {
      return this.a;
   }

   public static IntArrayList wrap(int[] a, int length) {
      if (length > a.length) {
         throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + a.length + ")");
      } else {
         IntArrayList l = new IntArrayList(a, false);
         l.size = length;
         return l;
      }
   }

   public static IntArrayList wrap(int[] a) {
      return wrap(a, a.length);
   }

   public void ensureCapacity(int capacity) {
      this.a = IntArrays.ensureCapacity(this.a, capacity, this.size);
   }

   private void grow(int capacity) {
      this.a = IntArrays.grow(this.a, capacity, this.size);
   }

   @Override
   public void add(int index, int k) {
      this.ensureIndex(index);
      this.grow(this.size + 1);
      if (index != this.size) {
         System.arraycopy(this.a, index, this.a, index + 1, this.size - index);
      }

      this.a[index] = k;
      this.size++;
   }

   @Override
   public boolean add(int k) {
      this.grow(this.size + 1);
      this.a[this.size++] = k;
      return true;
   }

   @Override
   public int getInt(int index) {
      if (index >= this.size) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
      } else {
         return this.a[index];
      }
   }

   @Override
   public int indexOf(int k) {
      for (int i = 0; i < this.size; i++) {
         if (k == this.a[i]) {
            return i;
         }
      }

      return -1;
   }

   @Override
   public int lastIndexOf(int k) {
      int i = this.size;

      while (i-- != 0) {
         if (k == this.a[i]) {
            return i;
         }
      }

      return -1;
   }

   @Override
   public int removeInt(int index) {
      if (index >= this.size) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
      } else {
         int old = this.a[index];
         this.size--;
         if (index != this.size) {
            System.arraycopy(this.a, index + 1, this.a, index, this.size - index);
         }

         return old;
      }
   }

   @Override
   public boolean rem(int k) {
      int index = this.indexOf(k);
      if (index == -1) {
         return false;
      } else {
         this.removeInt(index);
         return true;
      }
   }

   @Override
   public int set(int index, int k) {
      if (index >= this.size) {
         throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
      } else {
         int old = this.a[index];
         this.a[index] = k;
         return old;
      }
   }

   @Override
   public void clear() {
      this.size = 0;
   }

   @Override
   public int size() {
      return this.size;
   }

   @Override
   public void size(int size) {
      if (size > this.a.length) {
         this.ensureCapacity(size);
      }

      if (size > this.size) {
         Arrays.fill(this.a, this.size, size, 0);
      }

      this.size = size;
   }

   @Override
   public boolean isEmpty() {
      return this.size == 0;
   }

   public void trim() {
      this.trim(0);
   }

   public void trim(int n) {
      if (n < this.a.length && this.size != this.a.length) {
         int[] t = new int[Math.max(n, this.size)];
         System.arraycopy(this.a, 0, t, 0, this.size);
         this.a = t;
      }
   }

   @Override
   public void getElements(int from, int[] a, int offset, int length) {
      IntArrays.ensureOffsetLength(a, offset, length);
      System.arraycopy(this.a, from, a, offset, length);
   }

   @Override
   public void removeElements(int from, int to) {
      net.catfoolyou.fastutil.Arrays.ensureFromTo(this.size, from, to);
      System.arraycopy(this.a, to, this.a, from, this.size - to);
      this.size -= to - from;
   }

   @Override
   public void addElements(int index, int[] a, int offset, int length) {
      this.ensureIndex(index);
      IntArrays.ensureOffsetLength(a, offset, length);
      this.grow(this.size + length);
      System.arraycopy(this.a, index, this.a, index + length, this.size - index);
      System.arraycopy(a, offset, this.a, index, length);
      this.size += length;
   }

   @Override
   public int[] toIntArray(int[] a) {
      if (a == null || a.length < this.size) {
         a = new int[this.size];
      }

      System.arraycopy(this.a, 0, a, 0, this.size);
      return a;
   }

   @Override
   public boolean addAll(int index, IntCollection c) {
      this.ensureIndex(index);
      int n = c.size();
      if (n == 0) {
         return false;
      } else {
         this.grow(this.size + n);
         if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
         }

         IntIterator i = c.iterator();
         this.size += n;

         while (n-- != 0) {
            this.a[index++] = i.nextInt();
         }

         return true;
      }
   }

   @Override
   public boolean addAll(int index, IntList l) {
      this.ensureIndex(index);
      int n = l.size();
      if (n == 0) {
         return false;
      } else {
         this.grow(this.size + n);
         if (index != this.size) {
            System.arraycopy(this.a, index, this.a, index + n, this.size - index);
         }

         l.getElements(0, this.a, index, n);
         this.size += n;
         return true;
      }
   }

   @Override
   public boolean removeAll(IntCollection c) {
      int[] a = this.a;
      int j = 0;

      for (int i = 0; i < this.size; i++) {
         if (!c.contains(a[i])) {
            a[j++] = a[i];
         }
      }

      boolean modified = this.size != j;
      this.size = j;
      return modified;
   }

   @Override
   public boolean removeAll(Collection<?> c) {
      int[] a = this.a;
      int j = 0;

      for (int i = 0; i < this.size; i++) {
         if (!c.contains(a[i])) {
            a[j++] = a[i];
         }
      }

      boolean modified = this.size != j;
      this.size = j;
      return modified;
   }

   @Override
   public IntListIterator listIterator(final int index) {
      this.ensureIndex(index);
      return new AbstractIntListIterator() {
         int pos = index;
         int last = -1;

         @Override
         public boolean hasNext() {
            return this.pos < IntArrayList.this.size;
         }

         @Override
         public boolean hasPrevious() {
            return this.pos > 0;
         }

         @Override
         public int nextInt() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               return IntArrayList.this.a[this.last = this.pos++];
            }
         }

         @Override
         public int previousInt() {
            if (!this.hasPrevious()) {
               throw new NoSuchElementException();
            } else {
               return IntArrayList.this.a[this.last = --this.pos];
            }
         }

         @Override
         public int nextIndex() {
            return this.pos;
         }

         @Override
         public int previousIndex() {
            return this.pos - 1;
         }

         @Override
         public void add(int k) {
            IntArrayList.this.add(this.pos++, k);
            this.last = -1;
         }

         @Override
         public void set(int k) {
            if (this.last == -1) {
               throw new IllegalStateException();
            } else {
               IntArrayList.this.set(this.last, k);
            }
         }

         @Override
         public void remove() {
            if (this.last == -1) {
               throw new IllegalStateException();
            } else {
               IntArrayList.this.removeInt(this.last);
               if (this.last < this.pos) {
                  this.pos--;
               }

               this.last = -1;
            }
         }
      };
   }

   public IntArrayList clone() {
      IntArrayList c = new IntArrayList(this.size);
      System.arraycopy(this.a, 0, c.a, 0, this.size);
      c.size = this.size;
      return c;
   }

   public boolean equals(IntArrayList l) {
      if (l == this) {
         return true;
      } else {
         int s = this.size();
         if (s != l.size()) {
            return false;
         } else {
            int[] a1 = this.a;
            int[] a2 = l.a;

            while (s-- != 0) {
               if (a1[s] != a2[s]) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int compareTo(IntArrayList l) {
      int s1 = this.size();
      int s2 = l.size();
      int[] a1 = this.a;
      int[] a2 = l.a;

      int i;
      for (i = 0; i < s1 && i < s2; i++) {
         int e1 = a1[i];
         int e2 = a2[i];
         int r;
         if ((r = Integer.compare(e1, e2)) != 0) {
            return r;
         }
      }

      return i < s2 ? -1 : (i < s1 ? 1 : 0);
   }

   private void writeObject(ObjectOutputStream s) throws IOException {
      s.defaultWriteObject();

      for (int i = 0; i < this.size; i++) {
         s.writeInt(this.a[i]);
      }
   }

   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
      s.defaultReadObject();
      this.a = new int[this.size];

      for (int i = 0; i < this.size; i++) {
         this.a[i] = s.readInt();
      }
   }
}
