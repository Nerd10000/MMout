/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SequencedCollection;
/*     */ 
/*     */ class ReversedView extends AbstractList<T> implements ListAndDeque<T> {
/*     */   private final ArrayListDeque<T> source;
/*     */   
/*     */   public ReversedView(ArrayListDeque<T> source) {
/* 317 */     this.source = source;
/*     */   }
/*     */   
/*     */   public ListAndDeque<T> reversed() {
/* 322 */     return this.source;
/*     */   }
/*     */   
/*     */   public T getFirst() {
/* 327 */     return this.source.getLast();
/*     */   }
/*     */   
/*     */   public T getLast() {
/* 332 */     return this.source.getFirst();
/*     */   }
/*     */   
/*     */   public void addFirst(T t) {
/* 337 */     this.source.addLast(t);
/*     */   }
/*     */   
/*     */   public void addLast(T t) {
/* 342 */     this.source.addFirst(t);
/*     */   }
/*     */   
/*     */   public boolean offerFirst(T t) {
/* 347 */     return this.source.offerLast(t);
/*     */   }
/*     */   
/*     */   public boolean offerLast(T t) {
/* 352 */     return this.source.offerFirst(t);
/*     */   }
/*     */   
/*     */   public T pollFirst() {
/* 357 */     return this.source.pollLast();
/*     */   }
/*     */   
/*     */   public T pollLast() {
/* 362 */     return this.source.pollFirst();
/*     */   }
/*     */   
/*     */   public T peekFirst() {
/* 367 */     return this.source.peekLast();
/*     */   }
/*     */   
/*     */   public T peekLast() {
/* 372 */     return this.source.peekFirst();
/*     */   }
/*     */   
/*     */   public T removeFirst() {
/* 377 */     return this.source.removeLast();
/*     */   }
/*     */   
/*     */   public T removeLast() {
/* 382 */     return this.source.removeFirst();
/*     */   }
/*     */   
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 387 */     return this.source.removeLastOccurrence(o);
/*     */   }
/*     */   
/*     */   public boolean removeLastOccurrence(Object o) {
/* 392 */     return this.source.removeFirstOccurrence(o);
/*     */   }
/*     */   
/*     */   public Iterator<T> descendingIterator() {
/* 397 */     return this.source.iterator();
/*     */   }
/*     */   
/*     */   public int size() {
/* 402 */     return this.source.size();
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 407 */     return this.source.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean contains(Object o) {
/* 412 */     return this.source.contains(o);
/*     */   }
/*     */   
/*     */   public T get(int index) {
/* 417 */     return this.source.get(reverseIndex(index));
/*     */   }
/*     */   
/*     */   public T set(int index, T element) {
/* 422 */     return this.source.set(reverseIndex(index), element);
/*     */   }
/*     */   
/*     */   public void add(int index, T element) {
/* 428 */     this.source.add(reverseIndex(index) + 1, element);
/*     */   }
/*     */   
/*     */   public T remove(int index) {
/* 433 */     return this.source.remove(reverseIndex(index));
/*     */   }
/*     */   
/*     */   public int indexOf(Object o) {
/* 438 */     return reverseIndex(this.source.lastIndexOf(o));
/*     */   }
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 443 */     return reverseIndex(this.source.indexOf(o));
/*     */   }
/*     */   
/*     */   public List<T> subList(int fromIndex, int toIndex) {
/* 448 */     return this.source.subList(reverseIndex(toIndex) + 1, reverseIndex(fromIndex) + 1).reversed();
/*     */   }
/*     */   
/*     */   public Iterator<T> iterator() {
/* 453 */     return this.source.descendingIterator();
/*     */   }
/*     */   
/*     */   public void clear() {
/* 458 */     this.source.clear();
/*     */   }
/*     */   
/*     */   private int reverseIndex(int index) {
/* 462 */     return (index == -1) ? -1 : (this.source.size() - 1 - index);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ArrayListDeque$ReversedView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */