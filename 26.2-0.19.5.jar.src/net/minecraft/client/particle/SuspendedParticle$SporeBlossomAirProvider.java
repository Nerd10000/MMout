/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.core.particles.ParticleLimit;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.SimpleParticleType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class SporeBlossomAirProvider implements ParticleProvider<SimpleParticleType> {
/*    */   private final SpriteSet sprite;
/*    */   
/*    */   public SporeBlossomAirProvider(SpriteSet sprite) {
/* 62 */     this.sprite = sprite;
/*    */   }
/*    */   
/*    */   public Particle createParticle(SimpleParticleType options, ClientLevel level, double x, double y, double z, double xAux, double yAux, double zAux, RandomSource random) {
/* 67 */     SuspendedParticle particle = new SuspendedParticle(this, level, x, y, z, 0.0D, -0.800000011920929D, 0.0D, this.sprite.get(random)) {
/*    */         public Optional<ParticleLimit> getParticleLimit() {
/* 70 */           return Optional.of(ParticleLimit.SPORE_BLOSSOM);
/*    */         }
/*    */       };
/* 73 */     particle.lifetime = Mth.randomBetweenInclusive(random, 500, 1000);
/* 74 */     particle.gravity = 0.01F;
/* 75 */     particle.setColor(0.32F, 0.5F, 0.22F);
/* 76 */     return particle;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\particle\SuspendedParticle$SporeBlossomAirProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */