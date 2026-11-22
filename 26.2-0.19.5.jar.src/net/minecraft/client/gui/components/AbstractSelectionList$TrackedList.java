/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.AbstractList;
/*     */ import java.util.List;
/*     */ 
/*     */ class TrackedList extends AbstractList<E> {
/* 528 */   private final List<E> delegate = Lists.newArrayList();
/*     */   
/*     */   public E get(int index) {
/* 532 */     return this.delegate.get(index);
/*     */   }
/*     */   
/*     */   public int size() {
/* 537 */     return this.delegate.size();
/*     */   }
/*     */   
/*     */   public E set(int index, E element) {
/* 542 */     AbstractSelectionList.Entry entry = (AbstractSelectionList.Entry)this.delegate.set(index, element);
/* 543 */     AbstractSelectionList.this.bindEntryToSelf((AbstractSelectionList.Entry)element);
/* 544 */     return (E)entry;
/*     */   }
/*     */   
/*     */   public void add(int index, E element) {
/* 549 */     this.delegate.add(index, element);
/* 550 */     AbstractSelectionList.this.bindEntryToSelf((AbstractSelectionList.Entry)element);
/*     */   }
/*     */   
/*     */   public E remove(int index) {
/* 555 */     return this.delegate.remove(index);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\AbstractSelectionList$TrackedList.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */