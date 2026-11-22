/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceList;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class DestructionQueue<T> implements AutoCloseable {
/*    */   private final Destroyer<T> destroyCallback;
/*    */   
/*    */   private final ReferenceList<ReferenceArrayList<T>> destructionQueues;
/*    */   
/* 10 */   private int currentDestructionQueueIndex = 0;
/*    */   
/*    */   public DestructionQueue(int internalQueueCount, Destroyer<T> destroyCallback) {
/* 13 */     this.destroyCallback = destroyCallback;
/* 14 */     this.destructionQueues = (ReferenceList<ReferenceArrayList<T>>)new ReferenceArrayList(internalQueueCount);
/* 15 */     for (int i = 0; i < internalQueueCount; i++)
/* 16 */       this.destructionQueues.add(new ReferenceArrayList()); 
/*    */   }
/*    */   
/*    */   public void close() {
/* 22 */     for (int i = 0; i < this.destructionQueues.size(); i++) {
/* 23 */       if (rotate())
/* 24 */         i = 0; 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean rotate() {
/* 30 */     this.currentDestructionQueueIndex++;
/* 31 */     this.currentDestructionQueueIndex %= this.destructionQueues.size();
/* 34 */     ReferenceArrayList<T> currentQueue = (ReferenceArrayList<T>)this.destructionQueues.set(this.currentDestructionQueueIndex, new ReferenceArrayList());
/* 35 */     if (currentQueue.isEmpty())
/* 36 */       return false; 
/* 38 */     this.destroyCallback.begin(currentQueue.size());
/* 39 */     Objects.requireNonNull(this.destroyCallback);
/* 39 */     currentQueue.forEach(this.destroyCallback::destroy);
/* 40 */     this.destroyCallback.end();
/* 42 */     return true;
/*    */   }
/*    */   
/*    */   public void add(T t) {
/* 46 */     ReferenceArrayList<T> currentQueue = (ReferenceArrayList<T>)this.destructionQueues.get(this.currentDestructionQueueIndex);
/* 47 */     currentQueue.add(t);
/*    */   }
/*    */   
/*    */   public static interface Destroyer<T> {
/*    */     default void begin(int count) {}
/*    */     
/*    */     void destroy(T param1T);
/*    */     
/*    */     default void end() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\DestructionQueue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */