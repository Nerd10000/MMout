/*    */ package com.mojang.blaze3d.textures;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ public abstract class GpuTexture implements AutoCloseable {
/*    */   public static final int USAGE_COPY_DST = 1;
/*    */   
/*    */   public static final int USAGE_COPY_SRC = 2;
/*    */   
/*    */   public static final int USAGE_TEXTURE_BINDING = 4;
/*    */   
/*    */   public static final int USAGE_RENDER_ATTACHMENT = 8;
/*    */   
/*    */   public static final int USAGE_CUBEMAP_COMPATIBLE = 16;
/*    */   
/*    */   private final GpuFormat format;
/*    */   
/*    */   private final int width;
/*    */   
/*    */   private final int height;
/*    */   
/*    */   private final int depthOrLayers;
/*    */   
/*    */   private final int mipLevels;
/*    */   
/*    */   private final int usage;
/*    */   
/*    */   private final String label;
/*    */   
/*    */   public GpuTexture(int usage, String label, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/* 50 */     this.usage = usage;
/* 51 */     this.label = label;
/* 52 */     this.format = format;
/* 53 */     this.width = width;
/* 54 */     this.height = height;
/* 55 */     this.depthOrLayers = depthOrLayers;
/* 56 */     this.mipLevels = mipLevels;
/*    */   }
/*    */   
/*    */   public int getWidth(int mipLevel) {
/* 60 */     return this.width >> mipLevel;
/*    */   }
/*    */   
/*    */   public int getHeight(int mipLevel) {
/* 64 */     return this.height >> mipLevel;
/*    */   }
/*    */   
/*    */   public int getDepthOrLayers() {
/* 68 */     return this.depthOrLayers;
/*    */   }
/*    */   
/*    */   public int getMipLevels() {
/* 72 */     return this.mipLevels;
/*    */   }
/*    */   
/*    */   public GpuFormat getFormat() {
/* 76 */     return this.format;
/*    */   }
/*    */   
/*    */   public int usage() {
/* 80 */     return this.usage;
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 84 */     return this.label;
/*    */   }
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public abstract boolean isClosed();
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.TYPE_USE})
/*    */   public static @interface Usage {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\textures\GpuTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */