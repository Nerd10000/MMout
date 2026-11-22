/*    */ package com.mojang.blaze3d.resource;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.ArrayDeque;
/*    */ import java.util.Collection;
/*    */ import java.util.Deque;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class CrossFrameResourcePool implements GraphicsResourceAllocator, AutoCloseable {
/*    */   private final int framesToKeepResource;
/*    */   
/* 16 */   private final Deque<ResourceEntry<?>> pool = new ArrayDeque<>();
/*    */   
/*    */   public CrossFrameResourcePool(int framesToKeepResource) {
/* 19 */     this.framesToKeepResource = framesToKeepResource;
/*    */   }
/*    */   
/*    */   public void endFrame() {
/* 23 */     Iterator<? extends ResourceEntry<?>> iterator = this.pool.iterator();
/* 24 */     while (iterator.hasNext()) {
/* 25 */       ResourceEntry<?> entry = iterator.next();
/* 26 */       if (entry.framesToLive-- == 0) {
/* 27 */         entry.close();
/* 28 */         iterator.remove();
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public <T> T acquire(ResourceDescriptor<T> descriptor) {
/* 35 */     T resource = acquireWithoutPreparing(descriptor);
/* 36 */     descriptor.prepare(resource);
/* 37 */     return resource;
/*    */   }
/*    */   
/*    */   private <T> T acquireWithoutPreparing(ResourceDescriptor<T> descriptor) {
/* 42 */     Iterator<? extends ResourceEntry<?>> iterator = this.pool.iterator();
/* 43 */     while (iterator.hasNext()) {
/* 44 */       ResourceEntry<?> entry = iterator.next();
/* 45 */       if (descriptor.canUsePhysicalResource(entry.descriptor)) {
/* 46 */         iterator.remove();
/* 47 */         return entry.value;
/*    */       } 
/*    */     } 
/* 50 */     return descriptor.allocate();
/*    */   }
/*    */   
/*    */   public <T> void release(ResourceDescriptor<T> descriptor, T resource) {
/* 55 */     this.pool.addFirst(new ResourceEntry(descriptor, resource, this.framesToKeepResource));
/*    */   }
/*    */   
/*    */   public void clear() {
/* 59 */     this.pool.forEach(ResourceEntry::close);
/* 60 */     this.pool.clear();
/*    */   }
/*    */   
/*    */   public void close() {
/* 65 */     clear();
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected Collection<ResourceEntry<?>> entries() {
/* 70 */     return this.pool;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   protected static final class ResourceEntry<T> implements AutoCloseable {
/*    */     private final ResourceDescriptor<T> descriptor;
/*    */     
/*    */     private final T value;
/*    */     
/*    */     private int framesToLive;
/*    */     
/*    */     private ResourceEntry(ResourceDescriptor<T> descriptor, T value, int framesToLive) {
/* 80 */       this.descriptor = descriptor;
/* 81 */       this.value = value;
/* 82 */       this.framesToLive = framesToLive;
/*    */     }
/*    */     
/*    */     public void close() {
/* 87 */       this.descriptor.free(this.value);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\resource\CrossFrameResourcePool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */