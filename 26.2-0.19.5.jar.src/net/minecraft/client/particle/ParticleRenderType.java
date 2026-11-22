/*   */ package net.minecraft.client.particle;
/*   */ 
/*   */ public final class ParticleRenderType extends Record {
/*   */   private final String name;
/*   */   
/*   */   private final String shorthand;
/*   */   
/*   */   public ParticleRenderType(String name, String shorthand) {
/* 3 */     this.name = name;
/* 3 */     this.shorthand = shorthand;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ParticleRenderType;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/particle/ParticleRenderType;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ParticleRenderType;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/particle/ParticleRenderType;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ParticleRenderType;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/particle/ParticleRenderType;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public String name() {
/* 3 */     return this.name;
/*   */   }
/*   */   
/*   */   public String shorthand() {
/* 3 */     return this.shorthand;
/*   */   }
/*   */   
/* 4 */   public static final ParticleRenderType SINGLE_QUADS = new ParticleRenderType("SINGLE_QUADS", "SQ");
/*   */   
/* 5 */   public static final ParticleRenderType ITEM_PICKUP = new ParticleRenderType("ITEM_PICKUP", "IP");
/*   */   
/* 6 */   public static final ParticleRenderType ELDER_GUARDIANS = new ParticleRenderType("ELDER_GUARDIANS", "EG");
/*   */   
/* 7 */   public static final ParticleRenderType NO_RENDER = new ParticleRenderType("NO_RENDER", "NR");
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\ParticleRenderType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */