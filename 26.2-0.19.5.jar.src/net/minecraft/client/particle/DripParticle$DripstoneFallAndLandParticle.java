/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ 
/*     */ class DripstoneFallAndLandParticle extends DripParticle.FallAndLandParticle {
/*     */   private DripstoneFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, TextureAtlasSprite sprite) {
/*  98 */     super(level, x, y, z, type, landParticle, sprite);
/*     */   }
/*     */   
/*     */   protected void postMoveUpdate() {
/* 103 */     if (this.onGround) {
/* 104 */       remove();
/* 105 */       this.level.addParticle(this.landParticle, this.x, this.y, this.z, 0.0D, 0.0D, 0.0D);
/* 106 */       SoundEvent sound = (getType() == Fluids.LAVA) ? SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA : SoundEvents.POINTED_DRIPSTONE_DRIP_WATER;
/* 107 */       float volume = Mth.randomBetween(this.random, 0.3F, 1.0F);
/* 108 */       this.level.playLocalSound(this.x, this.y, this.z, sound, SoundSource.BLOCKS, volume, 1.0F, false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$DripstoneFallAndLandParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */