/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ public final class StageCheckpoint extends Record {
/*    */   private final long stage;
/*    */   
/*    */   private final CheckpointExtension.CheckpointType type;
/*    */   
/*    */   private final String label;
/*    */   
/*    */   public StageCheckpoint(long stage, CheckpointExtension.CheckpointType type, String label) {
/* 31 */     this.stage = stage;
/* 31 */     this.type = type;
/* 31 */     this.label = label;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #31	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long stage() {
/* 31 */     return this.stage;
/*    */   }
/*    */   
/*    */   public CheckpointExtension.CheckpointType type() {
/* 31 */     return this.type;
/*    */   }
/*    */   
/*    */   public String label() {
/* 31 */     return this.label;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\CheckpointExtension$StageCheckpoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */