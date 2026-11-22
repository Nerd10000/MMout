/*     */ package com.mojang.blaze3d.util;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongFunction;
/*     */ 
/*     */ public interface Allocator<T> {
/*     */   T alloc(long paramLong);
/*     */   
/*     */   void free(T paramT);
/*     */   
/*     */   static <T> Allocator<T> create(final LongFunction<T> alloc, final Consumer<T> free) {
/* 146 */     return new Allocator<T>() {
/*     */         public T alloc(long size) {
/* 149 */           return alloc.apply(size);
/*     */         }
/*     */         
/*     */         public void free(T t) {
/* 154 */           free.accept(t);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3\\util\TransientBlockAllocator$Allocator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */