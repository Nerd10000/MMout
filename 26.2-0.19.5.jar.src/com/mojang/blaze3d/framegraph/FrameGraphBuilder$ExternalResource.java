/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ class ExternalResource<T> extends FrameGraphBuilder.VirtualResource<T> {
/*     */   private final T resource;
/*     */   
/*     */   public ExternalResource(String name, FrameGraphBuilder.Pass createdBy, T resource) {
/* 338 */     super(name, createdBy);
/* 339 */     this.resource = resource;
/*     */   }
/*     */   
/*     */   public T get() {
/* 344 */     return this.resource;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FrameGraphBuilder$ExternalResource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */