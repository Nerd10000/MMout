/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ public final class Checkpoint extends Record {
/*    */   private final int id;
/*    */   
/*    */   private final String label;
/*    */   
/*    */   private final CheckpointExtension.CheckpointType type;
/*    */   
/*    */   protected Checkpoint(int id, String label, CheckpointExtension.CheckpointType type) {
/* 56 */     this.id = id;
/* 56 */     this.label = label;
/* 56 */     this.type = type;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int id() {
/* 56 */     return this.id;
/*    */   }
/*    */   
/*    */   public String label() {
/* 56 */     return this.label;
/*    */   }
/*    */   
/*    */   public CheckpointExtension.CheckpointType type() {
/* 56 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\AbstractCheckpointStorage$Checkpoint.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */