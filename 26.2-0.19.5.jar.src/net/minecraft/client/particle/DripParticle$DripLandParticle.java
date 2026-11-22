/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ 
/*     */ class DripLandParticle extends DripParticle {
/*     */   private DripLandParticle(ClientLevel level, double x, double y, double z, Fluid type, TextureAtlasSprite sprite) {
/* 128 */     super(level, x, y, z, type, sprite);
/* 129 */     this.lifetime = (int)(16.0D / (this.random.nextFloat() * 0.8D + 0.2D));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\DripParticle$DripLandParticle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */