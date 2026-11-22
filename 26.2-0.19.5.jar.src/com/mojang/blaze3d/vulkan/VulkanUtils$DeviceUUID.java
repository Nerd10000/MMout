/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ public final class DeviceUUID extends Record {
/*     */   private final int driverID;
/*     */   
/*     */   private final int vendorID;
/*     */   
/*     */   private final int deviceID;
/*     */   
/*     */   public DeviceUUID(int driverID, int vendorID, int deviceID) {
/* 227 */     this.driverID = driverID;
/* 227 */     this.vendorID = vendorID;
/* 227 */     this.deviceID = deviceID;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #227	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #227	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #227	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanUtils$DeviceUUID;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int driverID() {
/* 227 */     return this.driverID;
/*     */   }
/*     */   
/*     */   public int vendorID() {
/* 227 */     return this.vendorID;
/*     */   }
/*     */   
/*     */   public int deviceID() {
/* 227 */     return this.deviceID;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanUtils$DeviceUUID.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */