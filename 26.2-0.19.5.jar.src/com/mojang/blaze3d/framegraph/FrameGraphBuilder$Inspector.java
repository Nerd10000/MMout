/*     */ package com.mojang.blaze3d.framegraph;
/*     */ 
/*     */ public interface Inspector {
/* 349 */   public static final Inspector NONE = new Inspector() {
/*     */     
/*     */     };
/*     */   
/*     */   default void acquireResource(String name) {}
/*     */   
/*     */   default void releaseResource(String name) {}
/*     */   
/*     */   default void beforeExecutePass(String name) {}
/*     */   
/*     */   default void afterExecutePass(String name) {}
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FrameGraphBuilder$Inspector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */