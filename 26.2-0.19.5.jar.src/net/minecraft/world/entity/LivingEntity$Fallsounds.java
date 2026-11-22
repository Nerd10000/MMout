/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ 
/*      */ public final class Fallsounds extends Record {
/*      */   private final SoundEvent small;
/*      */   
/*      */   private final SoundEvent big;
/*      */   
/*      */   public Fallsounds(SoundEvent small, SoundEvent big) {
/* 1763 */     this.small = small;
/* 1763 */     this.big = big;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1763	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1763	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1763	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public SoundEvent small() {
/* 1763 */     return this.small;
/*      */   }
/*      */   
/*      */   public SoundEvent big() {
/* 1763 */     return this.big;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\LivingEntity$Fallsounds.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */