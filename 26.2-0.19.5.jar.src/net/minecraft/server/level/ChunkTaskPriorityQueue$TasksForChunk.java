/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public final class TasksForChunk extends Record {
/*    */   private final long chunkPos;
/*    */   
/*    */   private final List<Runnable> tasks;
/*    */   
/*    */   public TasksForChunk(long chunkPos, List<Runnable> tasks) {
/* 88 */     this.chunkPos = chunkPos;
/* 88 */     this.tasks = tasks;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #88	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ChunkTaskPriorityQueue$TasksForChunk;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long chunkPos() {
/* 88 */     return this.chunkPos;
/*    */   }
/*    */   
/*    */   public List<Runnable> tasks() {
/* 88 */     return this.tasks;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkTaskPriorityQueue$TasksForChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */