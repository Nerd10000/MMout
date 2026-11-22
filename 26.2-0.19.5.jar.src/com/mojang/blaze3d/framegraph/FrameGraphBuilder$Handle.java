/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ class Handle<T> implements ResourceHandle<T> {
/*     */   private final FrameGraphBuilder.VirtualResource<T> holder;
/*     */   
/*     */   private final int version;
/*     */   
/*     */   private final FrameGraphBuilder.Pass createdBy;
/*     */   
/* 250 */   private final BitSet readBy = new BitSet();
/*     */   
/*     */   private Handle<T> aliasedBy;
/*     */   
/*     */   private Handle(FrameGraphBuilder.VirtualResource<T> holder, int version, FrameGraphBuilder.Pass createdBy) {
/* 254 */     this.holder = holder;
/* 255 */     this.version = version;
/* 256 */     this.createdBy = createdBy;
/*     */   }
/*     */   
/*     */   public T get() {
/* 261 */     return this.holder.get();
/*     */   }
/*     */   
/*     */   private Handle<T> writeAndAlias(FrameGraphBuilder.Pass pass) {
/* 265 */     if (this.holder.handle != this)
/* 266 */       throw new IllegalStateException("Handle " + String.valueOf(this) + " is no longer valid, as its contents were moved into " + String.valueOf(this.aliasedBy)); 
/* 268 */     Handle<T> newHandle = new Handle(this.holder, this.version + 1, pass);
/* 269 */     this.holder.handle = newHandle;
/* 270 */     this.aliasedBy = newHandle;
/* 271 */     return newHandle;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 276 */     if (this.createdBy != null)
/* 277 */       return String.valueOf(this.holder) + "#" + String.valueOf(this.holder) + " (from " + this.version + ")"; 
/* 279 */     return String.valueOf(this.holder) + "#" + String.valueOf(this.holder);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FrameGraphBuilder$Handle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */