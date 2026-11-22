/*    */ package com.mojang.blaze3d.textures;
/*    */ 
/*    */ public abstract class GpuTextureView implements AutoCloseable {
/*    */   private final GpuTexture texture;
/*    */   
/*    */   private final int baseMipLevel;
/*    */   
/*    */   private final int mipLevels;
/*    */   
/*    */   protected GpuTextureView(GpuTexture texture, int baseMipLevel, int mipLevels) {
/*  9 */     this.texture = texture;
/* 10 */     this.baseMipLevel = baseMipLevel;
/* 11 */     this.mipLevels = mipLevels;
/*    */   }
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public GpuTexture texture() {
/* 18 */     return this.texture;
/*    */   }
/*    */   
/*    */   public int baseMipLevel() {
/* 22 */     return this.baseMipLevel;
/*    */   }
/*    */   
/*    */   public int mipLevels() {
/* 26 */     return this.mipLevels;
/*    */   }
/*    */   
/*    */   public int getWidth(int mipLevel) {
/* 30 */     return this.texture.getWidth(mipLevel + this.baseMipLevel);
/*    */   }
/*    */   
/*    */   public int getHeight(int mipLevel) {
/* 34 */     return this.texture.getHeight(mipLevel + this.baseMipLevel);
/*    */   }
/*    */   
/*    */   public abstract boolean isClosed();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\textures\GpuTextureView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */