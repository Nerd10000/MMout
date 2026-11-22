/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ class AnimatedTexture {
/*     */   private final List<SpriteContents.FrameInfo> frames;
/*     */   
/*     */   private final IntList uniqueFrames;
/*     */   
/*     */   private final int frameRowSize;
/*     */   
/*     */   private final boolean interpolateFrames;
/*     */   
/*     */   private AnimatedTexture(List<SpriteContents.FrameInfo> frames, int frameRowSize, boolean interpolateFrames) {
/* 274 */     this.frames = frames;
/* 275 */     this.frameRowSize = frameRowSize;
/* 276 */     this.interpolateFrames = interpolateFrames;
/* 277 */     this.uniqueFrames = (IntList)IntArrayList.toList(frames.stream().mapToInt(SpriteContents.FrameInfo::index).distinct());
/*     */   }
/*     */   
/*     */   private int getFrameX(int index) {
/* 286 */     return index % this.frameRowSize;
/*     */   }
/*     */   
/*     */   private int getFrameY(int index) {
/* 290 */     return index / this.frameRowSize;
/*     */   }
/*     */   
/*     */   public SpriteContents.AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/* 294 */     GpuDevice device = RenderSystem.getDevice();
/* 295 */     Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 296 */     GpuBufferSlice[] spriteUbosByMip = new GpuBufferSlice[SpriteContents.this.byMipLevel.length];
/* 298 */     CommandEncoder encoder = device.createCommandEncoder();
/* 299 */     List<GpuBufferSlice> stagingBuffers = encoder.transientMemory().multiUploadStaging(Arrays.<NativeImage>stream(SpriteContents.this.byMipLevel).map(NativeImage::getPixelBytes).toList(), 1L, 16);
/* 300 */     for (int i = 0; i < this.uniqueFrames.size(); i++) {
/* 301 */       int frame = this.uniqueFrames.getInt(i);
/* 302 */       GpuTexture texture = device.createTexture(() -> String.valueOf(SpriteContents.this.name) + " animation frame " + String.valueOf(SpriteContents.this.name), 5, GpuFormat.RGBA8_UNORM, SpriteContents.this.width, SpriteContents.this.height, 1, SpriteContents.this.byMipLevel.length);
/* 303 */       int offsetX = getFrameX(frame) * SpriteContents.this.width;
/* 304 */       int offsetY = getFrameY(frame) * SpriteContents.this.height;
/* 305 */       for (int j = 0; j < SpriteContents.this.byMipLevel.length; j++)
/* 306 */         encoder.copyBufferToTexture(stagingBuffers.get(j), offsetX >> j, offsetY >> j, SpriteContents.this.byMipLevel[j].getWidth(), SpriteContents.this.byMipLevel[j].getHeight(), texture, 0, 0, SpriteContents.this.width >> j, SpriteContents.this.height >> j, j, 0); 
/* 308 */       int2ObjectOpenHashMap.put(frame, RenderSystem.getDevice().createTextureView(texture));
/*     */     } 
/* 311 */     for (int level = 0; level < SpriteContents.this.byMipLevel.length; level++)
/* 312 */       spriteUbosByMip[level] = uboSlice.slice((level * spriteUboSize), spriteUboSize); 
/* 315 */     return new SpriteContents.AnimationState(SpriteContents.this, this, (Int2ObjectMap<GpuTextureView>)int2ObjectOpenHashMap, spriteUbosByMip);
/*     */   }
/*     */   
/*     */   public IntList getUniqueFrames() {
/* 319 */     return this.uniqueFrames;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\SpriteContents$AnimatedTexture.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */