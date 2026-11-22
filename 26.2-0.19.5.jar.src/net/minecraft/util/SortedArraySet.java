/*     */ package net.minecraft.util;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrays;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class SortedArraySet<T> extends AbstractSet<T> {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   
/*     */   private final Comparator<T> comparator;
/*     */   
/*     */   private T[] contents;
/*     */   
/*     */   private int size;
/*     */   
/*     */   private SortedArraySet(int initialCapacity, Comparator<T> comparator) {
/*  24 */     this.comparator = comparator;
/*  26 */     if (initialCapacity < 0)
/*  27 */       throw new IllegalArgumentException("Initial capacity (" + initialCapacity + ") is negative"); 
/*  29 */     this.contents = castRawArray(new Object[initialCapacity]);
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> SortedArraySet<T> create() {
/*  33 */     return create(10);
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> SortedArraySet<T> create(int initialCapacity) {
/*  37 */     return new SortedArraySet<>(initialCapacity, (Comparator)Comparator.naturalOrder());
/*     */   }
/*     */   
/*     */   public static <T> SortedArraySet<T> create(Comparator<T> comparator) {
/*  41 */     return create(comparator, 10);
/*     */   }
/*     */   
/*     */   public static <T> SortedArraySet<T> create(Comparator<T> comparator, int initialCapacity) {
/*  45 */     return new SortedArraySet<>(initialCapacity, comparator);
/*     */   }
/*     */   
/*     */   private static <T> T[] castRawArray(Object[] array) {
/*  50 */     return (T[])array;
/*     */   }
/*     */   
/*     */   private int findIndex(T t) {
/*  54 */     return Arrays.binarySearch(this.contents, 0, this.size, t, this.comparator);
/*     */   }
/*     */   
/*     */   private static int getInsertionPosition(int position) {
/*  61 */     return -position - 1;
/*     */   }
/*     */   
/*     */   public boolean add(T t) {
/*  66 */     int position = findIndex(t);
/*  67 */     if (position >= 0)
/*  68 */       return false; 
/*  71 */     int pos = getInsertionPosition(position);
/*  72 */     addInternal(t, pos);
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   private void grow(int capacity) {
/*  77 */     if (capacity <= this.contents.length)
/*     */       return; 
/*  80 */     if (this.contents != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
/*  81 */       capacity = Util.growByHalf(this.contents.length, capacity);
/*  82 */     } else if (capacity < 10) {
/*  83 */       capacity = 10;
/*     */     } 
/*  86 */     Object[] t = new Object[capacity];
/*  87 */     System.arraycopy(this.contents, 0, t, 0, this.size);
/*  88 */     this.contents = castRawArray(t);
/*     */   }
/*     */   
/*     */   private void addInternal(T t, int pos) {
/*  92 */     grow(this.size + 1);
/*  93 */     if (pos != this.size)
/*  94 */       System.arraycopy(this.contents, pos, this.contents, pos + 1, this.size - pos); 
/*  96 */     this.contents[pos] = t;
/*  97 */     this.size++;
/*     */   }
/*     */   
/*     */   private void removeInternal(int position) {
/* 101 */     this.size--;
/* 102 */     if (position != this.size)
/* 103 */       System.arraycopy(this.contents, position + 1, this.contents, position, this.size - position); 
/* 105 */     this.contents[this.size] = null;
/*     */   }
/*     */   
/*     */   private T getInternal(int position) {
/* 109 */     return this.contents[position];
/*     */   }
/*     */   
/*     */   public T addOrGet(T t) {
/* 113 */     int position = findIndex(t);
/* 114 */     if (position >= 0)
/* 115 */       return getInternal(position); 
/* 118 */     addInternal(t, getInsertionPosition(position));
/* 119 */     return t;
/*     */   }
/*     */   
/*     */   public boolean remove(Object o) {
/* 125 */     int position = findIndex((T)o);
/* 126 */     if (position >= 0) {
/* 127 */       removeInternal(position);
/* 128 */       return true;
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */   
/*     */   public T get(T t) {
/* 134 */     int position = findIndex(t);
/* 135 */     if (position >= 0)
/* 136 */       return getInternal(position); 
/* 138 */     return null;
/*     */   }
/*     */   
/*     */   public T first() {
/* 142 */     return getInternal(0);
/*     */   }
/*     */   
/*     */   public T last() {
/* 146 */     return getInternal(this.size - 1);
/*     */   }
/*     */   
/*     */   public boolean contains(Object o) {
/* 152 */     int result = findIndex((T)o);
/* 153 */     return (result >= 0);
/*     */   }
/*     */   
/*     */   private class ArrayIterator implements Iterator<T> {
/*     */     private int index;
/*     */     
/* 158 */     private int last = -1;
/*     */     
/*     */     public boolean hasNext() {
/* 162 */       return (this.index < SortedArraySet.this.size);
/*     */     }
/*     */     
/*     */     public T next() {
/* 167 */       if (this.index >= SortedArraySet.this.size)
/* 168 */         throw new NoSuchElementException(); 
/* 170 */       this.last = this.index++;
/* 171 */       return SortedArraySet.this.contents[this.last];
/*     */     }
/*     */     
/*     */     public void remove() {
/* 176 */       if (this.last == -1)
/* 177 */         throw new IllegalStateException(); 
/* 179 */       SortedArraySet.this.removeInternal(this.last);
/* 180 */       this.index--;
/* 181 */       this.last = -1;
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 187 */     return new ArrayIterator();
/*     */   }
/*     */   
/*     */   public int size() {
/* 192 */     return this.size;
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/* 197 */     return Arrays.copyOf(this.contents, this.size, Object[].class);
/*     */   }
/*     */   
/*     */   public <U> U[] toArray(U[] a) {
/* 203 */     if (a.length < this.size)
/* 204 */       return Arrays.copyOf(this.contents, this.size, (Class)a.getClass()); 
/* 206 */     System.arraycopy(this.contents, 0, a, 0, this.size);
/* 207 */     if (a.length > this.size)
/* 208 */       a[this.size] = null; 
/* 210 */     return a;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 215 */     Arrays.fill((Object[])this.contents, 0, this.size, (Object)null);
/* 216 */     this.size = 0;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 221 */     if (this == o)
/* 222 */       return true; 
/* 224 */     if (o instanceof SortedArraySet) {
/* 224 */       SortedArraySet<?> that = (SortedArraySet)o;
/* 225 */       if (this.comparator.equals(that.comparator))
/* 226 */         return (this.size == that.size && Arrays.equals((Object[])this.contents, (Object[])that.contents)); 
/*     */     } 
/* 230 */     return super.equals(o);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\SortedArraySet.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */