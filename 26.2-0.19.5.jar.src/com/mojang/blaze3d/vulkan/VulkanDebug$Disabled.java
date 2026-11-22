/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkDevice;
/*     */ import org.lwjgl.vulkan.VkInstance;
/*     */ import org.lwjgl.vulkan.VkInstanceCreateInfo;
/*     */ 
/*     */ public class Disabled implements VulkanDebug {
/*     */   public void chainCreateInfo(VkInstanceCreateInfo instanceCreateInfo, MemoryStack stack) {}
/*     */   
/*     */   public void setup(VkInstance vkInstance) {}
/*     */   
/*     */   public void setObjectName(VkDevice device, int objectType, long objectHandle, String label) {}
/*     */   
/*     */   public void setObjectName(VkDevice device, int objectType, long objectHandle, Supplier<String> label) {}
/*     */   
/*     */   public void beginDebugGroup(VkCommandBuffer buffer, Supplier<String> label) {}
/*     */   
/*     */   public void endDebugGroup(VkCommandBuffer buffer) {}
/*     */   
/*     */   public void destroy(VkInstance instance) {}
/*     */   
/*     */   public boolean enabled() {
/* 230 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanDebug$Disabled.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */