/*    */ package com.mojang.blaze3d.pipeline;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ 
/*    */ public class TextureTarget extends RenderTarget {
/*    */   public TextureTarget(String label, int width, int height, boolean useDepth, GpuFormat format) {
/*  9 */     super(label, useDepth, format);
/* 10 */     RenderSystem.assertOnRenderThread();
/* 11 */     resize(width, height);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\TextureTarget.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */