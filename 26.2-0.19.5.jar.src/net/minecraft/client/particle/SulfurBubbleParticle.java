/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public class SulfurBubbleParticle extends SingleQuadParticle {
/*    */   private static final float SIZE_START = 0.02F;
/*    */   
/*    */   private static final float SIZE_END = 0.15F;
/*    */   
/*    */   private static final float UPWARDS_SPEED = 0.04F;
/*    */   
/*    */   private static final float HORIZONTAL_WIGGLING = 0.003F;
/*    */   
/*    */   private final double yStart;
/*    */   
/*    */   private final double yEnd;
/*    */   
/*    */   private final float sizeStart;
/*    */   
/*    */   private double yPrev;
/*    */   
/*    */   private SulfurBubbleParticle(ClientLevel level, double x, double y, double z, double xa, double za, TextureAtlasSprite sprite) {
/* 26 */     super(level, x, y, z, sprite);
/* 27 */     this.gravity = -0.04F;
/* 28 */     this.friction = 0.85F;
/* 29 */     setSize(0.02F, 0.02F);
/* 32 */     this.xd = xa * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/* 33 */     this.zd = za * 0.20000000298023224D + ((this.random.nextFloat() * 2.0F - 1.0F) * 0.02F);
/* 35 */     this.sizeStart = 0.02F + 0.02F * this.random.nextFloat();
/* 36 */     this.quadSize = this.sizeStart;
/* 39 */     this.lifetime = Integer.MAX_VALUE;
/* 40 */     this.yStart = this.yo;
/* 41 */     this.yEnd = this.yo + 4.0D - 1.0D;
/* 42 */     this.yPrev = y;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 47 */     super.tick();
/* 50 */     if (!this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).isSourceOfType((Fluid)Fluids.WATER))
/* 51 */       remove(); 
/* 55 */     if (!this.removed && this.y >= this.yEnd)
/* 56 */       remove(); 
/* 60 */     if (!this.removed && this.y <= this.yPrev)
/* 61 */       remove(); 
/* 64 */     this.xd += randomHorizontalWiggling();
/* 65 */     this.zd += randomHorizontalWiggling();
/* 66 */     move(this.xd, 0.0D, this.zd);
/* 69 */     float travelProgress = (float)((this.y - this.yStart) / (this.yEnd - this.yStart));
/* 70 */     this.quadSize = this.sizeStart + travelProgress * (0.15F - this.sizeStart);
/* 72 */     this.yPrev = this.y;
/*    */   }
/*    */   
/*    */   private double randomHorizontalWiggling() {
/* 76 */     return (this.random.nextFloat() * 0.003F * (this.random.nextBoolean() ? true : -1)) * 0.5D;
/*    */   }
/*    */   
/*    */   public SingleQuadParticle.Layer getLayer() {
/* 81 */     return SingleQuadParticle.Layer.OPAQUE;
/*    */   }
/*    */   
/*    */   public static class Provider implements ParticleProvider<SimpleParticleType> {
/*    */     private final SpriteSet sprite;
/*    */     
/*    */     public Provider(SpriteSet sprite) {
/* 88 */       this.sprite = sprite;
/*    */     }
/*    */     
/*    */     public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 93 */       return new SulfurBubbleParticle(level, x, y, z, xAux, yAux, this.sprite.get(random));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SulfurBubbleParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */