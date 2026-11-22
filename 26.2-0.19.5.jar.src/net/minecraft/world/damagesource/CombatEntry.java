/*   */ package net.minecraft.world.damagesource;
/*   */ 
/*   */ public final class CombatEntry extends Record {
/*   */   private final DamageSource source;
/*   */   
/*   */   private final float damage;
/*   */   
/*   */   private final FallLocation fallLocation;
/*   */   
/*   */   private final float fallDistance;
/*   */   
/*   */   public CombatEntry(DamageSource source, float damage, FallLocation fallLocation, float fallDistance) {
/* 5 */     this.source = source;
/* 5 */     this.damage = damage;
/* 5 */     this.fallLocation = fallLocation;
/* 5 */     this.fallDistance = fallDistance;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/damagesource/CombatEntry;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/damagesource/CombatEntry;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/damagesource/CombatEntry;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/damagesource/CombatEntry;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/damagesource/CombatEntry;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/damagesource/CombatEntry;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public DamageSource source() {
/* 5 */     return this.source;
/*   */   }
/*   */   
/*   */   public float damage() {
/* 5 */     return this.damage;
/*   */   }
/*   */   
/*   */   public FallLocation fallLocation() {
/* 5 */     return this.fallLocation;
/*   */   }
/*   */   
/*   */   public float fallDistance() {
/* 5 */     return this.fallDistance;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\damagesource\CombatEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */