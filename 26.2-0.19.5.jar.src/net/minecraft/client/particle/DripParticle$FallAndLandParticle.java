/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class FallAndLandParticle extends DripParticle.FallingParticle {
/*    */   protected final ParticleOptions landParticle;
/*    */   
/*    */   private FallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/* 66 */     super(level, x, y, z, type, sprite);
/* 67 */     this.lifetime = (int)(64.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/* 68 */     this.landParticle = landParticle;
/*    */   }
/*    */   
/*    */   protected void postMoveUpdate() {
/* 73 */     if (this.onGround) {
/* 74 */       remove();
/* 75 */       this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$FallAndLandParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */