/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ abstract class VirtualResource<T> {
/*     */   public final String name;
/*     */   
/*     */   public FrameGraphBuilder.Handle<T> handle;
/*     */   
/*     */   public VirtualResource(String name, FrameGraphBuilder.Pass createdBy) {
/* 288 */     this.name = name;
/* 289 */     this.handle = new FrameGraphBuilder.Handle<>(this, 0, createdBy);
/*     */   }
/*     */   
/*     */   public abstract T get();
/*     */   
/*     */   public String toString() {
/* 296 */     return this.name;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FrameGraphBuilder$VirtualResource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */