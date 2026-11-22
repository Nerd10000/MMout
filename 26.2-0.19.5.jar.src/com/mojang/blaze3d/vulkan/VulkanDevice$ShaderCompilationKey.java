/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class ShaderCompilationKey extends Record {
/*     */   private final Identifier id;
/*     */   
/*     */   private final ShaderType type;
/*     */   
/*     */   private final ShaderDefines defines;
/*     */   
/*     */   private ShaderCompilationKey(Identifier id, ShaderType type, ShaderDefines defines) {
/* 318 */     this.id = id;
/* 318 */     this.type = type;
/* 318 */     this.defines = defines;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #318	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #318	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanDevice$ShaderCompilationKey;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 318 */     return this.id;
/*     */   }
/*     */   
/*     */   public ShaderType type() {
/* 318 */     return this.type;
/*     */   }
/*     */   
/*     */   public ShaderDefines defines() {
/* 318 */     return this.defines;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 321 */     String string = String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/* 322 */     if (!this.defines.isEmpty())
/* 323 */       return string + " with " + string; 
/* 325 */     return string;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanDevice$ShaderCompilationKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */