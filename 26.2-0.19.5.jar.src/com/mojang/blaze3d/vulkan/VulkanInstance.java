/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.BackendCreationException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Util;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.glfw.GLFWVulkan;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkApplicationInfo;
/*     */ import org.lwjgl.vulkan.VkExtensionProperties;
/*     */ import org.lwjgl.vulkan.VkInstance;
/*     */ import org.lwjgl.vulkan.VkInstanceCreateInfo;
/*     */ import org.lwjgl.vulkan.VkLayerProperties;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VulkanInstance implements AutoCloseable {
/*  27 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String APPLICATION_NAME = "Minecraft Java Edition";
/*     */   
/*  30 */   private static final int APPLICATION_VERSION = SharedConstants.getCurrentVersion().dataVersion().version();
/*     */   
/*     */   private static final String ENGINE_NAME = "MinecraftJE";
/*     */   
/*     */   private static final int ENGINE_VERSION = 0;
/*     */   
/*  35 */   private final Set<String> enabledExtensions = new HashSet<>();
/*     */   
/*     */   private final VkInstance vkInstance;
/*     */   
/*     */   private final VulkanDebug debug;
/*     */   
/*     */   protected VulkanInstance(int debugVerbosity, boolean wantsDebugLabels, boolean validation) throws BackendCreationException {
/*  40 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  47 */       VkApplicationInfo appInfo = VkApplicationInfo.calloc(stack).sType$Default().pApplicationName(stack.UTF8("Minecraft Java Edition")).applicationVersion(APPLICATION_VERSION).pEngineName(stack.UTF8("MinecraftJE")).engineVersion(0).apiVersion(VK12.VK_API_VERSION_1_2);
/*  49 */       List<String> validationLayers = getSupportedValidationLayers();
/*  50 */       PointerBuffer requiredLayers = null;
/*  51 */       if (validation)
/*  52 */         if (validationLayers.contains("VK_LAYER_KHRONOS_validation")) {
/*  53 */           requiredLayers = stack.callocPointer(1);
/*  54 */           requiredLayers.put(0, stack.ASCII("VK_LAYER_KHRONOS_validation"));
/*  55 */           LOGGER.warn("Enabling Vulkan validation layers");
/*  56 */           wantsDebugLabels = true;
/*     */         } else {
/*  58 */           LOGGER.warn("Vulkan validation layers requested but not found");
/*     */         }  
/*  61 */       Set<String> availableExtensions = getSupportedInstanceExtensions();
/*  62 */       PointerBuffer glfwExtensions = GLFWVulkan.glfwGetRequiredInstanceExtensions();
/*  63 */       if (glfwExtensions == null)
/*  64 */         throw new BackendCreationException("Failed to find the GLFW platform surface extensions", BackendCreationException.Reason.GLFW_ERROR); 
/*  66 */       while (glfwExtensions.remaining() > 0)
/*  67 */         this.enabledExtensions.add(MemoryUtil.memUTF8(glfwExtensions.get())); 
/*  70 */       this.debug = VulkanDebug.create(debugVerbosity, wantsDebugLabels, availableExtensions, this.enabledExtensions);
/*  73 */       boolean usePortability = (availableExtensions.contains("VK_KHR_portability_enumeration") && Util.getPlatform() == Util.OS.OSX);
/*  74 */       if (usePortability)
/*  75 */         this.enabledExtensions.add("VK_KHR_portability_enumeration"); 
/*  78 */       PointerBuffer enabledExtensionsBuffer = stack.callocPointer(this.enabledExtensions.size());
/*  79 */       for (String name : this.enabledExtensions)
/*  80 */         enabledExtensionsBuffer.put(stack.UTF8(name)); 
/*  82 */       enabledExtensionsBuffer.flip();
/*  88 */       VkInstanceCreateInfo instanceInfo = VkInstanceCreateInfo.calloc(stack).sType$Default().pApplicationInfo(appInfo).ppEnabledLayerNames(requiredLayers).ppEnabledExtensionNames(enabledExtensionsBuffer);
/*  89 */       if (usePortability)
/*  90 */         instanceInfo.flags(1); 
/*  93 */       this.debug.chainCreateInfo(instanceInfo, stack);
/*  95 */       PointerBuffer pInstance = stack.callocPointer(1);
/*  96 */       VulkanUtils.throwIfFailure(VK12.vkCreateInstance(instanceInfo, null, pInstance), "Error creating instance", BackendCreationException.Reason.VULKAN_INSTANCE_CREATION_FAILED);
/*  97 */       this.vkInstance = new VkInstance(pInstance.get(0), instanceInfo);
/*  98 */       this.debug.setup(this.vkInstance);
/*  99 */       if (stack != null)
/*  99 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public VkInstance vkInstance() {
/* 103 */     return this.vkInstance;
/*     */   }
/*     */   
/*     */   private Set<String> getSupportedInstanceExtensions() throws BackendCreationException {
/* 107 */     Set<String> instanceExtensions = new HashSet<>();
/* 108 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 109 */       IntBuffer numExtensionsBuf = stack.callocInt(1);
/* 110 */       VulkanUtils.throwIfFailure(VK12.vkEnumerateInstanceExtensionProperties((String)null, numExtensionsBuf, null), "Error enumerating instance extensions", BackendCreationException.Reason.VULKAN_INSTANCE_CREATION_FAILED);
/* 112 */       int numExtensions = numExtensionsBuf.get(0);
/* 113 */       VkExtensionProperties.Buffer instanceExtensionsProps = VkExtensionProperties.calloc(numExtensions, stack);
/* 114 */       VulkanUtils.throwIfFailure(VK12.vkEnumerateInstanceExtensionProperties((String)null, numExtensionsBuf, instanceExtensionsProps), "Error enumerating instance extensions", BackendCreationException.Reason.VULKAN_INSTANCE_CREATION_FAILED);
/* 116 */       for (int i = 0; i < numExtensions; i++) {
/* 117 */         VkExtensionProperties props = (VkExtensionProperties)instanceExtensionsProps.get(i);
/* 118 */         String extensionName = props.extensionNameString();
/* 119 */         instanceExtensions.add(extensionName);
/*     */       } 
/* 121 */       if (stack != null)
/* 121 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 122 */     return instanceExtensions;
/*     */   }
/*     */   
/*     */   private List<String> getSupportedValidationLayers() throws BackendCreationException {
/* 126 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 127 */       IntBuffer numLayersArr = stack.callocInt(1);
/* 128 */       VulkanUtils.throwIfFailure(VK12.vkEnumerateInstanceLayerProperties(numLayersArr, null), "Error enumerating validation layers", BackendCreationException.Reason.VULKAN_INSTANCE_CREATION_FAILED);
/* 130 */       int numLayers = numLayersArr.get(0);
/* 131 */       VkLayerProperties.Buffer propsBuf = VkLayerProperties.calloc(numLayers, stack);
/* 132 */       VulkanUtils.throwIfFailure(VK12.vkEnumerateInstanceLayerProperties(numLayersArr, propsBuf), "Error enumerating validation layers", BackendCreationException.Reason.VULKAN_INSTANCE_CREATION_FAILED);
/* 134 */       List<String> supportedLayers = new ArrayList<>();
/* 135 */       for (int i = 0; i < numLayers; i++) {
/* 136 */         VkLayerProperties props = (VkLayerProperties)propsBuf.get(i);
/* 137 */         String layerName = props.layerNameString();
/* 138 */         supportedLayers.add(layerName);
/*     */       } 
/* 140 */       List<String> list1 = supportedLayers;
/* 141 */       if (stack != null)
/* 141 */         stack.close(); 
/*     */       return list1;
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 146 */     this.debug.destroy(this.vkInstance);
/* 147 */     VK12.vkDestroyInstance(this.vkInstance, null);
/*     */   }
/*     */   
/*     */   public Set<String> getEnabledExtensions() {
/* 151 */     return this.enabledExtensions;
/*     */   }
/*     */   
/*     */   public VulkanDebug debug() {
/* 155 */     return this.debug;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */