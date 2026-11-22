/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ 
/*    */ final class Data extends Record {
/*    */   private final Heightmap.Types heightMap;
/*    */   
/*    */   private final SpawnPlacementType placement;
/*    */   
/*    */   private final SpawnPlacements.SpawnPredicate<?> predicate;
/*    */   
/*    */   private Data(Heightmap.Types heightMap, SpawnPlacementType placement, SpawnPlacements.SpawnPredicate<?> predicate) {
/* 57 */     this.heightMap = heightMap;
/* 57 */     this.placement = placement;
/* 57 */     this.predicate = predicate;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/SpawnPlacements$Data;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SpawnPlacements$Data;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/SpawnPlacements$Data;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/SpawnPlacements$Data;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/SpawnPlacements$Data;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #57	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/SpawnPlacements$Data;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Heightmap.Types heightMap() {
/* 57 */     return this.heightMap;
/*    */   }
/*    */   
/*    */   public SpawnPlacementType placement() {
/* 57 */     return this.placement;
/*    */   }
/*    */   
/*    */   public SpawnPlacements.SpawnPredicate<?> predicate() {
/* 57 */     return this.predicate;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SpawnPlacements$Data.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */