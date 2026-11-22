/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.BackendCreationException;
/*     */ import com.mojang.blaze3d.systems.DeviceType;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMaps;
/*     */ import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
/*     */ import it.unimi.dsi.fastutil.ints.IntIntPair;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.glfw.GLFWVulkan;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkExtensionProperties;
/*     */ import org.lwjgl.vulkan.VkPhysicalDevice;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceDriverProperties;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceMultiDrawPropertiesEXT;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceProperties2;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceVulkan11Properties;
/*     */ import org.lwjgl.vulkan.VkQueueFamilyProperties;
/*     */ 
/*     */ public class VulkanPhysicalDevice implements AutoCloseable {
/*     */   private final VkPhysicalDevice vkPhysicalDevice;
/*     */   
/*     */   private final VkExtensionProperties.Buffer vkDeviceExtensions;
/*     */   
/*     */   private final VkPhysicalDeviceFeatures2 vkPhysicalDeviceFeatures;
/*     */   
/*     */   private final VkPhysicalDeviceProperties2 vkPhysicalDeviceProperties;
/*     */   
/*     */   private final VkPhysicalDeviceVulkan11Properties vkPhysicalDeviceVulkan11Properties;
/*     */   
/*     */   private final VkPhysicalDeviceDriverProperties vkPhysicalDeviceDriverProperties;
/*     */   
/*     */   private final VkPhysicalDeviceMultiDrawPropertiesEXT vkPhysicalDeviceMultiDrawPropertiesEXT;
/*     */   
/*     */   private final Int2IntMap queueFamilyCreateInfoMap;
/*     */   
/*     */   private final IntIntPair graphicsQueueFamilyAndIndex;
/*     */   
/*     */   private final IntIntPair computeQueueFamilyAndIndex;
/*     */   
/*     */   private final IntIntPair transferQueueFamilyAndIndex;
/*     */   
/*     */   public VulkanPhysicalDevice(VkPhysicalDevice vkPhysicalDevice) throws BackendCreationException {
/*  48 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  49 */       this.vkPhysicalDevice = vkPhysicalDevice;
/*  51 */       IntBuffer intBuffer = stack.callocInt(1);
/*  54 */       VulkanUtils.throwIfFailure(VK12.vkEnumerateDeviceExtensionProperties(vkPhysicalDevice, (String)null, intBuffer, null), "Failed to get number of device extension properties", BackendCreationException.Reason.VULKAN_NO_DEVICE);
/*  56 */       this.vkDeviceExtensions = VkExtensionProperties.calloc(intBuffer.get(0));
/*  57 */       VulkanUtils.throwIfFailure(VK12.vkEnumerateDeviceExtensionProperties(vkPhysicalDevice, (String)null, intBuffer, this.vkDeviceExtensions), "Failed to get extension properties", BackendCreationException.Reason.VULKAN_NO_DEVICE);
/*  61 */       this.vkPhysicalDeviceProperties = VkPhysicalDeviceProperties2.calloc().sType$Default();
/*  62 */       this.vkPhysicalDeviceVulkan11Properties = VkPhysicalDeviceVulkan11Properties.calloc().sType$Default();
/*  63 */       this.vkPhysicalDeviceDriverProperties = VkPhysicalDeviceDriverProperties.calloc().sType$Default();
/*  64 */       this.vkPhysicalDeviceMultiDrawPropertiesEXT = VkPhysicalDeviceMultiDrawPropertiesEXT.calloc().sType$Default();
/*  65 */       this.vkPhysicalDeviceProperties.pNext(this.vkPhysicalDeviceDriverProperties);
/*  66 */       this.vkPhysicalDeviceProperties.pNext(this.vkPhysicalDeviceVulkan11Properties);
/*  67 */       if (hasDeviceExtension("VK_EXT_multi_draw"))
/*  68 */         this.vkPhysicalDeviceProperties.pNext(this.vkPhysicalDeviceMultiDrawPropertiesEXT); 
/*  70 */       VK12.vkGetPhysicalDeviceProperties2(vkPhysicalDevice, this.vkPhysicalDeviceProperties);
/*  73 */       VK12.vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, intBuffer, null);
/*  74 */       VkQueueFamilyProperties.Buffer vkQueueFamilyProps = VkQueueFamilyProperties.calloc(intBuffer.get(0), stack);
/*  75 */       VK12.vkGetPhysicalDeviceQueueFamilyProperties(vkPhysicalDevice, intBuffer, vkQueueFamilyProps);
/*  77 */       this.vkPhysicalDeviceFeatures = VkPhysicalDeviceFeatures2.calloc().sType$Default();
/*  78 */       VK12.vkGetPhysicalDeviceFeatures2(vkPhysicalDevice, this.vkPhysicalDeviceFeatures);
/*  95 */       int graphicsQueueFamily = -1;
/*  96 */       int computeQueueFamily = -1;
/*  97 */       int transferQueueFamily = -1;
/*  99 */       int computeQueueFamilyBits = -1;
/* 100 */       int transferQueueFamilyBits = -1;
/* 102 */       int numQueueFamilies = vkQueueFamilyProps.capacity();
/* 103 */       for (int i = 0; i < numQueueFamilies; i++) {
/* 104 */         int familyUsedQueues = 0;
/* 105 */         VkQueueFamilyProperties queueFamilyProperties = (VkQueueFamilyProperties)vkQueueFamilyProps.get(i);
/* 109 */         if (graphicsQueueFamily == -1 && VulkanUtils.hasAllBits(queueFamilyProperties.queueFlags(), 3) && GLFWVulkan.glfwGetPhysicalDevicePresentationSupport(vkPhysicalDevice.getInstance(), vkPhysicalDevice, i)) {
/* 110 */           graphicsQueueFamily = i;
/* 111 */           familyUsedQueues++;
/*     */         } 
/* 113 */         if (queueFamilyProperties.queueCount() > familyUsedQueues) {
/* 116 */           if (VulkanUtils.hasAllBits(queueFamilyProperties.queueFlags(), 2))
/* 118 */             if (computeQueueFamily == -1 || Integer.bitCount(queueFamilyProperties.queueFlags()) <= Integer.bitCount(computeQueueFamilyBits)) {
/* 119 */               computeQueueFamily = i;
/* 120 */               computeQueueFamilyBits = queueFamilyProperties.queueFlags();
/* 121 */               familyUsedQueues++;
/*     */             }  
/* 124 */           if (queueFamilyProperties.queueCount() > familyUsedQueues)
/* 128 */             if (VulkanUtils.hasAnyBit(queueFamilyProperties.queueFlags(), 7))
/* 131 */               if (transferQueueFamily == -1 || Integer.bitCount(queueFamilyProperties.queueFlags()) <= Integer.bitCount(transferQueueFamilyBits)) {
/* 132 */                 transferQueueFamily = i;
/* 133 */                 transferQueueFamilyBits = queueFamilyProperties.queueFlags();
/* 134 */                 familyUsedQueues++;
/*     */               }   
/*     */         } 
/*     */       } 
/* 139 */       Int2IntArrayMap int2IntArrayMap = new Int2IntArrayMap();
/* 140 */       int graphicsQueueIndex = int2IntArrayMap.put(graphicsQueueFamily, int2IntArrayMap.get(graphicsQueueFamily) + 1);
/* 141 */       int computeQueueIndex = int2IntArrayMap.put(computeQueueFamily, int2IntArrayMap.get(computeQueueFamily) + 1);
/* 142 */       int transferQueueIndex = int2IntArrayMap.put(transferQueueFamily, int2IntArrayMap.get(transferQueueFamily) + 1);
/* 144 */       int2IntArrayMap.remove(-1);
/* 146 */       this.queueFamilyCreateInfoMap = Int2IntMaps.unmodifiable((Int2IntMap)int2IntArrayMap);
/* 147 */       this.graphicsQueueFamilyAndIndex = (graphicsQueueFamily == -1) ? null : (IntIntPair)new IntIntImmutablePair(graphicsQueueFamily, graphicsQueueIndex);
/* 148 */       this.computeQueueFamilyAndIndex = (computeQueueFamily == -1) ? null : (IntIntPair)new IntIntImmutablePair(computeQueueFamily, computeQueueIndex);
/* 149 */       this.transferQueueFamilyAndIndex = (transferQueueFamily == -1) ? null : (IntIntPair)new IntIntImmutablePair(transferQueueFamily, transferQueueIndex);
/* 151 */       if (stack != null)
/* 151 */         stack.close(); 
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
/* 156 */     this.vkPhysicalDeviceFeatures.free();
/* 157 */     this.vkDeviceExtensions.free();
/* 158 */     this.vkPhysicalDeviceMultiDrawPropertiesEXT.free();
/* 159 */     this.vkPhysicalDeviceVulkan11Properties.free();
/* 160 */     this.vkPhysicalDeviceDriverProperties.free();
/* 161 */     this.vkPhysicalDeviceProperties.free();
/*     */   }
/*     */   
/*     */   public String deviceName() {
/* 165 */     return this.vkPhysicalDeviceProperties.properties().deviceNameString();
/*     */   }
/*     */   
/*     */   public String vendorName() {
/* 169 */     int vendorId = this.vkPhysicalDeviceProperties.properties().vendorID();
/* 170 */     switch (vendorId) {
/*     */       case 4098:
/*     */       case 4130:
/*     */       
/*     */       case 4318:
/*     */       case 4818:
/*     */       
/*     */       case 32902:
/*     */       
/*     */       case 5772:
/*     */       case 6091:
/*     */       case 6505:
/*     */       case 20803:
/*     */       
/*     */       case 4112:
/*     */       
/*     */       case 5348:
/*     */       
/*     */       case 5045:
/*     */       
/*     */       case 5140:
/*     */       
/*     */       case 4203:
/*     */       
/*     */     } 
/* 170 */     return 
/*     */       
/* 180 */       String.format(Locale.ROOT, "0x%x", new Object[] { Integer.valueOf(vendorId) });
/*     */   }
/*     */   
/*     */   public VkPhysicalDevice vkPhysicalDevice() {
/* 185 */     return this.vkPhysicalDevice;
/*     */   }
/*     */   
/*     */   public VkPhysicalDeviceProperties vkPhysicalDeviceProperties() {
/* 189 */     return this.vkPhysicalDeviceProperties.properties();
/*     */   }
/*     */   
/*     */   public VkPhysicalDeviceVulkan11Properties vkPhysicalDeviceVulkan11Properties() {
/* 193 */     return this.vkPhysicalDeviceVulkan11Properties;
/*     */   }
/*     */   
/*     */   public VkPhysicalDeviceDriverProperties vkPhysicalDeviceDriverProperties() {
/* 197 */     return this.vkPhysicalDeviceDriverProperties;
/*     */   }
/*     */   
/*     */   public VkPhysicalDeviceMultiDrawPropertiesEXT vkPhysicalDeviceMultiDrawPropertiesEXT() {
/* 201 */     return this.vkPhysicalDeviceMultiDrawPropertiesEXT;
/*     */   }
/*     */   
/*     */   public boolean hasDeviceExtension(String name) {
/* 205 */     return this.vkDeviceExtensions.stream().anyMatch(e -> e.extensionNameString().equals(name));
/*     */   }
/*     */   
/*     */   public Set<String> getMissingExtensions(Collection<String> required) {
/* 209 */     Set<String> remaining = new HashSet<>(required);
/* 210 */     for (VkExtensionProperties extension : this.vkDeviceExtensions)
/* 211 */       remaining.remove(extension.extensionNameString()); 
/* 213 */     return remaining;
/*     */   }
/*     */   
/*     */   public Int2IntMap queueFamilyCreateInfoMap() {
/* 217 */     return this.queueFamilyCreateInfoMap;
/*     */   }
/*     */   
/*     */   public IntIntPair graphicsQueueFamilyAndIndex() {
/* 221 */     return this.graphicsQueueFamilyAndIndex;
/*     */   }
/*     */   
/*     */   public IntIntPair computeQueueFamilyAndIndex() {
/* 225 */     return this.computeQueueFamilyAndIndex;
/*     */   }
/*     */   
/*     */   public IntIntPair transferQueueFamilyAndIndex() {
/* 229 */     return this.transferQueueFamilyAndIndex;
/*     */   }
/*     */   
/*     */   private static String getStandardEncodingVersion(int version) {
/* 233 */     int major = version >>> 22 & 0x7F;
/* 234 */     int minor = version >>> 12 & 0x3FF;
/* 235 */     int patch = version & 0xFFF;
/* 236 */     return String.format(Locale.ROOT, "%d.%d.%d", new Object[] { Integer.valueOf(major), Integer.valueOf(minor), Integer.valueOf(patch) });
/*     */   }
/*     */   
/*     */   public String driverInfo() {
/* 240 */     int apiVersion = this.vkPhysicalDeviceProperties.properties().apiVersion();
/* 241 */     String versionString = getStandardEncodingVersion(apiVersion);
/* 243 */     return String.format(Locale.ROOT, "%s %s %s", new Object[] { versionString, this.vkPhysicalDeviceDriverProperties.driverNameString(), this.vkPhysicalDeviceDriverProperties.driverInfoString() });
/*     */   }
/*     */   
/*     */   public DeviceType deviceType() {
/* 247 */     switch (this.vkPhysicalDeviceProperties.properties().deviceType()) {
/*     */       case 1:
/*     */       
/*     */       case 2:
/*     */       
/*     */       case 3:
/*     */       
/*     */       case 4:
/*     */       
/*     */     } 
/* 247 */     return 
/*     */       
/* 252 */       DeviceType.OTHER;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanPhysicalDevice.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */