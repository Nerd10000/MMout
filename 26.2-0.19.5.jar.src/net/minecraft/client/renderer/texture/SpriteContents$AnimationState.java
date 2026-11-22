/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ 
/*     */ public class AnimationState implements AutoCloseable {
/*     */   private int frame;
/*     */   
/*     */   private int subFrame;
/*     */   
/*     */   private final SpriteContents.AnimatedTexture animationInfo;
/*     */   
/*     */   private final Int2ObjectMap<GpuTextureView> frameTexturesByIndex;
/*     */   
/*     */   private final GpuBufferSlice[] spriteUbosByMip;
/*     */   
/*     */   private boolean isDirty = true;
/*     */   
/*     */   private AnimationState(SpriteContents this$0, SpriteContents.AnimatedTexture animationInfo, Int2ObjectMap<GpuTextureView> frameTexturesByIndex, GpuBufferSlice[] spriteUbosByMip) {
/* 333 */     this.animationInfo = animationInfo;
/* 334 */     this.frameTexturesByIndex = frameTexturesByIndex;
/* 335 */     this.spriteUbosByMip = spriteUbosByMip;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 339 */     this.subFrame++;
/* 340 */     this.isDirty = false;
/* 341 */     SpriteContents.FrameInfo currentFrame = this.animationInfo.frames.get(this.frame);
/* 342 */     if (this.subFrame >= currentFrame.time) {
/* 343 */       int oldFrame = currentFrame.index;
/* 344 */       this.frame = (this.frame + 1) % this.animationInfo.frames.size();
/* 345 */       this.subFrame = 0;
/* 347 */       int newFrame = ((SpriteContents.FrameInfo)this.animationInfo.frames.get(this.frame)).index;
/* 348 */       if (oldFrame != newFrame)
/* 349 */         this.isDirty = true; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public GpuBufferSlice getDrawUbo(int level) {
/* 355 */     return this.spriteUbosByMip[level];
/*     */   }
/*     */   
/*     */   public boolean needsToDraw() {
/* 359 */     return (this.animationInfo.interpolateFrames || this.isDirty);
/*     */   }
/*     */   
/*     */   public void drawToAtlas(RenderPass renderPass, GpuBufferSlice ubo) {
/* 363 */     GpuSampler sampler = RenderSystem.getSamplerCache().getClampToEdge(FilterMode.NEAREST, true);
/* 364 */     List<SpriteContents.FrameInfo> frames = this.animationInfo.frames;
/* 365 */     int oldFrame = ((SpriteContents.FrameInfo)frames.get(this.frame)).index;
/* 366 */     float frameProgress = this.subFrame / ((SpriteContents.FrameInfo)this.animationInfo.frames.get(this.frame)).time;
/* 367 */     int frameProgressAsInt = (int)(frameProgress * 1000.0F);
/* 369 */     if (this.animationInfo.interpolateFrames) {
/* 370 */       int newFrame = ((SpriteContents.FrameInfo)frames.get((this.frame + 1) % frames.size())).index;
/* 372 */       renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_INTERPOLATE);
/* 373 */       renderPass.bindTexture("CurrentSprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
/* 374 */       renderPass.bindTexture("NextSprite", (GpuTextureView)this.frameTexturesByIndex.get(newFrame), sampler);
/* 375 */     } else if (this.isDirty) {
/* 376 */       renderPass.setPipeline(RenderPipelines.ANIMATE_SPRITE_BLIT);
/* 377 */       renderPass.bindTexture("Sprite", (GpuTextureView)this.frameTexturesByIndex.get(oldFrame), sampler);
/*     */     } 
/* 380 */     renderPass.setUniform("SpriteAnimationInfo", ubo);
/* 382 */     renderPass.draw(6, 1, frameProgressAsInt << 3, 0);
/*     */   }
/*     */   
/*     */   public void close() {
/* 387 */     for (ObjectIterator<GpuTextureView> objectIterator = this.frameTexturesByIndex.values().iterator(); objectIterator.hasNext(); ) {
/* 387 */       GpuTextureView view = objectIterator.next();
/* 388 */       view.texture().close();
/* 389 */       view.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\SpriteContents$AnimationState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */