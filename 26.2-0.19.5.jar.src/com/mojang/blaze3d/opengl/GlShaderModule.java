/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.shaders.ShaderType;
/*    */ import com.mojang.blaze3d.systems.RenderSystem;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class GlShaderModule implements AutoCloseable {
/*    */   private static final int NOT_ALLOCATED = -1;
/*    */   
/*  9 */   public static final GlShaderModule INVALID_SHADER = new GlShaderModule(-1, Identifier.withDefaultNamespace("invalid"), ShaderType.VERTEX);
/*    */   
/*    */   private final Identifier id;
/*    */   
/*    */   private int shaderId;
/*    */   
/*    */   private final ShaderType type;
/*    */   
/*    */   public GlShaderModule(int shaderId, Identifier id, ShaderType type) {
/* 16 */     this.id = id;
/* 17 */     this.shaderId = shaderId;
/* 18 */     this.type = type;
/*    */   }
/*    */   
/*    */   public void close() {
/* 23 */     if (this.shaderId == -1)
/* 24 */       throw new IllegalStateException("Already closed"); 
/* 26 */     RenderSystem.assertOnRenderThread();
/* 27 */     GlStateManager.glDeleteShader(this.shaderId);
/* 28 */     this.shaderId = -1;
/*    */   }
/*    */   
/*    */   public Identifier getId() {
/* 32 */     return this.id;
/*    */   }
/*    */   
/*    */   public int getShaderId() {
/* 36 */     return this.shaderId;
/*    */   }
/*    */   
/*    */   public String getDebugLabel() {
/* 40 */     return this.type.idConverter().idToFile(this.id).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlShaderModule.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */