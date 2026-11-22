/*   */ package net.minecraft.util.worldupdate;
/*   */ 
/*   */ import java.util.List;
/*   */ import net.minecraft.world.level.ChunkPos;
/*   */ import net.minecraft.world.level.chunk.storage.RegionFile;
/*   */ 
/*   */ public final class FileToUpgrade extends Record {
/*   */   private final RegionFile file;
/*   */   
/*   */   private final List<ChunkPos> chunksToUpgrade;
/*   */   
/*   */   public FileToUpgrade(RegionFile file, List<ChunkPos> chunksToUpgrade) {
/* 8 */     this.file = file;
/* 8 */     this.chunksToUpgrade = chunksToUpgrade;
/*   */   }
/*   */   
/*   */   public final String toString() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/worldupdate/FileToUpgrade;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/worldupdate/FileToUpgrade;
/*   */   }
/*   */   
/*   */   public final int hashCode() {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/worldupdate/FileToUpgrade;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/util/worldupdate/FileToUpgrade;
/*   */   }
/*   */   
/*   */   public final boolean equals(Object o) {
/*   */     // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/worldupdate/FileToUpgrade;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/util/worldupdate/FileToUpgrade;
/*   */     //   0	8	1	o	Ljava/lang/Object;
/*   */   }
/*   */   
/*   */   public RegionFile file() {
/* 8 */     return this.file;
/*   */   }
/*   */   
/*   */   public List<ChunkPos> chunksToUpgrade() {
/* 8 */     return this.chunksToUpgrade;
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\FileToUpgrade.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */