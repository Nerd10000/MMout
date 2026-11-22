/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public final class ConversionParams extends Record {
/*    */   private final ConversionType type;
/*    */   
/*    */   private final boolean keepEquipment;
/*    */   
/*    */   private final boolean preserveCanPickUpLoot;
/*    */   
/*    */   private final PlayerTeam team;
/*    */   
/*    */   public ConversionParams(ConversionType type, boolean keepEquipment, boolean preserveCanPickUpLoot, PlayerTeam team) {
/*  6 */     this.type = type;
/*  6 */     this.keepEquipment = keepEquipment;
/*  6 */     this.preserveCanPickUpLoot = preserveCanPickUpLoot;
/*  6 */     this.team = team;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ConversionParams;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ConversionParams;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ConversionParams;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/ConversionParams;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ConversionParams;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/ConversionParams;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ConversionType type() {
/*  6 */     return this.type;
/*    */   }
/*    */   
/*    */   public boolean keepEquipment() {
/*  6 */     return this.keepEquipment;
/*    */   }
/*    */   
/*    */   public boolean preserveCanPickUpLoot() {
/*  6 */     return this.preserveCanPickUpLoot;
/*    */   }
/*    */   
/*    */   public PlayerTeam team() {
/*  6 */     return this.team;
/*    */   }
/*    */   
/*    */   public static ConversionParams single(Mob mob, boolean keepEquipment, boolean preserveCanPickUpLoot) {
/* 19 */     return new ConversionParams(ConversionType.SINGLE, keepEquipment, preserveCanPickUpLoot, mob.getTeam());
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface AfterConversion<T extends Mob> {
/*    */     void finalizeConversion(T param1T);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ConversionParams.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */