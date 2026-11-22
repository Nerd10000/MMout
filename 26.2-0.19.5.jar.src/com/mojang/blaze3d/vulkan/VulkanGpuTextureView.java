/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import java.nio.LongBuffer;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.vulkan.VK12;
/*    */ import org.lwjgl.vulkan.VkImageSubresourceRange;
/*    */ import org.lwjgl.vulkan.VkImageViewCreateInfo;
/*    */ 
/*    */ public class VulkanGpuTextureView extends GpuTextureView implements Destroyable {
/*    */   private final VulkanDevice device;
/*    */   
/*    */   private final long vkImageView;
/*    */   
/*    */   private boolean closed;
/*    */   
/*    */   protected VulkanGpuTextureView(VulkanDevice device, VulkanGpuTexture texture, int baseMipLevel, int mipLevels) {
/* 19 */     super(texture, baseMipLevel, mipLevels);
/* 20 */     this.device = device;
/* 22 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 23 */       boolean isCubemap = ((texture.usage() & 0x10) != 0);
/* 25 */       VkImageViewCreateInfo imageViewCreateInfo = VkImageViewCreateInfo.calloc(stack).sType$Default();
/* 26 */       imageViewCreateInfo.image(texture.vkImage());
/* 27 */       imageViewCreateInfo.viewType(isCubemap ? 3 : 1);
/* 28 */       imageViewCreateInfo.format(VulkanConst.toVk(texture.getFormat()));
/* 29 */       VkImageSubresourceRange subresourceRange = imageViewCreateInfo.subresourceRange();
/* 30 */       subresourceRange.aspectMask(texture.getFormat().hasColorAspect() ? 1 : 2);
/* 31 */       subresourceRange.baseMipLevel(baseMipLevel);
/* 32 */       subresourceRange.levelCount(mipLevels);
/* 33 */       subresourceRange.baseArrayLayer(0);
/* 34 */       subresourceRange.layerCount(isCubemap ? 6 : 1);
/* 36 */       LongBuffer handlePtr = stack.callocLong(1);
/* 37 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateImageView(device.vkDevice(), imageViewCreateInfo, null, handlePtr), "Failed to create VkImageView");
/* 38 */       this.vkImageView = handlePtr.get(0);
/* 40 */       device.instance().debug().setObjectName(device.vkDevice(), 14, this.vkImageView, texture.getLabel());
/* 41 */       if (stack != null)
/* 41 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 43 */     texture.addViews();
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 48 */     VK12.vkDestroyImageView(this.device.vkDevice(), this.vkImageView, null);
/*    */   }
/*    */   
/*    */   public void close() {
/* 53 */     if (!this.closed) {
/* 54 */       this.closed = true;
/* 55 */       this.device.createCommandEncoder().queueForDestroy(this);
/* 56 */       texture().removeViews();
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 62 */     return this.closed;
/*    */   }
/*    */   
/*    */   public VulkanGpuTexture texture() {
/* 67 */     return (VulkanGpuTexture)super.texture();
/*    */   }
/*    */   
/*    */   public long vkImageView() {
/* 71 */     return this.vkImageView;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanGpuTextureView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */