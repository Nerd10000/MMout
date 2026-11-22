/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import com.mojang.blaze3d.vulkan.VulkanDevice;
/*    */ import com.mojang.blaze3d.vulkan.VulkanQueue;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ 
/*    */ public interface CheckpointExtension extends AutoCloseable {
/*    */   CheckpointStorage createStorage(VulkanDevice paramVulkanDevice, VulkanQueue paramVulkanQueue, int paramInt);
/*    */   
/*    */   List<QueueCheckpoints> retrieveCheckpoints(boolean paramBoolean);
/*    */   
/*    */   void close();
/*    */   
/*    */   public static interface CheckpointStorage {
/*    */     void rotate();
/*    */     
/*    */     void recordCheckpoint(VkCommandBuffer param1VkCommandBuffer, CheckpointExtension.CheckpointType param1CheckpointType, Supplier<String> param1Supplier);
/*    */   }
/*    */   
/*    */   public enum CheckpointType {
/* 25 */     BEGIN_RENDER_PASS, END_RENDER_PASS;
/*    */   }
/*    */   
/*    */   public static final class QueueCheckpoints extends Record {
/*    */     private final long queue;
/*    */     
/*    */     private final List<CheckpointExtension.StageCheckpoint> checkpoints;
/*    */     
/*    */     public QueueCheckpoints(long queue, List<CheckpointExtension.StageCheckpoint> checkpoints) {
/* 29 */       this.queue = queue;
/* 29 */       this.checkpoints = checkpoints;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #29	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$QueueCheckpoints;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public long queue() {
/* 29 */       return this.queue;
/*    */     }
/*    */     
/*    */     public List<CheckpointExtension.StageCheckpoint> checkpoints() {
/* 29 */       return this.checkpoints;
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class StageCheckpoint extends Record {
/*    */     private final long stage;
/*    */     
/*    */     private final CheckpointExtension.CheckpointType type;
/*    */     
/*    */     private final String label;
/*    */     
/*    */     public StageCheckpoint(long stage, CheckpointExtension.CheckpointType type, String label) {
/* 31 */       this.stage = stage;
/* 31 */       this.type = type;
/* 31 */       this.label = label;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #31	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/checkpoints/CheckpointExtension$StageCheckpoint;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public long stage() {
/* 31 */       return this.stage;
/*    */     }
/*    */     
/*    */     public CheckpointExtension.CheckpointType type() {
/* 31 */       return this.type;
/*    */     }
/*    */     
/*    */     public String label() {
/* 31 */       return this.label;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\CheckpointExtension.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */