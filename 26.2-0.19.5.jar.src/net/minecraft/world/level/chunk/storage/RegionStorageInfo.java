/*   */ package net.minecraft.world.level.chunk.storage;
/*   */ 
/*   */ import net.minecraft.resources.ResourceKey;
/*   */ import net.minecraft.world.level.Level;
/*   */ 
/*   */ public final class RegionStorageInfo extends Record {
/*   */   private final String level;
/*   */   
/*   */   private final ResourceKey<Level> dimension;
/*   */   
/*   */   private final String type;
/*   */   
/*   */   public RegionStorageInfo(String level, ResourceKey<Level> dimension, String type) {
/* 6 */     this.level = level;
/* 6 */     this.dimension = dimension;
/* 6 */     this.type = type;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/storage/RegionStorageInfo;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public String level() {
/* 6 */     return this.level;
/*   */   }
/*   */   
/*   */   public ResourceKey<Level> dimension() {
/* 6 */     return this.dimension;
/*   */   }
/*   */   
/*   */   public String type() {
/* 6 */     return this.type;
/*   */   }
/*   */   
/*   */   public RegionStorageInfo withTypeSuffix(String suffix) {
/* 8 */     return new RegionStorageInfo(this.level, this.dimension, this.type + this.type);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\RegionStorageInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */