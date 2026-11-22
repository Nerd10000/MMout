/*     */ package net.minecraft.client.renderer.texture.atlas.sources;
/*     */ 
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.SpriteContents;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
/*     */ import net.minecraft.client.renderer.texture.atlas.SpriteSource;
/*     */ import net.minecraft.client.resources.metadata.animation.FrameSize;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ class RegionInstance implements SpriteSource.DiscardableLoader {
/*     */   private final LazyLoadedImage image;
/*     */   
/*     */   private final Unstitcher.Region region;
/*     */   
/*     */   private final double xDivisor;
/*     */   
/*     */   private final double yDivisor;
/*     */   
/*     */   private RegionInstance(LazyLoadedImage image, Unstitcher.Region region, double xDivisor, double yDivisor) {
/*  74 */     this.image = image;
/*  75 */     this.region = region;
/*  76 */     this.xDivisor = xDivisor;
/*  77 */     this.yDivisor = yDivisor;
/*     */   }
/*     */   
/*     */   public SpriteContents get(SpriteResourceLoader loader) {
/*     */     try {
/*  83 */       NativeImage fullImage = this.image.get();
/*  85 */       double xScale = fullImage.getWidth() / this.xDivisor;
/*  86 */       double yScale = fullImage.getHeight() / this.yDivisor;
/*  88 */       int x = Mth.floor(this.region.x * xScale);
/*  89 */       int y = Mth.floor(this.region.y * yScale);
/*  91 */       int width = Mth.floor(this.region.width * xScale);
/*  92 */       int height = Mth.floor(this.region.height * yScale);
/*  94 */       NativeImage target = new NativeImage(NativeImage.Format.RGBA, width, height, false);
/*  95 */       fullImage.copyRect(target, x, y, 0, 0, width, height, false, false);
/* 101 */       return new SpriteContents(this.region.sprite, new FrameSize(width, height), target);
/* 102 */     } catch (Exception e) {
/* 103 */       Unstitcher.LOGGER.error("Failed to unstitch region {}", this.region.sprite, e);
/*     */     } finally {
/* 105 */       this.image.release();
/*     */     } 
/* 107 */     return MissingTextureAtlasSprite.create();
/*     */   }
/*     */   
/*     */   public void discard() {
/* 112 */     this.image.release();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\atlas\sources\Unstitcher$RegionInstance.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */