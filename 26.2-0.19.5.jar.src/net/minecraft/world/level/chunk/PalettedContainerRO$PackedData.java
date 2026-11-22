/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.LongStream;
/*    */ 
/*    */ public final class PackedData<T> extends Record {
/*    */   private final List<T> paletteEntries;
/*    */   
/*    */   private final Optional<LongStream> storage;
/*    */   
/*    */   private final int bitsPerEntry;
/*    */   
/*    */   public static final int UNKNOWN_BITS_PER_ENTRY = -1;
/*    */   
/*    */   public int bitsPerEntry() {
/* 37 */     return this.bitsPerEntry;
/*    */   }
/*    */   
/*    */   public Optional<LongStream> storage() {
/* 37 */     return this.storage;
/*    */   }
/*    */   
/*    */   public List<T> paletteEntries() {
/* 37 */     return this.paletteEntries;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #37	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/chunk/PalettedContainerRO$PackedData<TT;>;
/*    */   }
/*    */   
/*    */   public PackedData(List<T> paletteEntries, Optional<LongStream> storage, int bitsPerEntry) {
/* 37 */     this.paletteEntries = paletteEntries;
/* 37 */     this.storage = storage;
/* 37 */     this.bitsPerEntry = bitsPerEntry;
/*    */   }
/*    */   
/*    */   public PackedData(List<T> paletteEntries, Optional<LongStream> storage) {
/* 46 */     this(paletteEntries, storage, -1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PalettedContainerRO$PackedData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */