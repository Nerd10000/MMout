/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public final class Configs extends Record {
/*     */   private final Map<ShaderManager.ShaderSourceKey, String> shaderSources;
/*     */   
/*     */   private final Map<Identifier, PostChainConfig> postChains;
/*     */   
/*     */   public Configs(Map<ShaderManager.ShaderSourceKey, String> shaderSources, Map<Identifier, PostChainConfig> postChains) {
/* 215 */     this.shaderSources = shaderSources;
/* 215 */     this.postChains = postChains;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/ShaderManager$Configs;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderManager$Configs;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderManager$Configs;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #215	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Map<ShaderManager.ShaderSourceKey, String> shaderSources() {
/* 215 */     return this.shaderSources;
/*     */   }
/*     */   
/*     */   public Map<Identifier, PostChainConfig> postChains() {
/* 215 */     return this.postChains;
/*     */   }
/*     */   
/* 219 */   public static final Configs EMPTY = new Configs(Map.of(), Map.of());
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\ShaderManager$Configs.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */