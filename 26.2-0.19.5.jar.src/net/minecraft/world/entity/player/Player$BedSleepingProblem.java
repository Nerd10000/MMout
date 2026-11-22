/*      */ package net.minecraft.world.entity.player;
/*      */ 
/*      */ import net.minecraft.network.chat.Component;
/*      */ 
/*      */ public final class BedSleepingProblem extends Record {
/*      */   private final Component message;
/*      */   
/*      */   public BedSleepingProblem(Component message) {
/* 1396 */     this.message = message;
/*      */   }
/*      */   
/*      */   public final String toString() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)Ljava/lang/String;
/*      */     //   6: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1396	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */   }
/*      */   
/*      */   public final int hashCode() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)I
/*      */     //   6: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1396	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	7	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */   }
/*      */   
/*      */   public final boolean equals(Object o) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: aload_1
/*      */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;Ljava/lang/Object;)Z
/*      */     //   7: ireturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1396	-> 0
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   0	8	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */     //   0	8	1	o	Ljava/lang/Object;
/*      */   }
/*      */   
/*      */   public Component message() {
/* 1396 */     return this.message;
/*      */   }
/*      */   
/* 1399 */   public static final BedSleepingProblem TOO_FAR_AWAY = new BedSleepingProblem((Component)Component.translatable("block.minecraft.bed.too_far_away"));
/*      */   
/* 1400 */   public static final BedSleepingProblem OBSTRUCTED = new BedSleepingProblem((Component)Component.translatable("block.minecraft.bed.obstructed"));
/*      */   
/* 1401 */   public static final BedSleepingProblem OTHER_PROBLEM = new BedSleepingProblem(null);
/*      */   
/* 1402 */   public static final BedSleepingProblem NOT_SAFE = new BedSleepingProblem((Component)Component.translatable("block.minecraft.bed.not_safe"));
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\Player$BedSleepingProblem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */