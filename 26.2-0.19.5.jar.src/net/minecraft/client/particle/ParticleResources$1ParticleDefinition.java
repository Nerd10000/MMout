/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ final class ParticleDefinition extends Record {
/*     */   private final Identifier id;
/*     */   
/*     */   private final Optional<List<Identifier>> sprites;
/*     */   
/*     */   ParticleDefinition(Identifier id, Optional<List<Identifier>> sprites) {
/* 206 */     this.id = id;
/* 206 */     this.sprites = sprites;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ParticleResources$1ParticleDefinition;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #206	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/particle/ParticleResources$1ParticleDefinition;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ParticleResources$1ParticleDefinition;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #206	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/particle/ParticleResources$1ParticleDefinition;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ParticleResources$1ParticleDefinition;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #206	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/particle/ParticleResources$1ParticleDefinition;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public Identifier id() {
/* 206 */     return this.id;
/*     */   }
/*     */   
/*     */   public Optional<List<Identifier>> sprites() {
/* 206 */     return this.sprites;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleResources$1ParticleDefinition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */