/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ 
/*    */ class NoopCheckpointStorage implements CheckpointExtension.CheckpointStorage {
/* 28 */   private static final NoopCheckpointStorage INSTANCE = new NoopCheckpointStorage();
/*    */   
/*    */   public void rotate() {}
/*    */   
/*    */   public void recordCheckpoint(VkCommandBuffer commandBuffer, CheckpointExtension.CheckpointType type, Supplier<String> label) {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\NoopCheckpointExtension$NoopCheckpointStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */