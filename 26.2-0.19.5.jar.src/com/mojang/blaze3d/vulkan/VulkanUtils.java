/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuDeviceLossException;
/*     */ import com.mojang.blaze3d.systems.BackendCreationException;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.CheckpointExtension;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.joml.Vector4fc;
/*     */ import org.lwjgl.vulkan.VkClearColorValue;
/*     */ 
/*     */ public class VulkanUtils {
/*     */   public static void throwIfFailure(int result, String message, BackendCreationException.Reason reason) throws BackendCreationException {
/*  20 */     if (result < 0)
/*  21 */       throw new BackendCreationException(resultToString(result) + ": " + resultToString(result), reason); 
/*     */   }
/*     */   
/*     */   public static void crashIfFailure(VulkanDevice device, int result, String message) {
/*  26 */     if (result < 0) {
/*  27 */       String error = resultToString(result) + ": " + resultToString(result);
/*  28 */       if (result == -4) {
/*  29 */         List<CheckpointExtension.QueueCheckpoints> checkpoints = device.checkpointExtension().retrieveCheckpoints(true);
/*  30 */         throw new GpuDeviceLossException(error + "\n" + error);
/*     */       } 
/*  32 */       throw new IllegalStateException(error);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String formatCheckpoints(List<CheckpointExtension.QueueCheckpoints> queueCheckpoints) {
/*  38 */     StringBuilder result = new StringBuilder();
/*  40 */     for (CheckpointExtension.QueueCheckpoints queue : queueCheckpoints) {
/*  41 */       result.append("Queue 0x").append(Long.toHexString(queue.queue())).append('\n');
/*  42 */       for (CheckpointExtension.StageCheckpoint checkpoint : queue.checkpoints())
/*  43 */         result.append(' ').append(pipelineStageToString(checkpoint.stage())).append(" = ").append(checkpoint.type()).append(' ').append(checkpoint.label()).append('\n'); 
/*     */     } 
/*  47 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static String pipelineStageToString(long pipelineStage) {
/*  51 */     if (pipelineStage == 0L)
/*  52 */       return "NONE"; 
/*  54 */     if (pipelineStage == 1L)
/*  55 */       return "TOP_OF_PIPE"; 
/*  57 */     if (pipelineStage == 2L)
/*  58 */       return "DRAW_INDIRECT"; 
/*  60 */     if (pipelineStage == 4L)
/*  61 */       return "VERTEX_INPUT"; 
/*  63 */     if (pipelineStage == 8L)
/*  64 */       return "VERTEX_SHADER"; 
/*  66 */     if (pipelineStage == 16L)
/*  67 */       return "TESSELLATION_CONTROL_SHADER"; 
/*  69 */     if (pipelineStage == 32L)
/*  70 */       return "TESSELLATION_EVALUATION_SHADER"; 
/*  72 */     if (pipelineStage == 64L)
/*  73 */       return "GEOMETRY_SHADER"; 
/*  75 */     if (pipelineStage == 128L)
/*  76 */       return "FRAGMENT_SHADER"; 
/*  78 */     if (pipelineStage == 256L)
/*  79 */       return "EARLY_FRAGMENT_TESTS"; 
/*  81 */     if (pipelineStage == 512L)
/*  82 */       return "LATE_FRAGMENT_TESTS"; 
/*  84 */     if (pipelineStage == 1024L)
/*  85 */       return "COLOR_ATTACHMENT_OUTPUT"; 
/*  87 */     if (pipelineStage == 2048L)
/*  88 */       return "COMPUTE_SHADER"; 
/*  90 */     if (pipelineStage == 4096L)
/*  91 */       return "TRANSFER"; 
/*  93 */     if (pipelineStage == 8192L)
/*  94 */       return "BOTTOM_OF_PIPE"; 
/*  96 */     if (pipelineStage == 16384L)
/*  97 */       return "HOST"; 
/*  99 */     if (pipelineStage == 32768L)
/* 100 */       return "ALL_GRAPHICS"; 
/* 102 */     if (pipelineStage == 65536L)
/* 103 */       return "ALL_COMMANDS"; 
/* 105 */     if (pipelineStage == 131072L)
/* 106 */       return "COMMAND_PREPROCESS"; 
/* 108 */     if (pipelineStage == 262144L)
/* 109 */       return "CONDITIONAL_RENDERING"; 
/* 111 */     if (pipelineStage == 524288L)
/* 112 */       return "TASK_SHADER"; 
/* 114 */     if (pipelineStage == 1048576L)
/* 115 */       return "MESH_SHADER"; 
/* 117 */     if (pipelineStage == 2097152L)
/* 118 */       return "RAY_TRACING_SHADER"; 
/* 120 */     if (pipelineStage == 4194304L)
/* 121 */       return "FRAGMENT_SHADING_RATE_ATTACHMENT"; 
/* 123 */     if (pipelineStage == 8388608L)
/* 124 */       return "FRAGMENT_DENSITY_PROCESS"; 
/* 126 */     if (pipelineStage == 16777216L)
/* 127 */       return "TRANSFORM_FEEDBACK"; 
/* 129 */     if (pipelineStage == 33554432L)
/* 130 */       return "ACCELERATION_STRUCTURE_BUILD"; 
/* 132 */     if (pipelineStage == 4294967296L)
/* 133 */       return "COPY"; 
/* 135 */     if (pipelineStage == 8589934592L)
/* 136 */       return "RESOLVE"; 
/* 138 */     if (pipelineStage == 17179869184L)
/* 139 */       return "BLIT"; 
/* 141 */     if (pipelineStage == 34359738368L)
/* 142 */       return "CLEAR"; 
/* 144 */     if (pipelineStage == 68719476736L)
/* 145 */       return "INDEX_INPUT"; 
/* 147 */     if (pipelineStage == 137438953472L)
/* 148 */       return "VERTEX_ATTRIBUTE_INPUT"; 
/* 150 */     if (pipelineStage == 274877906944L)
/* 151 */       return "PRE_RASTERIZATION_SHADERS"; 
/* 153 */     return "0x" + Long.toHexString(pipelineStage);
/*     */   }
/*     */   
/*     */   public static String resultToString(int error) {
/* 157 */     switch (error) {
/*     */       case 0:
/*     */       
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */       case 5:
/*     */       
/*     */       case -1:
/*     */       
/*     */       case -2:
/*     */       
/*     */       case -3:
/*     */       
/*     */       case -4:
/*     */       
/*     */       case -5:
/*     */       
/*     */       case -6:
/*     */       
/*     */       case -7:
/*     */       
/*     */       case -8:
/*     */       
/*     */       case -9:
/*     */       
/*     */       case -10:
/*     */       
/*     */       case -11:
/*     */       
/*     */       case -12:
/*     */       
/*     */       case -13:
/*     */       
/*     */       case -1000069000:
/*     */       
/*     */       case -1000072003:
/*     */       
/*     */       case -1000257000:
/*     */       
/*     */       case -1000161000:
/*     */       
/*     */       case -1000000000:
/*     */       
/*     */       case -1000000001:
/*     */       
/*     */       case 1000001003:
/*     */       
/*     */       case -1000001004:
/*     */       
/*     */     } 
/* 191 */     return "0x" + Integer.toHexString(error);
/*     */   }
/*     */   
/*     */   public static boolean hasAllBits(int bitfield, int bitmask) {
/* 196 */     return ((bitfield & bitmask) == bitmask);
/*     */   }
/*     */   
/*     */   public static boolean hasAllBits(long bitfield, long bitmask) {
/* 200 */     return ((bitfield & bitmask) == bitmask);
/*     */   }
/*     */   
/*     */   public static boolean hasAnyBit(int bitfield, int bitmask) {
/* 204 */     return ((bitfield & bitmask) != 0);
/*     */   }
/*     */   
/*     */   public static boolean hasAnyBit(long bitfield, long bitmask) {
/* 208 */     return ((bitfield & bitmask) != 0L);
/*     */   }
/*     */   
/*     */   public static boolean hasNoBit(int bitfield, int bitmask) {
/* 212 */     return ((bitfield & bitmask) == 0);
/*     */   }
/*     */   
/*     */   public static boolean hasNoBit(long bitfield, long bitmask) {
/* 216 */     return ((bitfield & bitmask) == 0L);
/*     */   }
/*     */   
/*     */   public static VkClearColorValue putArgb(VkClearColorValue vkClearColor, Vector4fc argb) {
/* 220 */     vkClearColor.float32(0, argb.x());
/* 221 */     vkClearColor.float32(1, argb.y());
/* 222 */     vkClearColor.float32(2, argb.z());
/* 223 */     vkClearColor.float32(3, argb.w());
/* 224 */     return vkClearColor;
/*     */   }
/*     */   
/*     */   public static final class DeviceUUID extends Record {
/*     */     private final int driverID;
/*     */     
/*     */     private final int vendorID;
/*     */     
/*     */     private final int deviceID;
/*     */     
/*     */     public DeviceUUID(int driverID, int vendorID, int deviceID) {
/* 227 */       this.driverID = driverID;
/* 227 */       this.vendorID = vendorID;
/* 227 */       this.deviceID = deviceID;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #227	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #227	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #227	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int driverID() {
/* 227 */       return this.driverID;
/*     */     }
/*     */     
/*     */     public int vendorID() {
/* 227 */       return this.vendorID;
/*     */     }
/*     */     
/*     */     public int deviceID() {
/* 227 */       return this.deviceID;
/*     */     }
/*     */   }
/*     */   
/* 230 */   public static final Set<DeviceUUID> KNOWN_PROBLEMATIC_DEVICES = Set.of(new DeviceUUID[] { 
/* 230 */         new DeviceUUID(14, 32902, 338), new DeviceUUID(14, 32902, 341), new DeviceUUID(14, 32902, 343), new DeviceUUID(14, 32902, 346), new DeviceUUID(14, 32902, 354), new DeviceUUID(14, 32902, 358), new DeviceUUID(14, 32902, 362), new DeviceUUID(14, 32902, 1026), new DeviceUUID(14, 32902, 1030), new DeviceUUID(14, 32902, 1034), 
/* 230 */         new DeviceUUID(14, 32902, 1035), new DeviceUUID(14, 32902, 1038), new DeviceUUID(14, 32902, 1042), new DeviceUUID(14, 32902, 1046), new DeviceUUID(14, 32902, 1050), new DeviceUUID(14, 32902, 1051), new DeviceUUID(14, 32902, 1054), new DeviceUUID(14, 32902, 1058), new DeviceUUID(14, 32902, 1062), new DeviceUUID(14, 32902, 1066), 
/* 230 */         new DeviceUUID(14, 32902, 1067), new DeviceUUID(14, 32902, 1070), new DeviceUUID(14, 32902, 2562), new DeviceUUID(14, 32902, 2566), new DeviceUUID(14, 32902, 2570), new DeviceUUID(14, 32902, 2571), new DeviceUUID(14, 32902, 2574), new DeviceUUID(14, 32902, 2578), new DeviceUUID(14, 32902, 2582), new DeviceUUID(14, 32902, 2586), 
/* 230 */         new DeviceUUID(14, 32902, 2587), new DeviceUUID(14, 32902, 2590), new DeviceUUID(14, 32902, 2594), new DeviceUUID(14, 32902, 2598), new DeviceUUID(14, 32902, 2602), new DeviceUUID(14, 32902, 2603), new DeviceUUID(14, 32902, 2606), new DeviceUUID(14, 32902, 3362), new DeviceUUID(14, 32902, 3366), new DeviceUUID(14, 32902, 3370), 
/* 230 */         new DeviceUUID(14, 32902, 3371), new DeviceUUID(14, 32902, 3374), new DeviceUUID(14, 32902, 3888), new DeviceUUID(14, 32902, 3889), new DeviceUUID(14, 32902, 3890), new DeviceUUID(14, 32902, 3891), new DeviceUUID(14, 32902, 5638), new DeviceUUID(14, 32902, 5650), new DeviceUUID(14, 32902, 5654), new DeviceUUID(14, 32902, 5662), 
/* 230 */         new DeviceUUID(14, 32902, 5666), new DeviceUUID(14, 32902, 5670), new DeviceUUID(14, 32902, 5674), new DeviceUUID(14, 32902, 5675), new DeviceUUID(14, 32902, 8880), new DeviceUUID(14, 32902, 8881), new DeviceUUID(14, 32902, 8882), new DeviceUUID(14, 32902, 8883) });
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanUtils.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */