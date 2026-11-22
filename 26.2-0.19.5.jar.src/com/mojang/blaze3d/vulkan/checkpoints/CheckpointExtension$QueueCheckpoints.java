/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public final class QueueCheckpoints extends Record {
/*    */   private final long queue;
/*    */   
/*    */   private final List<CheckpointExtension.StageCheckpoint> checkpoints;
/*    */   
/*    */   public QueueCheckpoints(long queue, List<CheckpointExtension.StageCheckpoint> checkpoints) {
/* 29 */     this.queue = queue;
/* 29 */     this.checkpoints = checkpoints;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #29	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long queue() {
/* 29 */     return this.queue;
/*    */   }
/*    */   
/*    */   public List<CheckpointExtension.StageCheckpoint> checkpoints() {
/* 29 */     return this.checkpoints;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\CheckpointExtension$QueueCheckpoints.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */