/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import java.nio.LongBuffer;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ import org.lwjgl.util.vma.VmaAllocationCreateInfo;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkImageCreateInfo;
/*     */ import org.lwjgl.vulkan.VkImageMemoryBarrier;
/*     */ import org.lwjgl.vulkan.VkImageSubresourceRange;
/*     */ 
/*     */ public class VulkanGpuTexture extends GpuTexture implements Destroyable {
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final long vkImage;
/*     */   
/*     */   private final long vmaAllocation;
/*     */   
/*     */   private boolean closed = false;
/*     */   
/*  21 */   private int views = 0;
/*     */   
/*     */   public VulkanGpuTexture(VulkanDevice device, int usage, String label, GpuFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/*  24 */     super(usage, label, format, width, height, depthOrLayers, mipLevels);
/*  25 */     this.device = device;
/*  27 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  28 */       VkImageCreateInfo imageCreateInfo = VkImageCreateInfo.calloc(stack).sType$Default();
/*  29 */       imageCreateInfo.imageType(1);
/*  30 */       imageCreateInfo.extent().set(width, height, 1);
/*  31 */       imageCreateInfo.mipLevels(mipLevels);
/*  32 */       imageCreateInfo.arrayLayers(depthOrLayers);
/*  34 */       imageCreateInfo.format(VulkanConst.toVk(format));
/*  35 */       imageCreateInfo.tiling(0);
/*  36 */       imageCreateInfo.initialLayout(0);
/*  37 */       imageCreateInfo.usage(VulkanConst.textureUsageToVk(usage, format));
/*  38 */       imageCreateInfo.sharingMode(0);
/*  39 */       imageCreateInfo.samples(1);
/*  40 */       imageCreateInfo.flags(VulkanUtils.hasAnyBit(usage, 16) ? 16 : 0);
/*  42 */       VmaAllocationCreateInfo allocationCreateInfo = VmaAllocationCreateInfo.calloc(stack);
/*  43 */       allocationCreateInfo.usage(8);
/*  45 */       LongBuffer imageHandlePtr = stack.callocLong(1);
/*  46 */       PointerBuffer allocationHandlePtr = stack.callocPointer(1);
/*  47 */       VulkanUtils.crashIfFailure(device, Vma.vmaCreateImage(device.vma(), imageCreateInfo, allocationCreateInfo, imageHandlePtr, allocationHandlePtr, null), "Failed to create image");
/*  48 */       this.vkImage = imageHandlePtr.get(0);
/*  49 */       this.vmaAllocation = allocationHandlePtr.get(0);
/*  51 */       VkImageMemoryBarrier.Buffer barrier = VkImageMemoryBarrier.calloc(1, stack).sType$Default();
/*  52 */       barrier.oldLayout(0);
/*  53 */       barrier.newLayout(1);
/*  54 */       barrier.srcAccessMask(0);
/*  55 */       barrier.dstAccessMask(98304);
/*  56 */       barrier.srcQueueFamilyIndex(-1);
/*  57 */       barrier.dstQueueFamilyIndex(-1);
/*  58 */       barrier.image(this.vkImage);
/*  59 */       VkImageSubresourceRange subresourceRange = barrier.subresourceRange();
/*  60 */       subresourceRange.aspectMask(getFormat().hasColorAspect() ? 1 : 2);
/*  61 */       subresourceRange.baseMipLevel(0);
/*  62 */       subresourceRange.levelCount(getMipLevels());
/*  63 */       subresourceRange.baseArrayLayer(0);
/*  64 */       subresourceRange.layerCount(depthOrLayers);
/*  66 */       VK12.vkCmdPipelineBarrier(device.createCommandEncoder().textureInitCommandBuffer(), 1, 65536, 0, null, null, barrier);
/*  68 */       device.instance().debug().setObjectName(device.vkDevice(), 10, this.vkImage, label);
/*  69 */       if (stack != null)
/*  69 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*  71 */     addViews();
/*     */   }
/*     */   
/*     */   public void destroy() {
/*  76 */     Vma.vmaDestroyImage(this.device.vma(), this.vkImage, this.vmaAllocation);
/*     */   }
/*     */   
/*     */   public void close() {
/*  81 */     if (this.closed)
/*     */       return; 
/*  84 */     this.closed = true;
/*  85 */     removeViews();
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/*  90 */     return this.closed;
/*     */   }
/*     */   
/*     */   public void addViews() {
/*  94 */     this.views++;
/*     */   }
/*     */   
/*     */   public void removeViews() {
/*  98 */     this.views--;
/* 100 */     if (this.views < 0)
/* 101 */       throw new IllegalStateException("Too many views removed from texture"); 
/* 104 */     if (this.closed && this.views == 0)
/* 105 */       this.device.createCommandEncoder().queueForDestroy(this); 
/*     */   }
/*     */   
/*     */   public long vkImage() {
/* 110 */     return this.vkImage;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanGpuTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */