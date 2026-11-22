/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ 
/*     */ class Dimension {
/*     */   public final int width;
/*     */   
/*     */   public final int height;
/*     */   
/*     */   private Dimension(int width, int height) {
/*  90 */     this.width = width;
/*  91 */     this.height = height;
/*     */   }
/*     */   
/*     */   private static List<Dimension> listWithFallback(int width, int height) {
/*  95 */     RenderSystem.assertOnRenderThread();
/*  97 */     int maxTextureSize = RenderSystem.getDevice().getDeviceInfo().limits().maxTextureSize();
/*  98 */     if (width <= 0 || width > maxTextureSize || height <= 0 || height > maxTextureSize)
/*  99 */       return (List<Dimension>)ImmutableList.of(MainTarget.DEFAULT_DIMENSIONS); 
/* 101 */     return (List<Dimension>)ImmutableList.of(new Dimension(width, height), MainTarget.DEFAULT_DIMENSIONS);
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 110 */     if (this == other)
/* 111 */       return true; 
/* 113 */     if (other == null || getClass() != other.getClass())
/* 114 */       return false; 
/* 116 */     Dimension that = (Dimension)other;
/* 117 */     return (this.width == that.width && this.height == that.height);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 122 */     return Objects.hash(new Object[] { Integer.valueOf(this.width), Integer.valueOf(this.height) });
/*     */   }
/*     */   
/*     */   public String toString() {
/* 127 */     return "" + this.width + "x" + this.width;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\MainTarget$Dimension.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */