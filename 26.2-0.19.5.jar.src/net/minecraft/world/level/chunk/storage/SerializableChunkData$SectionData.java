/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ 
/*     */ public final class SectionData extends Record {
/*     */   private final int y;
/*     */   
/*     */   private final LevelChunkSection chunkSection;
/*     */   
/*     */   private final DataLayer blockLight;
/*     */   
/*     */   private final DataLayer skyLight;
/*     */   
/*     */   public SectionData(int y, LevelChunkSection chunkSection, DataLayer blockLight, DataLayer skyLight) {
/* 112 */     this.y = y;
/* 112 */     this.chunkSection = chunkSection;
/* 112 */     this.blockLight = blockLight;
/* 112 */     this.skyLight = skyLight;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #112	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/SerializableChunkData$SectionData;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int y() {
/* 112 */     return this.y;
/*     */   }
/*     */   
/*     */   public LevelChunkSection chunkSection() {
/* 112 */     return this.chunkSection;
/*     */   }
/*     */   
/*     */   public DataLayer blockLight() {
/* 112 */     return this.blockLight;
/*     */   }
/*     */   
/*     */   public DataLayer skyLight() {
/* 112 */     return this.skyLight;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\SerializableChunkData$SectionData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */