/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import com.mojang.blaze3d.vulkan.VulkanDevice;
/*    */ import com.mojang.blaze3d.vulkan.VulkanQueue;
/*    */ import java.util.List;
/*    */ import java.util.function.Supplier;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ 
/*    */ public class NoopCheckpointExtension implements CheckpointExtension {
/* 11 */   public static final NoopCheckpointExtension INSTANCE = new NoopCheckpointExtension();
/*    */   
/*    */   public CheckpointExtension.CheckpointStorage createStorage(VulkanDevice device, VulkanQueue queue, int maxFramesInFlight) {
/* 15 */     return NoopCheckpointStorage.INSTANCE;
/*    */   }
/*    */   
/*    */   public List<CheckpointExtension.QueueCheckpoints> retrieveCheckpoints(boolean isDeviceLost) {
/* 20 */     return List.of();
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */   
/*    */   private static class NoopCheckpointStorage implements CheckpointExtension.CheckpointStorage {
/* 28 */     private static final NoopCheckpointStorage INSTANCE = new NoopCheckpointStorage();
/*    */     
/*    */     public void rotate() {}
/*    */     
/*    */     public void recordCheckpoint(VkCommandBuffer commandBuffer, CheckpointExtension.CheckpointType type, Supplier<String> label) {}
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\NoopCheckpointExtension.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */