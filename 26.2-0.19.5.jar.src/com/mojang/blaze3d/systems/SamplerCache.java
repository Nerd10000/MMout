/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import java.util.OptionalDouble;
/*    */ 
/*    */ public class SamplerCache {
/* 12 */   private final GpuSampler[] samplers = new GpuSampler[32];
/*    */   
/*    */   public void initialize() {
/* 15 */     GpuDevice device = RenderSystem.getDevice();
/* 16 */     if ((AddressMode.values()).length != 2 || (FilterMode.values()).length != 2)
/* 17 */       throw new IllegalStateException("AddressMode and FilterMode enum sizes must be 2 - if you expanded them, please update SamplerCache"); 
/* 19 */     for (AddressMode addressModeU : AddressMode.values()) {
/* 20 */       for (AddressMode addressModeV : AddressMode.values()) {
/* 21 */         for (FilterMode minFilter : FilterMode.values()) {
/* 22 */           for (FilterMode magFilter : FilterMode.values()) {
/* 23 */             for (boolean useMipmaps : new boolean[] { true, false })
/* 24 */               this.samplers[encode(addressModeU, addressModeV, minFilter, magFilter, useMipmaps)] = device.createSampler(addressModeU, addressModeV, minFilter, magFilter, 1, useMipmaps ? OptionalDouble.empty() : OptionalDouble.of(0.0D)); 
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public GpuSampler getSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, boolean useMipmaps) {
/* 39 */     return this.samplers[encode(addressModeU, addressModeV, minFilter, magFilter, useMipmaps)];
/*    */   }
/*    */   
/*    */   public GpuSampler getClampToEdge(FilterMode minMag) {
/* 43 */     return getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, minMag, minMag, false);
/*    */   }
/*    */   
/*    */   public GpuSampler getClampToEdge(FilterMode minMag, boolean mipmaps) {
/* 47 */     return getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, minMag, minMag, mipmaps);
/*    */   }
/*    */   
/*    */   public GpuSampler getRepeat(FilterMode minMag) {
/* 51 */     return getSampler(AddressMode.REPEAT, AddressMode.REPEAT, minMag, minMag, false);
/*    */   }
/*    */   
/*    */   public GpuSampler getRepeat(FilterMode minMag, boolean mipmaps) {
/* 55 */     return getSampler(AddressMode.REPEAT, AddressMode.REPEAT, minMag, minMag, mipmaps);
/*    */   }
/*    */   
/*    */   public void close() {
/* 59 */     for (GpuSampler sampler : this.samplers)
/* 60 */       sampler.close(); 
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   static int encode(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, boolean useMipmaps) {
/* 73 */     int result = 0;
/* 74 */     result |= addressModeU.ordinal() & 0x1;
/* 75 */     result |= (addressModeV.ordinal() & 0x1) << 1;
/* 76 */     result |= (minFilter.ordinal() & 0x1) << 2;
/* 77 */     result |= (magFilter.ordinal() & 0x1) << 3;
/* 78 */     if (useMipmaps)
/* 79 */       result |= 0x10; 
/* 81 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\SamplerCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */