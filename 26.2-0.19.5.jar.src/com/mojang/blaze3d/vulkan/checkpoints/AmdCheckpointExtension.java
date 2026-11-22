/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import com.mojang.blaze3d.vulkan.VulkanDevice;
/*    */ import com.mojang.blaze3d.vulkan.VulkanGpuBuffer;
/*    */ import com.mojang.blaze3d.vulkan.VulkanQueue;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.lwjgl.vulkan.AMDBufferMarker;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ 
/*    */ public class AmdCheckpointExtension implements CheckpointExtension {
/* 16 */   private static final long[] STAGES = new long[] { 1L, 8192L };
/*    */   
/* 21 */   private final List<AmdCheckpointStorage> storages = new ArrayList<>();
/*    */   
/*    */   public CheckpointExtension.CheckpointStorage createStorage(VulkanDevice device, VulkanQueue queue, int maxFramesInFlight) {
/* 25 */     AmdCheckpointStorage storage = new AmdCheckpointStorage(device, queue, maxFramesInFlight);
/* 26 */     this.storages.add(storage);
/* 27 */     return storage;
/*    */   }
/*    */   
/*    */   public List<CheckpointExtension.QueueCheckpoints> retrieveCheckpoints(boolean isDeviceLost) {
/* 32 */     List<CheckpointExtension.QueueCheckpoints> result = new ArrayList<>(this.storages.size());
/* 33 */     for (AmdCheckpointStorage storage : this.storages)
/* 34 */       result.add(storage.retrieveCheckpoints()); 
/* 36 */     return result;
/*    */   }
/*    */   
/*    */   public void close() {
/* 41 */     for (AmdCheckpointStorage storage : this.storages)
/* 42 */       storage.close(); 
/*    */   }
/*    */   
/*    */   private static class AmdCheckpointStorage extends AbstractCheckpointStorage implements AutoCloseable {
/*    */     private final VulkanGpuBuffer buffer;
/*    */     
/*    */     private final GpuBufferSlice.MappedView mappedView;
/*    */     
/*    */     protected AmdCheckpointStorage(VulkanDevice device, VulkanQueue queue, int maxFramesInFlight) {
/* 51 */       super(queue, maxFramesInFlight);
/* 52 */       this.buffer = device.createBuffer(() -> "Internal marker storage", 9, (AmdCheckpointExtension.STAGES.length * 4));
/* 53 */       this.mappedView = this.buffer.map(true, false);
/*    */     }
/*    */     
/*    */     protected void recordCheckpoint(VkCommandBuffer commandBuffer, int id) {
/* 58 */       for (int i = 0; i < AmdCheckpointExtension.STAGES.length; i++)
/* 59 */         AMDBufferMarker.vkCmdWriteBufferMarker2AMD(commandBuffer, AmdCheckpointExtension.STAGES[i], this.buffer.vkBuffer(), (i * 4), id); 
/*    */     }
/*    */     
/*    */     public CheckpointExtension.QueueCheckpoints retrieveCheckpoints() {
/* 64 */       List<CheckpointExtension.StageCheckpoint> stageCheckpoints = new ArrayList<>();
/* 66 */       for (int i = 0; i < AmdCheckpointExtension.STAGES.length; i++) {
/* 67 */         AbstractCheckpointStorage.Checkpoint checkpoint = findCheckpoint(this.mappedView.data().getInt(i * 4));
/* 68 */         if (checkpoint != null)
/* 69 */           stageCheckpoints.add(new CheckpointExtension.StageCheckpoint(AmdCheckpointExtension.STAGES[i], checkpoint.type(), checkpoint.label())); 
/*    */       } 
/* 73 */       return new CheckpointExtension.QueueCheckpoints(this.queue.address(), stageCheckpoints);
/*    */     }
/*    */     
/*    */     public void close() {
/* 78 */       this.mappedView.close();
/* 79 */       this.buffer.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\AmdCheckpointExtension.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */