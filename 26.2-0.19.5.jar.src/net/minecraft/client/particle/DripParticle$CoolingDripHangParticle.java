/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.world.level.material.Fluid;
/*    */ 
/*    */ class CoolingDripHangParticle extends DripParticle.DripHangParticle {
/*    */   private CoolingDripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, TextureAtlasSprite sprite) {
/* 50 */     super(level, x, y, z, type, fallingParticle, sprite);
/*    */   }
/*    */   
/*    */   protected void preMoveUpdate() {
/* 55 */     this.rCol = 1.0F;
/* 56 */     this.gCol = 16.0F / (40 - this.lifetime + 16);
/* 57 */     this.bCol = 4.0F / (40 - this.lifetime + 8);
/* 58 */     super.preMoveUpdate();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$CoolingDripHangParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */