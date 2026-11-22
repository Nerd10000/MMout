/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class HoneyFallAndLandParticle extends DripParticle.FallAndLandParticle {
/*    */   private HoneyFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/* 82 */     super(level, x, y, z, type, landParticle, sprite);
/*    */   }
/*    */   
/*    */   protected void postMoveUpdate() {
/* 87 */     if (this.onGround) {
/* 88 */       remove();
/* 89 */       this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/* 90 */       float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
/* 91 */       this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.BEEHIVE_DRIP, SoundSource.BLOCKS, volume, 1.0F, false);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$HoneyFallAndLandParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */