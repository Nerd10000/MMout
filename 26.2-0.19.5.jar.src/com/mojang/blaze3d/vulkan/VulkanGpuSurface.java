/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.CommandEncoderBackend;
/*     */ import com.mojang.blaze3d.systems.GpuSurface;
/*     */ import com.mojang.blaze3d.systems.GpuSurfaceBackend;
/*     */ import com.mojang.blaze3d.systems.SurfaceException;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vulkan.checkpoints.CheckpointExtension;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrays;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.glfw.GLFWVulkan;
/*     */ import org.lwjgl.system.CustomBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.vulkan.KHRSurface;
/*     */ import org.lwjgl.vulkan.KHRSwapchain;
/*     */ import org.lwjgl.vulkan.KHRSynchronization2;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkDependencyInfo;
/*     */ import org.lwjgl.vulkan.VkExtent2D;
/*     */ import org.lwjgl.vulkan.VkImageBlit;
/*     */ import org.lwjgl.vulkan.VkImageMemoryBarrier2;
/*     */ import org.lwjgl.vulkan.VkImageSubresourceLayers;
/*     */ import org.lwjgl.vulkan.VkImageSubresourceRange;
/*     */ import org.lwjgl.vulkan.VkMemoryBarrier2;
/*     */ import org.lwjgl.vulkan.VkOffset3D;
/*     */ import org.lwjgl.vulkan.VkPresentInfoKHR;
/*     */ import org.lwjgl.vulkan.VkQueue;
/*     */ import org.lwjgl.vulkan.VkSemaphoreCreateInfo;
/*     */ import org.lwjgl.vulkan.VkSurfaceCapabilitiesKHR;
/*     */ import org.lwjgl.vulkan.VkSurfaceFormatKHR;
/*     */ import org.lwjgl.vulkan.VkSwapchainCreateInfoKHR;
/*     */ 
/*     */ public class VulkanGpuSurface implements GpuSurfaceBackend {
/*     */   private static final int NO_CURRENT_IMAGE = -1;
/*     */   
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final VkQueue presentQueue;
/*     */   
/*     */   private final long surface;
/*     */   
/*     */   private final int swapchainImageFormat;
/*     */   
/*     */   private long swapchain;
/*     */   
/*     */   private int swapchainWidth;
/*     */   
/*     */   private int swapchainHeight;
/*     */   
/*  58 */   private final LongList swapchainImages = (LongList)new LongArrayList();
/*     */   
/*  59 */   private final long[] acquireSemaphores = new long[2];
/*     */   
/*  60 */   private int currentAcquireSemaphore = 0;
/*     */   
/*  61 */   private long[] presentSemaphores = LongArrays.EMPTY_ARRAY;
/*     */   
/*  62 */   private int currentImageIndex = -1;
/*     */   
/*  63 */   private SurfaceException eatenException = null;
/*     */   
/*     */   private boolean swapchainSuboptimal;
/*     */   
/*     */   private boolean swapchainOutOfDate;
/*     */   
/*     */   private final Set<GpuSurface.PresentMode> supportedPresentModes;
/*     */   
/*     */   public VulkanGpuSurface(VulkanDevice device, long windowHandle) {
/*  69 */     this.device = device;
/*  70 */     this.presentQueue = device.graphicsQueue().vkQueue();
/*  71 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  72 */       LongBuffer handlePtr = stack.longs(0L);
/*  73 */       VulkanUtils.crashIfFailure(device, GLFWVulkan.glfwCreateWindowSurface(device.vkDevice().getPhysicalDevice().getInstance(), windowHandle, null, handlePtr), "Failed to create window surface");
/*  74 */       this.surface = handlePtr.get(0);
/*  76 */       IntBuffer countPtr = stack.callocInt(1);
/*  77 */       VulkanUtils.crashIfFailure(device, KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device.vkDevice().getPhysicalDevice(), this.surface, countPtr, null), "Failed to enumerate surface present modes");
/*  78 */       int presentModeCount = countPtr.get(0);
/*  79 */       IntBuffer presentModes = stack.callocInt(presentModeCount);
/*  80 */       VulkanUtils.crashIfFailure(device, KHRSurface.vkGetPhysicalDeviceSurfacePresentModesKHR(device.vkDevice().getPhysicalDevice(), this.surface, countPtr, presentModes), "Failed to enumerate surface present modes");
/*  81 */       this.supportedPresentModes = Collections.unmodifiableSet(convertPresentModes(presentModes));
/*  83 */       IntBuffer formatCount = stack.callocInt(1);
/*  84 */       KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device.vkDevice().getPhysicalDevice(), this.surface, formatCount, null);
/*  85 */       VkSurfaceFormatKHR.Buffer formatsBuffer = VkSurfaceFormatKHR.calloc(formatCount.get(0));
/*  86 */       KHRSurface.vkGetPhysicalDeviceSurfaceFormatsKHR(device.vkDevice().getPhysicalDevice(), this.surface, formatCount, formatsBuffer);
/*  88 */       this.swapchainImageFormat = pickSwapchainSurfaceFormat(formatsBuffer).format();
/*  89 */       MemoryUtil.memFree((CustomBuffer)formatsBuffer);
/*  91 */       VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo.calloc(stack).sType$Default();
/*  92 */       LongBuffer semaphoreHandlePtr = stack.callocLong(1);
/*  93 */       for (int i = 0; i < this.acquireSemaphores.length; i++) {
/*  94 */         VulkanUtils.crashIfFailure(device, VK12.vkCreateSemaphore(device.vkDevice(), semaphoreCreateInfo, null, semaphoreHandlePtr), "Failed to create VkSemaphore(binary)");
/*  95 */         this.acquireSemaphores[i] = semaphoreHandlePtr.get(0);
/*     */       } 
/*  97 */       if (stack != null)
/*  97 */         stack.close(); 
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
/*     */   private Set<GpuSurface.PresentMode> convertPresentModes(IntBuffer presentModes) {
/* 101 */     Set<GpuSurface.PresentMode> result = EnumSet.noneOf(GpuSurface.PresentMode.class);
/* 102 */     for (int i = 0; i < presentModes.limit(); i++) {
/* 103 */       int mode = presentModes.get(i);
/* 104 */       switch (mode) {
/*     */         case 0:
/* 105 */           result.add(GpuSurface.PresentMode.IMMEDIATE);
/*     */           break;
/*     */         case 1:
/* 106 */           result.add(GpuSurface.PresentMode.MAILBOX);
/*     */           break;
/*     */         case 2:
/* 107 */           result.add(GpuSurface.PresentMode.FIFO);
/*     */           break;
/*     */         case 3:
/* 108 */           result.add(GpuSurface.PresentMode.FIFO_RELAXED);
/*     */           break;
/*     */       } 
/*     */     } 
/* 113 */     return result;
/*     */   }
/*     */   
/*     */   public Collection<GpuSurface.PresentMode> supportedPresentModes() {
/* 118 */     return this.supportedPresentModes;
/*     */   }
/*     */   
/*     */   public VkSurfaceFormatKHR pickSwapchainSurfaceFormat(VkSurfaceFormatKHR.Buffer formats) {
/* 122 */     for (VkSurfaceFormatKHR format : formats) {
/* 123 */       if (format.colorSpace() != 0)
/*     */         continue; 
/* 126 */       if (format.format() == 37 || format.format() == 44)
/* 127 */         return format; 
/*     */     } 
/* 130 */     throw new IllegalStateException("Could not find compatible swapchain format");
/*     */   }
/*     */   
/*     */   public static void throwIfFailure(int result, String message) throws SurfaceException {
/* 136 */     if (result < 0)
/* 137 */       throw new SurfaceException(VulkanUtils.resultToString(result) + ": " + VulkanUtils.resultToString(result)); 
/*     */   }
/*     */   
/*     */   public void close() {
/* 143 */     destroySwapchain();
/* 144 */     for (int i = 0; i < this.acquireSemaphores.length; i++)
/* 145 */       VK12.vkDestroySemaphore(this.device.vkDevice(), this.acquireSemaphores[i], null); 
/* 147 */     KHRSurface.vkDestroySurfaceKHR(this.device.instance().vkInstance(), this.surface, null);
/*     */   }
/*     */   
/*     */   private void destroySwapchain() {
/* 151 */     if (this.swapchain == 0L)
/*     */       return; 
/* 157 */     this.device.graphicsQueue().waitIdle();
/* 158 */     KHRSwapchain.vkDestroySwapchainKHR(this.device.vkDevice(), this.swapchain, null);
/* 159 */     for (int i = 0; i < this.presentSemaphores.length; i++)
/* 160 */       VK12.vkDestroySemaphore(this.device.vkDevice(), this.presentSemaphores[i], null); 
/* 162 */     this.presentSemaphores = LongArrays.EMPTY_ARRAY;
/* 163 */     this.swapchain = 0L;
/*     */   }
/*     */   
/*     */   public void configure(GpuSurface.Configuration config) throws SurfaceException {
/* 168 */     destroySwapchain();
/*     */     try {
/* 169 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 170 */         VkSurfaceCapabilitiesKHR surfaceCapabilities = VkSurfaceCapabilitiesKHR.calloc(stack);
/* 171 */         throwIfFailure(KHRSurface.vkGetPhysicalDeviceSurfaceCapabilitiesKHR(this.device.vkDevice().getPhysicalDevice(), this.surface, surfaceCapabilities), "Failed to get surface capabilities");
/* 173 */         VkExtent2D minExtent = surfaceCapabilities.minImageExtent();
/* 174 */         VkExtent2D maxExtent = surfaceCapabilities.maxImageExtent();
/* 177 */         if (config.width() < minExtent.width() || maxExtent.width() < config.width() || config.height() < minExtent.height() || maxExtent.height() < config.height())
/* 178 */           throw new SurfaceException(String.format(Locale.ROOT, "Requested swapchain extent (%d x %d) not within allowed extent: min(%d x %d) max(%d x %d)", new Object[] { Integer.valueOf(config.width()), Integer.valueOf(config.height()), Integer.valueOf(minExtent.width()), Integer.valueOf(minExtent.height()), Integer.valueOf(maxExtent.width()), Integer.valueOf(maxExtent.height()) })); 
/* 181 */         VkSwapchainCreateInfoKHR swapchainCreateInfo = VkSwapchainCreateInfoKHR.calloc(stack).sType$Default();
/* 182 */         swapchainCreateInfo.surface(this.surface);
/* 184 */         int currentPresentMode = VulkanConst.toVk(config.presentMode());
/* 187 */         int requestedImageCount = 3;
/* 189 */         swapchainCreateInfo.minImageCount(Math.max(3, surfaceCapabilities.minImageCount()));
/* 190 */         swapchainCreateInfo.imageFormat(this.swapchainImageFormat);
/* 191 */         swapchainCreateInfo.imageColorSpace(0);
/* 192 */         swapchainCreateInfo.imageExtent(VkExtent2D.calloc(stack).set(config.width(), config.height()));
/* 193 */         swapchainCreateInfo.imageArrayLayers(1);
/* 195 */         swapchainCreateInfo.imageUsage(2);
/* 197 */         swapchainCreateInfo.imageSharingMode(0);
/* 198 */         swapchainCreateInfo.preTransform(surfaceCapabilities.currentTransform());
/* 199 */         swapchainCreateInfo.compositeAlpha(1);
/* 200 */         swapchainCreateInfo.presentMode(currentPresentMode);
/* 201 */         swapchainCreateInfo.clipped(true);
/* 203 */         LongBuffer swapchainPtr = stack.callocLong(1);
/* 204 */         throwIfFailure(KHRSwapchain.vkCreateSwapchainKHR(this.device.vkDevice(), swapchainCreateInfo, null, swapchainPtr), "Failed to create Swapchain");
/* 205 */         this.swapchain = swapchainPtr.get(0);
/* 207 */         IntBuffer imageCountPtr = stack.callocInt(1);
/* 208 */         throwIfFailure(KHRSwapchain.vkGetSwapchainImagesKHR(this.device.vkDevice(), this.swapchain, imageCountPtr, null), "Failed to get swapchain image count");
/* 209 */         int swapchainImageCount = imageCountPtr.get(0);
/* 210 */         LongBuffer swapchainImagesPtr = stack.callocLong(swapchainImageCount);
/* 211 */         throwIfFailure(KHRSwapchain.vkGetSwapchainImagesKHR(this.device.vkDevice(), this.swapchain, imageCountPtr, swapchainImagesPtr), "Failed to get swapchain images");
/* 212 */         this.swapchainImages.clear();
/* 213 */         for (int i = 0; i < swapchainImageCount; i++)
/* 214 */           this.swapchainImages.add(swapchainImagesPtr.get(i)); 
/* 225 */         this.presentSemaphores = new long[swapchainImageCount];
/* 226 */         VkSemaphoreCreateInfo semaphoreCreateInfo = VkSemaphoreCreateInfo.calloc(stack).sType$Default();
/* 227 */         LongBuffer semaphoreHandlePtr = stack.callocLong(1);
/* 228 */         for (int j = 0; j < this.presentSemaphores.length; j++) {
/* 229 */           throwIfFailure(VK12.vkCreateSemaphore(this.device.vkDevice(), semaphoreCreateInfo, null, semaphoreHandlePtr), "Failed to create VkSemaphore(binary)");
/* 230 */           this.presentSemaphores[j] = semaphoreHandlePtr.get(0);
/*     */         } 
/* 233 */         this.swapchainSuboptimal = false;
/* 234 */         this.swapchainOutOfDate = false;
/* 235 */         this.swapchainWidth = config.width();
/* 236 */         this.swapchainHeight = config.height();
/* 237 */         if (stack != null)
/* 237 */           stack.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 237 */     } catch (SurfaceException e) {
/* 238 */       this.swapchainSuboptimal = true;
/* 239 */       this.swapchainOutOfDate = true;
/* 240 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isSuboptimal() {
/* 246 */     return this.swapchainSuboptimal;
/*     */   }
/*     */   
/*     */   public void acquireNextTexture() throws SurfaceException {
/* 251 */     if (this.eatenException != null) {
/* 252 */       SurfaceException toThrow = this.eatenException;
/* 253 */       this.eatenException = null;
/* 254 */       throw new SurfaceException(toThrow);
/*     */     } 
/* 256 */     if (this.swapchainOutOfDate)
/* 257 */       throw new IllegalStateException("Attempt to use out of date swapchain"); 
/* 259 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 260 */       IntBuffer frameIndexPtr = stack.callocInt(1);
/* 261 */       frameIndexPtr.put(0, -1);
/* 262 */       this.currentAcquireSemaphore++;
/* 263 */       this.currentAcquireSemaphore %= this.acquireSemaphores.length;
/* 264 */       long acquireSemaphore = this.acquireSemaphores[this.currentAcquireSemaphore];
/* 265 */       int result = KHRSwapchain.vkAcquireNextImageKHR(this.device.vkDevice(), this.swapchain, 5000000000L, acquireSemaphore, 0L, frameIndexPtr);
/* 266 */       if (result == 2) {
/* 267 */         List<CheckpointExtension.QueueCheckpoints> checkpoints = this.device.checkpointExtension().retrieveCheckpoints(false);
/* 268 */         throw new IllegalStateException("GPU timeout attempting to acquire next frame: " + VulkanUtils.formatCheckpoints(checkpoints));
/*     */       } 
/* 270 */       this.currentImageIndex = frameIndexPtr.get(0);
/* 271 */       if (result == -1000001004) {
/* 272 */         this.swapchainSuboptimal = true;
/* 273 */         this.swapchainOutOfDate = true;
/* 274 */         throw new SurfaceException("Failed to acquire image, swapchain out of date");
/*     */       } 
/* 275 */       if (result == 1000001003) {
/* 276 */         this.swapchainSuboptimal = true;
/*     */       } else {
/* 279 */         VulkanUtils.crashIfFailure(this.device, result, "Failed to acquire image");
/*     */       } 
/* 281 */       if (stack != null)
/* 281 */         stack.close(); 
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
/*     */   public void blitFromTexture(CommandEncoderBackend commandEncoder, GpuTextureView textureView) {
/* 290 */     if (this.swapchainOutOfDate)
/* 291 */       throw new IllegalStateException("Attempt to use out of date swapchain"); 
/* 293 */     assert this.currentImageIndex != -1;
/* 295 */     VulkanCommandEncoder vulkanCommandEncoder = (VulkanCommandEncoder)commandEncoder;
/* 296 */     VkCommandBuffer blitCommandBuffer = vulkanCommandEncoder.allocateAndBeginTransientCommandBuffer();
/* 298 */     long swapchainImage = this.swapchainImages.getLong(this.currentImageIndex);
/* 300 */     MemoryStack stack = MemoryStack.stackGet();
/* 301 */     MemoryStack memoryStack1 = stack.push();
/*     */     try {
/* 302 */       VkImageMemoryBarrier2.Buffer imageBarrier = VkImageMemoryBarrier2.calloc(1, stack).sType$Default();
/* 304 */       imageBarrier.srcStageMask(0L);
/* 305 */       imageBarrier.srcAccessMask(0L);
/* 306 */       imageBarrier.dstStageMask(4096L);
/* 307 */       imageBarrier.dstAccessMask(4096L);
/* 308 */       imageBarrier.oldLayout(0);
/* 309 */       imageBarrier.newLayout(7);
/* 310 */       imageBarrier.srcQueueFamilyIndex(-1);
/* 311 */       imageBarrier.dstQueueFamilyIndex(-1);
/* 312 */       imageBarrier.image(swapchainImage);
/* 314 */       VkImageSubresourceRange subresourceRange = imageBarrier.subresourceRange();
/* 315 */       subresourceRange.aspectMask(1);
/* 316 */       subresourceRange.baseMipLevel(0);
/* 317 */       subresourceRange.levelCount(1);
/* 318 */       subresourceRange.baseArrayLayer(0);
/* 319 */       subresourceRange.layerCount(1);
/* 321 */       VkDependencyInfo depinfo = VkDependencyInfo.calloc(stack).sType$Default();
/* 322 */       depinfo.pImageMemoryBarriers(imageBarrier);
/* 324 */       KHRSynchronization2.vkCmdPipelineBarrier2KHR(blitCommandBuffer, depinfo);
/* 325 */       if (memoryStack1 != null)
/* 325 */         memoryStack1.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (memoryStack1 != null)
/*     */         try {
/*     */           memoryStack1.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 326 */     memoryStack1 = stack.push();
/*     */     try {
/* 327 */       int copyWidth = Math.min(this.swapchainWidth, textureView.getWidth(0));
/* 328 */       int copyHeight = Math.min(this.swapchainHeight, textureView.getHeight(0));
/* 330 */       VkOffset3D.Buffer srcOffsets = VkOffset3D.calloc(2, stack);
/* 331 */       srcOffsets.x(0).y(0).z(0);
/* 332 */       srcOffsets.position(1);
/* 333 */       srcOffsets.x(copyWidth).y(copyHeight).z(1);
/* 334 */       srcOffsets.position(0);
/* 336 */       VkOffset3D.Buffer dstOffsets = VkOffset3D.calloc(2, stack);
/* 337 */       dstOffsets.x(0).y(copyHeight).z(0);
/* 338 */       dstOffsets.position(1);
/* 339 */       dstOffsets.x(copyWidth).y(0).z(1);
/* 340 */       dstOffsets.position(0);
/* 342 */       VkImageSubresourceLayers srcSubresource = VkImageSubresourceLayers.calloc(stack);
/* 343 */       srcSubresource.aspectMask(1);
/* 344 */       srcSubresource.mipLevel(textureView.baseMipLevel());
/* 345 */       srcSubresource.baseArrayLayer(0);
/* 346 */       srcSubresource.layerCount(1);
/* 348 */       VkImageSubresourceLayers dstSubresource = VkImageSubresourceLayers.calloc(stack);
/* 349 */       dstSubresource.aspectMask(1);
/* 350 */       dstSubresource.mipLevel(0);
/* 351 */       dstSubresource.baseArrayLayer(0);
/* 352 */       dstSubresource.layerCount(1);
/* 354 */       VkImageBlit.Buffer blitRegion = VkImageBlit.calloc(1, stack);
/* 355 */       blitRegion.srcSubresource(srcSubresource);
/* 356 */       blitRegion.srcOffsets(srcOffsets);
/* 357 */       blitRegion.dstSubresource(dstSubresource);
/* 358 */       blitRegion.dstOffsets(dstOffsets);
/* 360 */       VK12.vkCmdBlitImage(blitCommandBuffer, ((VulkanGpuTexture)textureView.texture()).vkImage(), 1, swapchainImage, 7, blitRegion, 0);
/* 361 */       if (memoryStack1 != null)
/* 361 */         memoryStack1.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (memoryStack1 != null)
/*     */         try {
/*     */           memoryStack1.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 362 */     memoryStack1 = stack.push();
/*     */     try {
/* 363 */       VkImageMemoryBarrier2.Buffer imageBarrier = VkImageMemoryBarrier2.calloc(1, stack).sType$Default();
/* 365 */       imageBarrier.srcStageMask(4096L);
/* 366 */       imageBarrier.srcAccessMask(4096L);
/* 367 */       imageBarrier.dstStageMask(65536L);
/* 368 */       imageBarrier.dstAccessMask(0L);
/* 369 */       imageBarrier.oldLayout(7);
/* 370 */       imageBarrier.newLayout(1000001002);
/* 371 */       imageBarrier.srcQueueFamilyIndex(-1);
/* 372 */       imageBarrier.dstQueueFamilyIndex(-1);
/* 373 */       imageBarrier.image(swapchainImage);
/* 375 */       VkImageSubresourceRange subresourceRange = imageBarrier.subresourceRange();
/* 376 */       subresourceRange.aspectMask(1);
/* 377 */       subresourceRange.baseMipLevel(0);
/* 378 */       subresourceRange.levelCount(1);
/* 379 */       subresourceRange.baseArrayLayer(0);
/* 380 */       subresourceRange.layerCount(1);
/* 382 */       VkMemoryBarrier2.Buffer memoryBarrier = VkMemoryBarrier2.calloc(1, stack).sType$Default();
/* 383 */       memoryBarrier.srcStageMask(4096L);
/* 384 */       memoryBarrier.srcAccessMask(2048L);
/* 385 */       memoryBarrier.dstStageMask(65536L);
/* 386 */       memoryBarrier.dstAccessMask(98304L);
/* 388 */       VkDependencyInfo depinfo = VkDependencyInfo.calloc(stack).sType$Default();
/* 389 */       depinfo.pMemoryBarriers(memoryBarrier);
/* 390 */       depinfo.pImageMemoryBarriers(imageBarrier);
/* 392 */       KHRSynchronization2.vkCmdPipelineBarrier2KHR(blitCommandBuffer, depinfo);
/* 393 */       if (memoryStack1 != null)
/* 393 */         memoryStack1.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (memoryStack1 != null)
/*     */         try {
/*     */           memoryStack1.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 394 */     VulkanUtils.crashIfFailure(this.device, VK12.vkEndCommandBuffer(blitCommandBuffer), "Failed to end VkCommandBuffer");
/* 396 */     vulkanCommandEncoder.waitSemaphore(this.acquireSemaphores[this.currentAcquireSemaphore], 0L, 65536L);
/* 397 */     vulkanCommandEncoder.execute(blitCommandBuffer);
/* 398 */     vulkanCommandEncoder.signalSemaphore(this.presentSemaphores[this.currentImageIndex], 0L, 4096L);
/*     */   }
/*     */   
/*     */   public void present() {
/* 403 */     if (this.swapchainOutOfDate)
/* 404 */       throw new IllegalStateException("Attempt to use out of date swapchain"); 
/* 406 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/* 407 */       VkPresentInfoKHR presentInfo = VkPresentInfoKHR.calloc(stack).sType$Default();
/* 408 */       presentInfo.pWaitSemaphores(stack.longs(this.presentSemaphores[this.currentImageIndex]));
/* 409 */       presentInfo.swapchainCount(1);
/* 410 */       presentInfo.pSwapchains(stack.longs(this.swapchain));
/* 411 */       presentInfo.pImageIndices(stack.ints(this.currentImageIndex));
/* 412 */       this.currentImageIndex = -1;
/* 413 */       int result = KHRSwapchain.vkQueuePresentKHR(this.presentQueue, presentInfo);
/* 414 */       if (result == -1000001004) {
/* 415 */         this.swapchainSuboptimal = true;
/* 416 */         this.swapchainOutOfDate = true;
/* 417 */         this.eatenException = new SurfaceException("Failed to present image, swapchain out of date");
/* 418 */       } else if (result == 1000001003) {
/* 419 */         this.swapchainSuboptimal = true;
/*     */       } else {
/* 422 */         VulkanUtils.crashIfFailure(this.device, result, "Failed to present image");
/*     */       } 
/* 424 */       if (stack != null)
/* 424 */         stack.close(); 
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


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanGpuSurface.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */