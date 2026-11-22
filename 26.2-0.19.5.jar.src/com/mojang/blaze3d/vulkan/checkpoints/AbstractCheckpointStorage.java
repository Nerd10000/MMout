/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import com.mojang.blaze3d.vulkan.VulkanQueue;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ import org.lwjgl.vulkan.VkQueue;
/*    */ 
/*    */ abstract class AbstractCheckpointStorage implements CheckpointExtension.CheckpointStorage {
/*    */   protected final VkQueue queue;
/*    */   
/*    */   private final int maxFramesInFlight;
/*    */   
/*    */   private int frame;
/*    */   
/*    */   private final Frame[] checkpointsByFrame;
/*    */   
/*    */   private int nextCheckpointId;
/*    */   
/*    */   protected AbstractCheckpointStorage(VulkanQueue queue, int maxFramesInFlight) {
/* 22 */     this.queue = queue.vkQueue();
/* 23 */     this.maxFramesInFlight = maxFramesInFlight;
/* 24 */     this.checkpointsByFrame = new Frame[maxFramesInFlight];
/* 25 */     for (int i = 0; i < maxFramesInFlight; i++)
/* 26 */       this.checkpointsByFrame[i] = new Frame(new ArrayList<>()); 
/*    */   }
/*    */   
/*    */   public void rotate() {
/* 32 */     this.frame = (this.frame + 1) % this.maxFramesInFlight;
/* 33 */     (this.checkpointsByFrame[this.frame]).checkpoints.clear();
/*    */   }
/*    */   
/*    */   public void recordCheckpoint(VkCommandBuffer commandBuffer, CheckpointExtension.CheckpointType type, Supplier<String> label) {
/* 38 */     int id = this.nextCheckpointId++;
/* 39 */     (this.checkpointsByFrame[this.frame]).checkpoints.add(new Checkpoint(id, label.get(), type));
/* 40 */     recordCheckpoint(commandBuffer, id);
/*    */   }
/*    */   
/*    */   protected abstract void recordCheckpoint(VkCommandBuffer paramVkCommandBuffer, int paramInt);
/*    */   
/*    */   protected Checkpoint findCheckpoint(int id) {
/* 46 */     for (Frame frame : this.checkpointsByFrame) {
/* 47 */       for (Checkpoint checkpoint : frame.checkpoints) {
/* 48 */         if (checkpoint.id() == id)
/* 49 */           return checkpoint; 
/*    */       } 
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */   
/*    */   protected static final class Checkpoint extends Record {
/*    */     private final int id;
/*    */     
/*    */     private final String label;
/*    */     
/*    */     private final CheckpointExtension.CheckpointType type;
/*    */     
/*    */     protected Checkpoint(int id, String label, CheckpointExtension.CheckpointType type) {
/* 56 */       this.id = id;
/* 56 */       this.label = label;
/* 56 */       this.type = type;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #56	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Checkpoint;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public int id() {
/* 56 */       return this.id;
/*    */     }
/*    */     
/*    */     public String label() {
/* 56 */       return this.label;
/*    */     }
/*    */     
/*    */     public CheckpointExtension.CheckpointType type() {
/* 56 */       return this.type;
/*    */     }
/*    */   }
/*    */   
/*    */   private static final class Frame extends Record {
/*    */     private final List<AbstractCheckpointStorage.Checkpoint> checkpoints;
/*    */     
/*    */     private Frame(List<AbstractCheckpointStorage.Checkpoint> checkpoints) {
/* 58 */       this.checkpoints = checkpoints;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Frame;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Frame;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Frame;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Frame;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Frame;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/AbstractCheckpointStorage$Frame;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public List<AbstractCheckpointStorage.Checkpoint> checkpoints() {
/* 58 */       return this.checkpoints;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\AbstractCheckpointStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */