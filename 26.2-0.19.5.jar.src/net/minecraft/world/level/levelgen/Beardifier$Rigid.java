/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
/*    */ 
/*    */ @VisibleForTesting
/*    */ public final class Rigid extends Record {
/*    */   private final BoundingBox box;
/*    */   
/*    */   private final TerrainAdjustment terrainAdjustment;
/*    */   
/*    */   private final int groundLevelDelta;
/*    */   
/*    */   public int groundLevelDelta() {
/* 36 */     return this.groundLevelDelta;
/*    */   }
/*    */   
/*    */   public TerrainAdjustment terrainAdjustment() {
/* 36 */     return this.terrainAdjustment;
/*    */   }
/*    */   
/*    */   public BoundingBox box() {
/* 36 */     return this.box;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #36	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Beardifier$Rigid;
/*    */   }
/*    */   
/*    */   public Rigid(BoundingBox box, TerrainAdjustment terrainAdjustment, int groundLevelDelta) {
/* 37 */     this.box = box;
/* 37 */     this.terrainAdjustment = terrainAdjustment;
/* 37 */     this.groundLevelDelta = groundLevelDelta;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Beardifier$Rigid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */