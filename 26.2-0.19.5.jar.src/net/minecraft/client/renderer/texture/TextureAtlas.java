/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.platform.TextureUtil;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TextureAtlas extends AbstractTexture implements TickableTexture, Dumpable {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   @Deprecated
/*  40 */   public static final Identifier LOCATION_BLOCKS = Identifier.withDefaultNamespace("textures/atlas/blocks.png");
/*     */   
/*     */   @Deprecated
/*  43 */   public static final Identifier LOCATION_ITEMS = Identifier.withDefaultNamespace("textures/atlas/items.png");
/*     */   
/*     */   @Deprecated
/*  46 */   public static final Identifier LOCATION_PARTICLES = Identifier.withDefaultNamespace("textures/atlas/particles.png");
/*     */   
/*  49 */   private List<TextureAtlasSprite> sprites = List.of();
/*     */   
/*  50 */   private List<SpriteContents.AnimationState> animatedTexturesStates = List.of();
/*     */   
/*  51 */   private Map<Identifier, TextureAtlasSprite> texturesByName = Map.of();
/*     */   
/*     */   private TextureAtlasSprite missingSprite;
/*     */   
/*     */   private final Identifier location;
/*     */   
/*     */   private final int maxSupportedTextureSize;
/*     */   
/*     */   private int width;
/*     */   
/*     */   private int height;
/*     */   
/*     */   private int maxMipLevel;
/*     */   
/*     */   private int mipLevelCount;
/*     */   
/*  60 */   private GpuTextureView[] mipViews = new GpuTextureView[0];
/*     */   
/*     */   private GpuBuffer spriteUbos;
/*     */   
/*     */   public TextureAtlas(Identifier location) {
/*  64 */     this.location = location;
/*  65 */     this.maxSupportedTextureSize = RenderSystem.getDevice().getDeviceInfo().limits().maxTextureSizeForFormat(GpuFormat.RGBA8_UNORM);
/*     */   }
/*     */   
/*     */   private void createTexture(int newWidth, int newHeight, int newMipLevel) {
/*  70 */     LOGGER.info("Created: {}x{}x{} {}-atlas", new Object[] { Integer.valueOf(newWidth), Integer.valueOf(newHeight), Integer.valueOf(newMipLevel), this.location });
/*  72 */     GpuDevice device = RenderSystem.getDevice();
/*  73 */     releaseTextures();
/*  74 */     Objects.requireNonNull(this.location);
/*  74 */     this.texture = device.createTexture(this.location::toString, 15, GpuFormat.RGBA8_UNORM, newWidth, newHeight, 1, newMipLevel + 1);
/*  75 */     this.textureView = device.createTextureView(this.texture);
/*  76 */     this.width = newWidth;
/*  77 */     this.height = newHeight;
/*  78 */     this.maxMipLevel = newMipLevel;
/*  79 */     this.mipLevelCount = newMipLevel + 1;
/*  80 */     this.mipViews = new GpuTextureView[this.mipLevelCount];
/*  81 */     for (int level = 0; level <= this.maxMipLevel; level++)
/*  82 */       this.mipViews[level] = device.createTextureView(this.texture, level, 1); 
/*     */   }
/*     */   
/*     */   public void upload(SpriteLoader.Preparations preparations) {
/*  87 */     createTexture(preparations.width(), preparations.height(), preparations.mipLevel());
/*  88 */     clearTextureData();
/*  90 */     this.sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST);
/*  91 */     this.texturesByName = Map.copyOf(preparations.regions());
/*  92 */     this.missingSprite = this.texturesByName.get(MissingTextureAtlasSprite.getLocation());
/*  93 */     if (this.missingSprite == null)
/*  94 */       throw new IllegalStateException("Atlas '" + String.valueOf(this.location) + "' (" + this.texturesByName.size() + " sprites) has no missing texture sprite"); 
/*  97 */     ImmutableList.Builder<TextureAtlasSprite> spritesBuilder = ImmutableList.builder();
/*  98 */     int animatedSpriteCount = 0;
/*  99 */     for (TextureAtlasSprite sprite : preparations.regions().values()) {
/* 100 */       spritesBuilder.add(sprite);
/* 101 */       if (sprite.isAnimated())
/* 102 */         animatedSpriteCount++; 
/*     */     } 
/* 105 */     this.sprites = (List<TextureAtlasSprite>)spritesBuilder.build();
/* 107 */     if (animatedSpriteCount > 0) {
/* 108 */       ImmutableList.Builder<SpriteContents.AnimationState> animationStates = ImmutableList.builder();
/* 109 */       int spriteUboSize = Mth.roundToward(SpriteContents.UBO_SIZE, RenderSystem.getDevice().getDeviceInfo().limits().minUniformOffsetAlignment());
/* 110 */       int uboBlockSize = spriteUboSize * this.mipLevelCount;
/* 111 */       ByteBuffer spriteUboBuffer = MemoryUtil.memAlloc(animatedSpriteCount * uboBlockSize);
/* 112 */       int animationIndex = 0;
/* 114 */       for (TextureAtlasSprite sprite : this.sprites) {
/* 115 */         if (sprite.isAnimated()) {
/* 116 */           sprite.uploadSpriteUbo(spriteUboBuffer, animationIndex * uboBlockSize, this.maxMipLevel, this.width, this.height, spriteUboSize);
/* 117 */           animationIndex++;
/*     */         } 
/*     */       } 
/* 121 */       GpuBuffer spriteUbos = RenderSystem.getDevice().createBuffer(() -> String.valueOf(this.location) + " sprite UBOs", 128, spriteUboBuffer);
/* 123 */       animationIndex = 0;
/* 124 */       for (TextureAtlasSprite sprite : this.sprites) {
/* 125 */         if (sprite.isAnimated()) {
/* 126 */           SpriteContents.AnimationState animationState = sprite.createAnimationState(spriteUbos.slice((animationIndex * uboBlockSize), uboBlockSize), spriteUboSize);
/* 127 */           animationIndex++;
/* 128 */           if (animationState != null)
/* 129 */             animationStates.add(animationState); 
/*     */         } 
/*     */       } 
/* 134 */       this.spriteUbos = spriteUbos;
/* 135 */       this.animatedTexturesStates = (List<SpriteContents.AnimationState>)animationStates.build();
/* 136 */       MemoryUtil.memFree(spriteUboBuffer);
/*     */     } 
/* 139 */     uploadInitialContents();
/* 141 */     if (SharedConstants.DEBUG_DUMP_TEXTURE_ATLAS) {
/* 142 */       Path dumpDir = TextureUtil.getDebugTexturePath();
/*     */       try {
/* 144 */         Files.createDirectories(dumpDir, (FileAttribute<?>[])new FileAttribute[0]);
/* 145 */         dumpContents(this.location, dumpDir);
/* 146 */       } catch (Exception e) {
/* 147 */         LOGGER.warn("Failed to dump atlas contents to {}", dumpDir);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void uploadInitialContents() {
/* 153 */     GpuDevice device = RenderSystem.getDevice();
/* 154 */     int spriteUboSize = Mth.roundToward(SpriteContents.UBO_SIZE, device.getDeviceInfo().limits().minUniformOffsetAlignment());
/* 155 */     int uboBlockSize = spriteUboSize * this.mipLevelCount;
/* 156 */     GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
/* 159 */     List<TextureAtlasSprite> staticSprites = this.sprites.stream().filter(s -> !s.isAnimated()).toList();
/* 160 */     List<GpuTextureView[]> scratchTextures = (List)new ArrayList<>();
/* 161 */     ByteBuffer buffer = MemoryUtil.memAlloc(staticSprites.size() * uboBlockSize);
/* 164 */     for (int i = 0; i < staticSprites.size(); i++) {
/* 165 */       TextureAtlasSprite sprite = staticSprites.get(i);
/* 166 */       sprite.uploadSpriteUbo(buffer, i * uboBlockSize, this.maxMipLevel, this.width, this.height, spriteUboSize);
/* 167 */       GpuTexture scratchTexture = device.createTexture(() -> sprite.contents().name().toString(), 5, GpuFormat.RGBA8_UNORM, sprite.contents().width(), sprite.contents().height(), 1, this.mipLevelCount);
/* 168 */       GpuTextureView[] views = new GpuTextureView[this.mipLevelCount];
/* 169 */       for (int level = 0; level <= this.maxMipLevel; level++) {
/* 170 */         sprite.uploadFirstFrame(scratchTexture, level);
/* 171 */         views[level] = device.createTextureView(scratchTexture);
/*     */       } 
/* 173 */       scratchTextures.add(views);
/*     */     } 
/* 177 */     GpuBuffer ubo = device.createBuffer(() -> "SpriteAnimationInfo", 128, buffer);
/*     */     try {
/* 178 */       for (int level = 0; level < this.mipLevelCount; level++) {
/* 179 */         RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Animate " + String.valueOf(this.location), this.mipViews[level], Optional.empty());
/*     */         try {
/* 180 */           RenderSystem.bindDefaultUniforms(renderPass);
/* 181 */           renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
/* 183 */           for (int j = 0; j < staticSprites.size(); j++) {
/* 184 */             renderPass.bindTexture("Sprite", ((GpuTextureView[])scratchTextures.get(j))[level], sampler);
/* 185 */             renderPass.setUniform("SpriteAnimationInfo", ubo.slice((j * uboBlockSize + level * spriteUboSize), SpriteContents.UBO_SIZE));
/* 186 */             renderPass.draw(6, 1, 0, 0);
/*     */           } 
/* 188 */           if (renderPass != null)
/* 188 */             renderPass.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (renderPass != null)
/*     */             try {
/*     */               renderPass.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       } 
/* 190 */       if (ubo != null)
/* 190 */         ubo.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (ubo != null)
/*     */         try {
/*     */           ubo.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 193 */     for (GpuTextureView[] views : scratchTextures) {
/* 194 */       for (GpuTextureView view : views) {
/* 195 */         view.close();
/* 196 */         view.texture().close();
/*     */       } 
/*     */     } 
/* 199 */     MemoryUtil.memFree(buffer);
/* 201 */     uploadAnimationFrames();
/*     */   }
/*     */   
/*     */   public void dumpContents(Identifier selfId, Path dir) throws IOException {
/* 206 */     String outputId = selfId.toDebugFileName();
/* 207 */     TextureUtil.writeAsPNG(dir, outputId, getTexture(), this.maxMipLevel, argb -> argb);
/* 208 */     dumpSpriteNames(dir, outputId, this.texturesByName);
/*     */   }
/*     */   
/*     */   private static void dumpSpriteNames(Path dir, String outputId, Map<Identifier, TextureAtlasSprite> regions) {
/* 212 */     Path outputPath = dir.resolve(outputId + ".txt");
/*     */     try {
/* 213 */       Writer output = Files.newBufferedWriter(outputPath, new java.nio.file.OpenOption[0]);
/*     */       try {
/* 214 */         for (Map.Entry<Identifier, TextureAtlasSprite> e : (Iterable<Map.Entry<Identifier, TextureAtlasSprite>>)regions.entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
/* 215 */           TextureAtlasSprite value = e.getValue();
/* 216 */           output.write(String.format(Locale.ROOT, "%s\tx=%d\ty=%d\tw=%d\th=%d%n", new Object[] { e.getKey(), Integer.valueOf(value.getX()), Integer.valueOf(value.getY()), Integer.valueOf(value.contents().width()), Integer.valueOf(value.contents().height()) }));
/*     */         } 
/* 218 */         if (output != null)
/* 218 */           output.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (output != null)
/*     */           try {
/*     */             output.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 218 */     } catch (IOException e) {
/* 219 */       LOGGER.warn("Failed to write file {}", outputPath, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cycleAnimationFrames() {
/* 224 */     if (this.texture == null)
/*     */       return; 
/* 228 */     for (SpriteContents.AnimationState animationState : this.animatedTexturesStates)
/* 229 */       animationState.tick(); 
/* 232 */     uploadAnimationFrames();
/*     */   }
/*     */   
/*     */   private void uploadAnimationFrames() {
/* 236 */     if (this.animatedTexturesStates.stream().anyMatch(SpriteContents.AnimationState::needsToDraw))
/* 237 */       for (int level = 0; level <= this.maxMipLevel; level++) {
/* 238 */         RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Animate " + String.valueOf(this.location), this.mipViews[level], Optional.empty());
/*     */         try {
/* 239 */           RenderSystem.bindDefaultUniforms(renderPass);
/* 240 */           for (SpriteContents.AnimationState animationState : this.animatedTexturesStates) {
/* 241 */             if (animationState.needsToDraw())
/* 242 */               animationState.drawToAtlas(renderPass, animationState.getDrawUbo(level)); 
/*     */           } 
/* 245 */           if (renderPass != null)
/* 245 */             renderPass.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (renderPass != null)
/*     */             try {
/*     */               renderPass.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public void tick() {
/* 252 */     cycleAnimationFrames();
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite getSprite(Identifier location) {
/* 256 */     TextureAtlasSprite result = this.texturesByName.getOrDefault(location, this.missingSprite);
/* 257 */     if (result == null)
/* 258 */       throw new IllegalStateException("Tried to lookup sprite, but atlas is not initialized"); 
/* 260 */     return result;
/*     */   }
/*     */   
/*     */   public TextureAtlasSprite missingSprite() {
/* 264 */     return Objects.<TextureAtlasSprite>requireNonNull(this.missingSprite, "Atlas not initialized");
/*     */   }
/*     */   
/*     */   public void clearTextureData() {
/* 268 */     this.sprites.forEach(TextureAtlasSprite::close);
/* 270 */     this.sprites = List.of();
/* 271 */     this.animatedTexturesStates.forEach(SpriteContents.AnimationState::close);
/* 272 */     this.animatedTexturesStates = List.of();
/* 273 */     this.texturesByName = Map.of();
/* 274 */     this.missingSprite = null;
/* 276 */     if (this.spriteUbos != null) {
/* 277 */       this.spriteUbos.close();
/* 278 */       this.spriteUbos = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void releaseTextures() {
/* 284 */     super.releaseTextures();
/* 285 */     for (GpuTextureView view : this.mipViews)
/* 286 */       view.close(); 
/*     */   }
/*     */   
/*     */   public void close() {
/* 292 */     clearTextureData();
/* 293 */     super.close();
/*     */   }
/*     */   
/*     */   public Identifier location() {
/* 297 */     return this.location;
/*     */   }
/*     */   
/*     */   public int maxSupportedTextureSize() {
/* 301 */     return this.maxSupportedTextureSize;
/*     */   }
/*     */   
/*     */   int getWidth() {
/* 306 */     return this.width;
/*     */   }
/*     */   
/*     */   int getHeight() {
/* 311 */     return this.height;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\TextureAtlas.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */