/*    */ package net.minecraft.world.entity.monster.warden;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Comparator;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ @VisibleForTesting
/*    */ public final class Sorter extends Record implements Comparator<Entity> {
/*    */   private final AngerManagement angerManagement;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/monster/warden/AngerManagement$Sorter;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public AngerManagement angerManagement() {
/* 57 */     return this.angerManagement;
/*    */   }
/*    */   
/*    */   protected Sorter(AngerManagement angerManagement) {
/* 58 */     this.angerManagement = angerManagement;
/*    */   }
/*    */   
/*    */   public int compare(Entity entity1, Entity entity2) {
/* 61 */     if (entity1.equals(entity2))
/* 62 */       return 0; 
/* 65 */     int anger1 = this.angerManagement.angerBySuspect.getOrDefault(entity1, 0);
/* 66 */     int anger2 = this.angerManagement.angerBySuspect.getOrDefault(entity2, 0);
/* 69 */     this.angerManagement.highestAnger = Math.max(this.angerManagement.highestAnger, Math.max(anger1, anger2));
/* 71 */     boolean angryAt1 = AngerLevel.byAnger(anger1).isAngry();
/* 72 */     boolean angryAt2 = AngerLevel.byAnger(anger2).isAngry();
/* 73 */     if (angryAt1 != angryAt2)
/* 74 */       return angryAt1 ? -1 : 1; 
/* 78 */     boolean isPlayer1 = entity1 instanceof net.minecraft.world.entity.player.Player;
/* 79 */     boolean isPlayer2 = entity2 instanceof net.minecraft.world.entity.player.Player;
/* 80 */     if (isPlayer1 != isPlayer2)
/* 81 */       return isPlayer1 ? -1 : 1; 
/* 83 */     return Integer.compare(anger2, anger1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\AngerManagement$Sorter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */