/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class DripHangParticle extends DripParticle {
/*    */   private final ParticleOptions fallingParticle;
/*    */   
/*    */   private DripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
/* 26 */     super(level, x, y, z, type, sprite);
/* 27 */     this.fallingParticle = fallingParticle;
/* 28 */     this.gravity *= 0.02F;
/* 29 */     this.lifetime = 40;
/*    */   }
/*    */   
/*    */   protected void preMoveUpdate() {
/* 34 */     if (this.lifetime-- <= 0) {
/* 35 */       remove();
/* 36 */       this.level.addParticle(this.fallingParticle, this.x, this.y, this.z, this.xd, this.yd, this.zd);
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void postMoveUpdate() {
/* 42 */     this.xd *= 0.02D;
/* 43 */     this.yd *= 0.02D;
/* 44 */     this.zd *= 0.02D;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$DripHangParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */