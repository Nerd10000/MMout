/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*    */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*    */ 
/*    */ public final class LevelDataAndDimensions extends Record {
/*    */   private final WorldDataAndGenSettings worldDataAndGenSettings;
/*    */   
/*    */   private final WorldDimensions.Complete dimensions;
/*    */   
/*    */   public LevelDataAndDimensions(WorldDataAndGenSettings worldDataAndGenSettings, WorldDimensions.Complete dimensions) {
/*  6 */     this.worldDataAndGenSettings = worldDataAndGenSettings;
/*  6 */     this.dimensions = dimensions;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public WorldDataAndGenSettings worldDataAndGenSettings() {
/*  6 */     return this.worldDataAndGenSettings;
/*    */   }
/*    */   
/*    */   public WorldDimensions.Complete dimensions() {
/*  6 */     return this.dimensions;
/*    */   }
/*    */   
/*    */   public static LevelDataAndDimensions create(WorldData data, WorldGenSettings genSettings, WorldDimensions.Complete dimensions) {
/*  8 */     return new LevelDataAndDimensions(new WorldDataAndGenSettings(data, genSettings), dimensions);
/*    */   }
/*    */   
/*    */   public static final class WorldDataAndGenSettings extends Record {
/*    */     private final WorldData data;
/*    */     
/*    */     private final WorldGenSettings genSettings;
/*    */     
/*    */     public WorldDataAndGenSettings(WorldData data, WorldGenSettings genSettings) {
/* 14 */       this.data = data;
/* 14 */       this.genSettings = genSettings;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #14	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelDataAndDimensions$WorldDataAndGenSettings;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public WorldData data() {
/* 14 */       return this.data;
/*    */     }
/*    */     
/*    */     public WorldGenSettings genSettings() {
/* 14 */       return this.genSettings;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelDataAndDimensions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */