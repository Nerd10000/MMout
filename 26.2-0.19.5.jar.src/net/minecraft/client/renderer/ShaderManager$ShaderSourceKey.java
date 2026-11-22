/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class ShaderSourceKey extends Record {
/*     */   private final Identifier id;
/*     */   
/*     */   private final ShaderType type;
/*     */   
/*     */   private ShaderSourceKey(Identifier id, ShaderType type) {
/* 262 */     this.id = id;
/* 262 */     this.type = type;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #262	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #262	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 262 */     return this.id;
/*     */   }
/*     */   
/*     */   public ShaderType type() {
/* 262 */     return this.type;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 265 */     return String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderManager$ShaderSourceKey.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */