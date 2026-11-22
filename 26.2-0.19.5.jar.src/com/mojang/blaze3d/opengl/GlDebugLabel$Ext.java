/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import org.lwjgl.opengl.EXTDebugLabel;
/*     */ 
/*     */ class Ext extends GlDebugLabel {
/*     */   public void applyLabel(GlBuffer buffer, Supplier<String> label) {
/* 114 */     if (label != null)
/* 115 */       EXTDebugLabel.glLabelObjectEXT(37201, buffer.handle(), StringUtil.truncateStringIfNecessary(label.get(), 256, true)); 
/*     */   }
/*     */   
/*     */   public void applyLabel(GlTexture texture) {
/* 121 */     EXTDebugLabel.glLabelObjectEXT(5890, texture.id, StringUtil.truncateStringIfNecessary(texture.getLabel(), 256, true));
/*     */   }
/*     */   
/*     */   public void applyLabel(GlShaderModule shaderModule) {
/* 126 */     EXTDebugLabel.glLabelObjectEXT(35656, shaderModule.getShaderId(), StringUtil.truncateStringIfNecessary(shaderModule.getDebugLabel(), 256, true));
/*     */   }
/*     */   
/*     */   public void applyLabel(GlProgram program) {
/* 131 */     EXTDebugLabel.glLabelObjectEXT(35648, program.getProgramId(), StringUtil.truncateStringIfNecessary(program.getDebugLabel(), 256, true));
/*     */   }
/*     */   
/*     */   public void applyLabel(VertexArrayCache.VertexArray vertexArray) {
/* 136 */     EXTDebugLabel.glLabelObjectEXT(32884, vertexArray.id, StringUtil.truncateStringIfNecessary(vertexArray.formatName, 256, true));
/*     */   }
/*     */   
/*     */   public boolean exists() {
/* 141 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlDebugLabel$Ext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */