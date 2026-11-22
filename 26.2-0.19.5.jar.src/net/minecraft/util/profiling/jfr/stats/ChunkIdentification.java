/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ 
/*    */ public final class ChunkIdentification extends Record {
/*    */   private final String level;
/*    */   
/*    */   private final String dimension;
/*    */   
/*    */   private final int x;
/*    */   
/*    */   private final int z;
/*    */   
/*    */   public ChunkIdentification(String level, String dimension, int x, int z) {
/*  6 */     this.level = level;
/*  6 */     this.dimension = dimension;
/*  6 */     this.x = x;
/*  6 */     this.z = z;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String level() {
/*  6 */     return this.level;
/*    */   }
/*    */   
/*    */   public String dimension() {
/*  6 */     return this.dimension;
/*    */   }
/*    */   
/*    */   public int x() {
/*  6 */     return this.x;
/*    */   }
/*    */   
/*    */   public int z() {
/*  6 */     return this.z;
/*    */   }
/*    */   
/*    */   public static ChunkIdentification from(RecordedEvent event) {
/*  8 */     return new ChunkIdentification(event
/*  9 */         .getString("level"), event
/* 10 */         .getString("dimension"), event
/* 11 */         .getInt("chunkPosX"), event
/* 12 */         .getInt("chunkPosZ"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\ChunkIdentification.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */