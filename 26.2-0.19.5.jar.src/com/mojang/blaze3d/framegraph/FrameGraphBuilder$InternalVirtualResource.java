/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*     */ import java.util.Objects;
/*     */ 
/*     */ class InternalVirtualResource<T> extends FrameGraphBuilder.VirtualResource<T> {
/*     */   private final int id;
/*     */   
/*     */   private final ResourceDescriptor<T> descriptor;
/*     */   
/*     */   private T physicalResource;
/*     */   
/*     */   public InternalVirtualResource(int id, String name, FrameGraphBuilder.Pass createdBy, ResourceDescriptor<T> descriptor) {
/* 308 */     super(name, createdBy);
/* 309 */     this.id = id;
/* 310 */     this.descriptor = descriptor;
/*     */   }
/*     */   
/*     */   public T get() {
/* 315 */     return Objects.requireNonNull(this.physicalResource, "Resource is not currently available");
/*     */   }
/*     */   
/*     */   public void acquire(GraphicsResourceAllocator allocator) {
/* 319 */     if (this.physicalResource != null)
/* 320 */       throw new IllegalStateException("Tried to acquire physical resource, but it was already assigned"); 
/* 322 */     this.physicalResource = (T)allocator.acquire(this.descriptor);
/*     */   }
/*     */   
/*     */   public void release(GraphicsResourceAllocator allocator) {
/* 326 */     if (this.physicalResource == null)
/* 327 */       throw new IllegalStateException("Tried to release physical resource that was not allocated"); 
/* 329 */     allocator.release(this.descriptor, this.physicalResource);
/* 330 */     this.physicalResource = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FrameGraphBuilder$InternalVirtualResource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */