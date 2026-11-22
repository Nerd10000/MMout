/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ 
/*     */ class FallingParticle extends DripParticle {
/*     */   private FallingParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 115 */     super(level, x, y, z, type, sprite);
/*     */   }
/*     */   
/*     */   protected void postMoveUpdate() {
/* 120 */     if (this.onGround)
/* 121 */       remove(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$FallingParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */