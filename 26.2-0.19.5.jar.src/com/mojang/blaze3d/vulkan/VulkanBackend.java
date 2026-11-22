/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.GLFWErrorCapture;
/*     */ import com.mojang.blaze3d.platform.NativeLibrariesBootstrap;
/*     */ import com.mojang.blaze3d.shaders.GpuDebugOptions;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.systems.BackendCreationException;
/*     */ import com.mojang.blaze3d.systems.GpuBackend;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.AmdCheckpointExtension;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.CheckpointExtension;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.NoopCheckpointExtension;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.NvidiaCheckpointExtension;
/*     */ import com.mojang.blaze3d.vulkan.init.VulkanFeature;
/*     */ import com.mojang.blaze3d.vulkan.init.VulkanPNextStruct;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*     */ import java.nio.IntBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWVulkan;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ import org.lwjgl.util.vma.VmaAllocatorCreateInfo;
/*     */ import org.lwjgl.util.vma.VmaVulkanFunctions;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkDevice;
/*     */ import org.lwjgl.vulkan.VkDeviceCreateInfo;
/*     */ import org.lwjgl.vulkan.VkDeviceQueueCreateInfo;
/*     */ import org.lwjgl.vulkan.VkPhysicalDevice;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceDynamicRenderingFeatures;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceFeatures;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceMultiDrawFeaturesEXT;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceProperties;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceProperties2;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceSynchronization2Features;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceVertexAttributeDivisorFeaturesEXT;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceVulkan11Features;
/*     */ import org.lwjgl.vulkan.VkPhysicalDeviceVulkan12Features;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VulkanBackend implements GpuBackend {
/*  63 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  65 */   public static final Set<String> REQUIRED_DEVICE_EXTENSIONS = Set.of("VK_KHR_dynamic_rendering", "VK_KHR_push_descriptor", "VK_KHR_synchronization2", "VK_EXT_vertex_attribute_divisor", "VK_KHR_swapchain");
/*     */   
/*  73 */   public static final VulkanPNextStruct VK10_FEATURES_STRUCT = new VulkanPNextStruct(1000059000, VkPhysicalDeviceProperties2.SIZEOF);
/*     */   
/*  74 */   public static final VulkanPNextStruct VK11_FEATURES_STRUCT = new VulkanPNextStruct(49, VkPhysicalDeviceVulkan11Features.SIZEOF);
/*     */   
/*  75 */   public static final VulkanPNextStruct VK12_FEATURES_STRUCT = new VulkanPNextStruct(51, VkPhysicalDeviceVulkan12Features.SIZEOF);
/*     */   
/*  76 */   public static final VulkanPNextStruct SYNC2_FEATURES_STRUCT = new VulkanPNextStruct(1000314007, VkPhysicalDeviceSynchronization2Features.SIZEOF);
/*     */   
/*  77 */   public static final VulkanPNextStruct DYNAMIC_RENDERING_FEATURES_STRUCT = new VulkanPNextStruct(1000044003, VkPhysicalDeviceDynamicRenderingFeatures.SIZEOF);
/*     */   
/*  78 */   public static final VulkanPNextStruct VERTEX_ATTRIB_DIVISOR_FEATURES_STRUCT = new VulkanPNextStruct(1000190002, VkPhysicalDeviceVertexAttributeDivisorFeaturesEXT.SIZEOF);
/*     */   
/*  79 */   public static final VulkanPNextStruct MULTI_DRAW_FEATURES_STRUCT = new VulkanPNextStruct(1000392000, VkPhysicalDeviceMultiDrawFeaturesEXT.SIZEOF);
/*     */   
/*  81 */   public static final Set<VulkanFeature> REQUIRED_DEVICE_FEATURES = Set.of(new VulkanFeature(VK10_FEATURES_STRUCT, "multiDrawIndirect", VkPhysicalDeviceFeatures.MULTIDRAWINDIRECT), new VulkanFeature(VK10_FEATURES_STRUCT, "fillModeNonSolid", VkPhysicalDeviceFeatures.FILLMODENONSOLID), new VulkanFeature(VK10_FEATURES_STRUCT, "samplerAnisotropy", VkPhysicalDeviceFeatures.SAMPLERANISOTROPY), new VulkanFeature(VK11_FEATURES_STRUCT, "shaderDrawParameters", VkPhysicalDeviceVulkan11Features.SHADERDRAWPARAMETERS), new VulkanFeature(VK12_FEATURES_STRUCT, "timelineSemaphore", VkPhysicalDeviceVulkan12Features.TIMELINESEMAPHORE), new VulkanFeature(VK12_FEATURES_STRUCT, "hostQueryReset", VkPhysicalDeviceVulkan12Features.HOSTQUERYRESET), new VulkanFeature(SYNC2_FEATURES_STRUCT, "synchronization2", VkPhysicalDeviceSynchronization2Features.SYNCHRONIZATION2), new VulkanFeature(DYNAMIC_RENDERING_FEATURES_STRUCT, "dynamicRendering", VkPhysicalDeviceDynamicRenderingFeatures.DYNAMICRENDERING), new VulkanFeature(VERTEX_ATTRIB_DIVISOR_FEATURES_STRUCT, "vertexAttributeInstanceRateDivisor", VkPhysicalDeviceVertexAttributeDivisorFeaturesEXT.VERTEXATTRIBUTEINSTANCERATEDIVISOR));
/*     */   
/*  93 */   private static final VulkanFeature MULTI_DRAW_FEATURE = new VulkanFeature(MULTI_DRAW_FEATURES_STRUCT, "multiDraw", VkPhysicalDeviceMultiDrawFeaturesEXT.MULTIDRAW);
/*     */   
/*     */   public String getName() {
/*  97 */     return "Vulkan";
/*     */   }
/*     */   
/*     */   public void setWindowHints() {
/* 102 */     GLFW.glfwWindowHint(139265, 0);
/*     */   }
/*     */   
/*     */   public static BackendCreationException checkBackendAvailable() {
/* 106 */     if (!NativeLibrariesBootstrap.isVulkanLoaderAvailable())
/* 107 */       return new BackendCreationException("Vulkan loader library is missing", BackendCreationException.Reason.VULKAN_LOADER_MISSING); 
/* 109 */     if (!GLFWVulkan.glfwVulkanSupported())
/* 110 */       return new BackendCreationException("Vulkan is not supported", BackendCreationException.Reason.GLFW_ERROR); 
/*     */     try {
/* 113 */       VulkanInstance instance = new VulkanInstance(0, false, false);
/*     */       try {
/* 114 */         VulkanPhysicalDevice physicalDevice = findPhysicalDevice(instance);
/*     */         try {
/* 116 */           BackendCreationException backendCreationException = null;
/* 117 */           if (physicalDevice != null)
/* 117 */             physicalDevice.close(); 
/* 117 */           instance.close();
/*     */           return backendCreationException;
/*     */         } catch (Throwable throwable) {
/*     */           if (physicalDevice != null)
/*     */             try {
/*     */               physicalDevice.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           instance.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 117 */     } catch (BackendCreationException e) {
/* 118 */       return e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleWindowCreationErrors(GLFWErrorCapture.Error error) throws BackendCreationException {
/* 124 */     if (error != null)
/* 125 */       throw new BackendCreationException(String.format(Locale.ROOT, "GLFW_ERROR: 0x%X", new Object[] { Integer.valueOf(error.error()) }), BackendCreationException.Reason.GLFW_ERROR); 
/* 127 */     throw new BackendCreationException("Failed to create window for Vulkan", BackendCreationException.Reason.GLFW_ERROR);
/*     */   }
/*     */   
/*     */   public GpuDevice createDevice(long window, ShaderSource defaultShaderSource, GpuDebugOptions debugOptions, Runnable criticalShaderLoader) throws BackendCreationException {
/*     */     NvidiaCheckpointExtension nvidiaCheckpointExtension;
/* 133 */     if (!NativeLibrariesBootstrap.isVulkanLoaderAvailable())
/* 134 */       throw new BackendCreationException("Vulkan loader library is missing", BackendCreationException.Reason.VULKAN_LOADER_MISSING); 
/* 136 */     if (!GLFWVulkan.glfwVulkanSupported())
/* 137 */       throw new BackendCreationException("Vulkan is not supported", BackendCreationException.Reason.GLFW_ERROR); 
/* 139 */     Set<String> deviceExtensions = new HashSet<>(REQUIRED_DEVICE_EXTENSIONS);
/* 140 */     VulkanInstance instance = null;
/* 141 */     VulkanPhysicalDevice physicalDevice = null;
/* 142 */     VkDevice device = null;
/* 143 */     long vma = 0L;
/* 144 */     NoopCheckpointExtension noopCheckpointExtension = NoopCheckpointExtension.INSTANCE;
/*     */     try {
/* 147 */       boolean renderdocAttached = "1".equals(System.getenv("ENABLE_VULKAN_RENDERDOC_CAPTURE"));
/* 148 */       instance = new VulkanInstance(debugOptions.logLevel(), (debugOptions.useLabels() || renderdocAttached), debugOptions.useValidationLayers());
/* 149 */       physicalDevice = findPhysicalDevice(instance);
/* 150 */       ObjectOpenHashSet<VulkanFeature> objectOpenHashSet = new ObjectOpenHashSet(REQUIRED_DEVICE_FEATURES);
/* 151 */       if (physicalDevice.hasDeviceExtension("VK_KHR_portability_subset"))
/* 152 */         deviceExtensions.add("VK_KHR_portability_subset"); 
/* 154 */       if (physicalDevice.hasDeviceExtension("VK_AMD_buffer_marker")) {
/* 155 */         deviceExtensions.add("VK_AMD_buffer_marker");
/* 156 */         AmdCheckpointExtension amdCheckpointExtension = new AmdCheckpointExtension();
/* 157 */       } else if (physicalDevice.hasDeviceExtension("VK_NV_device_diagnostic_checkpoints")) {
/* 158 */         deviceExtensions.add("VK_NV_device_diagnostic_checkpoints");
/* 159 */         nvidiaCheckpointExtension = new NvidiaCheckpointExtension();
/*     */       } 
/* 161 */       if (physicalDevice.hasDeviceExtension("VK_EXT_multi_draw") && isFeatureSupported(physicalDevice.vkPhysicalDevice(), MULTI_DRAW_FEATURE)) {
/* 162 */         deviceExtensions.add("VK_EXT_multi_draw");
/* 163 */         objectOpenHashSet.add(MULTI_DRAW_FEATURE);
/*     */       } 
/* 165 */       device = createDevice(deviceExtensions, physicalDevice, (Set<VulkanFeature>)objectOpenHashSet);
/* 166 */       vma = createVma(device);
/* 167 */     } catch (BackendCreationException e) {
/* 169 */       if (vma != 0L)
/* 170 */         Vma.vmaDestroyAllocator(vma); 
/* 172 */       if (device != null)
/* 173 */         VK12.vkDestroyDevice(device, null); 
/* 175 */       if (physicalDevice != null)
/* 176 */         physicalDevice.close(); 
/* 178 */       if (instance != null)
/* 179 */         instance.close(); 
/* 181 */       throw e;
/*     */     } 
/* 184 */     return new GpuDevice(new VulkanDevice(defaultShaderSource, instance, physicalDevice, deviceExtensions, device, vma, (CheckpointExtension)nvidiaCheckpointExtension), criticalShaderLoader);
/*     */   }
/*     */   
/*     */   private static long createVma(VkDevice vkDevice) throws BackendCreationException {
/* 188 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 190 */       VmaVulkanFunctions vmaVulkanFunctions = VmaVulkanFunctions.calloc(stack).set(vkDevice.getPhysicalDevice().getInstance(), vkDevice);
/* 197 */       VmaAllocatorCreateInfo createInfo = VmaAllocatorCreateInfo.calloc(stack).instance(vkDevice.getPhysicalDevice().getInstance()).vulkanApiVersion(VK12.VK_API_VERSION_1_2).device(vkDevice).physicalDevice(vkDevice.getPhysicalDevice()).pVulkanFunctions(vmaVulkanFunctions);
/* 199 */       PointerBuffer pointer = stack.callocPointer(1);
/* 200 */       VulkanUtils.throwIfFailure(Vma.vmaCreateAllocator(createInfo, pointer), "Failed to create VMA allocator", BackendCreationException.Reason.OTHER);
/* 201 */       long l = pointer.get(0);
/* 202 */       if (stack != null)
/* 202 */         stack.close(); 
/*     */       return l;
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
/*     */   private static VulkanPhysicalDevice findPhysicalDevice(VulkanInstance instance) throws BackendCreationException {
/* 207 */     VkPhysicalDevice firstDevice = null;
/* 208 */     VkPhysicalDevice selectedDevice = null;
/* 209 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 213 */       IntBuffer intBuffer = stack.callocInt(1);
/* 214 */       VulkanUtils.throwIfFailure(VK12.vkEnumeratePhysicalDevices(instance.vkInstance(), intBuffer, null), "Failed to get number of physical devices", BackendCreationException.Reason.VULKAN_NO_DEVICE);
/* 216 */       if (intBuffer.get(0) == 0)
/* 217 */         throw new BackendCreationException("No Vulkan capable devices", BackendCreationException.Reason.VULKAN_NO_DEVICE); 
/* 221 */       PointerBuffer pPhysicalDevices = stack.callocPointer(intBuffer.get(0));
/* 222 */       VulkanUtils.throwIfFailure(VK12.vkEnumeratePhysicalDevices(instance.vkInstance(), intBuffer, pPhysicalDevices), "Failed to get physical devices", BackendCreationException.Reason.VULKAN_NO_DEVICE);
/* 223 */       int numDevices = intBuffer.get(0);
/* 225 */       if (numDevices == 0)
/* 226 */         throw new BackendCreationException("No Vulkan capable devices", BackendCreationException.Reason.VULKAN_NO_DEVICE); 
/* 230 */       for (int i = 0; i < numDevices; i++) {
/* 231 */         if (pPhysicalDevices.get(i) != 0L) {
/* 234 */           VkPhysicalDevice currentDevice = new VkPhysicalDevice(pPhysicalDevices.get(i), instance.vkInstance());
/* 235 */           if (firstDevice == null)
/* 236 */             firstDevice = currentDevice; 
/* 238 */           if (deviceMeetsFeatureQueryRequirements(currentDevice))
/* 241 */             if (isDeviceSuitable(currentDevice))
/* 244 */               if (selectedDevice == null) {
/* 245 */                 selectedDevice = currentDevice;
/* 248 */               } else if (isDeviceDiscrete(currentDevice) && !isDeviceDiscrete(selectedDevice)) {
/* 249 */                 LOGGER.info("Preferring discrete GPU: {}", getDeviceName(currentDevice));
/* 250 */                 selectedDevice = currentDevice;
/*     */                 break;
/*     */               }   
/*     */         } 
/*     */       } 
/* 254 */       if (stack != null)
/* 254 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 256 */     if (firstDevice == null)
/* 257 */       throw new BackendCreationException("No Vulkan capable devices", BackendCreationException.Reason.VULKAN_NO_DEVICE); 
/* 260 */     if (selectedDevice == null) {
/* 261 */       throwForMissingRequrements(firstDevice);
/*     */       assert false;
/*     */     } 
/* 266 */     return new VulkanPhysicalDevice(selectedDevice);
/*     */   }
/*     */   
/*     */   private static boolean deviceMeetsFeatureQueryRequirements(VkPhysicalDevice vkPhysicalDevice) {
/* 272 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 273 */       VkPhysicalDeviceProperties properties = VkPhysicalDeviceProperties.calloc(stack);
/* 274 */       VK12.vkGetPhysicalDeviceProperties(vkPhysicalDevice, properties);
/* 275 */       boolean bool = (properties.apiVersion() >= VK12.VK_API_VERSION_1_1) ? true : false;
/* 276 */       if (stack != null)
/* 276 */         stack.close(); 
/*     */       return bool;
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
/*     */   private static boolean isDeviceSuitable(VkPhysicalDevice vkPhysicalDevice) throws BackendCreationException {
/* 281 */     VulkanPhysicalDevice physicalDevice = new VulkanPhysicalDevice(vkPhysicalDevice);
/*     */     try {
/* 282 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 285 */         String deviceName = physicalDevice.deviceName();
/* 290 */         VulkanUtils.DeviceUUID deviceUUID = new VulkanUtils.DeviceUUID(physicalDevice.vkPhysicalDeviceDriverProperties().driverID(), physicalDevice.vkPhysicalDeviceProperties().vendorID(), physicalDevice.vkPhysicalDeviceProperties().deviceID());
/* 292 */         if (VulkanUtils.KNOWN_PROBLEMATIC_DEVICES.contains(deviceUUID)) {
/* 293 */           LOGGER.warn("Device [{}] is known to be problematic, skipping", deviceName);
/* 294 */           boolean bool = false;
/* 334 */           if (stack != null)
/* 334 */             stack.close(); 
/* 334 */           physicalDevice.close();
/*     */           return bool;
/*     */         } 
/*     */         Set<String> missingExtensions = physicalDevice.getMissingExtensions(REQUIRED_DEVICE_EXTENSIONS);
/*     */         VkPhysicalDeviceFeatures2 deviceFeatures = VkPhysicalDeviceFeatures2.calloc(stack).sType$Default();
/*     */         for (VulkanFeature requiredDeviceFeature : REQUIRED_DEVICE_FEATURES)
/*     */           requiredDeviceFeature.struct().findOrCreateStructInPNextChain(deviceFeatures, stack); 
/*     */         VK12.vkGetPhysicalDeviceFeatures2(vkPhysicalDevice, deviceFeatures);
/*     */         boolean isSuitableDevice = true;
/*     */         if (physicalDevice.vkPhysicalDeviceProperties().apiVersion() < VK12.VK_API_VERSION_1_2) {
/*     */           LOGGER.warn("Device [{}] does not support Vulkan 1.2", deviceName);
/*     */           isSuitableDevice = false;
/*     */         } 
/*     */         if (physicalDevice.graphicsQueueFamilyAndIndex() == null) {
/*     */           LOGGER.warn("Device [{}] does not have a graphics queue", deviceName);
/*     */           isSuitableDevice = false;
/*     */         } 
/*     */         if (!missingExtensions.isEmpty()) {
/*     */           LOGGER.warn("Device [{}] does not support required extensions, missing: {}", deviceName, missingExtensions);
/*     */           isSuitableDevice = false;
/*     */         } 
/*     */         for (VulkanFeature requiredDeviceFeature : REQUIRED_DEVICE_FEATURES) {
/*     */           if (!requiredDeviceFeature.get(deviceFeatures)) {
/*     */             LOGGER.warn("Device [{}] does not have required feature [{}]", deviceName, requiredDeviceFeature.name());
/*     */             isSuitableDevice = false;
/*     */           } 
/*     */         } 
/*     */         if (isSuitableDevice)
/*     */           LOGGER.debug("Device [{}] is suitable", deviceName); 
/*     */         boolean bool1 = isSuitableDevice;
/* 334 */         if (stack != null)
/* 334 */           stack.close(); 
/* 334 */         physicalDevice.close();
/*     */         return bool1;
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         physicalDevice.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isDeviceDiscrete(VkPhysicalDevice vkPhysicalDevice) {
/* 338 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 339 */       VkPhysicalDeviceProperties2 deviceProperties = VkPhysicalDeviceProperties2.calloc(stack).sType$Default();
/* 340 */       VK12.vkGetPhysicalDeviceProperties2(vkPhysicalDevice, deviceProperties);
/* 341 */       boolean bool = (deviceProperties.properties().deviceType() == 2) ? true : false;
/* 342 */       if (stack != null)
/* 342 */         stack.close(); 
/*     */       return bool;
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
/*     */   private static String getDeviceName(VkPhysicalDevice vkPhysicalDevice) {
/* 346 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 347 */       VkPhysicalDeviceProperties2 deviceProperties = VkPhysicalDeviceProperties2.calloc(stack).sType$Default();
/* 348 */       VK12.vkGetPhysicalDeviceProperties2(vkPhysicalDevice, deviceProperties);
/* 349 */       String str = deviceProperties.properties().deviceNameString();
/* 350 */       if (stack != null)
/* 350 */         stack.close(); 
/*     */       return str;
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
/*     */   private static boolean isFeatureSupported(VkPhysicalDevice vkPhysicalDevice, VulkanFeature feature) {
/* 354 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 355 */       VkPhysicalDeviceFeatures2 deviceFeatures = VkPhysicalDeviceFeatures2.calloc(stack).sType$Default();
/* 356 */       feature.struct().findOrCreateStructInPNextChain(deviceFeatures, stack);
/* 357 */       VK12.vkGetPhysicalDeviceFeatures2(vkPhysicalDevice, deviceFeatures);
/* 358 */       boolean bool = feature.get(deviceFeatures);
/* 359 */       if (stack != null)
/* 359 */         stack.close(); 
/*     */       return bool;
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
/*     */   private static void throwForMissingRequrements(VkPhysicalDevice vkPhysicalDevice) throws BackendCreationException {
/* 363 */     ReferenceArrayList<String> referenceArrayList = new ReferenceArrayList();
/* 364 */     BackendCreationException.Reason mostProminentReason = BackendCreationException.Reason.OTHER;
/* 365 */     if (!deviceMeetsFeatureQueryRequirements(vkPhysicalDevice))
/* 368 */       throw new BackendCreationException("Device missing capabilities", BackendCreationException.Reason.VULKAN_DEVICE_VERSION_TOO_LOW, List.of("VULKAN_CORE_1_1")); 
/* 371 */     VulkanPhysicalDevice physicalDevice = new VulkanPhysicalDevice(vkPhysicalDevice);
/*     */     try {
/* 372 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 374 */         VkPhysicalDeviceFeatures2 deviceFeatures = VkPhysicalDeviceFeatures2.calloc(stack).sType$Default();
/* 375 */         for (VulkanFeature requiredDeviceFeature : REQUIRED_DEVICE_FEATURES)
/* 376 */           requiredDeviceFeature.struct().findOrCreateStructInPNextChain(deviceFeatures, stack); 
/* 378 */         VK12.vkGetPhysicalDeviceFeatures2(vkPhysicalDevice, deviceFeatures);
/* 380 */         for (VulkanFeature requiredDeviceFeature : REQUIRED_DEVICE_FEATURES) {
/* 381 */           if (!requiredDeviceFeature.get(deviceFeatures)) {
/* 382 */             mostProminentReason = BackendCreationException.Reason.VULKAN_MISSING_FEATURE;
/* 383 */             referenceArrayList.add(requiredDeviceFeature.name());
/*     */           } 
/*     */         } 
/* 387 */         Set<String> missingExtensions = physicalDevice.getMissingExtensions(REQUIRED_DEVICE_EXTENSIONS);
/* 388 */         if (!missingExtensions.isEmpty()) {
/* 389 */           mostProminentReason = BackendCreationException.Reason.VULKAN_MISSING_EXTENSION;
/* 390 */           referenceArrayList.addAll(missingExtensions);
/*     */         } 
/* 393 */         if (physicalDevice.graphicsQueueFamilyAndIndex() == null) {
/* 394 */           mostProminentReason = BackendCreationException.Reason.VULKAN_NO_GRAPHICS_QUEUE;
/* 395 */           referenceArrayList.add("COMBINED_GRAPHICS_COMPUTE_PRESENT_QUEUE");
/*     */         } 
/* 398 */         if (physicalDevice.vkPhysicalDeviceProperties().apiVersion() < VK12.VK_API_VERSION_1_2) {
/* 399 */           mostProminentReason = BackendCreationException.Reason.VULKAN_DEVICE_VERSION_TOO_LOW;
/* 400 */           referenceArrayList.add("VULKAN_CORE_1_2");
/*     */         } 
/* 402 */         if (stack != null)
/* 402 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 402 */       physicalDevice.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         physicalDevice.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/* 404 */     throw new BackendCreationException("Device missing capabilities", mostProminentReason, referenceArrayList);
/*     */   }
/*     */   
/*     */   private static VkDevice createDevice(Collection<String> deviceExtensions, VulkanPhysicalDevice physicalDevice, Set<VulkanFeature> vulkanFeatures) throws BackendCreationException {
/* 408 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 410 */       VkPhysicalDeviceFeatures2 deviceFeatures = VkPhysicalDeviceFeatures2.calloc(stack).sType$Default();
/* 411 */       for (VulkanFeature requiredDeviceFeature : vulkanFeatures)
/* 412 */         requiredDeviceFeature.set(deviceFeatures, true, stack); 
/* 415 */       Int2IntMap queuesToCreate = physicalDevice.queueFamilyCreateInfoMap();
/* 416 */       VkDeviceQueueCreateInfo.Buffer queueCreationInfo = VkDeviceQueueCreateInfo.calloc(queuesToCreate.size(), stack);
/* 417 */       for (ObjectIterator<Int2IntMap.Entry> objectIterator = queuesToCreate.int2IntEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 417 */         Int2IntMap.Entry familyCount = objectIterator.next();
/* 418 */         queueCreationInfo.sType$Default();
/* 419 */         queueCreationInfo.queueFamilyIndex(familyCount.getIntKey());
/* 420 */         queueCreationInfo.pQueuePriorities(stack.callocFloat(familyCount.getIntValue()));
/* 421 */         queueCreationInfo.position(queueCreationInfo.position() + 1);
/*     */       } 
/* 423 */       queueCreationInfo.position(0);
/* 425 */       PointerBuffer enabledExtensionsBuffer = stack.callocPointer(deviceExtensions.size());
/* 426 */       for (String name : deviceExtensions)
/* 427 */         enabledExtensionsBuffer.put(stack.UTF8(name)); 
/* 429 */       enabledExtensionsBuffer.flip();
/* 431 */       VkDeviceCreateInfo deviceCreateInfo = VkDeviceCreateInfo.calloc(stack).sType$Default();
/* 432 */       deviceCreateInfo.pNext(deviceFeatures.pNext());
/* 433 */       deviceCreateInfo.pQueueCreateInfos(queueCreationInfo);
/* 434 */       deviceCreateInfo.ppEnabledExtensionNames(enabledExtensionsBuffer);
/* 435 */       deviceCreateInfo.pEnabledFeatures(deviceFeatures.features());
/* 437 */       PointerBuffer pointer = stack.callocPointer(1);
/* 438 */       VulkanUtils.throwIfFailure(VK12.vkCreateDevice(physicalDevice.vkPhysicalDevice(), deviceCreateInfo, null, pointer), "Failed to create device", BackendCreationException.Reason.VULKAN_NO_DEVICE);
/* 439 */       VkDevice vkDevice = new VkDevice(pointer.get(0), physicalDevice.vkPhysicalDevice(), deviceCreateInfo);
/* 440 */       if (stack != null)
/* 440 */         stack.close(); 
/*     */       return vkDevice;
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
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */