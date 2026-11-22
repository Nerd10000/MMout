/*    */ package com.mojang.blaze3d.vulkan.checkpoints;
/*    */ 
/*    */ import com.mojang.blaze3d.vulkan.VulkanQueue;
/*    */ import java.nio.IntBuffer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.vulkan.NVDeviceDiagnosticCheckpoints;
/*    */ import org.lwjgl.vulkan.VkCheckpointData2NV;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ 
/*    */ class NvidiaCheckpointStorage extends AbstractCheckpointStorage {
/*    */   protected NvidiaCheckpointStorage(VulkanQueue queue, int maxFramesInFlight) {
/* 42 */     super(queue, maxFramesInFlight);
/*    */   }
/*    */   
/*    */   protected void recordCheckpoint(VkCommandBuffer commandBuffer, int id) {
/* 47 */     NVDeviceDiagnosticCheckpoints.vkCmdSetCheckpointNV(commandBuffer, id);
/*    */   }
/*    */   
/*    */   public CheckpointExtension.QueueCheckpoints retrieveCheckpoints() {
/* 51 */     List<CheckpointExtension.StageCheckpoint> stageCheckpoints = new ArrayList<>();
/* 53 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 54 */       IntBuffer count = stack.callocInt(1);
/* 55 */       NVDeviceDiagnosticCheckpoints.vkGetQueueCheckpointData2NV(this.queue, count, null);
/* 56 */       VkCheckpointData2NV.Buffer data = VkCheckpointData2NV.calloc(count.get(0), stack);
/* 57 */       for (int i = 0; i < count.get(0); i++)
/* 58 */         ((VkCheckpointData2NV)data.get(i)).sType$Default(); 
/* 60 */       NVDeviceDiagnosticCheckpoints.vkGetQueueCheckpointData2NV(this.queue, count, data);
/* 61 */       while (data.remaining() > 0) {
/* 62 */         VkCheckpointData2NV checkpointData = (VkCheckpointData2NV)data.get();
/* 63 */         AbstractCheckpointStorage.Checkpoint checkpoint = findCheckpoint((int)checkpointData.pCheckpointMarker());
/* 64 */         if (checkpoint != null)
/* 65 */           stageCheckpoints.add(new CheckpointExtension.StageCheckpoint(checkpointData.stage(), checkpoint.type(), checkpoint.label())); 
/*    */       } 
/* 68 */       if (stack != null)
/* 68 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 70 */     return new CheckpointExtension.QueueCheckpoints(this.queue.address(), stageCheckpoints);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\checkpoints\NvidiaCheckpointExtension$NvidiaCheckpointStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */