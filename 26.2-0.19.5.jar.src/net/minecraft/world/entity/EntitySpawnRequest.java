/*   */ package net.minecraft.world.entity;
/*   */ 
/*   */ public final class EntitySpawnRequest extends Record {
/*   */   private final EntitySpawnReason reason;
/*   */   
/*   */   private final boolean ignoreChecks;
/*   */   
/*   */   public EntitySpawnRequest(EntitySpawnReason reason, boolean ignoreChecks) {
/* 4 */     this.reason = reason;
/* 4 */     this.ignoreChecks = ignoreChecks;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/EntitySpawnRequest;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #4	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/EntitySpawnRequest;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/EntitySpawnRequest;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #4	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/EntitySpawnRequest;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/EntitySpawnRequest;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #4	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/entity/EntitySpawnRequest;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public EntitySpawnReason reason() {
/* 4 */     return this.reason;
/*   */   }
/*   */   
/*   */   public boolean ignoreChecks() {
/* 4 */     return this.ignoreChecks;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntitySpawnRequest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */