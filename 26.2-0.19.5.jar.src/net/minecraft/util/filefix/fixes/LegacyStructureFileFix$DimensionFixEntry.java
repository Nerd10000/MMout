/*    */ package net.minecraft.util.filefix.fixes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.filefix.access.ChunkNbt;
/*    */ import net.minecraft.util.filefix.access.FileAccess;
/*    */ import net.minecraft.util.filefix.access.SavedDataNbt;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ final class DimensionFixEntry extends Record {
/*    */   private final ResourceKey<Level> dimensionKey;
/*    */   
/*    */   private final List<FileAccess<SavedDataNbt>> structureFileAccess;
/*    */   
/*    */   private final FileAccess<ChunkNbt> chunkFileAccess;
/*    */   
/*    */   private final Long2ObjectOpenHashMap<LegacyStructureFileFix.LegacyStructureData> structures;
/*    */   
/*    */   private DimensionFixEntry(ResourceKey<Level> dimensionKey, List<FileAccess<SavedDataNbt>> structureFileAccess, FileAccess<ChunkNbt> chunkFileAccess, Long2ObjectOpenHashMap<LegacyStructureFileFix.LegacyStructureData> structures) {
/* 94 */     this.dimensionKey = dimensionKey;
/* 94 */     this.structureFileAccess = structureFileAccess;
/* 94 */     this.chunkFileAccess = chunkFileAccess;
/* 94 */     this.structures = structures;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #94	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public ResourceKey<Level> dimensionKey() {
/* 94 */     return this.dimensionKey;
/*    */   }
/*    */   
/*    */   public List<FileAccess<SavedDataNbt>> structureFileAccess() {
/* 94 */     return this.structureFileAccess;
/*    */   }
/*    */   
/*    */   public FileAccess<ChunkNbt> chunkFileAccess() {
/* 94 */     return this.chunkFileAccess;
/*    */   }
/*    */   
/*    */   public Long2ObjectOpenHashMap<LegacyStructureFileFix.LegacyStructureData> structures() {
/* 94 */     return this.structures;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\LegacyStructureFileFix$DimensionFixEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */