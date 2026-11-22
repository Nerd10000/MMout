/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import java.nio.LongBuffer;
/*     */ import java.util.OptionalDouble;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.vulkan.VK12;
/*     */ import org.lwjgl.vulkan.VkSamplerCreateInfo;
/*     */ 
/*     */ public class VulkanGpuSampler extends GpuSampler implements Destroyable {
/*     */   private final long vkSampler;
/*     */   
/*     */   private final VulkanDevice device;
/*     */   
/*     */   private final AddressMode addressModeU;
/*     */   
/*     */   private final AddressMode addressModeV;
/*     */   
/*     */   private final FilterMode minFilter;
/*     */   
/*     */   private final FilterMode magFilter;
/*     */   
/*     */   private final int maxAnisotropy;
/*     */   
/*     */   private final OptionalDouble maxLod;
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   public VulkanGpuSampler(VulkanDevice device, AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, int maxAnisotropy, OptionalDouble maxLod) {
/*  27 */     this.device = device;
/*  28 */     this.addressModeU = addressModeU;
/*  29 */     this.addressModeV = addressModeV;
/*  30 */     this.minFilter = minFilter;
/*  31 */     this.magFilter = magFilter;
/*  32 */     this.maxAnisotropy = maxAnisotropy;
/*  33 */     this.maxLod = maxLod;
/*  35 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  36 */       VkSamplerCreateInfo createInfo = VkSamplerCreateInfo.calloc(stack).sType$Default();
/*  37 */       createInfo.magFilter(VulkanConst.toVk(magFilter));
/*  38 */       createInfo.minFilter(VulkanConst.toVk(minFilter));
/*  39 */       createInfo.mipmapMode((maxLod.orElse(1000.0D) > 0.25D) ? 1 : 0);
/*  40 */       createInfo.addressModeU(VulkanConst.toVk(addressModeU));
/*  41 */       createInfo.addressModeV(VulkanConst.toVk(addressModeV));
/*  42 */       createInfo.mipLodBias(0.0F);
/*  48 */       createInfo.maxLod(Math.max(0.25F, (float)maxLod.orElse(1000.0D)));
/*  49 */       createInfo.anisotropyEnable((maxAnisotropy > 1));
/*  50 */       createInfo.maxAnisotropy(maxAnisotropy);
/*  51 */       LongBuffer pointer = stack.callocLong(1);
/*  52 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateSampler(device.vkDevice(), createInfo, null, pointer), "Can't create sampler");
/*  53 */       this.vkSampler = pointer.get(0);
/*  54 */       if (stack != null)
/*  54 */         stack.close(); 
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
/*     */   public void destroy() {
/*  59 */     VK12.vkDestroySampler(this.device.vkDevice(), this.vkSampler, null);
/*     */   }
/*     */   
/*     */   public void close() {
/*  64 */     if (this.closed)
/*     */       return; 
/*  67 */     this.closed = true;
/*  68 */     this.device.createCommandEncoder().queueForDestroy(this);
/*     */   }
/*     */   
/*     */   public AddressMode getAddressModeU() {
/*  73 */     return this.addressModeU;
/*     */   }
/*     */   
/*     */   public AddressMode getAddressModeV() {
/*  78 */     return this.addressModeV;
/*     */   }
/*     */   
/*     */   public FilterMode getMinFilter() {
/*  83 */     return this.minFilter;
/*     */   }
/*     */   
/*     */   public FilterMode getMagFilter() {
/*  88 */     return this.magFilter;
/*     */   }
/*     */   
/*     */   public int getMaxAnisotropy() {
/*  93 */     return this.maxAnisotropy;
/*     */   }
/*     */   
/*     */   public OptionalDouble getMaxLod() {
/*  98 */     return this.maxLod;
/*     */   }
/*     */   
/*     */   public long vkSampler() {
/* 102 */     return this.vkSampler;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanGpuSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */