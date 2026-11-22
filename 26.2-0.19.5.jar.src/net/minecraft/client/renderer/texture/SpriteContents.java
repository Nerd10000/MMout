/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.platform.Transparency;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.metadata.animation.AnimationFrame;
/*     */ import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
/*     */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*     */ import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SpriteContents implements AutoCloseable, Stitcher.Entry {
/*  49 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  51 */   public static final int UBO_SIZE = (new Std140SizeCalculator())
/*  52 */     .putMat4f()
/*  53 */     .putMat4f()
/*  54 */     .putFloat()
/*  55 */     .putFloat()
/*  56 */     .putInt()
/*  57 */     .get();
/*     */   
/*     */   private final Identifier name;
/*     */   
/*     */   private final int width;
/*     */   
/*     */   private final int height;
/*     */   
/*     */   private final NativeImage originalImage;
/*     */   
/*     */   private NativeImage[] byMipLevel;
/*     */   
/*     */   private final AnimatedTexture animatedTexture;
/*     */   
/*     */   private final List<MetadataSectionType.WithValue<?>> additionalMetadata;
/*     */   
/*     */   private final MipmapStrategy mipmapStrategy;
/*     */   
/*     */   private final float alphaCutoffBias;
/*     */   
/*     */   private final Transparency transparency;
/*     */   
/*     */   public SpriteContents(Identifier name, FrameSize frameSize, NativeImage image) {
/*  72 */     this(name, frameSize, image, Optional.empty(), List.of(), Optional.empty());
/*     */   }
/*     */   
/*     */   public SpriteContents(Identifier name, FrameSize frameSize, NativeImage image, Optional<AnimationMetadataSection> animationInfo, List<MetadataSectionType.WithValue<?>> additionalMetadata, Optional<TextureMetadataSection> textureInfo) {
/*  76 */     this.name = name;
/*  77 */     this.width = frameSize.width();
/*  78 */     this.height = frameSize.height();
/*  79 */     this.additionalMetadata = additionalMetadata;
/*  81 */     this
/*     */       
/*  83 */       .animatedTexture = animationInfo.<AnimatedTexture>map(animation -> createAnimatedTexture(frameSize, image.getWidth(), image.getHeight(), animation)).orElse(null);
/*  84 */     this.originalImage = image;
/*  85 */     this.byMipLevel = new NativeImage[] { this.originalImage };
/*  86 */     this.mipmapStrategy = textureInfo.<MipmapStrategy>map(TextureMetadataSection::mipmapStrategy).orElse(MipmapStrategy.AUTO);
/*  87 */     this.alphaCutoffBias = ((Float)textureInfo.<Float>map(TextureMetadataSection::alphaCutoffBias).orElse(Float.valueOf(0.0F))).floatValue();
/*  89 */     this.transparency = image.computeTransparency();
/*     */   }
/*     */   
/*     */   public void increaseMipLevel(int mipLevel) {
/*     */     try {
/*  94 */       this.byMipLevel = MipmapGenerator.generateMipLevels(this.name, this.byMipLevel, mipLevel, this.mipmapStrategy, this.alphaCutoffBias, this.transparency);
/*  95 */     } catch (Throwable t) {
/*  96 */       CrashReport report = CrashReport.forThrowable(t, "Generating mipmaps for frame");
/*  98 */       CrashReportCategory frameCategory = report.addCategory("Frame being iterated");
/*  99 */       frameCategory.setDetail("Sprite name", this.name);
/* 100 */       frameCategory.setDetail("Sprite size", () -> "" + this.width + " x " + this.width);
/* 101 */       frameCategory.setDetail("Sprite frames", () -> "" + getFrameCount() + " frames");
/* 102 */       frameCategory.setDetail("Mipmap levels", Integer.valueOf(mipLevel));
/* 103 */       frameCategory.setDetail("Original image size", () -> "" + this.originalImage.getWidth() + "x" + this.originalImage.getWidth());
/* 105 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getFrameCount() {
/* 110 */     return (this.animatedTexture != null) ? this.animatedTexture.frames.size() : 1;
/*     */   }
/*     */   
/*     */   public boolean isAnimated() {
/* 114 */     return (getFrameCount() > 1);
/*     */   }
/*     */   
/*     */   public Transparency transparency() {
/* 118 */     return this.transparency;
/*     */   }
/*     */   
/*     */   private AnimatedTexture createAnimatedTexture(FrameSize frameSize, int fullWidth, int fullHeight, AnimationMetadataSection metadata) {
/*     */     List<FrameInfo> frames;
/* 122 */     int frameRowSize = fullWidth / frameSize.width();
/* 123 */     int frameColumnSize = fullHeight / frameSize.height();
/* 124 */     int totalFrameCount = frameRowSize * frameColumnSize;
/* 126 */     int defaultFrameTime = metadata.defaultFrameTime();
/* 129 */     if (metadata.frames().isEmpty()) {
/* 130 */       frames = new ArrayList<>(totalFrameCount);
/* 132 */       for (int i = 0; i < totalFrameCount; i++)
/* 133 */         frames.add(new FrameInfo(i, defaultFrameTime)); 
/*     */     } else {
/* 136 */       List<AnimationFrame> metadataFrames = metadata.frames().get();
/* 137 */       frames = new ArrayList<>(metadataFrames.size());
/* 139 */       for (AnimationFrame frame : metadataFrames)
/* 140 */         frames.add(new FrameInfo(frame.index(), frame.timeOr(defaultFrameTime))); 
/* 143 */       int index = 0;
/* 144 */       IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
/* 146 */       Iterator<FrameInfo> iterator = frames.iterator();
/* 147 */       while (iterator.hasNext()) {
/* 148 */         FrameInfo frame = iterator.next();
/* 149 */         boolean isValid = true;
/* 150 */         if (frame.time <= 0) {
/* 151 */           LOGGER.warn("Invalid frame duration on sprite {} frame {}: {}", new Object[] { this.name, Integer.valueOf(index), Integer.valueOf(frame.time) });
/* 152 */           isValid = false;
/*     */         } 
/* 154 */         if (frame.index < 0 || frame.index >= totalFrameCount) {
/* 155 */           LOGGER.warn("Invalid frame index on sprite {} frame {}: {}", new Object[] { this.name, Integer.valueOf(index), Integer.valueOf(frame.index) });
/* 156 */           isValid = false;
/*     */         } 
/* 159 */         if (isValid) {
/* 160 */           intOpenHashSet.add(frame.index);
/*     */         } else {
/* 162 */           iterator.remove();
/*     */         } 
/* 164 */         index++;
/*     */       } 
/* 167 */       int[] unusedFrameIndices = IntStream.range(0, totalFrameCount).filter(i -> !usedFrameIndices.contains(i)).toArray();
/* 168 */       if (unusedFrameIndices.length > 0)
/* 169 */         LOGGER.warn("Unused frames in sprite {}: {}", this.name, Arrays.toString(unusedFrameIndices)); 
/*     */     } 
/* 173 */     if (frames.size() <= 1)
/* 174 */       return null; 
/* 177 */     return new AnimatedTexture(List.copyOf(frames), frameRowSize, metadata.interpolatedFrames());
/*     */   }
/*     */   
/*     */   public int width() {
/* 182 */     return this.width;
/*     */   }
/*     */   
/*     */   public int height() {
/* 187 */     return this.height;
/*     */   }
/*     */   
/*     */   public Identifier name() {
/* 192 */     return this.name;
/*     */   }
/*     */   
/*     */   public IntList getUniqueFrames() {
/* 196 */     return (this.animatedTexture != null) ? this.animatedTexture.getUniqueFrames() : IntList.of(1);
/*     */   }
/*     */   
/*     */   public AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/* 200 */     return (this.animatedTexture != null) ? this.animatedTexture.createAnimationState(uboSlice, spriteUboSize) : null;
/*     */   }
/*     */   
/*     */   public <T> Optional<T> getAdditionalMetadata(MetadataSectionType<T> type) {
/* 204 */     for (MetadataSectionType.WithValue<?> metadata : this.additionalMetadata) {
/* 205 */       Optional<T> result = metadata.unwrapToType(type);
/* 206 */       if (result.isPresent())
/* 207 */         return result; 
/*     */     } 
/* 210 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void close() {
/* 215 */     for (NativeImage image : this.byMipLevel)
/* 216 */       image.close(); 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 222 */     return "SpriteContents{name=" + String.valueOf(this.name) + ", frameCount=" + getFrameCount() + ", height=" + this.height + ", width=" + this.width + "}";
/*     */   }
/*     */   
/*     */   public boolean isTransparent(int frame, int x, int y) {
/* 226 */     int actualX = x;
/* 227 */     int actualY = y;
/* 228 */     if (this.animatedTexture != null) {
/* 229 */       actualX += this.animatedTexture.getFrameX(frame) * this.width;
/* 230 */       actualY += this.animatedTexture.getFrameY(frame) * this.height;
/*     */     } 
/* 232 */     return (ARGB.alpha(this.originalImage.getPixel(actualX, actualY)) == 0);
/*     */   }
/*     */   
/*     */   public Transparency computeTransparency(float u0, float v0, float u1, float v1) {
/* 236 */     if (this.transparency.isOpaque())
/* 237 */       return this.transparency; 
/* 238 */     if (u0 == 0.0F && v0 == 0.0F && u1 == 1.0F && v1 == 1.0F)
/* 239 */       return this.transparency; 
/* 242 */     int x0 = Mth.floor(u0 * this.width);
/* 243 */     int y0 = Mth.floor(v0 * this.height);
/* 244 */     int x1 = Mth.ceil(u1 * this.width);
/* 245 */     int y1 = Mth.ceil(v1 * this.height);
/* 246 */     if (this.animatedTexture != null) {
/* 247 */       IntList uniqueFrames = this.animatedTexture.uniqueFrames;
/* 248 */       Transparency transparency = Transparency.NONE;
/* 249 */       for (int i = 0; i < uniqueFrames.size(); i++) {
/* 250 */         int frame = uniqueFrames.getInt(i);
/* 251 */         int frameX = this.animatedTexture.getFrameX(frame) * this.width;
/* 252 */         int frameY = this.animatedTexture.getFrameY(frame) * this.height;
/* 253 */         transparency = transparency.or(this.originalImage.computeTransparency(frameX + x0, frameY + y0, frameX + x1, frameY + y1));
/*     */       } 
/* 255 */       return transparency;
/*     */     } 
/* 257 */     return this.originalImage.computeTransparency(x0, y0, x1, y1);
/*     */   }
/*     */   
/*     */   public void uploadFirstFrame(GpuTexture destination, int level) {
/* 262 */     RenderSystem.getDevice().createCommandEncoder().writeToTexture(destination, this.byMipLevel[level], level, 0, 0, 0);
/*     */   }
/*     */   
/*     */   private static final class FrameInfo extends Record {
/*     */     private final int index;
/*     */     
/*     */     private final int time;
/*     */     
/*     */     private FrameInfo(int index, int time) {
/* 265 */       this.index = index;
/* 265 */       this.time = time;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #265	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #265	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #265	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/texture/SpriteContents$FrameInfo;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int index() {
/* 265 */       return this.index;
/*     */     }
/*     */     
/*     */     public int time() {
/* 265 */       return this.time;
/*     */     }
/*     */   }
/*     */   
/*     */   private class AnimatedTexture {
/*     */     private final List<SpriteContents.FrameInfo> frames;
/*     */     
/*     */     private final IntList uniqueFrames;
/*     */     
/*     */     private final int frameRowSize;
/*     */     
/*     */     private final boolean interpolateFrames;
/*     */     
/*     */     private AnimatedTexture(List<SpriteContents.FrameInfo> frames, int frameRowSize, boolean interpolateFrames) {
/* 274 */       this.frames = frames;
/* 275 */       this.frameRowSize = frameRowSize;
/* 276 */       this.interpolateFrames = interpolateFrames;
/* 277 */       this.uniqueFrames = (IntList)IntArrayList.toList(frames.stream().mapToInt(SpriteContents.FrameInfo::index).distinct());
/*     */     }
/*     */     
/*     */     private int getFrameX(int index) {
/* 286 */       return index % this.frameRowSize;
/*     */     }
/*     */     
/*     */     private int getFrameY(int index) {
/* 290 */       return index / this.frameRowSize;
/*     */     }
/*     */     
/*     */     public SpriteContents.AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/* 294 */       GpuDevice device = RenderSystem.getDevice();
/* 295 */       Int2ObjectOpenHashMap int2ObjectOpenHashMap = new Int2ObjectOpenHashMap();
/* 296 */       GpuBufferSlice[] spriteUbosByMip = new GpuBufferSlice[SpriteContents.this.byMipLevel.length];
/* 298 */       CommandEncoder encoder = device.createCommandEncoder();
/* 299 */       List<GpuBufferSlice> stagingBuffers = encoder.transientMemory().multiUploadStaging(Arrays.<NativeImage>stream(SpriteContents.this.byMipLevel).map(NativeImage::getPixelBytes).toList(), 1L, 16);
/* 300 */       for (int i = 0; i < this.uniqueFrames.size(); i++) {
/* 301 */         int frame = this.uniqueFrames.getInt(i);
/* 302 */         GpuTexture texture = device.createTexture(() -> String.valueOf(SpriteContents.this.name) + " animation frame " + String.valueOf(SpriteContents.this.name), 5, GpuFormat.RGBA8_UNORM, SpriteContents.this.width, SpriteContents.this.height, 1, SpriteContents.this.byMipLevel.length);
/* 303 */         int offsetX = getFrameX(frame) * SpriteContents.this.width;
/* 304 */         int offsetY = getFrameY(frame) * SpriteContents.this.height;
/* 305 */         for (int j = 0; j < SpriteContents.this.byMipLevel.length; j++)
/* 306 */           encoder.copyBufferToTexture(stagingBuffers.get(j), offsetX >> j, offsetY >> j, SpriteContents.this.byMipLevel[j].getWidth(), SpriteContents.this.byMipLevel[j].getHeight(), texture, 0, 0, SpriteContents.this.width >> j, SpriteContents.this.height >> j, j, 0); 
/* 308 */         int2ObjectOpenHashMap.put(frame, RenderSystem.getDevice().createTextureView(texture));
/*     */       } 
/* 311 */       for (int level = 0; level < SpriteContents.this.byMipLevel.length; level++)
/* 312 */         spriteUbosByMip[level] = uboSlice.slice((level * spriteUboSize), spriteUboSize); 
/* 315 */       return new SpriteContents.AnimationState(SpriteContents.this, this, (Int2ObjectMap<GpuTextureView>)int2ObjectOpenHashMap, spriteUbosByMip);
/*     */     }
/*     */     
/*     */     public IntList getUniqueFrames() {
/* 319 */       return this.uniqueFrames;
/*     */     }
/*     */   }
/*     */   
/*     */   public class AnimationState implements AutoCloseable {
/*     */     private int frame;
/*     */     
/*     */     private int subFrame;
/*     */     
/*     */     private final SpriteContents.AnimatedTexture animationInfo;
/*     */     
/*     */     private final Int2ObjectMap<GpuTextureView> frameTexturesByIndex;
/*     */     
/*     */     private final GpuBufferSlice[] spriteUbosByMip;
/*     */     
/*     */     private boolean isDirty = true;
/*     */     
/*     */     private AnimationState(SpriteContents this$0, SpriteContents.AnimatedTexture animationInfo, Int2ObjectMap<GpuTextureView> frameTexturesByIndex, GpuBufferSlice[] spriteUbosByMip) {
/* 333 */       this.animationInfo = animationInfo;
/* 334 */       this.frameTexturesByIndex = frameTexturesByIndex;
/* 335 */       this.spriteUbosByMip = spriteUbosByMip;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 339 */       this.subFrame++;
/* 340 */       this.isDirty = false;
/* 341 */       SpriteContents.FrameInfo currentFrame = this.animationInfo.frames.get(this.frame);
/* 342 */       if (this.subFrame >= currentFrame.time) {
/* 343 */         int oldFrame = currentFrame.index;
/* 344 */         this.frame = (this.frame + 1) % this.animationInfo.frames.size();
/* 345 */         this.subFrame = 0;
/* 347 */         int newFrame = ((SpriteContents.FrameInfo)this.animationInfo.frames.get(this.frame)).index;
/* 348 */         if (oldFrame != newFrame)
/* 349 */           this.isDirty = true; 
/*     */       } 
/*     */     }
/*     */     
/*     */     public GpuBufferSlice getDrawUbo(int level) {
/* 355 */       return this.spriteUbosByMip[level];
/*     */     }
/*     */     
/*     */     public boolean needsToDraw() {
/* 359 */       return (this.animationInfo.interpolateFrames || this.isDirty);
/*     */     }
/*     */     
/*     */     public void drawToAtlas(RenderPass renderPass, GpuBufferSlice ubo) {
/* 363 */       GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
/* 364 */       List<SpriteContents.FrameInfo> frames = this.animationInfo.frames;
/* 365 */       int oldFrame = ((SpriteContents.FrameInfo)frames.get(this.frame)).index;
/* 366 */       float frameProgress = this.subFrame / ((SpriteContents.FrameInfo)this.animationInfo.frames.get(this.frame)).time;
/* 367 */       int frameProgressAsInt = (int)(frameProgress * 1000.0F);
/* 369 */       if (this.animationInfo.interpolateFrames) {
/* 370 */         int newFrame = ((SpriteContents.FrameInfo)frames.get((this.frame + 1) % frames.size())).index;
/* 372 */         renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_INTERPOLATE);
/* 373 */         renderPass.bindTexture("CurrentSprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
/* 374 */         renderPass.bindTexture("NextSprite", (GpuTextureView)this.frameTexturesByIndex.get(newFrame), sampler);
/* 375 */       } else if (this.isDirty) {
/* 376 */         renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
/* 377 */         renderPass.bindTexture("Sprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
/*     */       } 
/* 380 */       renderPass.setUniform("SpriteAnimationInfo", ubo);
/* 382 */       renderPass.draw(6, 1, frameProgressAsInt << 3, 0);
/*     */     }
/*     */     
/*     */     public void close() {
/* 387 */       for (ObjectIterator<GpuTextureView> objectIterator = this.frameTexturesByIndex.values().iterator(); objectIterator.hasNext(); ) {
/* 387 */         GpuTextureView view = objectIterator.next();
/* 388 */         view.texture().close();
/* 389 */         view.close();
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\SpriteContents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */