/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ 
/*     */ public class VertexArray {
/*     */   @VisibleForDebug
/*     */   final int id;
/*     */   
/*     */   @VisibleForDebug
/*     */   final String formatName;
/*     */   
/*     */   private VertexArray(int id, VertexFormat[] vertexBindings) {
/* 203 */     this.id = id;
/* 204 */     this.formatName = Arrays.<VertexFormat>stream(vertexBindings).filter(Objects::nonNull).map(VertexFormat::toString).collect(Collectors.joining(", "));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\VertexArrayCache$VertexArray.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */