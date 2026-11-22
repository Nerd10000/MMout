/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import java.util.Map;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class ResolvedModels extends Record {
/*     */   private final ResolvedModel missing;
/*     */   
/*     */   private final Map<Identifier, ResolvedModel> models;
/*     */   
/*     */   private ResolvedModels(ResolvedModel missing, Map<Identifier, ResolvedModel> models) {
/* 254 */     this.missing = missing;
/* 254 */     this.models = models;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #254	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public ResolvedModel missing() {
/* 254 */     return this.missing;
/*     */   }
/*     */   
/*     */   public Map<Identifier, ResolvedModel> models() {
/* 254 */     return this.models;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\model\ModelManager$ResolvedModels.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */