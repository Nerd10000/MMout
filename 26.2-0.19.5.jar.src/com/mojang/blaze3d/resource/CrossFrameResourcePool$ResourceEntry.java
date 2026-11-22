/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ 
/*    */ @VisibleForTesting
/*    */ public final class ResourceEntry<T> implements AutoCloseable {
/*    */   private final ResourceDescriptor<T> descriptor;
/*    */   
/*    */   private final T value;
/*    */   
/*    */   private int framesToLive;
/*    */   
/*    */   private ResourceEntry(ResourceDescriptor<T> descriptor, T value, int framesToLive) {
/* 80 */     this.descriptor = descriptor;
/* 81 */     this.value = value;
/* 82 */     this.framesToLive = framesToLive;
/*    */   }
/*    */   
/*    */   public void close() {
/* 87 */     this.descriptor.free(this.value);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\resource\CrossFrameResourcePool$ResourceEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */