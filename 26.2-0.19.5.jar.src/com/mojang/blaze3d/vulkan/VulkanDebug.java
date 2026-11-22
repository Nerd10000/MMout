/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.vulkan.EXTDebugUtils;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkDebugUtilsLabelEXT;
/*     */ import org.lwjgl.vulkan.VkDebugUtilsMessengerCallbackDataEXT;
/*     */ import org.lwjgl.vulkan.VkDebugUtilsMessengerCreateInfoEXT;
/*     */ import org.lwjgl.vulkan.VkDebugUtilsObjectNameInfoEXT;
/*     */ import org.lwjgl.vulkan.VkDevice;
/*     */ import org.lwjgl.vulkan.VkInstance;
/*     */ import org.lwjgl.vulkan.VkInstanceCreateInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface VulkanDebug {
/*     */   static VulkanDebug create(int verbosity, boolean wantsDebugLabels, Set<String> availableExtensions, Set<String> enabledExtensions) {
/*  26 */     if ((verbosity > 0 || wantsDebugLabels) && availableExtensions.contains("VK_EXT_debug_utils")) {
/*  27 */       enabledExtensions.add("VK_EXT_debug_utils");
/*  28 */       return new Enabled(verbosity, wantsDebugLabels);
/*     */     } 
/*  30 */     return new Disabled();
/*     */   }
/*     */   
/*     */   void chainCreateInfo(VkInstanceCreateInfo paramVkInstanceCreateInfo, MemoryStack paramMemoryStack);
/*     */   
/*     */   void setup(VkInstance paramVkInstance);
/*     */   
/*     */   void setObjectName(VkDevice paramVkDevice, int paramInt, long paramLong, String paramString);
/*     */   
/*     */   void setObjectName(VkDevice paramVkDevice, int paramInt, long paramLong, Supplier<String> paramSupplier);
/*     */   
/*     */   void beginDebugGroup(VkCommandBuffer paramVkCommandBuffer, Supplier<String> paramSupplier);
/*     */   
/*     */   void endDebugGroup(VkCommandBuffer paramVkCommandBuffer);
/*     */   
/*     */   void destroy(VkInstance paramVkInstance);
/*     */   
/*     */   boolean enabled();
/*     */   
/*     */   public static class Enabled implements VulkanDebug {
/*  50 */     private static final StackWalker STACK_WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 3);
/*     */     
/*  51 */     private static final Logger LOGGER = LogUtils.getLogger();
/*     */     
/*     */     public static final int MESSAGE_TYPE_BITMASK = 7;
/*     */     
/*  53 */     private static final int[] DEBUG_LEVELS = new int[] { 4096, 256, 16, 1 };
/*     */     
/*     */     private final boolean wantsDebugLabels;
/*     */     
/*     */     private long messenger;
/*     */     
/*     */     private final int severityBitmask;
/*     */     
/*     */     public Enabled(int verbosity, boolean wantsDebugLabels) {
/*  64 */       this.wantsDebugLabels = wantsDebugLabels;
/*  65 */       int severityBitmask = 0;
/*  68 */       if (verbosity > 0)
/*  69 */         for (int i = 0; i < Math.min(verbosity, DEBUG_LEVELS.length); i++)
/*  70 */           severityBitmask |= DEBUG_LEVELS[i];  
/*  74 */       this.severityBitmask = severityBitmask;
/*     */     }
/*     */     
/*     */     public void chainCreateInfo(VkInstanceCreateInfo instanceCreateInfo, MemoryStack stack) {
/*  79 */       if (this.severityBitmask > 0)
/*  80 */         instanceCreateInfo.pNext(
/*  81 */             VkDebugUtilsMessengerCreateInfoEXT.calloc(stack)
/*  82 */             .sType$Default()
/*  83 */             .messageSeverity(this.severityBitmask)
/*  84 */             .messageType(7)
/*  85 */             .pfnUserCallback(this::onDebugMessage)); 
/*     */     }
/*     */     
/*     */     public void setup(VkInstance vkInstance) {
/*  91 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/*  92 */         LongBuffer pointer = stack.callocLong(1);
/*  98 */         VkDebugUtilsMessengerCreateInfoEXT createInfo = VkDebugUtilsMessengerCreateInfoEXT.calloc(stack).sType$Default().messageSeverity(this.severityBitmask).messageType(7).pfnUserCallback(this::onDebugMessage);
/*  99 */         int result = EXTDebugUtils.vkCreateDebugUtilsMessengerEXT(vkInstance, createInfo, null, pointer);
/* 100 */         if (result != 0) {
/* 101 */           LOGGER.error("Error creating debug utils messenger: {}", VulkanUtils.resultToString(result));
/* 105 */           if (stack != null)
/* 105 */             stack.close(); 
/*     */           return;
/*     */         } 
/*     */         this.messenger = pointer.get(0);
/* 105 */         if (stack != null)
/* 105 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setObjectName(VkDevice device, int objectType, long objectHandle, String label) {
/* 110 */       if (!this.wantsDebugLabels)
/*     */         return; 
/* 113 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 114 */         ByteBuffer name = stack.UTF8(label);
/* 120 */         VkDebugUtilsObjectNameInfoEXT nameInfo = VkDebugUtilsObjectNameInfoEXT.calloc(stack).sType$Default().pObjectName(name).objectType(objectType).objectHandle(objectHandle);
/* 121 */         EXTDebugUtils.vkSetDebugUtilsObjectNameEXT(device, nameInfo);
/* 122 */         if (stack != null)
/* 122 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void setObjectName(VkDevice device, int objectType, long objectHandle, Supplier<String> label) {
/* 127 */       if (!this.wantsDebugLabels)
/*     */         return; 
/* 130 */       setObjectName(device, objectType, objectHandle, label.get());
/*     */     }
/*     */     
/*     */     private int onDebugMessage(int messageSeverity, int messageTypes, long pCallbackData, long pUserData) {
/* 134 */       VkDebugUtilsMessengerCallbackDataEXT callbackData = VkDebugUtilsMessengerCallbackDataEXT.create(pCallbackData);
/* 135 */       String message = callbackData.pMessageString();
/* 136 */       if ((messageSeverity & 0x10) != 0) {
/* 137 */         LOGGER.info("{}", message);
/* 138 */       } else if ((messageSeverity & 0x100) != 0) {
/* 139 */         LOGGER.warn("{}", message);
/*     */       } else {
/* 140 */         if ((messageSeverity & 0x1000) != 0) {
/* 141 */           if (message != null && (message.contains("vkDestroyInstance") || message.contains("vkDestroyDevice"))) {
/* 143 */             LOGGER.error("{}", message);
/*     */           } else {
/* 146 */             String callStack = STACK_WALKER.<String>walk(s -> (String)s.filter(()).limit(5L).map(()).collect(Collectors.joining("\n")));
/* 152 */             LOGGER.error("{}\n{}", message, callStack);
/*     */           } 
/* 154 */           return 1;
/*     */         } 
/* 156 */         LOGGER.debug("{}", message);
/*     */       } 
/* 158 */       return 0;
/*     */     }
/*     */     
/*     */     public void beginDebugGroup(VkCommandBuffer buffer, Supplier<String> label) {
/* 163 */       if (!this.wantsDebugLabels)
/*     */         return; 
/* 166 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 167 */         ByteBuffer name = stack.UTF8(label.get());
/* 171 */         VkDebugUtilsLabelEXT nameInfo = VkDebugUtilsLabelEXT.calloc(stack).sType$Default().pLabelName(name);
/* 172 */         EXTDebugUtils.vkCmdBeginDebugUtilsLabelEXT(buffer, nameInfo);
/* 173 */         if (stack != null)
/* 173 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void endDebugGroup(VkCommandBuffer buffer) {
/* 178 */       if (!this.wantsDebugLabels)
/*     */         return; 
/* 181 */       EXTDebugUtils.vkCmdEndDebugUtilsLabelEXT(buffer);
/*     */     }
/*     */     
/*     */     public void destroy(VkInstance instance) {
/* 186 */       if (this.messenger != 0L)
/* 187 */         EXTDebugUtils.vkDestroyDebugUtilsMessengerEXT(instance, this.messenger, null); 
/*     */     }
/*     */     
/*     */     public boolean enabled() {
/* 193 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Disabled implements VulkanDebug {
/*     */     public void chainCreateInfo(VkInstanceCreateInfo instanceCreateInfo, MemoryStack stack) {}
/*     */     
/*     */     public void setup(VkInstance vkInstance) {}
/*     */     
/*     */     public void setObjectName(VkDevice device, int objectType, long objectHandle, String label) {}
/*     */     
/*     */     public void setObjectName(VkDevice device, int objectType, long objectHandle, Supplier<String> label) {}
/*     */     
/*     */     public void beginDebugGroup(VkCommandBuffer buffer, Supplier<String> label) {}
/*     */     
/*     */     public void endDebugGroup(VkCommandBuffer buffer) {}
/*     */     
/*     */     public void destroy(VkInstance instance) {}
/*     */     
/*     */     public boolean enabled() {
/* 230 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanDebug.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */