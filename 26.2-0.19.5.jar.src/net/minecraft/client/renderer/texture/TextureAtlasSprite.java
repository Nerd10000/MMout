/*     */ package net.minecraft.client.renderer.texture;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.Std140Builder;
/*     */ import com.mojang.blaze3d.platform.Transparency;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.client.renderer.SpriteCoordinateExpander;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class TextureAtlasSprite implements AutoCloseable {
/*     */   private final Identifier atlasLocation;
/*     */   
/*     */   private final SpriteContents contents;
/*     */   
/*     */   private final int x;
/*     */   
/*     */   private final int y;
/*     */   
/*     */   private final float u0;
/*     */   
/*     */   private final float u1;
/*     */   
/*     */   private final float v0;
/*     */   
/*     */   private final float v1;
/*     */   
/*     */   private final int padding;
/*     */   
/*     */   protected TextureAtlasSprite(Identifier atlasLocation, SpriteContents contents, int atlasWidth, int atlasHeight, int x, int y, int padding) {
/*  29 */     this.atlasLocation = atlasLocation;
/*  30 */     this.contents = contents;
/*  31 */     this.padding = padding;
/*  32 */     this.x = x;
/*  33 */     this.y = y;
/*  35 */     this.u0 = (x + padding) / atlasWidth;
/*  36 */     this.u1 = (x + padding + contents.width()) / atlasWidth;
/*  37 */     this.v0 = (y + padding) / atlasHeight;
/*  38 */     this.v1 = (y + padding + contents.height()) / atlasHeight;
/*     */   }
/*     */   
/*     */   public int getX() {
/*  42 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  46 */     return this.y;
/*     */   }
/*     */   
/*     */   public float getU0() {
/*  50 */     return this.u0;
/*     */   }
/*     */   
/*     */   public float getU1() {
/*  54 */     return this.u1;
/*     */   }
/*     */   
/*     */   public SpriteContents contents() {
/*  58 */     return this.contents;
/*     */   }
/*     */   
/*     */   public SpriteContents.AnimationState createAnimationState(GpuBufferSlice uboSlice, int spriteUboSize) {
/*  62 */     return this.contents.createAnimationState(uboSlice, spriteUboSize);
/*     */   }
/*     */   
/*     */   public Transparency transparency() {
/*  66 */     return this.contents.transparency();
/*     */   }
/*     */   
/*     */   public float getU(float offset) {
/*  70 */     float diff = this.u1 - this.u0;
/*  71 */     return this.u0 + diff * offset;
/*     */   }
/*     */   
/*     */   public float getV0() {
/*  75 */     return this.v0;
/*     */   }
/*     */   
/*     */   public float getV1() {
/*  79 */     return this.v1;
/*     */   }
/*     */   
/*     */   public float getV(float offset) {
/*  83 */     float diff = this.v1 - this.v0;
/*  84 */     return this.v0 + diff * offset;
/*     */   }
/*     */   
/*     */   public Identifier atlasLocation() {
/*  88 */     return this.atlasLocation;
/*     */   }
/*     */   
/*     */   public String toString() {
/*  93 */     return "TextureAtlasSprite{contents='" + String.valueOf(this.contents) + "', u0=" + this.u0 + ", u1=" + this.u1 + ", v0=" + this.v0 + ", v1=" + this.v1 + "}";
/*     */   }
/*     */   
/*     */   public void uploadFirstFrame(GpuTexture destination, int level) {
/*  97 */     this.contents.uploadFirstFrame(destination, level);
/*     */   }
/*     */   
/*     */   public VertexConsumer wrap(VertexConsumer buffer) {
/* 101 */     return (VertexConsumer)new SpriteCoordinateExpander(buffer, this);
/*     */   }
/*     */   
/*     */   public boolean isAnimated() {
/* 105 */     return this.contents.isAnimated();
/*     */   }
/*     */   
/*     */   public void uploadSpriteUbo(ByteBuffer uboBuffer, int startOffset, int maxMipLevel, int atlasWidth, int atlasHeight, int spriteUboSize) {
/* 109 */     for (int level = 0; level <= maxMipLevel; level++)
/* 110 */       Std140Builder.intoBuffer(MemoryUtil.memSlice(uboBuffer, startOffset + level * spriteUboSize, spriteUboSize))
/* 111 */         .putMat4f((Matrix4fc)(new Matrix4f()).ortho2D(0.0F, (atlasWidth >> level), 0.0F, (atlasHeight >> level)))
/* 112 */         .putMat4f((Matrix4fc)(new Matrix4f()).translate((this.x >> level), (this.y >> level), 0.0F).scale((this.contents.width() + this.padding * 2 >> level), (this.contents.height() + this.padding * 2 >> level), 1.0F))
/* 113 */         .putFloat(this.padding / this.contents.width())
/* 114 */         .putFloat(this.padding / this.contents.height())
/* 115 */         .putInt(level); 
/*     */   }
/*     */   
/*     */   public void close() {
/* 121 */     this.contents.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\texture\TextureAtlasSprite.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */