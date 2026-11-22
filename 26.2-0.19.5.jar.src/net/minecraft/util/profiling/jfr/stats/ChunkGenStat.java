/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import jdk.jfr.consumer.RecordedEvent;
/*    */ import net.minecraft.server.level.ColumnPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*    */ 
/*    */ public final class ChunkGenStat extends Record implements TimedStat {
/*    */   private final Duration duration;
/*    */   
/*    */   private final ChunkPos chunkPos;
/*    */   
/*    */   private final ColumnPos worldPos;
/*    */   
/*    */   private final ChunkStatus status;
/*    */   
/*    */   private final String level;
/*    */   
/*    */   public ChunkGenStat(Duration duration, ChunkPos chunkPos, ColumnPos worldPos, ChunkStatus status, String level) {
/* 11 */     this.duration = duration;
/* 11 */     this.chunkPos = chunkPos;
/* 11 */     this.worldPos = worldPos;
/* 11 */     this.status = status;
/* 11 */     this.level = level;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Duration duration() {
/* 11 */     return this.duration;
/*    */   }
/*    */   
/*    */   public ChunkPos chunkPos() {
/* 11 */     return this.chunkPos;
/*    */   }
/*    */   
/*    */   public ColumnPos worldPos() {
/* 11 */     return this.worldPos;
/*    */   }
/*    */   
/*    */   public ChunkStatus status() {
/* 11 */     return this.status;
/*    */   }
/*    */   
/*    */   public String level() {
/* 11 */     return this.level;
/*    */   }
/*    */   
/*    */   public static ChunkGenStat from(RecordedEvent event) {
/* 14 */     return new ChunkGenStat(event.getDuration(), new ChunkPos(event
/* 15 */           .getInt("chunkPosX"), event.getInt("chunkPosX")), new ColumnPos(event
/* 16 */           .getInt("worldPosX"), event.getInt("worldPosZ")), 
/* 17 */         ChunkStatus.byName(event.getString("status")), event
/* 18 */         .getString("level"));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\stats\ChunkGenStat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */