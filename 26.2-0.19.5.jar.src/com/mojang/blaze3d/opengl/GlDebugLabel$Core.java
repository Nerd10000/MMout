/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.lwjgl.opengl.GL33C;
/*     */ import org.lwjgl.opengl.KHRDebug;
/*     */ 
/*     */ class Core extends GlDebugLabel {
/*  65 */   private final int maxLabelLength = GL33C.glGetInteger(33512);
/*     */   
/*     */   public void applyLabel(GlBuffer buffer, Supplier<String> label) {
/*  70 */     if (label != null)
/*  71 */       KHRDebug.glObjectLabel(33504, buffer.handle(), StringUtil.truncateStringIfNecessary(label.get(), this.maxLabelLength, true)); 
/*     */   }
/*     */   
/*     */   public void applyLabel(GlTexture texture) {
/*  77 */     KHRDebug.glObjectLabel(5890, texture.id, StringUtil.truncateStringIfNecessary(texture.getLabel(), this.maxLabelLength, true));
/*     */   }
/*     */   
/*     */   public void applyLabel(GlShaderModule shaderModule) {
/*  82 */     KHRDebug.glObjectLabel(33505, shaderModule.getShaderId(), StringUtil.truncateStringIfNecessary(shaderModule.getDebugLabel(), this.maxLabelLength, true));
/*     */   }
/*     */   
/*     */   public void applyLabel(GlProgram program) {
/*  87 */     KHRDebug.glObjectLabel(33506, program.getProgramId(), StringUtil.truncateStringIfNecessary(program.getDebugLabel(), this.maxLabelLength, true));
/*     */   }
/*     */   
/*     */   public void applyLabel(VertexArrayCache.VertexArray vertexArray) {
/*  92 */     KHRDebug.glObjectLabel(32884, vertexArray.id, StringUtil.truncateStringIfNecessary(vertexArray.formatName, this.maxLabelLength, true));
/*     */   }
/*     */   
/*     */   public void pushDebugGroup(Supplier<String> label) {
/*  97 */     KHRDebug.glPushDebugGroup(33354, 0, label.get());
/*     */   }
/*     */   
/*     */   public void popDebugGroup() {
/* 102 */     KHRDebug.glPopDebugGroup();
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 107 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlDebugLabel$Core.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */